/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.styk.martin.bakalarka.common;

import java.io.File;
import java.util.List;

import sk.styk.martin.bakalarka.linkfinders.AndroidApksFreeLinkFinder;

/**
 *
 * @author Martin Styk
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Arguments arguments = ArgumentUtils.parseArguments(args);

        ApkLinkFinder finder = arguments.getLinkFinder();
        finder.setMetadataFile(arguments.getMetadataFile());
        finder.setNumberOfApks(arguments.getNumberOfApks());
        List<String> urls = finder.findLinks();

        ApkDownloader downloader = new ApkDownloader();
        downloader.setDownloadDirectory(arguments.getDownloadDirectory());
        downloader.setNumberOfThreads(arguments.getNumberOfThreads());
        downloader.setOverwriteExisting(arguments.isOverwriteExisting());
        downloader.download(urls);
    }
}
