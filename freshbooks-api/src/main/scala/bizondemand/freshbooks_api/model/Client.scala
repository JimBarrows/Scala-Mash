package bizondemand.freshbooks_api.model

import xml._
import NodeSeq._
import bizondemand.util.Helpers._
import bizondemand.rest.{Ok,Created,RestException}


/** While freshbooks has the address xml inline, and differentiated by a p_ or s_, this blows up the case class for 
	this, so I break out into to classes with a common ancestor.
	*/
class Address(
		street1:Option[String],
		street2:Option[String],
		city:Option[String],
		state:Option[String],
		country:Option[String],
		code:Option[String]) {

	def toXml : NodeSeq = {
		<street1>{street1.getOrElse(Empty)}</street1>
		<street2>{street2.getOrElse(Empty)}</street2>
		<city>{city.getOrElse(Empty)}</city>
		<state>{state.getOrElse(Empty)}</state>
		<country>{country.getOrElse(Empty)}</country>
		<code>{code.getOrElse(Empty)}</code>
	}

}

case class PrimaryAddress (
		street1:Option[String],
		street2:Option[String],
		city:Option[String],
		state:Option[String],
		country:Option[String],
		code:Option[String]) extends Address (street1, street2, city,state,country,code){

	override def toXml : NodeSeq = {
		<p_street1>{street1.getOrElse(Empty)}</p_street1>
		<p_street2>{street2.getOrElse(Empty)}</p_street2>
		<p_city>{city.getOrElse(Empty)}</p_city>
		<p_state>{state.getOrElse(Empty)}</p_state>
		<p_country>{country.getOrElse(Empty)}</p_country>
		<p_code>{code.getOrElse(Empty)}</p_code>
	}

}

case class SecondaryAddress (
		street1:Option[String],
		street2:Option[String],
		city:Option[String],
		state:Option[String],
		country:Option[String],
		code:Option[String]) extends Address (street1, street2, city,state,country,code){

	override def toXml : NodeSeq = {
		<s_street1>{street1.getOrElse(Empty)}</s_street1>
		<s_street2>{street2.getOrElse(Empty)}</s_street2>
		<s_city>{city.getOrElse(Empty)}</s_city>
		<s_state>{state.getOrElse(Empty)}</s_state>
		<s_country>{country.getOrElse(Empty)}</s_country>
		<s_code>{code.getOrElse(Empty)}</s_code>
	}

}

case class Client ( clientId : Option[Long],
		firstName:String, 
		lastName:String, 
		organization:String,
		email:String,
		username:Option[String], 
		password:Option[String], 
		workPhone:Option[String], 
		homePhone:Option[String], 
		mobile:Option[String], 
		fax:Option[String], 
		notes:Option[String], 
		primaryAddress:PrimaryAddress,
		secondaryAddress:SecondaryAddress) {

	def toXml : NodeSeq = {
		<client>
			{if(clientId.isDefined) <client_id>{ clientId.getOrElse(Empty) }</client_id>}
			<first_name>{firstName}</first_name>
			<last_name>{lastName}</last_name>
			<organization>{organization}</organization>
			<email>{email}</email> 
			<username>{username.getOrElse(Empty)}</username>
			<password>{password.getOrElse(Empty)}</password>
			<work_phone>{workPhone.getOrElse(Empty)}</work_phone>
			<home_phone>{homePhone.getOrElse(Empty)}</home_phone>
			<mobile>{mobile.getOrElse(Empty)}</mobile>
			<fax>{fax.getOrElse(Empty)}</fax>
			<notes>{notes.getOrElse(Empty)}</notes>
			{primaryAddress.toXml}
			{secondaryAddress.toXml}
		</client>
	}

}

object Client extends FreshbooksResource[Client] {
	
	def parseCreateResponse(xml:NodeSeq) : Option[Long]= 	{
		debug("Client:parseCreateResponse xml: {}", xml)		
		optionalLong(xml, "client_id")				
	}


	def parse(xml:NodeSeq) : Client = {
		debug("Client:parse xml {}", xml)
		val node:NodeSeq = xml \ ("client")			
		debug("Client:parse node {}", node)
		Client(
			optionalLong(node, "client_id"),
			node \ "first_name" text,  //firstName
			node \ "last_name" text,  //lastName
			node \ "organization" text,//organization
			node \ "email" text,//email
			optionalString(node, "username"), //username
			optionalString(node, "password"), //password
			optionalString(node, "work_phone"),//workPhone
			optionalString(node, "home_phone"),//homePhone
			optionalString(node, "mobile"),//mobile
			optionalString(node, "fax"),
			optionalString( node, "notes"),
			PrimaryAddress(
				optionalString(node, "p_street1"),
				optionalString(node, "p_street2"),
				optionalString(node, "p_city"),
				optionalString(node, "p_state"),
				optionalString(node, "p_country"),
				optionalString(node, "p_code")
			),
			SecondaryAddress(
				optionalString(node, "s_street1"),
				optionalString(node, "s_street2"),
				optionalString(node, "s_city"),
				optionalString(node, "s_state"),
				optionalString(node, "s_country"),
				optionalString(node, "s_code")
			)
		)
	}


	def parseList(node:NodeSeq) : List[Client] = {
		debug("Client:parseList {}", node)
		( node \\ "client").map( parse(_)).toList
	}


	def create( client:Client, account:Account) : Client = {
		debug("Client:create")
		val request = <request method="client.create">{client.toXml}</request>

		val response = post( account.domainName, account.authenticationToken, request)
		debug("Client.create - returned : {}", response)
		response match {
			case n:Created => {
				Client(
					parseCreateResponse(convertResponseToXml(n.response)),
					client.firstName,
					client.lastName,
					client.organization,
					client.email,
					client.username,
					client.password,
					client.workPhone,
					client.homePhone,
					client.mobile,
					client.fax,
					client.notes,
					client.primaryAddress,
					client.secondaryAddress
				)
			}
			case n => throw new RestException(n)			
		}		
	}

	def update( client:Client, account:Account) : Unit = {
		debug("Client:update")
		val request = <request method="client.update">{client.toXml}</request>

		post( account.domainName, account.authenticationToken, request)
	}

	def get( clientId:Long, account:Account) : Client = {
		debug("Client:get")
		val request = <request method="client.get"><client_id>{clientId.toString}</client_id></request>

		val status = post( account.domainName, account.authenticationToken, request)
		debug("Client.get - returned: {}", status)
		status match {
			case n:Created => ( parse( convertResponseToXml(n.response)))
			case n => throw new RestException(n)
		}
	}

	def delete( clientId:Long, account:Account) = {
		debug("Client:delete")
		val request = <request method="client.delete"><client_id>{clientId.toString}</client_id></request>

		val status = post( account.domainName, account.authenticationToken, request)
		debug("Client.delete - returned: {}", status)
		status match {
			case n:Ok => 
			case n => throw new RestException(n)
		} 
	}


	def list( account:Account) : List[Client] = {
		debug("Client:list")
		val request = <request method="client.list"></request>

		val status = post( account.domainName, account.authenticationToken, request)
		debug("Client.list - returned: {}", status)
		status match {
			case n:Created => ( parseList( convertResponseToXml(n.response)))
			case n => throw new RestException(n)
		}
	}
}
