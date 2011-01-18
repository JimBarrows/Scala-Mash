package scala_mash.highrise_api.models.enumerations

import xml.NodeSeq

object DealStatus extends Enumeration {
	type DealStatus = Value
	val Pending = Value("pending")
	val Won = Value("won")
	val Lost = Value("lost")

	def parse( node:NodeSeq, tag: String ) = 
  	if( ( node \ tag text).isEmpty ) None else Some( DealStatus.withName( node \ tag text))

}
import DealStatus._
