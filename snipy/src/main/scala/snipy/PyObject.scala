package snipy
import snipy.CApi._
import snipy.facades.builtins
import scala.language.dynamics
import scala.language.implicitConversions
import scala.scalanative.native.{CSize, Ptr, Zone, stackalloc, toCString}

class PyObject[+T <: PyObjectPtr](val ptr: T) extends AnyVal with Dynamic {
  def selectDynamic(attr: String)(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = {
    val s = stackalloc[Byte](attr.getBytes().length + 1)
    val zone = new Zone {
      override def alloc(size: CSize): Ptr[Byte] = s
    }
    PyObject(PyObject_GetAttrString(ptr, toCString(attr)(zone)))
  }

  def applyDynamic(name: String)(args: PyObject[PyObjectPtr]*)(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = {
    val func = selectDynamic(name)
    var i: Int = 0
    val tuple = PyTuple_New(args.length)
    args.foreach { arg =>
      PyTuple_SetItem(tuple, i, arg.ptr)
      i = i + 1
    }
    PyObject(PyObject_CallObject(func.ptr, tuple))
  }

  def applyDynamicNamed(name: String)(args: (String, PyObject[PyObjectPtr])*)(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = {
    val func = selectDynamic(name)
    val argsNum = args.count(_._1.isEmpty)
    val kwargs = PyDict_New()
    val argsTuple = if(argsNum == 0) PyNone else PyTuple_New(argsNum)
    var argsTupleIndex = 0
    for((name, value) <- args)
      if(name.isEmpty) {
        PyTuple_SetItem(argsTuple, argsTupleIndex, value.ptr)
        argsTupleIndex = argsTupleIndex + 1
      }
      else
        PyDict_SetItem(kwargs, name.asPython.ptr, value.ptr)
    PyObject(PyObject_Call(func.ptr, argsTuple, kwargs))
  }

  def +(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__add__(other)
  def -(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__sub__(other)
  def *(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__mul__(other)
  def `//`(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__floordiv__(other)
  def /(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__div__(other)
  def %(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__mod__(other)
  def divmod(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__divmod__(other)
  def **(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__pow__(other)
  def <<(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__lshift__(other)
  def >>(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__rshift__(other)
  def &(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__and__(other)
  def |(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__or__(other)
  def ^(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__xor__(other)
  def +=(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__iadd__(other)
  def -=(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__isub__(other)
  def *=(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__imul__(other)
  def `//=`(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__ifloordiv__(other)
  def /=(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__idiv__(other)
  def %=(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__imod__(other)
  def divmod_=(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__idivmod__(other)
  def **=(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__ipow__(other)
  def <<=(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__ilshift__(other)
  def >>=(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__irshift__(other)
  def &=(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__iand__(other)
  def |=(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__ior__(other)
  def ^=(other: PyObject[PyObjectPtr])(implicit z: PyZone, pyDynamic: PyDynamic): PyObject[PyObjectPtr] = PyObject(ptr).__ixor__(other)

//  def as[T <: Ptr[Byte]]: T = ptr.asInstanceOf[T]

  override def toString: String = builtins.str(this)

  def asScala[U](implicit asScala: AsScala[T, U]): U =
    asScala.asScala(ptr)
}
object PyObject {
  def apply[T <: PyObjectPtr](ptr: T): PyObject[T] = new PyObject(ptr)
}