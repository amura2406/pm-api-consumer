package com.pocketmath.bot

import com.google.inject.Guice
import com.pocketmath.bot.module.{APIModule, TypesafeConfigModule}
import com.pocketmath.bot.service.{TraderService, TransactionService}
import com.twitter.util.Await

/**
  * Created by amura on 12/25/16.
  */
object Main extends App{
  println("Program started!")

  // Use Guice for lightweight Dependency Injection
  val injector = Guice.createInjector(
    new TypesafeConfigModule, // Load the file configuration
    new APIModule // Provides API client
  )

  import net.codingwell.scalaguice.InjectorExtensions._

  // Create the services instance
  val traderSvc = injector.instance[TraderService]
  val trxSvc = injector.instance[TransactionService]

  // Create request to get all traders from Singapore in async
  val fut1 = traderSvc.getAllTradersSortByName(country = Some("Singapore")) map { arr =>
    // Process this block only if data is fetched & parsed successfully
    println(s"ALL TRADERS FROM SINGAPORE SORT BY NAME\n${arr.mkString("\n")}\n")
  }

  // Create request to get highest transaction in async
  val fut2 = trxSvc.getHighestTransaction() map { trx =>
    // Process this block only if data is fetched & parsed successfully
    println(s"HIGHEST TRANSACTION VALUE\n$trx\n")
  }

  // Create request to get all transactions in 2016 sorted by value in async
  val fut3 = trxSvc.getAllTransactionsSortedByValueDesc(year = Some(2016)) map { arr =>
    // Process this block only if data is fetched & parsed successfully
    println(s"ALL TRANSACTIONS IN 2016 SORTED BY VALUE\n${arr.mkString("\n")}\n")
  }

  // Create request to get average transactions value from Beijing traders in async
  val fut4 = for{
    traders <- traderSvc.getAllTraders(country = Some("Beijing"))
    // Process this block only if data is fetched & parsed successfully
    // Create an array of only the traderId
    tradersSet = traders map (_.id)
    avg     <- trxSvc.getAverageValue(traderIndices = Some(tradersSet))
  } yield {
    // Process this block only if data is fetched & parsed successfully
    println(s"AVERAGE TRANSACTIONS VALUE FROM TRADERS IN BEIJING\n$avg\n")
  }

  // Wait all future to be completed then program may exit
  // All future operations are done in parallel
  Await.result(fut1)
  Await.result(fut2)
  Await.result(fut3)
  Await.result(fut4)

  println("Program finished !")
}
