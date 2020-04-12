package janjaali

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.scaladsl.Source
import akka.util.ByteString
import janjaali.blobstorage.model._
import org.scalamock.scalatest.MockFactory

import scala.concurrent.Future

class EndpointsSpec extends Spec with ScalatestRouteTest with MockFactory {

  import EndpointsSpec._

  "Endpoints" - {

    "should provide a route to download blob items." - {

      "when path is empty" - {

        val path = "/"

        "should respond with a 404 Not Found." in {

          val fixture = createFixture()

          Get(path) ~> Route.seal(fixture.sut.routes) ~> check {
            status shouldBe StatusCodes.NotFound
          }
        }
      }

      "when path contains only container name part" - {

        val path = "/container-name"

        "should respond with a 404 Not Found." in {

          val fixture = createFixture()

          Get(path) ~> Route.seal(fixture.sut.routes) ~> check {
            status shouldBe StatusCodes.NotFound
          }
        }
      }

      "when path contains both container name and blob name" - {

        val path = "/container-name/blob-name"

        "when blob exists." - {

          "should provide a download stream and the content-type." in {

            val fixture = createFixture()

            val content = "some-content"
            val downloadSource = Source.single(ByteString(content)).mapMaterializedValue(_ => Future.successful(()))
            val akkaContentType = ContentTypes.`application/octet-stream`
            val contentType = BlobItem.Properties.ContentType(akkaContentType.toString())

            (fixture.azureStorageServiceMock.download _)
              .expects("container-name", "blob-name")
              .returns(Future.successful(Some((downloadSource, contentType))))

            Get(path) ~> Route.seal(fixture.sut.routes) ~> check {
              status shouldBe StatusCodes.OK
              responseEntity.contentType shouldBe akkaContentType
            }
          }
        }

        "when blob does not exists." - {

          "should respond with a 404 Not Found." in {

            val fixture = createFixture()

            (fixture.azureStorageServiceMock.download _)
              .expects("container-name", "blob-name")
              .returns(Future.successful(None))

            Get(path) ~> Route.seal(fixture.sut.routes) ~> check {
              status shouldBe StatusCodes.NotFound
            }
          }
        }
      }
    }
  }

  private def createFixture(): Fixture = {

    val azureStorageService = mock[AzureStorageService]

    val endpoints = new Endpoints(azureStorageService)

    Fixture(
      sut = endpoints,
      azureStorageServiceMock = azureStorageService
    )
  }
}

object EndpointsSpec {

  private case class Fixture(sut: Endpoints, azureStorageServiceMock: AzureStorageService)
}
