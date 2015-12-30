/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.styk.martin.bakalarka.downloaders;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Martin Styk
 */
public class DownloadTask implements Runnable {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(DownloadTask.class);

    private final String urlString;
    private final File file;

    public DownloadTask(String url, File file) {
        urlString = url;
        this.file = file;
    }

    @Override
    public void run() {

        logger.info("Starting download from " + urlString);

        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            logger.error("Malformed URL : " + urlString);
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            logger.error("File can not be created " + urlString);
        }
        try {
            FileUtils.copyURLToFile(url, file);
        } catch (IOException e) {
            file.delete();
            logger.error("File can not be downloaded " + urlString + "\n" + e.toString());
        }

        logger.info("Finished download from " + urlString);

    }
}
