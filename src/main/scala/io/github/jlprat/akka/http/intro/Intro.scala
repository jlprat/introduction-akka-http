package io.github.jlprat.akka.http.intro

import akka.http.scaladsl.server.{HttpApp, Route}

/**
  * Short server using [[HttpApp]] to help getting started
  * Created by @jlprat on 14/09/2017.
  */
class Intro extends HttpApp {
  override protected[intro] def routes: Route = {
    path("hello") {
      complete("world!")
    }
  }
}

object Intro extends App {
  new Intro().startServer("localhost", 9000)
}
