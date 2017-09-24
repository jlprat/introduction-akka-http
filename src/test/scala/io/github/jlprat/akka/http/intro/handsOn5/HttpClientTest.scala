package io.github.jlprat.akka.http.intro.handsOn5

import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.stream.ActorMaterializer
import akka.testkit.TestKit
import akka.util.ByteString
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}

import scala.concurrent.duration._

class HttpClientTest extends TestKit(ActorSystem("test-actor-system"))
  with FlatSpecLike with Matchers with BeforeAndAfterAll with ScalaFutures {

  behavior of "Akka Http Client"

  implicit val materializer = ActorMaterializer()

  override implicit def patienceConfig: PatienceConfig = PatienceConfig(timeout = Span(5, Seconds))

  override protected def afterAll(): Unit = {
    super.afterAll()
    TestKit.shutdownActorSystem(system)
  }

  it should "fetch a page and return the right status code" in {
    val response: HttpResponse = Http().singleRequest(HttpRequest(uri = "http://akka.io")).futureValue
    response.status shouldBe StatusCodes.OK
    response.discardEntityBytes()
  }

  it should "fetch the content of the page (small)" in {
    val response: HttpResponse = Http().singleRequest(HttpRequest(uri = "http://google.com")).futureValue
    println("-------------------------------------------")
    println(response.entity)
    println("-------------------------------------------")
    println(response.entity.dataBytes)
  }

  it should "fetch the content of the page (big)" in {
    val response: HttpResponse = Http().singleRequest(HttpRequest(uri = "http://akka.io")).futureValue
    println("*******************************************")
    println(response.entity)
    println("*******************************************")
    println(response.entity.dataBytes)
    println("*******************************************")
    val page = response.entity.dataBytes.runFold(ByteString(""))(_ ++ _).futureValue
    println(page.utf8String)
  }

  it should "fetch the content of the page (big) using toStrict" in {
    val response: HttpResponse = Http().singleRequest(HttpRequest(uri = "http://akka.io")).futureValue
    println("###########################################")
    println(response.entity)
    println("###########################################")
    println(response.entity.dataBytes)
    println("###########################################")
    println(response.entity.toStrict(3.seconds).futureValue)
  }

  it should "die unless entity is consumed" in {
    for {
      i <- 0 until 10
      _ = println(i)
      response = Http().singleRequest(HttpRequest(uri = "http://akka.io")).futureValue
      _ = response.discardEntityBytes()
    } yield Done
  }
}
