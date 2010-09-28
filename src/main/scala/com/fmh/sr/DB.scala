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
import org.scalaquery.ql.extended.PostgresDriver.Implicit._

object DB {
  //returns a Session if the database is initialized
  def apply[T](f: => T): T = db match {
    case Some(d) => return d withSession f
    case None => throw new RuntimeException("Database not yet initialized");
  }

  def initialize(user:String, pw:String) = db match {
    case None => db = Some(org.scalaquery.session.Database.forURL(db_url,
								  user = user,
								  password = pw,
								  driver = driver))
    case Some(_) => throw new RuntimeException("Database already inizialized")
  }

  //an utility method to create a table
  def createTable[T](table: Table[T]) = DB {
    table.ddl.create
  }

  def dropTable[T](table: Table[T]) = DB {
    table.ddl.drop
  }

  private var db:Option[Database] = None
  private val db_name = "sr"
  private val db_url = "jdbc:postgresql:sr"
  private val driver = "org.postgresql.Driver"
}

object DBCmd {
  def apply(user:String, pw:String, cmd:String = "none") = {
    DB.initialize(user,pw)
    cmd match {
      case "test" => {
	Authorization.actor !! AUTH("halconnen","pw2")
	System exit 0
      }
      case "add_testusers" => {
	DB {
	  Users.noID insert ("Roman","Naumann","fmh","a@b.de","pw1")
	  Users.noID insert ("Muh","Kuh","halconnen","a@b.de","pw2")
	}
      }
      case "create_tables" => {
	DB createTable Users
      }
      case "drop_tables" => {
	DB dropTable Users
      }
      case "none" => {
	Logger("Nothing to do")
      }
    }
  }
}
