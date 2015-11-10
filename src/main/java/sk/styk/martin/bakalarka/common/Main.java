/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.styk.martin.bakalarka.common;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import sk.styk.martin.bakalarka.playdrone.PlaydroneLinkFinder;

/**
 *
 * @author Martin Styk
 */
public class Main {

    public static void main(String[] args) throws Exception {
        ApkLinkFinder finder = new PlaydroneLinkFinder();
        finder.setNumberOfApks(3);
        finder.setMetadataFile(new File("D:\\Projects\\PlaydroneApkDownloader\\2014-10-31.json"));
        List<String> urls = finder.findLinks();

        ApkDownloader downloader = new ApkDownloader();
        downloader.setDownloadDirectory(new File("D:\\APK_test"));
        downloader.download(urls);
    }
}
