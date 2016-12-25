package com.pocketmath.bot.client

import com.google.inject.Inject
import com.pocketmath.bot.domain.{Trader, Transaction}
import com.twitter.finagle.Http
import com.twitter.finagle.service.{Backoff, RetryBudget}
import com.twitter.conversions.time._
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.Future
import com.typesafe.config.Config
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import com.pocketmath.bot.serializer.Implicits._

/**
  * This class abstract all communication to the Pocketmath API server
  * Exchange message is in JSON format, so Circe is used for performance reason (Macro optimized)
  */
class PocketmathAPIClient @Inject()(
  config: Config
){

  private val apiKeyHeaderKey = "x-api-key"
  private val apiKey = config.getString("client.http.apiKey")

  lazy val httpClient = {
    val cfg = config.getConfig("client.http")

    val host = cfg.getString("host")

    Http.client
      .withLabel(cfg.getString("label"))
      .withRetryBackoff(Backoff.decorrelatedJittered(8 seconds, 16 seconds))
      .withRetryBudget(RetryBudget(10 seconds, 4, 0.5))
      .withRequestTimeout(15 seconds)
      .withSessionQualifier.noFailFast
      .withSessionPool.minSize(cfg.getInt("minPool"))
      .withSessionPool.maxSize(cfg.getInt("maxPool"))
      .withSessionPool.maxWaiters(cfg.getInt("maxWaiters"))
      .withTls(host)
      .newService(s"$host:443")
  }

  /**
    * Sends HTTP request to server and if supplied handler can't satisfy the response then return IllegalStateException
    */
  private[pocketmath] def doRequest[T](request: Request)(handler: PartialFunction[Response, Future[T]]): Future[T] = {
    request.headerMap += apiKeyHeaderKey -> apiKey
    httpClient(request) flatMap (handler orElse{
      case _ => Future.exception(new IllegalStateException)
    })
  }

  /**
    * Invoke /prod/traders on the server and parse the JSON into domain objects
    */
  def getTraders = {
    val req = Request("/prod/traders")
    doRequest[Array[Trader]](req){
      case r:Response =>
        decode[Array[Trader]](r.getContentString()) match {
          case Left(failure) => Future.exception(failure) // Occur when parsing error
          case Right(traders) => Future.value(traders)
        }
    }
  }

  /**
    * Invoke /prod/transactions on the server and parse the JSON into domain objects
    */
  def getTransactions = {
    val req = Request("/prod/transactions")
    doRequest[Array[Transaction]](req){
      case r:Response =>
        decode[Array[Transaction]](r.getContentString()) match {
          case Left(failure) => Future.exception(failure) // Occur when parsing error
          case Right(transactions) => Future.value(transactions)
        }
    }
  }
}
