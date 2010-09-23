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

import se.scalablesolutions.akka.util.UUID

@serializable
@SerialVersionUID(0)
class Node(uuid_c: Long,data_c: AnyRef) {
  val uuid: Long   = uuid_c
  val data: AnyRef = data_c
  override def toString(): String = "Node["+uuid+"]"
}

object Node {
  def apply(data: AnyRef): Node = new Node(UUID.newUuid,data)
  def apply(uuid: Long, data: AnyRef): Node = new Node(uuid,data)
}
