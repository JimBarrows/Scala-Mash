package scala_mash.highrise_api.models

import org.joda.time.DateTime
import xml.NodeSeq
import xml.NodeSeq.Empty

import bizondemand.utils.models.internet.Parameter
import scala_mash.rest.{Ok, Created, RestException, HttpStatusCode}
import scala_mash.rest.util.Helpers.{optionalLong, optionalString, optionalInt, optionalDateTimeWithTimeZone, printWithTimeZone}

import scala_mash.highrise_api.models.enumerations.VisibleToValues
import VisibleToValues._

case class Party(id: Option[Long],
                background: Option[String],
                createdAt: Option[DateTime],
                updatedAt: Option[DateTime],
                visibleTo: Option[VisibleToValues],
                ownerId: Option[Int],
                groupId: Option[Int],
                authorId: Option[Int],
                contactData: ContactData) {


	def parentFieldsAsXml = {
		id.map( i => {<id type="integer">{i}</id>}).getOrElse(Empty) ++
		background.map( back=> {<background>{back}</background>}).getOrElse(Empty) ++
		createdAt.map( atTime =>{<created-at type="datetime">{printWithTimeZone(atTime)}</created-at>}).getOrElse(Empty) ++
		updatedAt.map( datetime =>{<updated-at type="datetime">{printWithTimeZone(datetime)}</updated-at>}).getOrElse(Empty) ++ 
		visibleTo.map( vt => <visible-to>{vt}</visible-to>).getOrElse(Empty) ++
		ownerId.map( id =>{<owner-id type="integer">{id}</owner-id>}).getOrElse(Empty)  ++
		groupId.map( id =>{<group-id type="integer">{id}</group-id>}).getOrElse(Empty)  ++
		authorId.map( id =>{<author-id type="integer">{id}</author-id>}).getOrElse(Empty)  ++
		contactData.toXml
	}


	def toXml = <party>
		{parentFieldsAsXml}
	</party>
}

object Party extends HighriseServices[Party] {
	
	def parse(node: NodeSeq) :  Party = {
		debug("Party.parse({})", node.toString)
		node match {
			case party @ <party>{ _ *}</party> if party \ "@type" == "Person"=> Person.parse( node)
			case party @ <party>{ _ *}</party> if party \ "type" == "Person" => Person.parse( node)
			case party @ <party>{ _ *}</party> if party \ "@type" == "Company"=> Company.parse( node)
			case party @ <party>{_*}</party> if ((party \ "type" text) == "Company") => Company.parse( node)
			case unknown => throw new UnknownParty( unknown)
		}
	}

	def parseList(node: NodeSeq) : List[Party] = {
		debug("Party.parseList({})", node)
		(node \\ "party").map( parse(_)).toList
	}

}

class UnknownParty(xml : NodeSeq) extends Exception( xml.toString)


object AddressLocationValues extends Enumeration {
	type AddressLocationValues = Value
	val Work = Value("Work")
 	val Home = Value("Home")
 	val Other = Value("Other")
}
import AddressLocationValues._

object InstantMessengerProtocolValues extends Enumeration {
 	type InstantMessengerProtocolValues = Value
	val AIM = Value("AIM")
 	val MSN = Value("MSN")
	val ICQ = Value("ICQ")
	val Jabber = Value("Jammer")
	val Yahoo = Value("Yahoo")
	val Skype = Value("Skype")
	val QQ = Value("QQ")
	val Sametime = Value("Sametime")
	val GaduGadu = Value("Gadu-Gadu")
	val GoogleTalk = Value("GoogleTalk")
	val Other = Value("other")
}
import InstantMessengerProtocolValues._

object InstantMessengerLocationValues extends Enumeration {
	type InstantMessengerLocationValues = Value
	val Work = Value("Work")
	val Personal = Value("Personal")
	val Other = Value("Other")
}
import InstantMessengerLocationValues._

object PhoneLocationValues extends Enumeration {
	type PhoneLocationValues = Value
	val Work = Value("Work")
	val Mobile = Value("Mobile")
	val Fax = Value("Fax")
	val Pager = Value("Pager")
	val Home = Value("Home")
	val Other = Value("Other")
}
import PhoneLocationValues._

case class InstantMessenger(id: Option[Long],
    		          		      address: String,
                            protocol: InstantMessengerProtocolValues,
                            location: InstantMessengerLocationValues) {
	def toXml: NodeSeq = <instant-messenger>
    	{id.map( v => <id type="integer">{v}</id>).getOrElse(Empty)} 
    	<address>{address}</address>
    	<protocol>{protocol}</protocol>
    	<location>{location}</location>
  	</instant-messenger>

}

object InstantMessenger {
 	def parseResponse(node: NodeSeq): InstantMessenger = {
    	InstantMessenger(
            Some((node \ "id" text).toLong),
            node \ "address" text,
            InstantMessengerProtocolValues.valueOf(node \ "protocol" text).getOrElse(InstantMessengerProtocolValues.Other),
            InstantMessengerLocationValues.valueOf(node \ "location" text).getOrElse(InstantMessengerLocationValues.Other)
        )
	}
}

case class WebAddress(id: Option[Long],
                      url: String,
                      location: InstantMessengerLocationValues) {
	def toXml = <web-address>
    {id.map( v => <id type="integer">{v}</id>).getOrElse(Empty)} 
		<url>{url}</url>
		<location>{location}</location>
	</web-address>
}

object WebAddress {
	def parseResponse(node: NodeSeq) = {
    	WebAddress(
          optionalLong( node, "id"),
          node \ "url" text,
          InstantMessengerLocationValues.valueOf(node \ "location" text).getOrElse(InstantMessengerLocationValues.Other)
    	)
	}
}


case class Address(id: Option[Long],
                   city: String,
                   country: String,
                   state: String,
                   street: String,
                   zip: String,
                   location: AddressLocationValues) {
  def toXml = <address>
    {id.map( v => <id type="integer">{v}</id>).getOrElse(Empty)} 
    <city>{city}</city>
    <country>{country}</country>
    <state>{state}</state>
    <street>{street}</street>
    <zip>{zip}</zip>
    <location>{location}</location>
  </address>


}

object Address {
	def parseResponse(node: NodeSeq) = {
    	Address(
           Some((node \ "id" text).toLong),
           node \ "city" text,
           node \ "country" text,
           node \ "state" text,
           node \ "street" text,
           node \ "zip" text,
           AddressLocationValues.valueOf(node \ "location" text).getOrElse(AddressLocationValues.Other)
          )
 	}
}


case class EmailAddress(id: Option[Long], address: String, location: AddressLocationValues) {
	assert(!address.isEmpty)
	def toXml = <email-address>
    	{id.map( v => <id type="integer">{v}</id>).getOrElse(Empty)} 
    	<address>{address}</address>
    	<location>{location}</location>
  	</email-address>
}

object EmailAddress {
	def parseResponse(node: NodeSeq) = {
    	EmailAddress(
            Some((node \ "id" text).toLong),
            node \ "address" text,
            AddressLocationValues.valueOf(node \ "location" text).getOrElse(AddressLocationValues.Other)
            )
  	}
}

case class PhoneNumber(id: Option[Long], number: String, location: PhoneLocationValues) {
 	def toXml = <phone-number>
    	{id.map( v => <id type="integer">{v}</id>).getOrElse(Empty)} 
    	<number>{number}</number>
    	<location>{location}</location>
  	</phone-number>
}

object PhoneNumber {
	def parseResponse(node: NodeSeq) = {
    	PhoneNumber(
        	Some((node \ "id" text).toLong),
            node \ "number" text,
            PhoneLocationValues.valueOf(node \ "location" text).getOrElse(PhoneLocationValues.Other)
        )
	}
}


case class ContactData(emailAddresses: Option[List[EmailAddress]],
    	               phoneNumbers: Option[List[PhoneNumber]],
                       addresses: Option[List[Address]],
                       instantMessengers: Option[List[InstantMessenger]],
                       webAddresses: Option[List[WebAddress]]) {
 	def toXml = <contact-data>
    	{
    		emailAddresses.map(list =>
            	<email-addresses>{list.map(_.toXml)}</email-addresses>
            ).toList            
        }{
			phoneNumbers.map(list =>
            	<phone-numbers>{list.map(_.toXml)}</phone-numbers>
            ).toList        
        }{
        	addresses.map(list =>
            	<addresses>{list.map(_.toXml)}</addresses>
            ).toList        
        }{
        	instantMessengers.map(list =>
            	<instant-messengers>{list.map(_.toXml)}</instant-messengers>
        	).toList        	
    	}{
    		webAddresses.map(list =>
            	<web-addresses>{list.map(_.toXml)}</web-addresses>
            ).toList            
        }
    </contact-data>
}


object ContactData {
	def parseResponse(node: NodeSeq) = 
    	ContactData(
	        if ((node \\ "email-addresses" \ "email-address").isEmpty) 
    	       	None 
    		else 
    			Some((node \\ "email-addresses" \ "email-address").map(EmailAddress.parseResponse(_)).toList.asInstanceOf[List[EmailAddress]]),
        	if ((node \\ "phone-numbers" \ "phone-number").isEmpty) 
        		None 
        	else 
        		Some((node \\ "phone-numbers" \ "phone-number").map(PhoneNumber.parseResponse(_)).toList.asInstanceOf[List[PhoneNumber]]),
        	if ((node \\ "addresses" \ "address").isEmpty) 
        		None 
        	else 
        		Some((node \\ "addresses" \ "address").map(Address.parseResponse(_)).toList.asInstanceOf[List[Address]]),
        	if ((node \\ "instant-messengers" \ "instant-messenger").isEmpty) 
        		None 
        	else 
        		Some((node \\ "instant-messengers" \ "instant-messenger").map(InstantMessenger.parseResponse(_)).toList.asInstanceOf[List[InstantMessenger]]),
        	if ((node \\ "web-addresses" \ "web-address").isEmpty) 
        		None 
        	else 
        		Some((node \\ "web-addresses" \ "web-address").map(WebAddress.parseResponse(_)).toList.asInstanceOf[List[WebAddress]]))
	}



