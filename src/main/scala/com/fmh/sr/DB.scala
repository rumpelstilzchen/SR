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

import se.scalablesolutions.akka.stm.Transaction.Global._
import se.scalablesolutions.akka.config._
import se.scalablesolutions.akka.actor._
import redis.clients.jedis._
import Actor._
import ScalaConfig._
import java.io._
import java.nio.charset.Charset
import net.iharder._

class DB(collName: String) extends Actor {
  self.lifeCycle = Some(LifeCycle(Permanent))

  private var db = getDB
  

  private def getDB() :Jedis = {
    val r = atomic { new Jedis("localhost") }
    log.info("Connected to Redis DB...")
    return r
  }
  
  override def postRestart(reason: Throwable) = {
    db = getDB
  }

  private def toStr(obj: Serializable): String = {
    return Base64 encodeObject obj
  }

  private def fromStr[T](str: String) :T = {
    return Base64.decodeToObject(str).asInstanceOf[T]
  }

/*  def tests() = {
    //write
    val k:Long = 12345
    val v:String = "helo"
    atomic { db.set(k.toString,toStr(v)) }

    //test
    println("###test")
    println(fromStr[String](toStr("hello")))
    println("###/test")

    //read
    val r = atomic {db.get(k.toString) }
    if(r==null)
      println(null)
    else {
      for(val i <- 0 until r.size)
	println(r(i));
      println(fromStr[String](r))
    }
  }*/

  def receive = {
    case DB.Put(k,v) => {
      self reply_? atomic { db.set(k.toString,toStr(v)) }
    }
    case (x:DB.Get[t]) => {
      val res = atomic {db.get(x.k.toString) } 
      if(res==null)
	self reply None
      else
	self reply fromStr[t](res)
    }
    //    case DB.PutNode(n) => atomic { self reply_? (db += n.uuid -> n.data) }
    //    case x:DB.GetNode[t](u) => self reply atomic {
    //      get(u) match {
    //        case None => None
    //        case Some(data) => Node(u,data)
    //      }
    //    }
    //    case DB.Clear => self reply_? atomic { db.clear }
  }
}

object DB {
  def apply(dbName: String): ActorRef = actorOf(new DB(dbName)).start

  def create(dbName: String): ActorRef = {
    val storage = apply(dbName)
    storage !! Clear match {
      case (Some(())) => return storage
      case res => throw new RuntimeException("could not empty redis storage, res: "+res)
    }
  }

  case class Put(k: Long, v:Serializable)
  case class PutNode[T <: Serializable](n:Node[T])
  case class Get[T](k: Long)
  case class GetNode[T](n:String)
  case class Clear
}

object DBTest {
  def apply() {
    val s = DB("sr.nodes")
    

    println(s !! DB.Put(1,"roman"))
    println(s !! DB.Put(2,"hendrik"))
    println(s !! DB.Put(1,"fmh"))
    
    //val n = Node("ROMAN IST TOLL")
    //println(n)

/*    s !! DB.PutNode(n)
    println(s !! DB.GetNode(n.uuid))

    println(s !! DB.Clear)
    println(s !! DB.GetNode(n.uuid))*/

    println(s !! DB.Get[String](1))
    println(s !! DB.Get[String](2))
    
    System exit 0
  }
}
