package com.strack.live.http.routes

import cats.*
import cats.effect.*
import cats.syntax.all.*
import com.strack.live.core.Paragraphs
import io.circe.generic.auto.*
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.server.Router
import org.http4s.*
import org.http4s.dsl.Http4sDsl

class ParagraphsRoutes[F[_]: Concurrent] private(paragraphs: Paragraphs[F]) extends Http4sDsl[F] {
  private val prefix = "/paragraphs"

  private val getAllParagraphs: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root =>
      paragraphs.all().flatMap(p => Ok(p))
  }

  private val getParagraphsFromId: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "fromId" / UUIDVar(u) =>
      paragraphs.fromId(u).flatMap(p => Ok(p))
  }

  val routes: HttpRoutes[F] = Router(
    prefix -> (getAllParagraphs <+> getParagraphsFromId)
  )

}


object ParagraphsRoutes {
  def resource[F[_]: Concurrent](paragraphs: Paragraphs[F]): Resource[F, ParagraphsRoutes[F]] =
    Resource.pure(new ParagraphsRoutes[F](paragraphs))
}