package com.strack.live.core

import com.strack.live.domain.strack.MatchTag

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

  override def getFromGroupId(groupId: UUID): F[List[MatchTag]] = {
    sql"""
      SELECT m.id, m.group_id, m.mode_id
      FROM matches m
      WHERE m.group_id = $groupId
       """.query[MatchTag].stream.compile.toList.transact(transactor)
  }
}

object MatchTagsLive {
  def make [F[_]: Concurrent](postgres: Transactor[F]): F[MatchTagsLive[F]] =
      new MatchTagsLive[F](postgres).pure[F]

  def resource[F[_]: Concurrent](postgres: Transactor[F]): Resource[F, MatchTagsLive[F]] =
    Resource.pure(new MatchTagsLive[F](postgres))
}
