package sk.styk.martin.bakalarka.common;

import sk.styk.martin.bakalarka.downloaders.ApkDownloader;
import sk.styk.martin.bakalarka.linkfinders.ApkLinkFinder;

import java.util.List;

/**
 * @author Martin Styk
 */
public class Main {

    public static void main(String[] args) throws Exception {

        Arguments arguments = Arguments.parseArguments(args);

        ApkLinkFinder finder = arguments.getLinkFinder();
        finder.setMetadataFile(arguments.getMetadataFile());
        finder.setNumberOfApks(arguments.getNumberOfApks());
        List<String> urls = finder.findLinks();

        ApkDownloader downloader = arguments.getApkDownloader();
        downloader.setDownloadDirectory(arguments.getDownloadDirectory());
        downloader.setNumberOfThreads(arguments.getNumberOfThreads());
        downloader.setOverwriteExisting(arguments.isOverwriteExisting());
        downloader.download(urls);
    }
}
