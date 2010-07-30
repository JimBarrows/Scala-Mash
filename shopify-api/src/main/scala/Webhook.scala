package scala_mash.shopify_api.model

import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormatterBuilder}
import xml.{NodeSeq, Node}

import scala_mash.shopify_api.{ShopifyPartnerInfo, ShopifyResource}
import scala_mash.rest.util.Helpers._
import scala_mash.rest.{Ok,Created,RestException}

import bizondemand.utils.models.internet.{Url, Parameter}



case class Webhook (address:Url, 
					createdAt:DateTime, 
					id:Int, 
					topic:String, 
					updatedAt:DateTime) {
	
	def toXml = <webhook>
		<address>{address}</address>
		<created-at>{printWithTimeZone(createdAt)}</created-at>
		<id>{id}</id>
		<topic>{topic}</topic>
		<updated-at>{printWithTimeZone(updatedAt)}</updated-at>
	</webhook>
	
}

object Webhook extends ShopifyResource[Webhook] {
	
	val url = shopifyUrl +/ ("admin")
	
	def listAll( shop: ShopCredentials,
			limit:Option[Int], 
			page:Option[Int], 
			createdAtMin:Option[DateTime], 
			createdAtMax:Option[DateTime], 
			updatedAtMin:Option[DateTime], 
			updatedAtMax:Option[DateTime],
			topic:Option[String],
			address:Option[Url]) : List[Webhook]= {
				
		debug("Webhook.listAll({},{},{},{},{},{},{},{})", limit,page,createdAtMin, createdAtMax, updatedAtMin, updatedAtMax, topic, address)
		
		val p = Parameter("foo","foo")
		
		val parameterList:List[Parameter] = 
			limit.map( Parameter("limit", _)).toList ::: 
			page.map(Parameter("page", _)).toList :::
			createdAtMin.map( n => {Parameter("created_at_min", printWithTimeZone(n))}).toList :::
			createdAtMax.map( n => {Parameter("created_at_max", printWithTimeZone(n))}).toList :::
			updatedAtMin.map( n => {Parameter("updated_at_min", printWithTimeZone(n))}).toList :::
			updatedAtMax.map( n => {Parameter("updated_at_max", printWithTimeZone(n))}).toList :::
			topic.map( Parameter("topic", _)).toList :::
			address.map( n => {Parameter("address", n.toString)}).toList :::
			Nil
			
		get( url +< shop.name +/ ("webhooks.xml") ++& parameterList, 
			Some(ShopifyPartnerInfo.apiKey), 
			Some(ShopifyPartnerInfo.createPasswordForStore(shop.authenticationToken))
			
		) match {
			case n:Ok => parseList( convertResponseToXml(n.response))	
			case n => throw new RestException(n)
		}
	}
	
	def count(shop: ShopCredentials,
			topic:Option[String], 
			address:Option[Url]) : Int = {
				
		debug("Webhook.count({},{})", topic, address)
		
		val parameterList:List[Parameter] = 
			topic.map( Parameter("topic", _)).toList :::
			address.map( n => {Parameter("address", n.toString)}).toList :::
			Nil
			
		get( url +< shop.name +/ "count.xml" ++& parameterList,
			Some(ShopifyPartnerInfo.apiKey), 
			Some(ShopifyPartnerInfo.createPasswordForStore(shop.authenticationToken))
		) match {
			case n:Ok => (convertResponseToXml(n.response) \ "count" text).toInt
			case n => throw new RestException(n)
		}
	}
	
	def get(shop: ShopCredentials,
			id:Int) : Webhook = {
		debug("Webhook.get({})", id)
		
		get( url +< shop.name +/ "webhooks" +/ "%d.xml".format(id),
			Some(ShopifyPartnerInfo.apiKey), 
			Some(ShopifyPartnerInfo.createPasswordForStore(shop.authenticationToken))
		) match {
			case n:Ok => parse( convertResponseToXml(n.response))	
			case n => throw new RestException(n)
		}
	}
	
	def create(shop: ShopCredentials,
			address:Url, 
			topic:String):Webhook = {
		debug("Webhook.create({},{})", address, topic)
		
		val xml = <webhook>
			<address>{address.toString}</address>
			<topic>{topic}</topic>
		</webhook>
		post( url +< shop.name +/ "webhooks.xml",
			Some(ShopifyPartnerInfo.apiKey), 
			Some(ShopifyPartnerInfo.createPasswordForStore(shop.authenticationToken)),
			xml
		) match {
			case n:Created => parse( convertResponseToXml(n.response))
			case n=> throw new RestException(n)
		}
	}
	
	def modify(shop: ShopCredentials, webhook:Webhook) : Webhook = {
		debug("Webhook.modify({}, {})", shop, webhook)
		
		put( url +< shop.name +/ "webhooks" +/ "%d.xml".format(webhook.id), 
			Some(ShopifyPartnerInfo.apiKey), 
			Some(ShopifyPartnerInfo.createPasswordForStore(shop.authenticationToken)),
			<webhook>
				<address>{webhook.address.toString}</address>
				<id>{webhook.id}</id>
			</webhook>
		) match {
			case n:Ok => parse( convertResponseToXml(n.response))
			case n=> throw new RestException(n)
		}
	}
	
	def remove(shop: ShopCredentials,
			id:Int) :Unit = {
		debug("Webhook.remove({})", id)
		
		delete(url +< shop.name +/ "webhooks" +/ "%d.xml".format(id), 
			Some(ShopifyPartnerInfo.apiKey), 
			Some(ShopifyPartnerInfo.createPasswordForStore(shop.authenticationToken))
		) match {
			case n:Ok =>
			case n => throw new RestException(n)
		}
	}
	
	def parse( node:NodeSeq) :Webhook = {
		debug("Webhook.parse({})",node)
		Webhook(
			Url(node \ "address" text),   								//address:Url, 
			parseDateTimeWithTimeZone(node \ "created-at" text), 	//createdAt:DateTime, 
			(node \ "id" text).toInt, 								//id:Int, 
			node \ "topic" text, 									//topic:String, 
			parseDateTimeWithTimeZone(node \ "updated-at" text)	//updatedAt:DateTime
		)
	}
	
	def parseList(node:NodeSeq) : List[Webhook] = {
		debug("Webhook.parseList({})", node)
		(node \\ "webhooks").map(parse(_)).toList
	}
}