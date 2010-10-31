package specs

import org.specs.Specification

import org.joda.time.DateTime

import scala_mash.shopify_api.model.Webhook
import scala_mash.rest.RestException
import bizondemand.utils.models.internet.{Url, Parameter}


object WebhookServicesSpec extends Specification {

	"Webhook Services" should {

		shareVariables()
		setSequential()

		"create a new webhook" in{
			val created = Webhook.create( ShopSpec.shopCredentials
					, webhook.address
					, webhook.topic)
			created.address must be_==( webhook.address)
			created.topic must be_==( webhook.topic)
			created.createdAt must beSome[DateTime]
			created.updatedAt must beSome[DateTime]
			webhook = created
		}

		"receive a list of all webhooks" in {
			val hookList = Webhook.listAll(ShopSpec.shopCredentials
					, None  //limit
					, None  //page
					, None  //createdAtMin
					, None  //createdAtMax
					, None  //updateAtMin
					, None  //updateAtMax
					, None  //topic
					, None  //address
				)

			hookList must contain(webhook)
			webhookList = hookList
		}

		"receive a count of all webhooks" in {
			val hookCount = Webhook.count(ShopSpec.shopCredentials
					, None
					, None)
			hookCount must be_==( webhookList.length)
		}

		"receive a single webhook" in {
			val hook = Webhook.get(ShopSpec.shopCredentials
				, webhook.id.getOrElse(0))
			hook must be_==( webhook)
		}

		"modify an existing webhook" in {
			var changedHook = Webhook(Url("http://somewhereElse.biz/change")
					, webhook.createdAt		//createdAt
					, webhook.id  				//id
					, webhook.topic				//topic
					, webhook.updatedAt		//updateAt
					)

			val postHook = Webhook.modify(ShopSpec.shopCredentials
					, changedHook)
			postHook.topic must be_==( changedHook.topic)
			postHook.address must be_==( changedHook.address)
			webhook = postHook
		}

		"remove a webhook" in {
			Webhook.remove( ShopSpec.shopCredentials, webhook)
			Webhook.get( ShopSpec.shopCredentials, webhook.id.getOrElse(0)) must throwA[RestException]
		}

	}

	var webhook = Webhook(Url("http://bizondemand.biz")
			, None
			, None
			, "orders/create"
			, None)
	var webhookList: List[Webhook] = Nil
}
