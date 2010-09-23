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

import se.scalablesolutions.akka.config._
import se.scalablesolutions.akka.actor._
import se.scalablesolutions.akka.util._
import se.scalablesolutions.akka.persistence.common._
import se.scalablesolutions.akka.persistence.mongo._
import se.scalablesolutions.akka.stm.Transaction.Global._
import Actor._
import ScalaConfig._
import java.io._

class DB(dbName: String) extends Actor {
  self.lifeCycle = Some(LifeCycle(Permanent))

  private var db = atomic { MongoStorage.getMap(dbName) }

  log.info("MongoDB started...")

  override def postRestart(reason: Throwable) = {
    db = atomic { MongoStorage.getMap(dbName) }
  }

  private def toByteArray(obj: AnyRef) : Array[Byte] = {
    val bos = new ByteArrayOutputStream
    val oos = new ObjectOutputStream(bos)
    oos writeObject obj
    oos.close
    return bos.toByteArray
  }

  private def fromByteArray[T](arr: Array[Byte]) : T = {
    val bis = new ByteArrayInputStream(arr)
    val ois = new ObjectInputStream(bis)
    return ois.readObject.asInstanceOf[T]
  }

  def receive = {
/*    case DB.Put(k,v) => self reply_? atomic {
      db.put(k,v) 
    }
    case DB.PutNode(n) => self reply_? atomic { db.put(n.uuid,n.data) }
    case DB.Get(k) => self reply atomic { db.get(k) }
    case DB.GetNode(u) => self reply atomic {
      db.get(u) match {
        case None => None
        case Some(data) => Node(u,data)
      }
    }
    case DB.Clear => self reply_? atomic { db.clear }*/
    case x:String => println("got: "+x);
  }
}

object DB {
  def apply(dbName: String): ActorRef = actorOf(new DB(dbName)).start

  def create(dbName: String): ActorRef = {
    val storage = apply(dbName)
    storage !! Clear match {
      case (Some(())) => return storage
      case res => throw new RuntimeException("could not empty mongo storage, res: "+res)
    }
  }

  case class Put(k: Long, v:AnyRef)
  case class PutNode(n:Node)
  case class Get(k: Long)
  case class GetNode(n:String)
  case class Clear
}

object DBTest {
  def apply() {
    val s = DB("sr.nodes")

/*    s ! DB.Put("1","roman")
    s !! DB.Put("2","hendrik")
    s !! DB.Put("1","fmh")
    
    val n = Node("ROMAN IST TOLL")
    println(n)

    s !! DB.PutNode(n)
    println(s !! DB.GetNode(n.uuid))

    println(s !! DB.Clear)
    println(s !! DB.GetNode(n.uuid))*/

    System exit 0
  }
}
