package bizondemand.highrise.models.enumerations

object DealStatus extends Enumeration {
	type DealStatus = Value
	val Pending = Value("pending")
	val Won = Value("won")
	val Lost = Value("lost")
}
import DealStatus._