package io.github.jlprat.akka.http.intro.handsOn4

import akka.http.scaladsl.server.{HttpApp, Route}
import org.apache.commons.net.ftp.FTPClient

import scala.concurrent.{ExecutionContext, Future}

object MyService {

  def listFiles()(implicit ec: ExecutionContext): Future[String] = Future {
    val ftp = new FTPClient
    ftp.connect("ftp.fu-berlin.de")
    ftp.login("anonymous", "")
    ftp.changeWorkingDirectory("doc/o-reilly")
    val files = ftp.listFiles()
    ftp.disconnect()
    files.map(_.getName).mkString("\n")
  }
}

class BlockingExample extends HttpApp {

  override protected def routes: Route = path("files") {
    extractActorSystem { implicit system =>
      implicit val ec: ExecutionContext = system.dispatchers.lookup("my-blocking-dispatcher")
      complete(MyService.listFiles())
    }
  }
}

object BlockingExample extends App {
  new BlockingExample().startServer("localhost", 9000)
}
