package com.pocketmath.bot.domain

import java.time.ZonedDateTime

/**
  * Domain class represents a transaction
  */
case class Transaction(
  timestamp: ZonedDateTime, // To make it easy when retrieving YEAR field
  traderId: String,
  value: BigDecimal
)
