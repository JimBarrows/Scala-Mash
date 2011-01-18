package scala_mash.highrise_api.models.enumerations

import xml.NodeSeq

object CollectionType extends Enumeration {
	type CollectionType = Value
	val Deal = Value("Deal")
	val Kase = Value("Kase")

	def parse( node:NodeSeq, tag:String) = 
		if( ( node \ tag text).isEmpty ) None else Some( CollectionType.withName( node \ tag text))

}
