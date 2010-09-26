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
import se.scalablesolutions.akka.actor.Actor._
import org.scalaquery.ql.extended.{ExtendedTable => Table}
import org.scalaquery.ql.TypeMapper._
import org.scalaquery.ql._
import org.scalaquery.session._
import org.scalaquery.session.Database.threadLocalSession
//import org.scalaquery.ql.basic.BasicDriver.Implicit._
import org.scalaquery.ql.extended.MySQLDriver.Implicit._

object Database {
  def actor(): ActorRef = akt match {
    case Some(a) => return a
    case None => throw new RuntimeException("Database not yet initialized");
  }

  def initialize(user:String, pw:String) = akt match {
    case None => akt = Some(actorOf(new DBActor(user,pw)).start)
    case Some(_) => throw new RuntimeException("Database already inizialized")
  }

  private var akt:Option[ActorRef] = None
  

  //actor msgs
  case class CREATE_TABLE(tableDDL: DDL)

  private class DBActor(user:String, pw:String) extends Actor {
    def receive = {
      case CREATE_TABLE(ddl) => {
	Logger("CREATING TABLE")
	try {
	  db withSession {
	    ddl.create
	  }
	  Logger("TABLE CREATED")
	  self reply_? SUCC()
	} catch {
	  case e:com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException => {
	    Logger("TABLE CREATION FAILED DUE TO SQL-EXCEPTION")
	    self reply_? FAIL()
	  }
	}
      }
    }

    val db_name = "sr"
    val db_url = "jdbc:mysql://localhost/sr"
    val driver = "com.mysql.jdbc.Driver"
    val db = org.scalaquery.session.Database.forURL(db_url,
						    user = user,
						    password = pw,
						    driver = driver)
  }
}

object DBCmd {
  def apply(user:String, pw:String, cmd:String = "none") = {
    Database.initialize(user,pw)
    val db = Database.actor
    cmd match {
      case "create_tables" => {
	db ! new Database.CREATE_TABLE(Users.ddl)
      }
      case "none" => {
	Logger("Nothing to do")
      }
    }
  }
}
