package janjaali

import akka.http.scaladsl.model.HttpEntity.ChunkStreamPart
import akka.http.scaladsl.model.Uri.Path
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class Endpoints(azureStorageService: AzureStorageService) {

  val routes: Route = {

    get {

      extractUnmatchedPath { path =>

        val pathWithoutFirstSlash = path.tail

        pathWithoutFirstSlash match {

          case _: Path.SlashOrEmpty =>

            complete(StatusCodes.NotFound)

          case Path.Segment(containerName, maybeBlobName) =>

            maybeBlobName match {

              case Path.Empty =>

                complete(StatusCodes.NotFound)

              case Path.Slash(blobName) =>

                onSuccess(azureStorageService.download(containerName, blobName.toString())) {
                  case Some((downloadSource, contentType)) =>

                    ContentType.parse(contentType.value) match {

                      case Left(failure) =>

                        complete((
                          StatusCodes.InternalServerError,
                          s"Invalid content type '$contentType' for [container: '$containerName', blob: '$blobName']: $failure."
                        ))

                      case Right(contentType) =>

                        val chunks = downloadSource.map(ChunkStreamPart.apply)

                        complete(HttpEntity.Chunked(contentType, chunks))
                    }

                  case None =>

                    complete(StatusCodes.NotFound)

                }
            }
        }
      }
    }
  }
}
