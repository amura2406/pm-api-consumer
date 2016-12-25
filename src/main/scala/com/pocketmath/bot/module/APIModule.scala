package com.pocketmath.bot.module

import com.google.inject.AbstractModule
import com.pocketmath.bot.client.PocketmathAPIClient
import net.codingwell.scalaguice.ScalaModule

/**
  * Created by amura on 12/25/16.
  */
class APIModule extends AbstractModule with ScalaModule{

  override def configure(): Unit = {
    bind[PocketmathAPIClient]
  }
}
