package scala_mash.freshbooks_api

import org.joda.time.format.DateTimeFormatterBuilder
import org.joda.time.DateTime


/**
 * 
 * @author jimbarrows
 * 
 */

object Utils {
  

  val dateFormatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss").toFormatter

  def parseDateTime(dateTime:String) = dateFormatter.parseDateTime(dateTime)  

  def print(dateTime:DateTime) = dateFormatter.print(dateTime)

}
