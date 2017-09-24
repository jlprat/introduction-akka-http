package io.github.jlprat.akka.http.intro.handsOn3

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{FlatSpec, Matchers}

class BookStoreTest extends FlatSpec with ScalatestRouteTest with Matchers with BookJsonSupport {

  behavior of "BookStore Server"

  private val routesToTest = new BookStore().routes

  it should "reply with books when asked" in {
    Get("/book/1") ~> routesToTest ~> check {
      status shouldBe StatusCodes.OK
      responseAs[Book] shouldBe Book(1, "Scala: From a Functional Programming Perspective")
      responseAs[String] shouldBe """{"id":1,"title":"Scala: From a Functional Programming Perspective"}"""
    }
  }

  it should "reply with empty response if book doesn't exist" in {
    Get("/book/2") ~> routesToTest ~> check {
      status shouldBe StatusCodes.NotFound
      responseAs[String] shouldBe "I do not know such book - 2"
    }
  }

  it should "ignore (404) book queries with no right Id" in {
    Get("/book/1a") ~> Route.seal(routesToTest) ~> check {
      status shouldBe StatusCodes.NotFound
      responseAs[String] shouldBe "Nothing to see here!"
    }
  }

  it should "handle exceptions correctly when occurr" in {
    Get("/kaboom") ~> Route.seal(routesToTest) ~> check {
      status shouldBe StatusCodes.InternalServerError
      responseAs[String] shouldBe "Do you math?"
    }
  }

}
