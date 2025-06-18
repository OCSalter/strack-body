package com.strack.live.core

import java.util.UUID
import cats.effect.*
import cats.syntax.all.*
import com.strack.live.domain.strack.{ResumeHeader, ResumeItem}
import doobie.implicits.*
import doobie.postgres.implicits.*
import doobie.util.transactor.Transactor

trait Resumes [F[_]]{
  def allHeaders(): F[List[ResumeHeader]]
  def allItems(): F[List[ResumeItem]]
}

class ResumesLive[F[_]: Concurrent] private (transactor: Transactor[F]) extends Resumes [F] {

  override def allHeaders(): F[List[ResumeHeader]] =
    sql"""
      SELECT r.id, r.title_text, r.group_text, r.location_text, r.date_text
      FROM resume_entry r
       """.query[ResumeHeader].stream.compile.toList.transact(transactor)

  override def allItems(): F[List[ResumeItem]] =
    sql"""
      SELECT r.id, r.entry_id, r.item_text
      FROM resume_list_item r
       """.query[ResumeItem].stream.compile.toList.transact(transactor)
}

object ResumesLive {
  def make[F[_] : Concurrent](postgres: Transactor[F]): F[ResumesLive[F]] =
    new ResumesLive[F](postgres).pure[F]

  def resource[F[_] : Concurrent](postgres: Transactor[F]): Resource[F, ResumesLive[F]] =
    Resource.pure(new ResumesLive[F](postgres))
}
