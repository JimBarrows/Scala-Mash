package test

import org.specs.Specification

import bizondemand.utils.models.internet.{Url, Parameter}
import scala_mash.shopify_api.Utils._

import scala_mash.shopify_api.model.Webhook

import scala_mash.rest.util.Helpers.{parseDateTimeWithTimeZone}

object WebhookSpecs extends Specification {
	
	"The Webhook class and object" should {
		
		val webhookXml = <webhook>
				<address>http://apple.com</address>
				<created-at>2010-05-14T21:04:56-04:00</created-at>
				<id>4759036</id>
				<topic>orders/create</topic>
				<updated-at>2010-05-14T21:04:56-04:00</updated-at>
			</webhook>
		
		val webhook = Webhook (Url("http://apple.com"), 								//address:Url, 
					parseDateTimeWithTimeZone("2010-05-14T21:04:56-04:00"), //createdAt:DateTime, 
					4759036, 												//id:Int, 
					"orders/create", 										//topic:String, 
					parseDateTimeWithTimeZone("2010-05-14T21:04:56-04:00")	//updatedAt:DateTime
			)
			
		"emit valid XML" in {			
			
			webhook.toXml must equalIgnoreSpace ( webhookXml)
		}
		
		"parse XML" in {
			Webhook.parse(webhookXml) must be_== ( webhook)
		}
	}
}