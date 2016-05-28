/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.styk.martin.bakalarka.downloaders;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for downloading APK files
 *
 * @author Martin Styk
 */
public class ApkDownloader {

    private final Logger logger = LoggerFactory.getLogger(ApkDownloader.class);
    private File downloadDirectory;
    private boolean overwriteExisting;
    private int numberOfThreads = 10;


    /**
     * Downloads files according to input parameter.
     * This method uses Executor to submit @see DownloadTask
     *
     * @param urls urls to files to download.
     * @throws InterruptedException
     */
    public void download(List<String> urls) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(numberOfThreads);

        logger.info(urls.size() + " download tasks will be scheduled");

        for (String url : urls) {
            File downloadFile = getDownloadFile(url);
            if (downloadFile == null) {
                logger.warn("File already exists for url : " + url);
            } else {
                try {
                    pool.submit((Runnable)getDownloadThreadClass().newInstance(url,downloadFile));
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        pool.shutdown();
        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }

    /**
     * @param url of file to download
     * @return name of file stored
     */
    public File getDownloadFile(String url) {
        String REGEX = getNameRegex();
        Pattern p = Pattern.compile(REGEX);
        Matcher matcher = p.matcher(url);
        String name;

        if (matcher.find()) {
            name = matcher.group(1);
        } else {
            name = UUID.randomUUID().toString();
            getLogger().warn("URL " + url + " cannot be matched to extract APK name. Following name will be used : " + name);
        }

        if(!name.endsWith(".apk") || !name.endsWith(".APK")){
            name = name + ".apk";
        }

        File fileToReturn;

        if (getDownloadDirectory() == null) {
            fileToReturn = new File(name);
        } else {
            fileToReturn = new File(getDownloadDirectory(), name);
        }

        if ((fileToReturn.exists() && !getOverwriteExistingFiles())) {
            return null;
        } else {
            return fileToReturn;
        }
    }

    /**
     * @param downloadDirectory location of donwloaded files
     */
    public void setDownloadDirectory(File downloadDirectory) {
        if (downloadDirectory == null) {
            throw new IllegalArgumentException("Download directory should not be null");
        }
        if (!downloadDirectory.isDirectory()) {
            throw new IllegalArgumentException("Download directory is not a valid directory");
        }
        this.downloadDirectory = downloadDirectory;
    }

    public File getDownloadDirectory() {
        return downloadDirectory;
    }

    /**
     * @param overwriteExisting overwrite file if laready exists
     */
    public void setOverwriteExisting(boolean overwriteExisting) {
        this.overwriteExisting = overwriteExisting;
    }

    public boolean getOverwriteExistingFiles() {
        return overwriteExisting;
    }

    public Logger getLogger() {
        return logger;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    /**
     * @param numberOfThreads number of concurrent downloads
     */
    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    protected String getNameRegex(){
        return ".*/(.*.apk)";
    }

    protected Constructor getDownloadThreadClass(){
        Class classToLoad = DownloadTask.class;

        Class[] cArg = new Class[2]; //Our constructor has 2 arguments
        cArg[0] = String.class;
        cArg[1] = File.class;

        try {
           return classToLoad.getDeclaredConstructor(cArg);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }


}
