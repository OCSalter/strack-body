package com.strack.live.http

import cats.*
import cats.effect.*
import cats.syntax.all.*
import org.http4s.*
import com.strack.live.domain.*
import com.strack.live.core.*
import com.strack.live.domain.strack.MatchTag
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.dsl.Http4sDsl
import io.circe.generic.auto.*
import org.http4s.server.Router

import java.util.UUID
import scala.util.Try

class MatchTagRoutes[F[_]: Concurrent] private(matchTags: MatchTags[F]) extends Http4sDsl[F] {
  private val prefix = "/matches"

  private object MatchTagInputPath{
    def unapply(s: String): Option[MatchTagInput] =
      Try{
        val tokens = s.split("/")
        MatchTagInput(UUID.fromString(tokens(0)),UUID.fromString(tokens(1)))
      }.toOption
  }

  private val createMatchRoute: HttpRoutes[F] = HttpRoutes.of[F] {
    case POST -> Root / "create" / MatchTagInputPath(m) =>
      for {
        id <- matchTags.create(m)
        resp <- Created(id)
      } yield resp
  }

  private val getAllRoute: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root =>
      matchTags.all().flatMap(Ok(_))
  }

  private val getFromGroupId: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "fromGroup" / UUIDVar(groupId) =>
      for {
        m <- matchTags.getFromGroupId(groupId)
        resp <- Ok(m)
      } yield resp
  }

  val routes: HttpRoutes[F] = Router(
    prefix -> (createMatchRoute <+> getAllRoute <+> getFromGroupId)
  )
}

object MatchTagRoutes {
  def resource[F[_]: Concurrent](matches: MatchTags[F]): Resource[F,MatchTagRoutes[F]] =
    Resource.pure(new MatchTagRoutes[F](matches))
}
