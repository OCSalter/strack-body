package com.strack.live.http.cors

import cats.effect.IO
import org.http4s.*
import org.http4s.server.middleware.CORS


object Cors {
  private val corsSettings = CORS.policy.withAllowOriginAll

  val apply = (r: HttpRoutes[IO]) => this.corsSettings.apply(r)
}
