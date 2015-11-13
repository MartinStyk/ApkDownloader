/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.styk.martin.bakalarka.linkfinders;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.LoggerFactory;

/**
 * @author Martin Styk
 */
public class AndroidApksFreeLinkFinder implements ApkLinkFinder {

    private static final String APPS_PAGE = "http://www.androidapksfree.com/page/";
    private static final int NUMBER_OF_PAGES = 117;
    private static final String DOWNLOAD_LINK_TEXT = "Download APK from secure server >>";
    private static final String DOWNLOAD_LINK_TEXT_1 = "Download APK from secure source >>";
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(AndroidApksFreeLinkFinder.class);
    private int numberOfApks;
    private int currentNumberOfApks;

    private Set<String> treasure = new HashSet<>();

    @Override
    public List<String> findLinks() {
        for (int i = 1; i < NUMBER_OF_PAGES; i++) {
            if (currentNumberOfApks >= numberOfApks) {
                break;
            }
            logger.info("processing list page " + i);
            processListPage(APPS_PAGE + i + "/");
        }
        logger.info("number of links found on pages " + treasure.size());
        return new ArrayList<>(treasure);
    }

    @Override
    public void setMetadataFile(File metadataFile) {
        if (metadataFile != null)
            throw new UnsupportedOperationException("not supported for this link finder");
    }

    @Override
    public void setNumberOfApks(int numberOfApks) {
        this.numberOfApks = numberOfApks;
    }

    private void processListPage(String url) {

        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException ex) {
            Logger.getLogger(AndroidApksFreeLinkFinder.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        List<Element> pagesLinks = document.getElementsByAttributeValue("itemprop", "name url");
        for (Element el : pagesLinks) {
            processDetailPage(el.attr("href"));
            if (currentNumberOfApks >= numberOfApks) {
                return;
            }
        }
    }

    private void processDetailPage(String url) {
        logger.info("processing detail page " + url);
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException ex) {
            Logger.getLogger(AndroidApksFreeLinkFinder.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        List<Element> links = document.select("a");
        for (Element link : links) {
            if (link.text().equalsIgnoreCase(DOWNLOAD_LINK_TEXT) || link.text().equalsIgnoreCase(DOWNLOAD_LINK_TEXT_1)) {
                treasure.add(link.attr("href"));
                currentNumberOfApks++;
            }
        }
    }
}
