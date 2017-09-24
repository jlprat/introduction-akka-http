package io.github.jlprat.akka.http.intro.handsOn1

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{FlatSpec, Matchers}

class PathExampleTest extends FlatSpec with ScalatestRouteTest with Matchers {

  behavior of "PathExample"
  private val routesToTest = new PathExample().routes


  it should "serve `/first` paths without slash" in {
    Get("/first") ~> routesToTest ~> check {
      responseAs[String] shouldBe "first path"
      status shouldBe StatusCodes.OK
    }
  }

  it should "serve `/first/` paths with slash" in {
    Get("/first/") ~> routesToTest ~> check {
      responseAs[String] shouldBe "first path"
      status shouldBe StatusCodes.OK
    }
  }

  it should "serve nested routes like `/first/second` only without trailing slash" in {
    Get("/first/second") ~> routesToTest ~> check {
      responseAs[String] shouldBe "second path"
      status shouldBe StatusCodes.OK
    }
  }

  it should "respond with `404` if route doesn't exist" in {
    Get("/notExisting") ~> Route.seal(routesToTest) ~> check {
      status shouldBe StatusCodes.NotFound
    }

    Get("/notExisting") ~> routesToTest ~> check {
      rejections shouldBe Seq.empty // when a route doesn't handle a request it returns an empty rejection list
    }
  }

  it should "serve other top level routes like `/other`" in {
    Get("/other") ~> routesToTest ~> check {
      responseAs[String] shouldBe "other path"
      status shouldBe StatusCodes.OK
    }
  }

  it should "serve other top level routes like `/other` but only if they are GET" in {
    Post("/other") ~> routesToTest ~> check {
      handled shouldBe false
    }
  }
}
