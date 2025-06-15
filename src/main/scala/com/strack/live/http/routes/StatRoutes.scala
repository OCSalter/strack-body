package com.strack.live.http.routes

import cats.*
import cats.effect.*
import cats.syntax.all.*
import com.strack.live.core.Stats
import io.circe.generic.auto.*
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.server.Router
import org.http4s.*
import org.http4s.dsl.Http4sDsl

class StatRoutes [F[_]:Concurrent] private(stats: Stats[F]) extends Http4sDsl[F] {
  private val prefix = "stats"

  private val getFromReference = HttpRoutes.of[F] {
    case GET -> Root / "fromReference" / UUIDVar(referenceId) =>
      for {
        s <- stats.fromReferenceId(referenceId)
        resp <- Ok(s)
      } yield resp
  }

  private val getPlaceFromTeam = HttpRoutes.of[F] {
    case GET -> Root / "placeFromTeam" / UUIDVar(teamId) =>
      for {
        s <- stats.placeFromTeamId(teamId)
        resp <- Ok(s)
      } yield resp
  }

  val routes: HttpRoutes[F] = Router(
    prefix -> (getFromReference <+> getPlaceFromTeam)
  )
}

object StatRoutes{
  def resource[F[_]: Concurrent] (stats: Stats[F]): Resource[F, StatRoutes[F]] =
    Resource.pure(new StatRoutes[F](stats))
}
