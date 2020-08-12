# Disk Uploader – File Uploading Tool for Dropbox and Yandex.Disk

Disk Uploader is a Java Tool. It is intended for working with different file storages. For now it can work with Yandex.Disk and Dropbox.

## Features

You can use Disk Uploader to upload files in your storage. Also, Disk Uploader is suitable for uploading database archives. Furthermore, you can set some parameters to specify lower and upper bounds for files that can be stored in your catalog.

Also, you can set a .xml file and receive emails every time when Disk Uploader sends files to your storage successfully or catches an error at this operation.

You can run Disk Uploader from command line, for example:

java -jar DiskUploader-1.0-jar-with-dependencies.jar --td=TypeOfTheDisk --fu=path\\to\\file\\file_name.txt --cu=/NameOfCatalogOnDisk/ --at=YourAuthenticationToken --tr=TopRest --mr=MinRest,

where TypeOfTheDisk is YA or DB, MinRest is minimum number of files that can be store in catalog, TopRest is maximum number of files that can be stored in catalog. Use only latin letters in --fu, --cu parameters.

Also, if you have spaces in your name of the file to upload, or in name of the catalog in storage, wrap it in apostrophes.

In addition, Disk Uploader is an utility that has these several features:
*	upload file to catalog in storage;
*	delete files when free space in storage is not enough;
*	delete files to fit in specified parameters on catalog;
*	send an email every time application works.

## Requirements

Disk Uploader requires JDK 1.6 or higher.

## Licensing

Disk Uploader is issued on under the GNU Lesser General Public License.

## Guide for getting autentication token

### Yandex.Disk

Firstly, go to [page](https://oauth.yandex.ru/) and click on Register a new application.

Then, enter the name of new application. Tick on Web service and add two callback URIs - [https://oauth.yandex.ru/verification_code](https://oauth.yandex.ru/verification_code). Also, tick all fields refers to Rest API.

After creating an application, go to URL https://oauth.yandex.ru/authorize?response_type=token&client_id=<application id>, where application ID you can find in application description.

After clicking Accept, you will be redirected to the page with URL like this: 

`https://oauth.yandex.ru/verification_code#access_token=<new OAuth-token>&expires_in=<lifetime in seconds>`,

where _new OAuth-token_ - generated token.

### Dropbox

First of all, go to the [page](https://www.dropbox.com/developers/apps).

Then, click on Create app. After that, tick on Dropbox API, Full Dropbox and name your app. Then, click on Generate access token.

