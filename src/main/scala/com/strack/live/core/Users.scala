package com.strack.live.core

import cats.effect.*
import cats.syntax.all.*
import com.strack.live.domain.strack.User
import doobie.implicits.*
import doobie.postgres.implicits.*
import doobie.util.transactor.Transactor

import java.util.UUID

trait Users[F[_]] {
  def all(): F[List[User]]
  def fromMatchId(Id: UUID): F[List[User]]
}

class UsersLive[F[_]: Concurrent] private(transactor: Transactor[F]) extends Users[F] {

  override def all(): F[List[User]] =
    sql"""
      SELECT u.id, u.user_name
      FROM users u
       """.query[User].stream.compile.toList.transact(transactor)

  override def fromMatchId(id: UUID): F[List[User]] =
    sql"""
      SELECT u.id, u.user_name
      FROM users u
      JOIN players p on u.id = p.user_id
      JOIN matches m on p.match_id = m.id
      WHERE m.id = $id
       """.query[User].stream.compile.toList.transact(transactor)
}

object UsersLive {
  def make[F[_]: Concurrent](postgres: Transactor[F]): F[UsersLive[F]] =
    new UsersLive[F](postgres).pure[F]

  def resource[F[_]: Concurrent](postgres: Transactor[F]): Resource[F, UsersLive[F]] =
    Resource.pure(new UsersLive[F](postgres))
}
