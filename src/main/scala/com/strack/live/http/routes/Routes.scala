package com.strack.live.http

import cats.effect.IO
import cats.effect.kernel.Resource
import cats.syntax.all.*
import com.strack.live.core.{GroupTagsLive, MatchTagsLive, PlayerTagsLive, UsersLive}
import com.strack.live.http.routes.GroupTagRoutes
import doobie.hikari.HikariTransactor
import org.http4s.HttpRoutes
import org.http4s.server.Router

object Routes {

  def allRoutes(postgres: HikariTransactor[IO]): Resource[IO, HttpRoutes[IO]] = for {
    matches <- MatchTagsLive.resource(postgres)
    matchTagApi <- MatchTagRoutes.resource(matches)
    playerTags <- PlayerTagsLive.resource(postgres)
    playerTagApi <- PlayerTagRoutes.resource(playerTags)
    users <- UsersLive.resource(postgres)
    usersApi <- UsersRoutes.resource(users)
    groupTags <- GroupTagsLive.resource(postgres)
    groupTagApi <- GroupTagRoutes.resource(groupTags)
    routes = Router(""->(matchTagApi.routes <+> playerTagApi.routes <+> usersApi.routes <+> groupTagApi.routes))
  } yield routes

}
