/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.styk.martin.bakalarka.linkfinders;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.LoggerFactory;

/**
 * @author Martin Styk
 */
public class CrackApkLinkFinder implements ApkLinkFinder {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(CrackApkLinkFinder.class);
    private int numberOfApks;
    private int currentNumberOfApks;
    private static final List<String> PAGES_TO_PROCESS = new ArrayList<>();

    static {
        PAGES_TO_PROCESS.add("http://www.crackapk.com/top-crack");
    }

    private List<String> treasure = new LinkedList<>();
    @Override
    public List<String> findLinks() {

        for (String page : PAGES_TO_PROCESS) {
            if (currentNumberOfApks >= numberOfApks) {
                break;
            }
            processListPage(page);
        }
        logger.info("number of links found on pages " + treasure.size());
        return treasure;
    }

    private void processListPage(String url) {

        logger.info("processing page " + url);

        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException ex) {
            logger.error("ProcessListPage " + url + " Exception : " +ex.toString());
            return;
        }
        List<Element> pagesLinks = document.getElementsByAttributeValue("class", "l");
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
            logger.error("processDetailPage " + url + " Exception : " +ex.toString());
            return;
        }
        List<Element> links = document.getElementsByAttributeValue("class", "btn_det_download");
        for (Element link: links) {
                treasure.add(link.attr("href"));
                currentNumberOfApks++;
        }
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


}
