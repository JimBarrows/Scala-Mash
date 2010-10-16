package scala_mash.highrise_api.models

class HighriseApiException(msg:String) extends Exception(msg) 

class NoIdException( msg: String) extends HighriseApiException ( msg)

object NoIdException {
	def apply( x: Note) = new NoIdException( x.toString)
	def apply( x: Person) = new NoIdException( x.toString)
	def apply( x: Company) = new NoIdException( x.toString)
	def apply( x: Deal) = new NoIdException( x.toString)
	def apply( x: Tag) = new NoIdException( x.toString)
}
