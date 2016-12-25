# PocketMath API Consumer 

This repo invokes several endpoints on PocketMath API and then process the retrieved data.

There are 4 tasks:

* Find all traders from Singapore and sort them by name.
* Find the transaction with the highest value.
* Find all transactions in the year 2016 and sort them by value (high to small).
* Find the average of transactions' values from the traders living in Beijing.

This app is using Scala + Guice + Finagle

To start this app please make sure **Scala 2.11.x** & **SBT 0.13.x** already installed.

Go into root of this project and type on terminal

``` bash
sbt run
```