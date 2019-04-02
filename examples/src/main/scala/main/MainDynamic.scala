package main

import snipy._
import snipy.dynamic._

object MainDynamic {
  def main(args: Array[String]): Unit = PyZone { implicit z =>
    val np = module("numpy")
    var i = 0
    while(i < 10) {
      val array1 = np.array(List(1,2,3,4))
      val array2 = np.array(List(1,2,3,4))
      val sumArray = array1 + array2
      println(sumArray)

      i += 1
    }

  }
}
