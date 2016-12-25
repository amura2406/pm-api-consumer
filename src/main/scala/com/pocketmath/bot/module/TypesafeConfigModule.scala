package com.pocketmath.bot.module

import com.github.racc.tscg.{TypesafeConfigModule => TCMModule}
import com.google.inject.AbstractModule
import com.typesafe.config.{Config, ConfigFactory}
import net.codingwell.scalaguice.ScalaModule

/**
  * Created by amura on 12/25/16.
  */
class TypesafeConfigModule extends AbstractModule with ScalaModule{

  override def configure(): Unit = {
    val config = ConfigFactory load "application.conf"

    install(TCMModule.fromConfigWithPackage(config, "com.pocketmath.bot"))
    bind[Config].toInstance(config)
  }
}
