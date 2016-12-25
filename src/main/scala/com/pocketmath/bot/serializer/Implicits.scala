package com.pocketmath.bot.serializer

import java.time.{Instant, ZoneOffset, ZonedDateTime}

import io.circe.{Decoder, Encoder}
import cats.syntax.either._

/**
  * Created by amura on 12/26/16.
  */
object Implicits {
  implicit val encodeInstant: Encoder[ZonedDateTime] = Encoder.encodeLong.contramap[ZonedDateTime](_.toEpochSecond)

  implicit val decodeInstant: Decoder[ZonedDateTime] = Decoder.decodeLong.emap { ts =>
    Either.catchNonFatal(ZonedDateTime.ofInstant(Instant.ofEpochSecond(ts), ZoneOffset.UTC)).leftMap(t => "ZonedDateTime")
  }
}
