package scala_mash.shopify_api

import model.{ShopCredentials, Shop}
import org.apache.commons.codec.digest.DigestUtils._


/**
 * 
 * @author jimbarrows
 * @created: Feb 4, 2010 11:14:02 PM
 * @version 1.0
 * 
 */

object ShopifyPartnerInfo {
  /**This is the apikey for the shopify partner.
   *
   */
  lazy val apiKey: String = System.getProperty("com.nsfw.shopifyapp.apikey", "48dbca26085d50ae1383c2c743ba8eb1")

  /**This is the secret for the shopify partner
   *
   */
  lazy val secret: String = System.getProperty("com.nsfw.shopifyapp.password", "ddd7818dbd53dcdae0d3d6301b5179a1")

  def createPasswordForStore(authenticationToken: String): String = md5Hex(secret + authenticationToken)

}
