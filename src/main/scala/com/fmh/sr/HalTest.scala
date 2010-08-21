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

/**
Testing functionality of scala supervisors
**/

import se.scalablesolutions.akka.actor._
import se.scalablesolutions.akka.util.UUID
import se.scalablesolutions.akka.remote._
import se.scalablesolutions.akka.util.Logging
import se.scalablesolutions.akka.config.OneForOneStrategy
import se.scalablesolutions.akka.config.ScalaConfig._
import Actor._

class HalTestSupervisor

object HalTest {
  def apply(args:String) = {
    args match {
      case "server" => {
	println("Starting Supervisor Test Server")
	HalTestServer
      }
      case "client" => {
	println("Starting Supervisor Test Client")
	HalTestClient
      }
    }
  }
}

class HalTestClientActor extends Actor {
  val remAkt = RemoteClient.actorFor("service","localhost",9998)

  def receive = {
    case ("send ping") => remAkt ! "ping"
    case "pong" => {
      println("got pong")
    }
  }
}

object HalTestClient {
    val client = actorOf(new HalTestClientActor).start
    client ! "send ping"
}

class HalTestServerActor extends Actor {

  self.faultHandler = Some(OneForOneStrategy(5, 5000))
  self.trapExit = List(classOf[Exception])

  self.id = UUID.newUuid.toString

  def receive = {
    case "ping" => {
      println("got ping")
      self reply "pong"
    }
    case _ => throw new RuntimeException("received unknown message")
  }
}

object HalTestServer {
    RemoteNode.start("localhost", 9998)
    RemoteNode.register("service", actorOf[HalTestServerActor])
}
