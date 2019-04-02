package snipy

import snipy.CApi._
import scala.scalanative.native._

trait AsPython[-T, +U <: PyObjectPtr] {
  def asPython(x: T)(implicit z: PyZone): U
}
object AsPython {
  implicit val cDoubleAsPython: AsPython[CDouble, PyFloatPtr] =
    new AsPython[CDouble, PyFloatPtr] {
      override def asPython(d: CDouble)(implicit z: PyZone): PyFloatPtr =
        PyFloat_FromDouble(d)
    }

  implicit val cLongAsPython: AsPython[CLong, PyLongPtr] =
    new AsPython[CLong, PyLongPtr] {
      override def asPython(v: CLong)(implicit z: PyZone): PyLongPtr =
        PyLong_FromLong(v)
    }

  implicit val intAsPython: AsPython[Int, PyLongPtr] =
    new AsPython[Int, PyLongPtr] {
      override def asPython(v: Int)(implicit z: PyZone): PyLongPtr =
        PyLong_FromLong(v)
    }

  implicit val stringAsPython: AsPython[String, PyStringPtr] =
    new AsPython[String, PyStringPtr] {
      override def asPython(s: String)(implicit z: PyZone): PyStringPtr = {
        val bytes = s.getBytes().asInstanceOf[scalanative.runtime.ByteArray]
        val length = bytes.length
          PyUnicode_FromStringAndSize(if (length == 0) null else bytes.at(0),
            bytes.length
        )
      }
    }

  implicit val booleanAsPython: AsPython[Boolean, PyBoolPtr] =
    new AsPython[Boolean, PyBoolPtr] {
      override def asPython(x: Boolean)(implicit z: PyZone): PyBoolPtr = {
        PyBool_FromLong(if (x) 1 else 0)
      }
    }


  implicit def tuple2AsPython[T1, T2, U1 <: PyObjectPtr, U2 <: PyObjectPtr](
      implicit asPy1: AsPython[T1, U1], asPy2: AsPython[T2, U2]): AsPython[(T1, T2), PyTuple2Ptr[U1, U2]] =
    new AsPython[(T1, T2), PyTuple2Ptr[U1, U2]] {
      override def asPython(x: (T1, T2))(
          implicit z: PyZone): PyTuple2Ptr[U1, U2] = {
        z.manage(
            Unmanaged.PyTuple_Pack(2,
                         x._1.asPython.asInstanceOf[PyObjectPtr], x._2.asPython.asInstanceOf[PyObjectPtr]))
          .asInstanceOf[PyTuple2Ptr[U1, U2]]
      }
    }

  implicit def listAsPython[T, U <: PyObjectPtr](implicit asPy: AsPython[T, U]): AsPython[Seq[T], PyListPtr[U]] =
    new AsPython[Seq[T], PyListPtr[U]] {
      override def asPython(x: Seq[T])(implicit z: PyZone): PyListPtr[U] = {
        val l = PyList_New(x.length)
        var i = 0
        while (i < x.length) {
          PyList_SetItem(l, i, x(i).asPython.ptr)
          i += 1
        }
        l.asInstanceOf[PyListPtr[U]]
      }
    }

  implicit def mapAsPython[A, B, C <: PyObjectPtr, D <: PyObjectPtr](
                                                                implicit asPyA: AsPython[A, C],
                                                                asPyB: AsPython[B, D]): AsPython[Map[A, B], PyDictPtr[C, D]] =
    new AsPython[Map[A, B], PyDictPtr[C, D]] {
      override def asPython(x: Map[A, B])(implicit z: PyZone): PyDictPtr[C, D] = {
        val d = PyDict_New()
        for ((k, v) <- x) {
          PyDict_SetItem(d, k.asPython.ptr, v.asPython.ptr)
        }
        d.asInstanceOf[PyDictPtr[C, D]]
      }
    }
}
