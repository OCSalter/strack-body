package com.strack.live.http

import java.util.UUID
import com.strack.live.domain.*

import cats.*
import cats.effect.*
import cats.syntax.all.*
import com.strack.live.core.PlayerTags
import io.circe.generic.auto.*
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.server.Router
import org.http4s.*
import org.http4s.dsl.Http4sDsl

class PlayerTagRoutes[F[_]: Concurrent] private(playerTags: PlayerTags[F]) extends Http4sDsl[F] {
  private val prefix = "/players"

  private val getAllRoute: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root =>
      playerTags.all().flatMap(m => Ok(m))
  }

  val routes: HttpRoutes[F] = Router(
    prefix -> (getAllRoute)
  )
}

object PlayerTagRoutes {
  def resource[F[_]: Concurrent](playerTags: PlayerTags[F]): Resource[F,PlayerTagRoutes[F]] =
    Resource.pure(new PlayerTagRoutes[F](playerTags))
}
