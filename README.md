# azure-storage-cdn-mock

Simple service which mocks a CDN service against [Azure Storage]. This may be useful for development purposes where services download content from [Azure Storage] via a CDN (e.g. [Azure CDN](https://azure.microsoft.com/en-us/services/cdn/)).

The service provides a dynamic route matcher which accepts requests in the form of `http://azure-storage-cdn-mock:8080/{container-name}/{blob-path}`. These requests will be forwarded to the configured [Azure Storage] based upon the provided container-name and blob-path.

You may start the dockerized service via:

```shell
docker run -p "8080:8080" -it janjaali/azure-storage-cdn-mock:0.1.0-SNAPSHOT
```

To CDN mock service can be configured with the following environment variables:

* **AZURE_STORAGE_ENDPOINT**

* **AZURE_STORAGE_ACCOUNT_NAME**

* **AZURE_STORAGE_ACCOUNT_KEY**

## Development

### Run the main application

sbt "app/run"

### Create executables

This project uses [sbt-native-packager](https://sbt-native-packager.readthedocs.io/) to create executables.

#### Application

To create an executable script which launches the main application you can command sbt to create a staged application via:

```shell
sbt "app/stage"
```

This will bundle and create the executable script to launch the application in [.\app\target\universal\stage](.\app\target\universal\stage). The script is executable via

```powershell
.\app\target\universal\stage\bin\app.bat
```

#### Dockerized

To create a docker image that contains the main application, you can use the sbt command:

```shell
sbt "app/docker:publishLocal"
```

This will build and publish an image called "janjaali/azure-storage-cdn-mock:0.1.0". You can run the image via:

```shell
docker run -p "8080:8080" -it janjaali/azure-storage-cdn-mock:0.1.0
```

### Test

sbt test

[Azure Storage]: https://azure.microsoft.com/en-us/services/storage/
