package scala_mash.rest.util

import xml._
import NodeSeq._
import org.joda.time.{DateTime, LocalDate}
import org.joda.time.format.DateTimeFormatterBuilder



/**This is for usefull little helper methods and functions for parsing rest.
*/
object Helpers {
	
	protected val dateFormatterWithTimeZone = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm:ssZZ").toFormatter.withOffsetParsed
  	def parseDateTimeWithTimeZone(dateTime:String) = dateFormatterWithTimeZone.parseDateTime(dateTime.replace("Z","+00:00"))  	
  	def optionalDateTimeWithTimeZone( node:NodeSeq, tag:String) = 
  		if( ( node \ tag text).isEmpty )None else Some(parseDateTimeWithTimeZone( node \ tag text))
  	def printWithTimeZone(dateTime: DateTime) = dateFormatterWithTimeZone.print(dateTime).replace("+00:00","Z")
  	
  	protected val dateFormatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm").toFormatter
  	def parseDateTime(dateTime:String) = dateFormatter.parseDateTime(dateTime)  
	def print(dateTime:DateTime) = dateFormatter.print(dateTime)
  	
  	protected val ymdFormatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").toFormatter    
  	def printYmd( date:LocalDate) = ymdFormatter.print(date)
	def parseYmd( ymd:String) = ymdFormatter.parseDateTime( ymd);
	def optionalParseYmd( node:NodeSeq, tag:String) =
		if( ( node \ tag text).isEmpty )None else Some(parseYmd( node \ tag text))
  
	def optionalYmd( node:NodeSeq, tag: String) = 
  		if( ( node \ tag text).isEmpty )None else Some(parseYmd( node \ tag text).toLocalDate)

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
