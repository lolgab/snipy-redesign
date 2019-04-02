package snipy

import scala.scalanative.native.{CDouble, CInt, CLong, CSize, CString, CVararg, Ptr, extern}

object CApi {
  @extern
  object Unmanaged {
    def Py_Initialize(): Unit = extern
    def Py_BuildValue(format: CString): PyObjectPtr = extern
    def PyUnicode_FromString(string: CString): PyStringPtr = extern
    def PyUnicode_FromStringAndSize(string: CString, size: CSize): PyStringPtr =
      extern
    def PyUnicode_AsUTF8(string: PyStringPtr): CString = extern
    def PyUnicode_AsUTF8AndSize(string: PyStringPtr, size: Ptr[CSize]): CString =
      extern
    def PyUnicode_GetLength(obj: PyObjectPtr): CSize = extern
    def PyImport_Import(name: PyStringPtr): PyModulePtr = extern
    def PyObject_GetAttrString(obj: PyObjectPtr, string: CString): PyObjectPtr = extern
    def PyObject_Call(obj: PyObjectPtr, args: PyTupleAny, kwargs: PyDictAnyPtr): PyObjectPtr = extern
    def PyObject_CallObject(obj: PyObjectPtr, args: PyTupleAny): PyObjectPtr = extern
    def PyObject_CallFunctionObjArgs[T](function: PyObjectPtr,
                                        arguments: CVararg*): PyObjectPtr = extern
    def PyObject_CallMethod(obj: PyObjectPtr,
                            name: CString,
                            format: CString): PyObjectPtr = extern
    def PyObject_CallMethodObjArgs(obj: PyObjectPtr,
                                   method: CString,
                                   arguments: CVararg*): PyObjectPtr = extern
    def PyFloat_FromDouble(v: CDouble): PyFloatPtr = extern
    def PyFloat_AsDouble(pyFloat: PyFloatPtr): CDouble = extern
    def PyLong_FromLong(v: CLong): PyLongPtr = extern
    def PyLong_AsLong(pyLong: PyLongPtr): Long = extern
    def PyBool_FromLong(l: CLong): PyBoolPtr = extern

    def PyTuple_New(len: CSize): PyTupleAny = extern
    def PyTuple_Pack(size: CSize, elements: CVararg*): PyTupleAny = extern
    def PyTuple_GetItem(tuple: PyTupleAny, elem: CSize): PyObjectPtr = extern
    def PyTuple_SetItem(tuple: PyTupleAny, elem: CSize, value: PyObjectPtr): CInt = extern
    def PyTuple_Size(tuple: PyTupleAny): CSize = extern
    def PyList_New(len: CSize): PyListAnyPtr = extern
    def PyList_Size(list: PyListAnyPtr): CSize = extern
    def PyList_GetItem(list: PyListAnyPtr, index: CSize): PyObjectPtr = extern
    def PyList_SetItem(list: PyListAnyPtr, index: CSize, element: PyObjectPtr): CInt =
      extern
    def PyDict_New(): PyDictAnyPtr = extern
    def PyDict_SetItem(dict: PyDictAnyPtr, key: PyObjectPtr, value: PyObjectPtr): CInt =
      extern
    def PyDict_GetItem(dict: PyDictAnyPtr, key: PyObjectPtr): PyObjectPtr = extern
    def PyDict_Next(dict: PyDictAnyPtr,
                    posPtr: Ptr[CSize],
                    keysPtr: Ptr[PyObjectPtr],
                    valuePtr: Ptr[PyObjectPtr]): CInt = extern
    def Py_DecRef(obj: PyObjectPtr): Unit = extern
  }

  @inline def Py_Initialize(): Unit = Unmanaged.Py_Initialize()
  @inline def Py_BuildValue(format: CString)(implicit z: PyZone): PyObjectPtr = z.manage(Unmanaged.Py_BuildValue(format))
  @inline def PyUnicode_FromString(string: CString)(implicit z: PyZone): PyStringPtr = z.manage(Unmanaged.PyUnicode_FromString(string))
  @inline def PyUnicode_FromStringAndSize(string: CString, size: CSize)(implicit z: PyZone): PyStringPtr = z.manage(Unmanaged.PyUnicode_FromStringAndSize(string, size))
  @inline def PyUnicode_AsUTF8(string: PyStringPtr): CString = Unmanaged.PyUnicode_AsUTF8(string)
  @inline def PyUnicode_AsUTF8AndSize(string: PyStringPtr, size: Ptr[CSize]): CString = Unmanaged.PyUnicode_AsUTF8AndSize(string, size)
  @inline def PyUnicode_GetLength(obj: PyObjectPtr): CSize = Unmanaged.PyUnicode_GetLength(obj)
  @inline def PyImport_Import(name: PyStringPtr)(implicit z: PyZone): PyModulePtr = z.manage(Unmanaged.PyImport_Import(name))
  @inline def PyObject_GetAttrString(obj: PyObjectPtr, string: CString)(implicit z: PyZone): PyObjectPtr = z.manage(Unmanaged.PyObject_GetAttrString(obj, string))
  @inline def PyObject_Call(obj: PyObjectPtr, args: PyTupleAny, kwargs: PyDictAnyPtr)(implicit z: PyZone): PyObjectPtr = z.manage(Unmanaged.PyObject_Call(obj, args, kwargs))
  @inline def PyObject_CallObject(obj: PyObjectPtr, args: PyTupleAny)(implicit z: PyZone): PyObjectPtr = z.manage(Unmanaged.PyObject_CallObject(obj, args))
//  @inline def PyObject_CallFunctionObjArgs[T](function: PyObjectPtr,arguments: CVararg*)(implicit z: PyZone): PyObjectPtr = z.manage(Unmanaged.PyObject_CallFunctionObjArgs(function, arguments: _*))
  @inline def PyObject_CallMethod(obj: PyObjectPtr,name: CString,format: CString)(implicit z: PyZone): PyObjectPtr = z.manage(Unmanaged.PyObject_CallMethod(obj, name, format))
//  @inline def PyObject_CallMethodObjArgs(obj: PyObjectPtr,method: CString,arguments: CVararg*)(implicit z: PyZone): PyObjectPtr = z.manage(Unmanaged.PyObject_CallMethodObjArgs(obj, method, arguments: _*))
  @inline def PyFloat_FromDouble(v: CDouble)(implicit z: PyZone): PyFloatPtr = z.manage(Unmanaged.PyFloat_FromDouble(v))
  @inline def PyFloat_AsDouble(pyFloat: PyFloatPtr): CDouble = Unmanaged.PyFloat_AsDouble(pyFloat)
  @inline def PyLong_FromLong(v: CLong)(implicit z: PyZone): PyLongPtr = z.manage(Unmanaged.PyLong_FromLong(v))
  @inline def PyLong_AsLong(pyLong: PyLongPtr): Long = Unmanaged.PyLong_AsLong(pyLong)
  @inline def PyBool_FromLong(l: CLong)(implicit z: PyZone): PyBoolPtr = z.manage(Unmanaged.PyBool_FromLong(l))
  @inline def PyTuple_New(len: CSize)(implicit z: PyZone): PyTupleAny = z.manage(Unmanaged.PyTuple_New(len))
//  @inline def PyTuple_Pack(size: CSize, elements: CVararg*)(implicit z: PyZone): PyTupleAny = z.manage(Unmanaged.PyTuple_Pack(size, elements: _*))
  @inline def PyTuple_GetItem(tuple: PyTupleAny, elem: CSize): PyObjectPtr = Unmanaged.PyTuple_GetItem(tuple, elem)
  @inline def PyTuple_SetItem(tuple: PyTupleAny, elem: CSize, value: PyObjectPtr): CInt = Unmanaged.PyTuple_SetItem(tuple, elem, value)
  @inline def PyTuple_Size(tuple: PyTupleAny): CSize = Unmanaged.PyTuple_Size(tuple)
  @inline def PyList_New(len: CSize)(implicit z: PyZone): PyListAnyPtr = z.manage(Unmanaged.PyList_New(len))
  @inline def PyList_Size(list: PyListAnyPtr): CSize = Unmanaged.PyList_Size(list)
  @inline def PyList_GetItem(list: PyListAnyPtr, index: CSize): PyObjectPtr = Unmanaged.PyList_GetItem(list, index)
  @inline def PyList_SetItem(list: PyListAnyPtr, index: CSize, element: PyObjectPtr): CInt = Unmanaged.PyList_SetItem(list, index, element)
  @inline def PyDict_New()(implicit z: PyZone): PyDictAnyPtr = z.manage(Unmanaged.PyDict_New())
  @inline def PyDict_SetItem(dict: PyDictAnyPtr, key: PyObjectPtr, value: PyObjectPtr): CInt = Unmanaged.PyDict_SetItem(dict, key, value)
  @inline def PyDict_GetItem(dict: PyDictAnyPtr, key: PyObjectPtr)(implicit z: PyZone): PyObjectPtr = z.manage(Unmanaged.PyDict_GetItem(dict, key))
  @inline def PyDict_Next(dict: PyDictAnyPtr, posPtr: Ptr[CSize], keysPtr: Ptr[PyObjectPtr], valuePtr: Ptr[PyObjectPtr]): CInt = Unmanaged.PyDict_Next(dict, posPtr, keysPtr, valuePtr)
  @inline def Py_DecRef(obj: PyObjectPtr): Unit = Unmanaged.Py_DecRef(obj)
}
