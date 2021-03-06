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

object App {
  def main(args: Array[String]) {
    println("Starting Program")
    args(0) match {
      case "--server" => {
        println("################ SERVER")
        Server start args(1)
      }
      case "--client" => {
        println("################ CLIENT")
        TestClient(args(1))
      }
      /*case "--haltest" => {
	println("################ AKKA PERFORMANCE BENCHMARK")
	HalTest(args(1))
      }*/
      case "--dbtest" => {
        println("################ DB Test")
	val user = args(1)
	val pw = args(2)
	if(args.size > 3) {
	  val cmd = args(3);
          DBCmd(user,pw,cmd)
	} else {
	  DBCmd(user,pw)
	}
      }
    }
  }
}

