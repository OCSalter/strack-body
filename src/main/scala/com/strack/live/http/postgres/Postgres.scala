package com.strack.live.http.postgres

import cats.effect.*
import com.strack.live.http.*
import doobie.ExecutionContexts
import doobie.hikari.HikariTransactor

object Postgres {
  def makePostgres = for {
    ec <- ExecutionContexts.fixedThreadPool[IO](32)
    transactor <- HikariTransactor.newHikariTransactor[IO](
      "org.postgresql.Driver",
      "jdbc:postgresql:strack",
      "docker",
      "docker",
      ec
    )
  } yield transactor
}
