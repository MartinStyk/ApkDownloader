# ApkDownloader
Utility for downloading .apk files. Originally created as a part of BAchelor`s thesis at Faculty of Informatics, Masaryk University. Downloaded files were used with [ApkAnalyzer project](https://github.com/MartinStyk/ApkAnalyzer).

###Currently supported web pages for downloading apks


* [Playdrone Apks](https://archive.org/details/playdrone-apks) (Apks downloaded from play store by project PlayDrone)
* [AndroidApksFree.com](http://www.androidapksfree.com)
* [CrackApk.com](http://www.crackapk.com/)
* [ApkManiaFull.com](http://www.crackapk.com/) (ApkManiaFullVK, ApkManiaFullArchive)
* [AppsApk.com](http://www.appsapk.com/) 

###Usage
Download file jar/ApkDownloader-1.0-SNAPSHOT-jar-with-dependencies.jar

From the directory with this jar file execute ApkDownloader with following command : java -jar ApkDownloader-1.0-SNAPSHOT-jar-with-dependencies.jar <args>

######Arguments 

Short  | Long | Required | Default |Info
------------- | ------------- | -------------  | ------------- | -------------
-l            | --location    | no | Playdrone| Specifies location from which apks will be downloaded. Supported values : Playdrone, AndroidApksFree, CrackApk.
-n            | --number-of-apks  |  no | 1500 |Maximum number of apk files searched.
-m            | --metadata-file  |  no | - |Path to metadata file. Only supported for PlaydroneApk link finder.
-o            | --overwrite-existing  |  no | false |Overwrite already downloaded files. (set only -o or -override-existing)
-d            | --download-directory  | yes | - |Path where apks will be downloaded. Directory must exist!
-t            | --number-of-threads  | no | 10 |Number of concurrent downloads.  

Example of command: <br/>java -jar ApkDownloader-1.0-SNAPSHOT-jar-with-dependencies.jar --location=CrackApk -n=100 --overwrite-existing --download-directory=D:\APK



