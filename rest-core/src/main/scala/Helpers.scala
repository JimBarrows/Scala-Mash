package scala_mash {

	import xml._
	import NodeSeq._

	import org._
	import joda.time.DateTime
	import org.joda.time.LocalDate
	import org.joda.time.format.DateTimeFormatterBuilder

	import bizondemand.utils.models.internet.Url

	package rest {
		package util  {


/**This is for usefull little helper methods and functions for parsing rest.
*/
object Helpers {
	
	protected val dateFormatterWithTimeZone = 
			new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm:ssZZ").toFormatter.withOffsetParsed

	protected val dateFormatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm").toFormatter

	protected val ymdFormatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").toFormatter    

	def parseDateTimeWithTimeZone(dateTime:String) = 
			dateFormatterWithTimeZone.parseDateTime(dateTime.replace("Z","+00:00"))  	

	def printWithTimeZone(dateTime: DateTime) = 
			dateFormatterWithTimeZone.print(dateTime).replace("+00:00","Z")
  	
	def parseDateTime(dateTime:String) = 
			dateFormatter.parseDateTime(dateTime)  

	def print(dateTime:DateTime) = 
			dateFormatter.print(dateTime)

	def printYmd( date:LocalDate) = 
			ymdFormatter.print(date)

	def parseYmd( ymd:String) = 
			ymdFormatter.parseDateTime( ymd);

	def boolean( node:NodeSeq, tag:String ): Boolean = {
		(node \ tag text) match {
			case n:String if n.isEmpty => false
			case "0" => false
			case "1" => true
			case "f" => false
			case "t" => true
			case "F" => false
			case "T" => true
			case "true" => true
			case "false" => false
		}
	}

	def optionalDateTimeWithTimeZone( node:NodeSeq, tag:String) = 
  	if( ( node \ tag text).isEmpty )None else Some(parseDateTimeWithTimeZone( node \ tag text))

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

	def optionalBoolean( node:NodeSeq, tag:String ) : Option[Boolean] = {
		(node \ tag text) match {
			case n:String if n.isEmpty => None
			case t => Some( boolean(node, tag))
		}
	}

	def optionalUrl( node:NodeSeq, tag:String) : Option[Url] =
		if( ( node \ tag text).isEmpty) None else Some( Url((node \ tag text)))

	def optionalBigDecimal( node:NodeSeq, tag:String) : Option[BigDecimal] =
		if( ( node \ tag text).isEmpty) None else Some( BigDecimal((node \ tag text)))
}

}}}
