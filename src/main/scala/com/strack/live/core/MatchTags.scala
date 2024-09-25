package com.strack.live.core

import com.strack.live.domain.strack.{MatchPreview, MatchTag}

import java.util.UUID
import cats.effect.*
import cats.syntax.all.*
import doobie.implicits.*
import doobie.postgres.implicits.*
import doobie.util.transactor.Transactor

trait MatchTags[F[_]] {
  def create(m: MatchTagInput): F[UUID]
  def all(): F[List[MatchTag]]
  def getFromGroupId(g: UUID): F[List[MatchTag]]
  def getMatchPreviewFromGroupId(id: UUID): F[List[MatchPreview]]
}

case class MatchTagInput(groupId: UUID, modeId: UUID)

class MatchTagsLive[F[_]: Concurrent] private(transactor: Transactor[F]) extends MatchTags[F] {
  override def create(m: MatchTagInput): F[UUID] =
    sql"""
      INSERT INTO matches(group_id, mode_id)
      VALUES(${m.groupId},${m.modeId})
       """.update.withUniqueGeneratedKeys[UUID]("id").transact(transactor)

  override def all(): F[List[MatchTag]] =
    sql"""
      SELECT m.id, m.group_id, m.mode_id
      FROM matches m
       """.query[MatchTag].stream.compile.toList.transact(transactor)

  override def getFromGroupId(id: UUID): F[List[MatchTag]] = {
    sql"""
      SELECT m.id, m.group_id, m.mode_id
      FROM matches m
      WHERE m.group_id = $id
       """.query[MatchTag].stream.compile.toList.transact(transactor)
  }

  override def getMatchPreviewFromGroupId(id: UUID): F[List[MatchPreview]] =
    sql"""
      SELECT m.id, mo.name, array_agg(t.id)
      FROM matches m
      JOIN teams t on m.id = t.match_id
      JOIN modes mo on m.mode_id = mo.id
      WHERE m.group_id = $id
      GROUP BY (m.id, mo.name)
       """.query[MatchPreview].stream.compile.toList.transact(transactor)
}

object MatchTagsLive {
  def make [F[_]: Concurrent](postgres: Transactor[F]): F[MatchTagsLive[F]] =
      new MatchTagsLive[F](postgres).pure[F]

  def resource[F[_]: Concurrent](postgres: Transactor[F]): Resource[F, MatchTagsLive[F]] =
    Resource.pure(new MatchTagsLive[F](postgres))
}
