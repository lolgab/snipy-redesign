package snipy.facades

import snipy._
import dynamic._

object builtins {
  private val builtins = module("builtins")(PyZone.leakingZone, pyDynamic)

  def str(o: PyObject[PyObjectPtr]): String = PyZone { implicit z =>
    builtins.str(o).asInstanceOf[PyObject[PyStringPtr]].asScala[String]
  }

  def print(o: PyObject[PyObjectPtr]): Unit = PyZone { implicit z =>
    builtins.print(o)
  }
}
