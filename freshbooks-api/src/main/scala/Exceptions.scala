package scala_mash.freshbooks_api.model

import xml._
import NodeSeq._
import org.joda.time.{LocalDate, DateTime}

import bizondemand.utils.models.internet.Url
import scala_mash.rest.util.Helpers._
import scala_mash.rest.{Ok,RestException}
import Utils._

class FreshbooksApiException(msg:String) extends Exception(msg) {
}

class ExistingInvoiceNumberException extends FreshbooksApiException( "This invoice number is already in use. Please choose another.")  

class NoClientExistsException extends FreshbooksApiException( "Client does not exist.")

class AutobillingOnlyAvailableInBaseCurrency extends FreshbooksApiException( "Auto-billing is only available in the base currency.")
