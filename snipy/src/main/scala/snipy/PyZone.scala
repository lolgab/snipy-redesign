package snipy

import snipy.CApi.Py_DecRef

trait PyZone {
  def manage[T <: PyObjectPtr](obj: T): T
}
object PyZone {
  def apply[T](f: PyZone => T): T = {
    val zone = new PyZoneImpl
    try {
      f(zone)
    } finally {
      zone.close()
    }
  }

  val leakingZone: PyZone = new PyZoneImpl
}
private class PyZoneImpl extends PyZone {
  final class Node(val head: PyObjectPtr, val tail: Node)

  private var node: Node = _

  final def manage[T <: PyObjectPtr](obj: T): T = {
    node = new Node(obj, node)
    obj
  }

  final def close(): Unit = {
    while (node != null) {
      Py_DecRef(node.head)
      node = node.tail
    }
  }
}