package com.bizondemand.freshbooks_api.model

import xml._
import NodeSeq._


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
	
	def parseCreateResponse(xml:NodeSeq) = 	{
		debug("Client:parseCreateResponse xml: {}", xml)
		Client(
				(if (( xml \ "client_id").isEmpty) None else Some((xml \ "client_id" text).toLong)),
				"", //firstName:String, 
				"", //lastName:String, 
				"", //organization:String,
				"", //email:String,
				None, //username:Option[String], 
				None, //password:Option[String], 
				None, //workPhone:Option[String], 
				None, //homePhone:Option[String], 
				None, //mobile:Option[String], 
				None, //fax:Option[String], 
				None, //notes:Option[String], 
				PrimaryAddress (//primaryAddress:PrimaryAddress,
						None, //street1:Option[String],
						None, //street2:Option[String],
						None, //city:Option[String],
						None, //state:Option[String],
						None, //country:Option[String],
						None //code:Option[String]
				),
				SecondaryAddress (//secondaryAddress:SecondaryAddress
						None, //street1:Option[String],
						None, //street2:Option[String],
						None, //city:Option[String],
						None, //state:Option[String],
						None, //country:Option[String],
						None //code:Option[String]
				) 
		) 
	}


	def parse(xml:NodeSeq) : Client = {
			debug("Client:parse xml {}", xml)
			val node:NodeSeq = xml \ ("client")
			/*match {
				case <response>{clientXml @ _ *}</response> =>debug("clientXml: {}", clientXml); clientXml
				case _ => debug("default");xml
			}*/
			debug("Client:parse node {}", node)
			Client(
			(if (( node \ "client_id").isEmpty) None else Some((node \ "client_id" text).toLong)),
			node \ "first_name" text,  //firstName
			node \ "last_name" text,  //lastName
			node \ "organization" text,//organization
			node \ "email" text,//email
			(if (( node \ "username" text).isEmpty) None else Some((node \ "username" text))), //username
			(if (( node \ "password" text).isEmpty) None else Some((node \ "password" text))), //password
			(if (( node \ "work_phone" text).isEmpty) None else Some((node \ "work_phone" text))),//workPhone
			(if (( node \ "home_phone" text).isEmpty) None else Some((node \ "home_phone" text))),//homePhone
			(if (( node \ "mobile" text).isEmpty) None else Some((node \ "mobile" text))),//mobile
			(if (( node \ "fax" text).isEmpty) None else Some((node \ "fax" text))),//fax
			(if (( node \ "notes" text).isEmpty) None else Some((node \ "notes" text))),//notes
			PrimaryAddress(
			(if (( node \ "p_street1" text).isEmpty) None else Some((node \ "p_street1" text).toString)),
			(if (( node \ "p_street2" text).isEmpty) None else Some((node \ "p_street2" text).toString)),
			(if (( node \ "p_city" text).isEmpty) None else Some((node \ "p_city" text).toString)),
			(if (( node \ "p_state" text).isEmpty) None else Some((node \ "p_state" text).toString)),
			(if (( node \ "p_country" text).isEmpty) None else Some((node \ "p_country" text).toString)),
			(if (( node \ "p_code" text).isEmpty) None else Some((node \ "p_code" text).toString))),	
			SecondaryAddress(
			(if (( node \ "s_street1" text).isEmpty) None else Some((node \ "s_street1" text).toString)),
			(if (( node \ "s_street2" text).isEmpty) None else Some((node \ "s_street2" text).toString)),
			(if (( node \ "s_city" text).isEmpty) None else Some((node \ "s_city" text).toString)),
			(if (( node \ "s_state" text).isEmpty) None else Some((node \ "s_state" text).toString)),
			(if (( node \ "s_country" text).isEmpty) None else Some((node \ "s_country" text).toString)),
			(if (( node \ "s_code" text).isEmpty) None else Some((node \ "s_code" text).toString)))	
			)
	}


	def parseList(node:NodeSeq) : List[Client] = {
			debug("Client:parseList {}", node)
		( node \\ "client").map( parse(_)).toList
	}


	def create( client:Client, account:Account) : Client = {
		debug("Client:create")
		val request = <request method="client.create">{client.toXml}</request>

		val response = post( account.domainName, account.authenticationToken, request, Client.parseCreateResponse _)
		Client(
			response.clientId,
			client.firstName,
			client.lastName,
			client.organization,
			client.email,
			client.username,
			(if (client.password.isEmpty) response.password else client.password),
			client.workPhone,
			client.homePhone,
			client.mobile,
			client.fax,
			client.notes,
			client.primaryAddress,
			client.secondaryAddress
		)
	}

	def update( client:Client, account:Account) : Unit = {
		debug("Client:update")
		val request = <request method="client.update">{client.toXml}</request>

		post( account.domainName, account.authenticationToken, request, Client.parse _)
	}

	def get( clientId:Long, account:Account) : Client = {
		debug("Client:get")
		val request = <request method="client.get"><client_id>{clientId.toString}</client_id></request>

		post( account.domainName, account.authenticationToken, request, Client.parse _)
	}

	def delete( clientId:Long, account:Account) : Client = {
		debug("Client:delete")
		val request = <request method="client.delete"><client_id>{clientId.toString}</client_id></request>

		post( account.domainName, account.authenticationToken, request, Client.parse _)
	}


	def list( account:Account) : List[Client] = {
		debug("Client:list")
		val request = <request method="client.list"></request>

		post( account.domainName, account.authenticationToken, request, Client.parseList _)
	}
}
