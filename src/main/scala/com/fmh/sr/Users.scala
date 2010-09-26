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

import org.scalaquery.ql.extended.{ExtendedTable => Table}
import org.scalaquery.ql.TypeMapper._
import org.scalaquery.ql._

@serializable
case class User(id: Int, first: String
               ,last: String, nick: String
	       ,email: String, pw: String)

object Users extends Table[User]("users") {
  def id = column[Int]("id", O PrimaryKey, O AutoInc, O NotNull)
  def first = column[String]("first", O NotNull)
  def last = column[String]("last", O NotNull)
  def nick = column[String]("nick", O NotNull)
  def email = column[String]("email", O NotNull)
  def pw = column[String]("pw", O NotNull)
  def * = id ~ first ~ last ~ nick ~ email ~ pw <> (User, User.unapply _)
}
