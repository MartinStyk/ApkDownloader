/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.styk.martin.bakalarka.common;

import java.io.File;
import java.util.List;

import sk.styk.martin.bakalarka.downloaders.ApkDownloader;
import sk.styk.martin.bakalarka.downloaders.CrackApkDownloader;
import sk.styk.martin.bakalarka.linkfinders.ApkLinkFinder;
import sk.styk.martin.bakalarka.linkfinders.CrackApkLinkFinder;

/**
 *
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
