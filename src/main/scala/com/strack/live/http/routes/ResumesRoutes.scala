package com.strack.live.http.routes

import cats.*
import cats.effect.*
import cats.syntax.all.*
import com.strack.live.core.Resumes
import com.strack.live.domain.strack.ResumeFull
import io.circe.generic.auto.*
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.server.Router
import org.http4s.*
import org.http4s.dsl.Http4sDsl

class ResumesRoutes[F[_]: Concurrent] private(resumes: Resumes[F]) extends Http4sDsl[F] {
  private val prefix = "/resumes"

  private val getAllResumeHeaders: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "headers" =>
      resumes.allHeaders().flatMap(h => Ok(h))
  }

  private val getAllResumeItems: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "items" =>
      resumes.allItems().flatMap(i => Ok(i))
  }

  private val getAllResumeFull: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root => for {
      h <- resumes.allHeaders()
      i <- resumes.allItems()
      f = h.map(header => ResumeFull(header,
        i.filter(item => item.headerId == header.id)))
      resp <- Ok(f)
    } yield resp
  }

  val routes: HttpRoutes[F] = Router(
    prefix -> (getAllResumeHeaders <+> getAllResumeItems <+> getAllResumeFull)
  )
}

object ResumesRoutes {
  def resource[F[_]: Concurrent](resumes: Resumes[F]): Resource[F, ResumesRoutes[F]] =
    Resource.pure(new ResumesRoutes[F](resumes))
}
