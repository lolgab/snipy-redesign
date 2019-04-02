package snipy.conversions

import org.scalatest.Assertion
import org.scalatest.prop.Generator
import snipy._
import facades.builtins

class ConversionsSpec extends SnipySpec {
  def testConversion[T, U <: PyObject](implicit asPython: AsPython[T, U],
                                       asScala: AsScala[U, T],
                                       gen: Generator[T]): Assertion =
    forAll { b: T =>
      PyZone { implicit z =>
        assert(b.asPython.asScala === b)
      }
    }

  "Default values should go back and forth from Scala to Python to Scala unchanged" - {
    "Long" in testConversion[Long, PyLongPtr]
    "Double" in testConversion[Double, PyFloatPtr]
    "String" in testConversion[String, PyStringPtr]
    "Boolean" in testConversion[Boolean, PyBoolPtr]
    "(Boolean, Double)" in testConversion[(Boolean, Double),
                                          PyTuple2Ptr[PyBoolPtr, PyFloatPtr]]
//    "Seq[Long]" in {
//      implicit val seqGen: Generator[Seq[Long]] = lists[Long].map(_.toSeq)
//      testConversion[Seq[Long], PyList[PyLong]]
//    }
    "Map[Long, Long]" in testConversion[Map[Long, Long], PyDictPtr[PyLongPtr, PyLongPtr]]
  }

}
