package com.strack.live.core

import com.strack.live.domain.strack.PlayerTag

import java.util.UUID
import cats.effect.*
import cats.syntax.all.*
import doobie.implicits.*
import doobie.postgres.implicits.*
import doobie.util.transactor.Transactor

case class PlayerTagInput(userId: UUID, teamId: UUID, matchId: UUID)

trait PlayerTags[F[_]] {
  def create(p: PlayerTagInput): F[UUID]
  def all(): F[List[PlayerTag]]
  def getFromMatchId(matchId: UUID): F[List[PlayerTag]]
}

class PlayerTagsLive[F[_]: Concurrent] private(transactor: Transactor[F]) extends PlayerTags[F] {
  override def create(p: PlayerTagInput): F[UUID] =
    sql"""
      INSERT INTO players(user_id, team_id, match_id)
      VALUES(${p.userId},${p.teamId},${p.matchId})
       """.update.withUniqueGeneratedKeys[UUID]("id").transact(transactor)
  override def all(): F[List[PlayerTag]] =
    sql"""
         SELECT p.id, p.user_id, p.team_id, p.match_id
         FROM players p
       """.query[PlayerTag].stream.compile.toList.transact(transactor)
  override def getFromMatchId(matchId: UUID): F[List[PlayerTag]] =
    sql"""
      SELECT p.id, p.user_id, p.team_id, p.match_id
      FROM players p
      WHERE p.match_id = $matchId
       """.query[PlayerTag].stream.compile.toList.transact(transactor)
}

object PlayerTagsLive {
  def make[F[_]: Concurrent](postgres: Transactor[F]): F[PlayerTagsLive[F]] =
    new PlayerTagsLive[F](postgres).pure[F]

  def resource[F[_]: Concurrent](postgres: Transactor[F]): Resource[F, PlayerTagsLive[F]] =
    Resource.pure(new PlayerTagsLive[F](postgres))
}
