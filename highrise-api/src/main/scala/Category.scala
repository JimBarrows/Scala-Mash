package scala_mash.highrise_api.models

import xml._
import  xml.NodeSeq
import  xml.NodeSeq._

import org.joda.time.DateTime

import scala_mash.highrise_api._
import scala_mash.highrise_api.Utils._

import scala_mash.rest.{Ok, Created, RestException}
import scala_mash.rest.util.Helpers.{optionalLong, optionalDateTimeWithTimeZone, printWithTimeZone}

import bizondemand.utils.models.internet.Url

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
		{id.map( v => <id type="integer">{v}</id>).getOrElse(Empty)}
		<name>{name}</name>
		{accountId.map( v => <account-id>{v}</account-id>).getOrElse(Empty)}
		{createdAt.map( v=> <created-at>{printWithTimeZone(v)}</created-at>).getOrElse(Empty)}
		{updatedAt.map( v => <updated-at>{printWithTimeZone(v)}</updated-at>).getOrElse(Empty)}
		{elementsCount.map( v => <elements-count type="integer">{v}</elements-count>).getOrElse(Empty)}
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
		{id.map( v => <id type="integer">{v}</id>).getOrElse(Empty)}
		<name>{name}</name>
		{accountId.map( v => <account-id>{v}</account-id>).getOrElse(Empty)}
		{createdAt.map( v=> <created-at>{printWithTimeZone(v)}</created-at>).getOrElse(Empty)}
		{updatedAt.map( v => <updated-at>{printWithTimeZone(v)}</updated-at>).getOrElse(Empty)}
		{elementsCount.map( v => <elements-count type="integer">{v}</elements-count>).getOrElse(Empty)}
	</deal-category>
}
		

object TaskCategory extends HighriseServices[TaskCategory] {
	
	def parse(node: NodeSeq) = {
		debug("TaskCategory.parse({})", node.toString)
		TaskCategory( 
			optionalLong( node, "id"),
			node \ "name" text,
			optionalLong(node,"account-id"),
			optionalDateTimeWithTimeZone(node, "created-at"),
			optionalDateTimeWithTimeZone(node, "updated-at"),
			optionalLong(node, "elements-count")
		)
	}

	def parseList(node: NodeSeq): List[TaskCategory] = { 
			(node \\ "task-category").map( parse( _)).toList
	}


	def show( id :Long, account:Account) : TaskCategory = {
		get( url +< (account.siteName) +/ ("task_categories") +/ (id.toString + ".xml"), Some(account.apiKey), Some("x")) match {
			case n:Ok => parse(convertResponseToXml(n.response))
			case n => defaultStatusHandler(n)
		}
	}

	def list( account:Account) : List[TaskCategory] = {
		get( url +< (account.siteName) +/ ("task_categories.xml"), Some(account.apiKey), Some("x") )match {
			case n:Ok => parseList( convertResponseToXml(n.response))
			case n => defaultStatusHandler(n)
		}
	}
	
	def create( name :String, account:Account) : TaskCategory = {
		post(url +< (account.siteName) +/  "task_categories.xml", 
			Some(account.apiKey), 
			Some("x"), 
			<task-category><name>{name}</name></task-category> 
		)match {
			case n:Created => getByUrl(Url(n.location.toString +".xml"), account, parse _)
			case n => defaultStatusHandler(n)
		}
	}

	def update( taskCategory :TaskCategory, account:Account)  = {
		put(url +< (account.siteName) +/  ("task_categories") +/ ( taskCategory.id.getOrElse(0) + ".xml"), 
			Some(account.apiKey), 
			Some("x"), 
			<task-category><name>{taskCategory.name}</name></task-category> 
		)match {
			case n:Ok => parse( convertResponseToXml(n.response))
			case n => defaultStatusHandler(n)
		} 
	}

	def destroy( taskCategory :TaskCategory, account:Account)  = {
		delete(url +< (account.siteName) +/  ("task_categories") +/ ( taskCategory.id.getOrElse(0) + ".xml"), 
			Some(account.apiKey), 
			Some("x")
		) match {
			case n:Ok => 
			case n => defaultStatusHandler(n)
		}
	}

}

object DealCategory extends HighriseServices[DealCategory] {
	
	def parse(node: NodeSeq) = {
		debug("DealCategory.parse({})", node)
		DealCategory( 
			optionalLong( node, "id"),
			node \ "name" text,
			optionalLong(node,"account-id"),
			optionalDateTimeWithTimeZone(node, "created-at"),
			optionalDateTimeWithTimeZone(node, "updated-at"),
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
			case n => defaultStatusHandler(n)
		} 
	}

	def list( account:Account) : List[DealCategory] = {
		get( url +< (account.siteName) +/ ("deal_categories.xml"), 
			Some(account.apiKey), 
			Some("x")
		)match {
			case n:Ok => parseList( convertResponseToXml(n.response))
			case n => defaultStatusHandler(n)
		} 
	}
	
	def create( name :String, account:Account) : DealCategory = {
		post(url +< (account.siteName) +/  ("deal_categories.xml"), 
			Some(account.apiKey), 
			Some("x"), 
			<deal-category><name>{name}</name></deal-category>
		)match {
			case n:Created => getByUrl(Url(n.location.toString + ".xml"), account, parse _)
			case n => defaultStatusHandler(n)
		} 
	}

	def update( dealCategory :DealCategory, account:Account)  = {
		put(url +< (account.siteName) +/  ("deal_categories") +/ ( dealCategory.id.getOrElse(0) + ".xml"), 
			Some(account.apiKey), 
			Some("x"), 
			<deal-category><name>{dealCategory.name}</name></deal-category> 
		)match {
			case n:Ok => parse( convertResponseToXml(n.response))
			case n => defaultStatusHandler(n)
		} 
	}

	def destroy( dealCategory :DealCategory, account:Account)  = {
		delete(url +< (account.siteName) +/  ("deal_categories") +/ ( dealCategory.id.getOrElse(0) + ".xml"), 
			Some(account.apiKey), 
			Some("x")
		)match {
			case n:Ok => 
			case n => defaultStatusHandler(n)
		} 
	}
}
