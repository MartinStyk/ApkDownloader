/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.styk.martin.bakalarka.downloaders;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Martin Styk
 */
public class WebClientDownloadTask implements Runnable {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(WebClientDownloadTask.class);

    private final String urlString;
    private final File file;

    public WebClientDownloadTask(String url, File file) {
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
        try (final WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.getPage(url);
            try {
                InputStream is = page.getWebResponse().getContentAsStream();
                try {
                    OutputStream os = new FileOutputStream(file);
                    byte[] bytes = new byte[10240]; // make it bigger if you want. Some recommend 8x, others 100x
                    int read;
                    while ((read = is.read(bytes)) != -1) {
                        os.write(bytes, 0, read);
                    }
                    os.close();
                    is.close();
                } catch (IOException ex) {
                    // Exception handling
                }
            } catch (IOException ex) {
                // Exception handling
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("Finished download from " + urlString);

    }
}
