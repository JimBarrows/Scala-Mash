package scala_mash.freshbooks_api

import xml._

import org.joda.time.format.DateTimeFormatterBuilder
import org.joda.time.DateTime


/**
 * 
 * @author jimbarrows
 * 
 */

object Utils {
  

  protected val dateTimeFormatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss").toFormatter
	def parseDateTime(dateTime:String) = dateTimeFormatter.parseDateTime(dateTime)  
	def printDateTime(dateTime:DateTime) = dateTimeFormatter.print(dateTime)

	def optionalDateTime( node:NodeSeq, tag:String) = 
		if( (node \ tag text).isEmpty) None else Some( parseDateTime( node \ tag text))


}
