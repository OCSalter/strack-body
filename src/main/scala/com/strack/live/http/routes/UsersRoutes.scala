package com.strack.live.http

import com.strack.live.core.Users

import cats.*
import cats.effect.*
import cats.syntax.all.*
import org.http4s.*
import cats.syntax.all.*
import io.circe.generic.auto.*
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.server.Router
import org.http4s.dsl.Http4sDsl


class UsersRoutes[F[_]: Concurrent] private(users: Users[F]) extends Http4sDsl[F] {
  val prefix = "/users"

  private val getAllRoute: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root =>
      users.all().flatMap(Ok(_))
  }

  private val getFromMatchIdRoute: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "fromMatch" / UUIDVar(m) => for {
      u <- users.fromMatchId(m)
      resp <- Ok(u)
    } yield resp
  }

  val routes: HttpRoutes[F] = Router(
    prefix -> (getAllRoute <+> getFromMatchIdRoute)
  )
}

object UsersRoutes {
  def resource[F[_]: Concurrent](users: Users[F]): Resource[F, UsersRoutes[F]] =
    Resource.pure(new UsersRoutes[F](users))
}
