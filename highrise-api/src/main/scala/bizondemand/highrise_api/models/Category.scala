package bizondemand.highrise_api.models

import xml._
import  xml.NodeSeq
import  xml.NodeSeq._

import org.joda.time.DateTime

import bizondemand.highrise_api._
import bizondemand.highrise_api.Utils._

import bizondemand.rest.{Ok, Created, RestException}
import bizondemand.util.Helpers.{optionalLong}

object CategoryTypes extends Enumeration {
	type CategoryTypes = Value
	val Task = Value("task")
	val Deal = Value("deal")

}
import CategoryTypes._

/**Represents a task category in HighriseHQ.  The only field that's user changelable is the name.
	*/
case class TaskCategory ( id :Option[Long], 
						var name :String, 
						accountId:Option[Long], 
						createdAt : Option[DateTime],
						updatedAt : Option[DateTime],
						elementsCount : Option[Long]) {
	def toXml : NodeSeq = <task-category>
		<id type="integer">{id.getOrElse(Empty).toString}</id>
		<name>{name}</name>
		<account-id>{accountId.getOrElse(Empty)}</account-id>
		<created-at>{createdAt.map(printWithTimeZone(_)).getOrElse(Empty)}</created-at>
		<updated-at>{updatedAt.map(printWithTimeZone(_)).getOrElse(Empty)}</updated-at>
		<elements-count type="integer">{elementsCount.getOrElse(0)}</elements-count>
	</task-category>
}


/**Represents a deal category in HighriseHQ.  The only field that's user changelable is the name.
	*/
case class DealCategory ( id :Option[Long], 
						var name :String, 
						accountId:Option[Long], 
						createdAt : Option[DateTime],
						updatedAt : Option[DateTime],
						elementsCount : Option[Long]) {
	def toXml : NodeSeq = <deal-category>
		<id type="integer">{id.getOrElse(Empty).toString}</id>
		<name>{name}</name>
		<account-id>{accountId.getOrElse(Empty)}</account-id>
		<created-at>{createdAt.map(printWithTimeZone(_)).getOrElse(Empty)}</created-at>
		<updated-at>{updatedAt.map(printWithTimeZone(_)).getOrElse(Empty)}</updated-at>
		<elements-count type="integer">{elementsCount.getOrElse(0)}</elements-count>
	</deal-category>
}
		

object TaskCategory extends HighriseServices[TaskCategory] {
	
	def parse(node: NodeSeq) = {
		debug("TaskCategory.parse({})", node.toString)
		TaskCategory( 
			optionalLong( node, "id"),
			node \ "name" text,
			optionalLong(node,"account-id"),
			optionalParseDateTimeWithTimeZone(node, "created-at"),
			optionalParseDateTimeWithTimeZone(node, "updated-at"),
			optionalLong(node, "elements-count")
		)
	}

	def parseList(node: NodeSeq): List[TaskCategory] = { 
			(node \\ "task-category").map( parse( _)).toList
	}


	def show( id :Long, account:Account) : TaskCategory = {
		get( url +< (account.siteName) +/ ("task_categories") +/ (id.toString + ".xml"), Some(account.apiKey), Some("x")) match {
			case n:Ok => parse(convertResponseToXml(n.response))
			case n => throw new RestException( n)
		}
	}

	def list( account:Account) : List[TaskCategory] = {
		get( url +< (account.siteName) +/ ("task_categories.xml"), Some(account.apiKey), Some("x") )match {
			case n:Ok => parseList( convertResponseToXml(n.response))
			case n => throw new RestException(n)
		}
	}
	
	def create( name :String, account:Account) : TaskCategory = {
		post(url +< (account.siteName) +/  "task_categories.xml", 
			Some(account.apiKey), 
			Some("x"), 
			<task-category><name>{name}</name></task-category> 
		)match {
			case n:Created => getByUrl(n.location, account, parse _)
			case n => throw new RestException(n)
		}
	}

	def update( taskCategory :TaskCategory, account:Account)  = {
		put(url +< (account.siteName) +/  ("task_categories") +/ ( taskCategory.id.getOrElse(0) + ".xml"), 
			Some(account.apiKey), 
			Some("x"), 
			<task-category><name>{taskCategory.name}</name></task-category> 
		)match {
			case n:Ok => parse( convertResponseToXml(n.response))
			case n => throw new RestException(n)
		} 
	}

	def destroy( taskCategory :TaskCategory, account:Account)  = {
		delete(url +< (account.siteName) +/  ("task_categories") +/ ( taskCategory.id.getOrElse(0) + ".xml"), 
			Some(account.apiKey), 
			Some("x")
		) match {
			case n:Ok => 
			case n => throw new RestException(n)
		}
	}

}

object DealCategory extends HighriseServices[DealCategory] {
	
	def parse(node: NodeSeq) = {
		debug("DealCategory.parse({})", node.toString)
		DealCategory( 
			optionalLong( node, "id"),
			node \ "name" text,
			optionalLong(node,"account-id"),
			optionalParseDateTimeWithTimeZone(node, "created-at"),
			optionalParseDateTimeWithTimeZone(node, "updated-at"),
			optionalLong(node, "elements-count")
		)
	}

	def parseList(node: NodeSeq): List[DealCategory] = { 
			(node \\ "deal-category").map( parse( _)).toList
	}

	def show( id :Long, account:Account) : DealCategory = {
		get( url +< (account.siteName) +/ ("deal_categories") +/ (id.toString + ".xml"), 
			Some(account.apiKey), 
			Some("x")
		)match {
			case n:Ok => parse( convertResponseToXml(n.response))
			case n => throw new RestException(n)
		} 
	}

	def list( account:Account) : List[DealCategory] = {
		get( url +< (account.siteName) +/ ("deal_categories.xml"), 
			Some(account.apiKey), 
			Some("x")
		)match {
			case n:Ok => parseList( convertResponseToXml(n.response))
			case n => throw new RestException(n)
		} 
	}
	
	def create( name :String, account:Account) : DealCategory = {
		post(url +< (account.siteName) +/  ("deal_categories.xml"), 
			Some(account.apiKey), 
			Some("x"), 
			<deal-category><name>{name}</name></deal-category>
		)match {
			case n:Created => getByUrl(n.location, account, parse _)
			case n => throw new RestException(n)
		} 
	}

	def update( dealCategory :DealCategory, account:Account)  = {
		put(url +< (account.siteName) +/  ("deal_categories") +/ ( dealCategory.id.getOrElse(0) + ".xml"), 
			Some(account.apiKey), 
			Some("x"), 
			<deal-category><name>{dealCategory.name}</name></deal-category> 
		)match {
			case n:Ok => parse( convertResponseToXml(n.response))
			case n => throw new RestException(n)
		} 
	}

	def destroy( dealCategory :DealCategory, account:Account)  = {
		delete(url +< (account.siteName) +/  ("deal_categories") +/ ( dealCategory.id.getOrElse(0) + ".xml"), 
			Some(account.apiKey), 
			Some("x")
		)match {
			case n:Ok => 
			case n => throw new RestException(n)
		} 
	}
}
