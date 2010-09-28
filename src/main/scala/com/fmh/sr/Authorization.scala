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
import Actor._
import org.scalaquery.ql._
import org.scalaquery.ql.TypeMapper._
import org.scalaquery.session._
import org.scalaquery.session.Database.threadLocalSession
import org.scalaquery.ql.extended.PostgresDriver.Implicit._

object Authorization {
  val actor = actorOf[AuthorizationActor].start

  //valid messages (case classes)
  //AUTH

  //actor definition
  private class AuthorizationActor extends Actor {
    def receive = {
      case AUTH(nick,pw) => {
	val query = for {
	  n <- Parameters[String]
	  u <- Users if u.nick is n
	} yield u.nick ~ u.password
	Logger(query(nick).selectStatement)
        Logger(DB(query(nick).list))
	self reply_? SUCC()
      }
      case msg => {
	println("actor Authorization got unknown msg "+msg.toString+", exiting")
	System exit 1
      }
    }
  }
}
