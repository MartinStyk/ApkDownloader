package sk.styk.martin.bakalarka.linkfinders;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.LoggerFactory;

/**
 * @author Martin Styk
 */
public class PaidFullProLinkFinder implements ApkLinkFinder {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(PaidFullProLinkFinder.class);
    private int numberOfApks;
    private int currentNumberOfApks;
    private static final List<String> PAGES_TO_PROCESS = new ArrayList<>();

    static {
//        PAGES_TO_PROCESS.add("http://www.paidfullpro.in/search?updated-max=2015-11-12T23%3A15%3A00-07%3A00&max-results=50");
//        PAGES_TO_PROCESS.add("http://www.paidfullpro.in/search?updated-max=2015-04-01T23%3A15%3A00-07%3A00&max-results=50");
//        PAGES_TO_PROCESS.add("http://www.paidfullpro.in/search?updated-max=2014-12-20T23%3A15%3A00-07%3A00&max-results=50");
//        PAGES_TO_PROCESS.add("http://www.paidfullpro.in/search?updated-max=2014-11-20T23%3A15%3A00-07%3A00&max-results=50");
//        PAGES_TO_PROCESS.add("http://www.paidfullpro.in/search?updated-max=2014-8-25T23%3A15%3A00-07%3A00&max-results=50");
//        PAGES_TO_PROCESS.add("http://www.paidfullpro.in/search?updated-max=2014-8-10T23%3A15%3A00-07%3A00&max-results=50");
//        PAGES_TO_PROCESS.add("http://www.paidfullpro.in/search?updated-max=2014-01-10T23%3A15%3A00-07%3A00&max-results=50");
//        PAGES_TO_PROCESS.add("http://www.paidfullpro.in/search?updated-max=2013-12-10T23%3A15%3A00-07%3A00&max-results=50");

        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2015-12-07T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2015-10-07T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2015-9-07T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2015-8-07T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2015-7-07T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2015-6-07T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2015-6-25T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2015-5-07T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2015-4-07T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2015-3-07T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2015-2-07T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2015-1-07T06%3A22%3A00-07%3A00&max-results=25");

        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2015-12-22T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2015-10-22T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2015-9-22T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2015-8-22T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2015-7-22T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2015-6-22T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2015-5-22T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2015-4-22T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2015-3-22T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2015-2-22T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2015-1-22T06%3A22%3A00-07%3A00&max-results=25");

        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2014-12-07T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2014-10-07T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2014-9-07T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2014-8-07T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2014-7-07T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2014-6-07T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2014-6-25T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2014-5-07T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2014-4-07T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2014-3-07T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2014-2-07T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2014-1-07T06%3A22%3A00-07%3A00&max-results=25");

        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2014-12-22T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2014-10-22T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2014-9-22T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2014-8-22T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2014-7-22T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2014-6-22T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2014-5-22T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2014-4-22T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2014-3-22T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2014-2-22T06%3A22%3A00-07%3A00&max-results=25");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2014-1-22T06%3A22%3A00-07%3A00&max-results=25");

        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2013-1-22T06%3A22%3A00-07%3A00&max-results=600");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2013-2-22T06%3A22%3A00-07%3A00&max-results=600");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2013-3-22T06%3A22%3A00-07%3A00&max-results=600");

        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2013-4-22T06%3A22%3A00-07%3A00&max-results=600");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2013-5-22T06%3A22%3A00-07%3A00&max-results=600");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2013-6-22T06%3A22%3A00-07%3A00&max-results=600");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2013-7-22T06%3A22%3A00-07%3A00&max-results=600");

        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2013-8-22T06%3A22%3A00-07%3A00&max-results=600");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2013-9-22T06%3A22%3A00-07%3A00&max-results=600");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2013-10-22T06%3A22%3A00-07%3A00&max-results=600");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2013-11-22T06%3A22%3A00-07%3A00&max-results=600");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2013-12-22T06%3A22%3A00-07%3A00&max-results=600");


        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2013-1-07T06%3A22%3A00-07%3A00&max-results=600");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2013-2-07T06%3A22%3A00-07%3A00&max-results=600");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2013-3-07T06%3A22%3A00-07%3A00&max-results=600");

        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2013-4-07T06%3A22%3A00-07%3A00&max-results=600");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2013-5-07T06%3A22%3A00-07%3A00&max-results=600");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2013-6-07T06%3A22%3A00-07%3A00&max-results=600");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2013-7-07T06%3A22%3A00-07%3A00&max-results=600");

        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2013-8-07T06%3A22%3A00-07%3A00&max-results=600");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2013-9-07T06%3A22%3A00-07%3A00&max-results=600");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2013-10-07T06%3A22%3A00-07%3A00&max-results=600");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2013-11-07T06%3A22%3A00-07%3A00&max-results=600");
        PAGES_TO_PROCESS.add("http://www.apkfullappss.com/search?updated-max=2013-12-07T06%3A22%3A00-07%3A00&max-results=600");


    }

    private Set<String> treasure = new HashSet<>();

    @Override
    public List<String> findLinks() {

        for (String page : PAGES_TO_PROCESS) {
            if (currentNumberOfApks >= numberOfApks) {
                break;
            }
            processListPage(page);
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
        List<Element> pagesLinks = document.getElementsByAttributeValueContaining("title", "Read more");
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
            logger.error("processDetailPage " + url + " Exception : " + ex.toString());
            return;
        }
        List<Element> links = document.getElementsByAttributeValue("rel", "nofollow");
        for (Element link : links) {
            if (link.attr("href").contains("zippyshare")) {
                processZippyPage(link.attr("href"));
                if (currentNumberOfApks >= numberOfApks) {
                    return;
                }
            }
        }
    }

    private void processZippyPage(String url) {
        logger.info("processing zippy page " + url);
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException ex) {
            logger.error("processZippylPage " + url + " Exception : " + ex.toString());
            return;
        }
        if (document.getElementsByAttributeValue("id", "dlbutton").size() > 0) {
            treasure.add(url);
            currentNumberOfApks++;
            if (currentNumberOfApks >= numberOfApks) {
                return;
            }
        }
    }

//    private void processZippyPage(String url) {
//        String regex = "document.getElementById..dlbutton...href = \"(/.*)\"\\s.\\s.(\\d*)\\s.\\s(\\d*)\\s.\\s(\\d*)\\s.\\s(\\d*).\\s.\\s\"(/.*)\";";
//        Pattern p = Pattern.compile(regex);
//
//
//        logger.info("processing zippy page " + url);
//        Document document = null;
//        try {
//            document = Jsoup.connect(url).get();
//        } catch (IOException ex) {
//            logger.error("processZippylPage " + url + " Exception : " + ex.toString());
//            return;
//        }
//        String docString = document.toString();
//
//        Matcher matcher = p.matcher(docString);
//
//        if (!matcher.find()) {
//            return;
//        }
//        String part1 = matcher.group(1);
//        String part2 = matcher.group(6);
//        Integer int1 = Integer.parseInt(matcher.group(2));
//        Integer int2 = Integer.parseInt(matcher.group(3));
//        Integer int3 = Integer.parseInt(matcher.group(4));
//        Integer int4 = Integer.parseInt(matcher.group(5));
//        int intResult = int1 % int2 + int3 % int4;
//
//        String regex1 = "(http://.*com).*";
//        Pattern p1 = Pattern.compile(regex1);
//
//        Matcher matcher1 = p1.matcher(url);
//
//        if (!matcher1.find()) {
//            return;
//        }
//
//
//        String linkResult = matcher1.group(1) + part1 + intResult + part2;
//
//        treasure.add(linkResult);
//        currentNumberOfApks++;
//    }


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
