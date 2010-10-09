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

		"parse xml" in {
			val parsedAttachment = Attachment.parse(basicAttachmentXml) 
			parsedAttachment must_==( basicAttachment)
		}

		"parse xml list" in {
			Attachment.parseList(basicAttachmentListXml) must_==( basicAttachmentList)
		}
	}

	val basicAttachment = Attachment( 
			Some(1), 
			Url("http://example.highrisehq.com/files/a"),
			"picture.png",
			72633)

	val basicAttachmentXml =
			<attachment>
				<id type="integer">1</id>
				<name>picture.png</name>
				<size type="integer">72633</size>
				<url>http://example.highrisehq.com/files/a</url>
			</attachment>

	val basicAttachmentList = basicAttachment :: basicAttachment :: Nil

	val basicAttachmentListXml = 
			<attachments>
				<attachment>
					<id type="integer">1</id>
					<name>picture.png</name>
					<size type="integer">72633</size>
					<url>http://example.highrisehq.com/files/a</url>
				</attachment>
				<attachment>
					<id type="integer">1</id>
					<name>picture.png</name>
					<size type="integer">72633</size>
					<url>http://example.highrisehq.com/files/a</url>
				</attachment>
			</attachments>

}

