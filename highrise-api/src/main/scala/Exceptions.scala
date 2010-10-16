package scala_mash.highrise_api.models

case class HighriseApiException(msg:String) extends Exception(msg) 

case class NoIdException( note: Note) extends Exception( note.toString)
