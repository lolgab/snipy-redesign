package snipy
import snipy.CApi.PyList_New

object PyList {
  def empty[T <: PyObjectPtr](implicit z: PyZone): PyListPtr[T] =
    PyList_New(0).asInstanceOf[PyListPtr[T]]
}