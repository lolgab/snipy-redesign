package snipy
import snipy.CApi._

import scala.scalanative.native.{CDouble, CLong, CSize, stackalloc}

trait AsScala[-T <: PyObjectPtr, +U] {
  def asScala(x: T): U
}

object AsScala {
  implicit val pyLongAsScala: AsScala[PyLongPtr, CLong] =
    new AsScala[PyLongPtr, CLong] {
      override def asScala(x: PyLongPtr): CLong = PyLong_AsLong(x)
    }

  implicit val pyFloatAsScala: AsScala[PyFloatPtr, CDouble] =
    new AsScala[PyFloatPtr, CDouble] {
      override def asScala(x: PyFloatPtr): CDouble = PyFloat_AsDouble(x)
    }

  implicit val pyStringAsScala: AsScala[PyStringPtr, String] =
    new AsScala[PyStringPtr, String] {
      def asScala(s: PyStringPtr): String = {
        val size = stackalloc[CSize]
        val cstr = PyUnicode_AsUTF8AndSize(s, size)
        val l    = (!size).toInt
        val arr  = new Array[Byte](l)
        var i    = 0
        while (i < l) {
          arr(i) = cstr(i)
          i += 1
        }
        new String(arr)
      }
    }

  implicit val pyBoolAsBoolean: AsScala[PyBoolPtr, Boolean] =
    new AsScala[PyBoolPtr, Boolean] {
      override def asScala(x: PyBoolPtr): Boolean = PyZone { implicit z =>
        if (x == true.asPython) true else false
      }
    }

  implicit def pyTuple2AsScala[T1 <: PyObjectPtr, T2 <: PyObjectPtr, U1, U2](implicit asScala1: AsScala[T1, U1],
                                                                       asScala2: AsScala[T2, U2]): AsScala[PyTuple2Ptr[T1, T2], (U1, U2)] =
    new AsScala[PyTuple2Ptr[T1, T2], (U1, U2)] {
      override def asScala(x: PyTuple2Ptr[T1, T2]): (U1, U2) =
        (asScala1.asScala(PyTuple_GetItem(x, 0).asInstanceOf[T1]), asScala2.asScala(PyTuple_GetItem(x, 1).asInstanceOf[T2]))
    }

  implicit def pyTupleAsScala[T <: PyObjectPtr, U](implicit asSc: AsScala[T, U]): AsScala[PyTuplePtr[T], Seq[U]] =
    new AsScala[PyTuplePtr[T], Seq[U]] {
      override def asScala(x: PyTuplePtr[T]): Seq[U] = {
        val tuple   = x.asInstanceOf[PyTupleAny]
        val length  = PyTuple_Size(tuple)
        val builder = Seq.newBuilder[U]
        var i       = 0
        while (i < length) {
          builder += asSc.asScala(PyTuple_GetItem(tuple, i).asInstanceOf[T])
          i += 1
        }
        builder.result()
      }
    }

  implicit def pyListAsScala[T <: PyObjectPtr, U](implicit asSc: AsScala[T, U]): AsScala[PyListPtr[T], Seq[U]] =
    new AsScala[PyListPtr[T], Seq[U]] {
      override def asScala(x: PyListPtr[T]): Seq[U] = {
        val list    = x.asInstanceOf[PyListAnyPtr]
        val length  = PyList_Size(list)
        val builder = Seq.newBuilder[U]
        var i       = 0
        while (i < length) {
          builder += asSc.asScala(PyList_GetItem(list, i).asInstanceOf[T])
          i += 1
        }
        builder.result()
      }
    }

  implicit def pyDictAsScala[A <: PyObjectPtr, B <: PyObjectPtr, C, D](implicit asScA: AsScala[A, C],
                                                                 asScB: AsScala[B, D]): AsScala[PyDictPtr[A, B], Map[C, D]] =
    new AsScala[PyDictPtr[A, B], Map[C, D]] {
      override def asScala(x: PyDictPtr[A, B]): Map[C, D] = {
        val m      = Map.newBuilder[C, D]
        val posPtr = stackalloc[CSize]
        !posPtr = 0
        val keyPtr   = stackalloc[PyObjectPtr]
        val valuePtr = stackalloc[PyObjectPtr]
        while (PyDict_Next(x.asInstanceOf[PyDictAnyPtr], posPtr, keyPtr, valuePtr) != 0) {
          m += asScA.asScala((!keyPtr).asInstanceOf[A]) -> asScB.asScala((!valuePtr)
            .asInstanceOf[B])
        }
        m.result()
      }
    }
}
