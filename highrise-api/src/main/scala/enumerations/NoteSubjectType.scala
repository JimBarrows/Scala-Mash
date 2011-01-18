package scala_mash.highrise_api.models.enumerations

import xml.NodeSeq

object NoteSubjectType extends Enumeration {
	type NoteSubjectType = Value
	val Deal = Value("Deal")
	val Party = Value("Party")
	val Kase = Value("Kase")

	def parse( node:NodeSeq, tag:String) = 
  	if( ( node \ tag text).isEmpty ) None else Some( NoteSubjectType.withName( node \ tag text))

}
