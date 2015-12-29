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

/**
 * Created by Martin Styk on 29.12.2015.
 */
public class AppsApkLinkFinder implements ApkLinkFinder {


    private final org.slf4j.Logger logger = LoggerFactory.getLogger(AppsApkLinkFinder.class);
    private int numberOfApks;
    private final String PAGE = "http://www.appsapk.com/android/all-apps/page/";
    private final int PAGE_MAX_NUM = 710;
    private Set<String> treasure = new HashSet<>();

    @Override
    public List<String> findLinks() {

        for (int i=1 ; i<= PAGE_MAX_NUM ; i++) {
            if (treasure.size() >= numberOfApks) {
                break;
            }
            processListPage(PAGE+ i +"/");
        }
        logger.info("number of links found on pages " + treasure.size());
        return new ArrayList<>(treasure);
    }

    private void processListPage(String url) {

        logger.info("processing page " + url);

        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException ex) {
            logger.error("ProcessListPage " + url + " Exception : " + ex.toString());
            return;
        }
        List<Element> figureElements = document.getElementsByAttributeValueContaining("class", "post-image");
        for (Element el : figureElements) {
            List<Element> a = el.getElementsByTag("a");
            processDetailPage(a.get(0).attr("href"));
            if (treasure.size() >= numberOfApks) {
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
            logger.error("processDetailPage " + url + " Exception : " + ex.toString());
            return;
        }
        List<Element> links = document.getElementsByAttributeValueEnding("href",".apk");
        for (Element link : links) {
                treasure.add(link.attr("href"));
                if (treasure.size() >= numberOfApks) {
                    return;
                }
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

