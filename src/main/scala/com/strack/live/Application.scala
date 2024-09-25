package com.strack.live

import cats.effect.*
import com.strack.live.http.*
import com.comcast.ip4s.*
import com.strack.live.http.cors.Cors
import com.strack.live.http.postgres.Postgres
import org.http4s.ember.server.EmberServerBuilder


object Application extends IOApp.Simple {
  private def makeServer = for {
    postgres <- Postgres.makePostgres
    routes <- Routes.allRoutes(postgres)
    server <- EmberServerBuilder
      .default[IO]
      .withHost(host"0.0.0.0")
      .withPort(port"4010")
      .withHttpApp(Cors.apply(routes).orNotFound)
      .build
  } yield server

  override def run: IO[Unit] = makeServer.use(_ => IO.println("Server Up!") *> IO.never)
}
