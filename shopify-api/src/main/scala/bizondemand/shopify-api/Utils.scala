package bizondemand.shopify_api

import org.joda.time.format.DateTimeFormatterBuilder
import org.joda.time.DateTime
import xml.NodeSeq
import bizondemand.utils.logging.Log

/**
 * 
 * @author jimbarrows
 * @created: Dec 19, 2009 11:05:08 AM
 * @version 1.0
 * 
 */

object Utils extends Log{
	protected val dateFormatterWithTimeZone = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm:ssZZ").toFormatter.withOffsetParsed

	protected val dateFormatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm").toFormatter

	protected val yearMonthDayFormatter = new DateTimeFormatterBuilder().appendYear(4,4).appendLiteral("-").appendMonthOfYear(2).appendLiteral("-").appendDayOfMonth(2).toFormatter

	def parseYearMonthDay(ymd:String) = yearMonthDayFormatter.parseDateTime(ymd).toLocalDate
  
	def parseDateTimeWithTimeZone(dateTime:String) = dateFormatterWithTimeZone.parseDateTime(dateTime)

	def printWithTimeZone(dateTime:DateTime):String = dateFormatterWithTimeZone.print(dateTime)

	def print(dateTime:DateTime) = dateFormatter.print(dateTime)

	def parseOptionalYearMonthDay(value: String) = {
		value match {
			case "" => None
			case _ =>Some(parseYearMonthDay(value))
		}
	}
	def parseOptionalBoolean(  value:String): Option[Boolean] = {
		debug("parseOptionalBoolean Value: %s".format( value))
		value match {
			case  "0" => Some(false)
			case "1" => Some(true)
			case _ => None
		}
	}

}
