package io.github.jlprat.akka.http.intro.handsOn1

import akka.http.scaladsl.server.{HttpApp, Route}

/**
  * Showcases some directives and how to concatenate them or use them together
  */
class PathExample extends HttpApp {
  override protected[handsOn1] def routes: Route = {
    pathPrefix("first") {
      concat(
        pathEndOrSingleSlash {
          complete("first path")
        },
        path("second") {
          complete("second path")
        }
      )
    } ~
    path("other") {
      get {
        complete("other path")
      }
    }
  }
}

object PathExample extends App {
  new PathExample().startServer("localhost", 9000)
}
