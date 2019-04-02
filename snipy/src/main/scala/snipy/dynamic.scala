package snipy

trait PyDynamic

object dynamic {
  implicit val pyDynamic: PyDynamic = new PyDynamic {}
}
