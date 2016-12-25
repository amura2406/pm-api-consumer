package com.pocketmath.bot.service

import com.google.inject.Inject
import com.pocketmath.bot.client.PocketmathAPIClient
import com.pocketmath.bot.domain.Trader
import com.twitter.util.Future

/**
  * Created by amura on 12/25/16.
  */
class TraderService @Inject()(
  apiClient: PocketmathAPIClient
) {

  /**
    * Get all traders and optionally can be filtered by country
    */
  def getAllTraders(country: Option[String] = None): Future[Array[Trader]] = {
    apiClient.getTraders map { traders =>
      // Check whether country 'query' is set
      country match {
        case Some(country) => traders filter(_.city == country) // Filter by country then returned the result
        case None => traders // Return the original array if no country
      }
    }
  }

  /**
    * Get all traders sorted by name in lexicographical order, optionally can be filtered by country
    */
  def getAllTradersSortByName(country: Option[String] = None): Future[Array[Trader]] = getAllTraders(country) map { traders =>
    // Sort the resulted array by name
    traders.sortWith((t1, t2) => t1.name.compareTo(t2.name) < 0)
  }
}
