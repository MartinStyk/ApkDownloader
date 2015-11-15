package sk.styk.martin.bakalarka.linkfinders;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Martin Styk
 */
public class ApkManiaFullVKLinkFinder extends ApkManiaFullLinkFinderBase {

    protected String handleLink(String url) {
        if (url.contains("//vk.com/")) {
            return getDownloadUrl(url);
        }
        return null;
    }

    private String getDownloadUrl(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (Exception ex) {
            Logger.getLogger(ApkManiaFullVKLinkFinder.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        Element link = document.getElementById("iframe");
        return link.attr("src");
    }

}
