package io.github.jlprat.akka.http.intro.handsOn2

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
      status shouldBe StatusCodes.OK
      responseAs[String] shouldBe ""
    }
  }

  it should "be able to create new books" in {
    val book = Book(2, "Big Data SMACK")
    Put("/book", book) ~> routesToTest ~> check {
      status shouldBe StatusCodes.OK
      responseAs[Book] shouldBe book
    }
  }

  it should "be able to update existing books" in {
    val book = Book(1, "Scala: FFPP")
    Put("/book", book) ~> routesToTest ~> check {
      status shouldBe StatusCodes.OK
      responseAs[Book] shouldBe book
    }
    Get("/book/1") ~> routesToTest ~> check {
      status shouldBe StatusCodes.OK
      responseAs[Book] shouldBe book
    }
  }

  it should "ignore (404) book queries with no right Id" in {
    Get("/book/1a") ~> Route.seal(routesToTest) ~> check {
      status shouldBe StatusCodes.NotFound
    }
  }

  it should "not accept creations with things that are not books" in {
    Put("/book", (3, "Looks Like a Book")) ~> Route.seal(routesToTest) ~> check {
      status shouldBe StatusCodes.BadRequest
    }
    
    Put("/book") ~> Route.seal(routesToTest) ~> check {
      status shouldBe StatusCodes.BadRequest
    }
  }
}
