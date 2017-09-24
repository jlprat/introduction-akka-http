package io.github.jlprat.akka.http.intro.handsOn2

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.{HttpApp, Route}
import spray.json.DefaultJsonProtocol

case class Book(id: Int, title: String)

trait BookJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val bookFormat = jsonFormat2(Book)

}

class BookStore extends HttpApp with BookJsonSupport {

  private var books = Map(1 -> Book(1, "Scala: From a Functional Programming Perspective"))

  override protected[handsOn2] def routes: Route = {
    concat(
      (get & path("book" / IntNumber)) { id =>
        complete(books.get(id))
      },
      (put & path("book")) {
        entity(as[Book]) { book =>
          books = books + (book.id -> book)
          complete(book)
        }
      }
    )
  }
}

object BookStore extends App {
  new BookStore().startServer("localhost", 9000)
}
