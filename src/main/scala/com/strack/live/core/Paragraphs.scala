package com.strack.live.core

import java.util.UUID
import cats.effect.*
import cats.syntax.all.*
import com.strack.live.domain.strack.Paragraph
import doobie.implicits.*
import doobie.postgres.implicits.*
import doobie.util.transactor.Transactor

trait Paragraphs [F[_]]{
  def all(): F[List[Paragraph]]
  def fromId(id: UUID): F[List[Paragraph]]
}

class ParagraphsLive[F[_]: Concurrent] private (transactor: Transactor[F]) extends Paragraphs[F] {

  override def all(): F[List[Paragraph]] = sql"""
      SELECT p.id, p.header_text, p.body_text
      FROM paragraphs p
       """.query[Paragraph].stream.compile.toList.transact(transactor)

  override def fromId(id: UUID): F[List[Paragraph]] = sql"""
      SELECT p.id, p.header_text, p.body_text
      FROM paragraphs p
      WHERE p.id = $id
       """.query[Paragraph].stream.compile.toList.transact(transactor)
}

object ParagraphsLive {
  def make[F[_] : Concurrent](postgres: Transactor[F]): F[ParagraphsLive[F]] =
    new ParagraphsLive[F](postgres).pure[F]

  def resource[F[_] : Concurrent](postgres: Transactor[F]): Resource[F, ParagraphsLive[F]] =
    Resource.pure(new ParagraphsLive[F](postgres))
}