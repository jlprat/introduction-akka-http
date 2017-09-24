package io.github.jlprat.akka.http.intro.handsOn3

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server._
import spray.json.DefaultJsonProtocol

case class Book(id: Int, title: String)

trait BookJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val bookFormat = jsonFormat2(Book)

}

case class NonExistentBook(id: Int) extends Rejection

class BookStore extends HttpApp with BookJsonSupport {

  private val rejectionHandler = RejectionHandler.newBuilder()
    .handle {
      case NonExistentBook(id) => complete(StatusCodes.NotFound, s"I do not know such book - $id")
    }
    .handleNotFound(complete(StatusCodes.NotFound, "Nothing to see here!"))
    .result()

  private val exceptionHandler = ExceptionHandler.apply {
    case _: ArithmeticException => complete(StatusCodes.InternalServerError, "Do you math?")
  }

  private val books = Map(1 -> Book(1, "Scala: From a Functional Programming Perspective"))

  override protected[handsOn3] def routes: Route = {
    (handleRejections(rejectionHandler) & handleExceptions(exceptionHandler)) {
      concat(
        (get & path("book" / IntNumber)) { id =>
          if (books.isDefinedAt(id)) complete(books(id))
          else reject(NonExistentBook(id))
        },
        path("kaboom") {
          complete(s"1/0 is ${1 / 0}")
        }
      )
    }
  }
}

object BookStore extends App {
  new BookStore().startServer("localhost", 9000)
}
