package janjaali

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger
import janjaali.blobstorage.BlobStorageService
import janjaali.blobstorage.clients.BlobServiceClient

import scala.concurrent.ExecutionContext

object AzureStorageCdnMock {

  private val logger = Logger("AzureStorageCdnMock")

  def main(args: Array[String]): Unit = {

    logger.info("Starting azure-storage-cdn-mock.")

    val config = ConfigFactory.load()

    implicit val actorSystem: ActorSystem = ActorSystem("AzureStorageCdnMock")
    implicit val executionContext: ExecutionContext = actorSystem.dispatcher

    val port = config.getInt("server.port")

    val blobServiceEndpoint = BlobServiceClient.Endpoint {
      config.getString("azure.storage.endpoint")
    }

    val blobServiceAccountName = BlobServiceClient.Credentials.AccountName {
      config.getString("azure.storage.account-name")
    }

    val blobServiceAccountKey = BlobServiceClient.Credentials.AccountKey {
      config.getString("azure.storage.account-key")
    }

    val downloadTimeout = {

      import scala.jdk.DurationConverters._

      config.getDuration("download-timeout").toScala
    }

    val blobServiceClient = BlobServiceClient(
      endpoint = blobServiceEndpoint,
      credentials = BlobServiceClient.Credentials(
        accountName = blobServiceAccountName,
        accountKey = blobServiceAccountKey
      ))

    val blobStorageService = new BlobStorageService(blobServiceClient)

    val azureStorageService = new AzureStorageService(blobStorageService, downloadTimeout)

    val endpoints = new Endpoints(azureStorageService)

    val handler = endpoints.routes

    Http().bindAndHandle(handler, "0.0.0.0", port)

    logger.info(s"azure-storage-cdn-mock started accessing azure-storage: '$blobServiceEndpoint'.")
  }
}
