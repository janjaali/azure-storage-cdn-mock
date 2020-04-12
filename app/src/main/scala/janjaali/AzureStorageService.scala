package janjaali

import akka.stream.scaladsl.Source
import akka.util.ByteString
import janjaali.blobstorage._
import janjaali.blobstorage.model._

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

class AzureStorageService(blobStorageService: BlobStorageService, downloadTimeout: FiniteDuration) {

  def download(containerName: String, blobName: String)
  : Future[Option[(Source[ByteString, Future[Unit]], BlobItem.Properties.ContentType)]] = {

    blobStorageService.download(ContainerName(containerName), BlobName(blobName), downloadTimeout)
  }
}
