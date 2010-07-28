package com.nsfw.util

import xml._
import NodeSeq._
import org.joda.time.DateTime

/**This is for usefull little helper methods and functions for parsing rest.
*/
object Helpers {

	def optionalString( node:NodeSeq, tag:String) : Option[String] = 
		if( ( node \ tag text).isEmpty )None else Some(node \ tag text)

	def optionalLong( node:NodeSeq, tag:String) : Option[Long] = 
		if( ( node \ tag text).isEmpty) None else Some((node \ tag text).toLong)

	def optionalInt( node:NodeSeq, tag:String) : Option[Int] = 
		if( ( node \ tag text).isEmpty) None else Some((node \ tag text).toInt)

	def optionalInt( node:NodeSeq, tag:String, parser:(String)=>DateTime) : Option[DateTime] = 
		if( ( node \ tag text).isEmpty) None else Some(parser(node \ tag text))

	def optionalBoolean( node:NodeSeq, tag:String ) : Option[Boolean] = 
		if( ( node \ tag text).isEmpty) None else Some((node \ tag text).toBoolean)

}
