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
import se.scalablesolutions.akka.util.UUID
import se.scalablesolutions.akka.remote._
import se.scalablesolutions.akka.util.Logging
import se.scalablesolutions.akka.config.OneForOneStrategy
import se.scalablesolutions.akka.config.ScalaConfig._
import Actor._

class TestClientActor extends Actor {
  def receive = {
    case ("send ping",akt:ActorRef) => akt ! "ping"
    case "pong" => {
      println("got pong")
    }
    case m => throw new RuntimeException("received unknown message: "+m)
  }
}

object TestClient {
  val supervisor = Supervisor(
    SupervisorConfig(
      RestartStrategy(OneForOne, 3, 1000, List(classOf[Exception])),
      Nil
    )
  )

  def apply(srv_ip:String) = {
    val remActor = RemoteClient.actorFor("srv",srv_ip,9999)
    val client = Actor.actorOf[TestClientActor]
    
    client.lifeCycle = Some(LifeCycle(Temporary))
    supervisor link client


    client.start
    client ! ("send ping",remActor)
  }
}
