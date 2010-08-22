/*
 * Copyright 2010 Roman Naumann
 *
 * This file is part of SR.
 *
 * SR is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SR is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SR.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.fmh.sr

import se.scalablesolutions.akka.config._
import se.scalablesolutions.akka.actor._
import se.scalablesolutions.akka.util._
import se.scalablesolutions.akka.persistence.common._
import se.scalablesolutions.akka.persistence.mongo._
import se.scalablesolutions.akka.stm.Transaction.Global._
import Actor._
import ScalaConfig._

class DB(dbName: String) extends Actor {
  self.lifeCycle = Some(LifeCycle(Permanent))

  private var db = atomic { MongoStorage.getMap(dbName) }

  log.info("MongoDB started...")

  override def postRestart(reason: Throwable) = {
    db = atomic { MongoStorage.getMap(dbName) }
  }

  def receive = {
    case DB.Clear => self reply_? atomic { db.clear }
    case DB.Put(k,v) => self reply_? atomic { db.put(k,v) }
    case DB.Get(k) => self reply atomic { db.get(k) }
  }
}

object DB {
  def apply(dbName: String): ActorRef = actorOf(new DB(dbName)).start

  def create(dbName: String): ActorRef = {
    val storage = apply(dbName)
    storage !! Clear match {
      case (Some(())) => return storage
      case res => throw new RuntimeException("could not empty mongo storage, res: "+res)
    }
  }

  case class Put(k:AnyRef, v:AnyRef)
  case class Get(k:AnyRef)
  case class Clear
}

object DBTest {
  def apply() {
    val s = DB("sr.nodes")
    s ! DB.Put("1","roman")
    s !! DB.Put("2","hendrik")
    s !! DB.Put("1","fmh")
    println(s !! DB.Get("2"))
    s !! DB.Clear
    println(s !! DB.Get("2"))

    System exit 0
  }
}
