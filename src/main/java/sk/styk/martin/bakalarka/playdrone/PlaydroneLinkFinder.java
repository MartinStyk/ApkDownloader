package sk.styk.martin.bakalarka.playdrone;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.styk.martin.bakalarka.common.ApkDownloader;
import sk.styk.martin.bakalarka.common.ApkLinkFinder;
import sk.styk.martin.bakalarka.common.DownloadTask;

/**
 *
 * @author Martin Styk
 */
public class PlaydroneLinkFinder implements ApkLinkFinder {

    private static final Logger logger = LoggerFactory.getLogger(PlaydroneLinkFinder.class);
    private final String DEFAULT_METADATA_URL = "https://archive.org/download/playdrone-snapshots/2014-10-31.json";
    private File metadataFile;
    private int numberOfApks=5000;

    @Override
    public List<String> findLinks() {
        prepareMetadata();
        return parseJSON(metadataFile, numberOfApks);
    }

    @Override
    public void setMetadataFile(File metadataFile) {
        if (metadataFile == null || !metadataFile.isFile()) {
            throw new IllegalArgumentException("metadata file not valid");
        }
        this.metadataFile = metadataFile;
    }

    @Override
    public void setNumberOfApks(int numberOfApks) {
        this.numberOfApks = numberOfApks;
    }

    private void prepareMetadata() {
        if (metadataFile != null) {
            return;
        }
        logger.info("metadataFile not found, starting download(707MB)");

        ApkDownloader downloader = new ApkDownloader() ;
        metadataFile = downloader.getDownloadFile(DEFAULT_METADATA_URL);

        logger.info("new metadata file :" + metadataFile.getAbsolutePath());

        Thread t = new Thread(new DownloadTask(DEFAULT_METADATA_URL, metadataFile));

        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            logger.warn("Interrupted exception while downloading url file");
        }

    }

    private List<String> parseJSON(File source, int numberOfApks) {

        List<String> urls = new ArrayList<String>();
        try {

            JsonFactory jsonfactory = new JsonFactory();
            JsonParser parser = jsonfactory.createJsonParser(source); // starting parsing of JSON

            int counter = 0;
            while (parser.nextToken() != JsonToken.END_ARRAY && counter < numberOfApks) {

                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    String token = parser.getCurrentName();

                    if ("apk_url".equals(token)) {
                        parser.nextToken();
                        String url = parser.getText();
                        urls.add(url);
                        counter++;
                    }
                }
            }
            parser.close();
        } catch (JsonGenerationException jge) {
            logger.error("JsonGenerationException", jge);
        } catch (JsonMappingException jme) {
            logger.error("JsonMappingException", jme);
        } catch (IOException ioex) {
            logger.error("IOException", ioex);
        }
        return urls;
    }
}
