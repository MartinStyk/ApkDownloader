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
public class ApkManiaFullArchiveLinkFinder extends ApkManiaFullLinkFinderBase{
    protected String handleLink(String url) {
        if (url.contains("archive.org")) {
            return url;
        }
        return null;
    }
}
