package scala_mash.highrise_api.models.enumerations

object VisibleToValues extends Enumeration {
  type VisibleToValues = Value
  val Everyone = Value("Everyone")
  val Owner = Value("Owner")
  val NamedGroup = Value("NamedGroup")
}
import VisibleToValues._
