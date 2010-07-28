package bizondemand.freshbooks_api

import org.joda.time.format.DateTimeFormatterBuilder
import org.joda.time.DateTime


/**
 * 
 * @author jimbarrows
 * 
 */

object Utils {
  val dateFormatterWithTimeZone = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm:ssZZ").toFormatter.withOffsetParsed
  def parseDateTimeWithTimeZone(dateTime:String) = dateFormatterWithTimeZone.parseDateTime(dateTime)

  val dateFormatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss").toFormatter

  def parseDateTime(dateTime:String) = dateFormatter.parseDateTime(dateTime)

  def printWithTimeZone(dateTime:DateTime) = dateFormatterWithTimeZone.print(dateTime)

  def print(dateTime:DateTime) = dateFormatter.print(dateTime)

}
