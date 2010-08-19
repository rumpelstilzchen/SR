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
      }
      case "client" => {
	println("Starting Supervisor Test Client")
      }
    }
  }
}