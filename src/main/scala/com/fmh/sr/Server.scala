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

import se.scalablesolutions.akka.actor._
import se.scalablesolutions.akka.remote._
import se.scalablesolutions.akka.config.OneForOneStrategy
import se.scalablesolutions.akka.config.ScalaConfig._
import Actor._

class ServerActor extends Actor {

  self.faultHandler = Some(OneForOneStrategy(5, 5000))
  self.trapExit = List(classOf[Exception])

  self.id = UGen.newUUID.toString

  def receive = {
    case "ping" => {
      println("got ping")
      self.sender match {
        case Some(snd) => {
          val remC = RemoteClient.clientFor(snd.getHomeAddress)
          remC.addListener(actorOf[RemoteClientShutter].start)
          remC.connect
        }
        case None => ;
      }
      self reply "pong"
    }
  }

  private var remShutClients: List[RemoteClient] = List()
}

class RemoteClientShutter extends Actor {
  def receive = {
    case RemoteClientDisconnected(c) => {
      log.info("SHUTTING DOWN!")
      c.shutdown
    }
    case _ => ;
  }  
}

object Server {
  def apply(host_ip: String) = {
    RemoteNode.start(host_ip, 9999)
    RemoteNode.register("srv:service", actorOf[ServerActor])
  }
}
