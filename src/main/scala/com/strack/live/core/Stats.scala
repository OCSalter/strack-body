package com.strack.live.core

import com.strack.live.domain.strack.Stat
import java.util.UUID
import cats.effect.*
import cats.syntax.all.*
import doobie.implicits.*
import doobie.postgres.implicits.*
import doobie.util.transactor.Transactor

trait Stats[F[_]]  {
  def fromReferenceId(id: UUID): F[List[Stat]]
  def placeFromTeamId(id: UUID): F[Stat]
}

class StatsLive[F[_]: Concurrent] private(transactor: Transactor[F]) extends Stats[F] {
  override def fromReferenceId(id: UUID): F[List[Stat]] =
    sql"""
        SELECT s.id, s.type_id, s.reference_id, s.event_value
        FROM general_events s
        WHERE s.reference_id = $id
       """.query[Stat].stream.compile.toList.transact(transactor)

  override def placeFromTeamId(id: UUID): F[Stat] =
    sql"""
      SELECT s.id, s.type_id. s.reference_id, s.event_value
      FROM general_events s
      WHERE s.reference_id = $id AND s.type_id = ${UUID.fromString("9c8a59af-31f3-464d-8d9f-2af3db0e759f")}
       """.query[Stat].unique.transact(transactor)
}

object StatsLive {
  def make[F[_]: Concurrent](postgres: Transactor[F]): F[StatsLive[F]] =
    new StatsLive[F](postgres).pure[F]

  def resource[F[_]: Concurrent](postgres: Transactor[F]): Resource[F,StatsLive[F]] =
    Resource.pure(new StatsLive[F](postgres))
}


