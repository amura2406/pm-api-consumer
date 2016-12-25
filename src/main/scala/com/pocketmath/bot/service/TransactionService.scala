package com.pocketmath.bot.service

import com.google.inject.Inject
import com.pocketmath.bot.client.PocketmathAPIClient
import com.pocketmath.bot.domain.Transaction
import com.twitter.util.Future

/**
  * Created by amura on 12/26/16.
  */
class TransactionService @Inject()(
  apiClient: PocketmathAPIClient
) {

  /**
    * Get all transactions and optionally can be filtered by year
    */
  def getAllTransactions(year: Option[Int] = None): Future[Array[Transaction]] = {
    apiClient.getTransactions map { trxs =>
      // Check whether year 'query' is set
      year match {
        case Some(year) => trxs.filter(_.timestamp.getYear == year) // Filter by year then returned the result
        case None => trxs // Return the original array if no year
      }
    }
  }

  /**
    * Ger highest value transaction and optionally can be filtered by year
    */
  def getHighestTransaction(year: Option[Int] = None): Future[Transaction] = getAllTransactions(year) map { trxs =>
    // Iterate through all element and find the max value
    trxs.maxBy(trx => trx.value)
  }

  /**
    * Get all transactions sorted by value in descending order, optionally can be filtered by year
    */
  def getAllTransactionsSortedByValueDesc(year: Option[Int] = None): Future[Array[Transaction]] = getAllTransactions(year) map { trxs =>
    // Sort by value descending
    trxs.sortWith((trx1, trx2) => trx1.value > trx2.value)
  }

  /**
    * Get average transaction value, optionally can be filtered by year & traderId
    */
  def getAverageValue(traderIndices: Option[Array[String]] = None, year: Option[Int] = None) = getAllTransactions(year) map { trxs =>
    // Check whether traderIds 'query' is set
    traderIndices match {
      case Some(indices) =>
        // Convert to set for performance on lookup
        val indicesSet = indices.toSet
        // Filter array to only yield all transactions that are made by specified traders
        trxs.filter(trx => indicesSet.contains(trx.traderId))
      case None => trxs // If no query, then return original array
    }
  } map { trxs =>
    // Sum all transactions value and then divide by size of array to get average
    trxs.foldLeft(BigDecimal(0))((acc, trx) => trx.value + acc) / trxs.size
  }
}
