package scala_mash.highrise_api.models.enumerations

import xml.NodeSeq

object VisibleToValues extends Enumeration {
  type VisibleToValues = Value
  val Everyone = Value("Everyone")
  val Owner = Value("Owner")
  val NamedGroup = Value("NamedGroup")

  def parse( node:NodeSeq, tag:String) = 
  	if( ( node \ tag text).isEmpty ) None else Some( VisibleToValues.withName( node \ tag text))
}
import VisibleToValues._
