package com.strack.live.core

import java.util.UUID
import cats.effect.*
import cats.syntax.all.*
import com.strack.live.domain.strack.GroupTag
import doobie.implicits.*
import doobie.postgres.implicits.*
import doobie.util.transactor.Transactor

trait GroupTags [F[_]]{
  def all(): F[List[GroupTag]]
  def getFromUserId(id: UUID): F[List[GroupTag]]
}

class GroupTagsLive[F[_]: Concurrent] private (transactor: Transactor[F]) extends GroupTags[F] {

  override def all(): F[List[GroupTag]] =
    sql"""
      SELECT g.id, g.group_name
      FROM groups g
       """.query[GroupTag].stream.compile.toList.transact(transactor)

  override def getFromUserId(id: UUID): F[List[GroupTag]] =
    sql"""
      SELECT g.id, g.group_name
      FROM groups g
      JOIN users_groups ug on g.id = ug.group_id
      WHERE ug.user_id = $id
       """.query[GroupTag].stream.compile.toList.transact(transactor)
}

object GroupTagsLive {
  def make[F[_]: Concurrent](postgres: Transactor[F]): F[GroupTagsLive[F]] =
    new GroupTagsLive[F](postgres).pure[F]

  def resource[F[_]: Concurrent](postgres: Transactor[F]): Resource[F, GroupTagsLive[F]] =
    Resource.pure(new GroupTagsLive[F](postgres))
}
