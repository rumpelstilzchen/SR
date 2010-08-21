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

class DB extends Actor {
  self.lifeCycle = Some(LifeCycle(Permanent))

  val FILENAME = "nodes.db"
  private var db = atomic { MongoStorage.getMap(FILENAME) }
  log.info("MongoDB started...")

  override def postRestart(reason: Throwable) = {
    db = atomic { MongoStorage.getMap(FILENAME) }
  }

  def receive = {
    case Add(k,v) => db += (k,v)
    case Get(k) => self reply db(k)
  }

  case class Add(k:AnyRef, v:AnyRef)
  case class Get(k:AnyRef)
}

object DBTest {
  def apply() {
    val storage = actorOf[DB].start
  }
}
