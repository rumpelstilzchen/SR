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


object TestClient {
  def apply(srv_ip:String) = {
    val client = actorOf(new TestClientActor(srv_ip)).start
    client ! SEND_PING()
  }

  private case class SEND_PING()

  private class TestClientActor(val srvIp:String) extends Actor {
    val remAkt = RemoteClient.actorFor("srv:service",srvIp,9999)


    def receive = {
      case _:SEND_PING => {
	remAkt ! PING()
      }
      case _:PONG => {
	println("got pong")
      }
      case UNKNOWN_MSG(msg) => {
	println("server couldn't deal with msg: "+msg);
      }
    }
  }
}
