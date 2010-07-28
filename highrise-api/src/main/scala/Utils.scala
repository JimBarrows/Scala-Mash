package scala_mash.highrise_api

import org.joda.time.format.DateTimeFormatterBuilder
import org.joda.time._

import xml._
import  xml.NodeSeq
import  xml.NodeSeq._


object Utils {
  val dateFormatterWithTimeZone = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm:ssZZ").toFormatter.withOffsetParsed

  val dateFormatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm").toFormatter
  
  val ymdFormatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").toFormatter

  def parseDateTimeWithTimeZone(dateTime: String) = {
    dateFormatterWithTimeZone.parseDateTime(dateTime.replace("Z","+00:00"))
  }

  def printWithTimeZone(dateTime: DateTime) = dateFormatterWithTimeZone.print(dateTime).replace("+00:00","Z")

  def print(dateTime: DateTime) = dateFormatter.print(dateTime)
  
  def printYmd( date:LocalDate) = ymdFormatter.print(date)
  def parseYmd( ymd:String) = ymdFormatter.parseDateTime( ymd);
  
  def optionalParseYmd( node:NodeSeq, tag: String) = 
  	if( ( node \ tag text).isEmpty )None else Some(parseYmd( node \ tag text).toLocalDate)
  	
  def optionalParseDateTimeWithTimeZone( node:NodeSeq, tag:String)  = 
		if( ( node \ tag text).isEmpty )None else Some(parseDateTimeWithTimeZone( node \ tag text))
}
