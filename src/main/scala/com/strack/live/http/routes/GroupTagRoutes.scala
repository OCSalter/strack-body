package com.strack.live.http.routes

import cats.*
import cats.effect.*
import cats.syntax.all.*
import com.strack.live.core.GroupTags
import io.circe.generic.auto.*
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.server.Router
import org.http4s.*
import org.http4s.dsl.Http4sDsl

class GroupTagRoutes[F[_]: Concurrent] private(groupTags: GroupTags[F]) extends Http4sDsl[F] {
  private val prefix = "/groups"

  private val getAllRoute: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root =>
      groupTags.all().flatMap(g => Ok(g))
  }

  private val getFromUserId: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "fromUser" / UUIDVar(u) => for {
      g <- groupTags.getFromUserId(u)
      resp <- Ok(g)
    } yield resp
  }

  val routes: HttpRoutes[F] = Router(
    prefix -> (getAllRoute <+> getFromUserId)
  )
}

object GroupTagRoutes {
  def resource[F[_]: Concurrent](groupTags: GroupTags[F]): Resource[F, GroupTagRoutes[F]] =
    Resource.pure(new GroupTagRoutes[F](groupTags))
}
