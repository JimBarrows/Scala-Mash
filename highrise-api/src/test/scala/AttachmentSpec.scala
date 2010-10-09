package specs

import org.specs.Specification

import org.joda.time._

import bizondemand.utils.models.internet.{Url, DomainName, Http}

import scala_mash.highrise_api._
import models._

object AttachmentSpec extends Specification {

	"The Attachment class" should {
		"create xml" in {
			basicAttachment.toXml must ==/( basicAttachmentXml)
		}
	}

	val basicAttachment = Attachment( 
			Some(1), 
			Url("http", DomainName("example"::"highrisehq"::"com"::Nil)) +/ "files" +/ "a", 
			"picture.png",
			72633)
/*
				*/
	val basicAttachmentXml =
			<attachment>
				<id type="integer">1</id>
				<name>picture.png</name>
				<size type="integer">72633</size>
				<url>http://example.highrisehq.com/files/a</url>
			</attachment>

}

