package sk.styk.martin.bakalarka.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.styk.martin.bakalarka.linkfinders.PlaydroneLinkFinder;

import java.io.File;

import static java.lang.System.exit;

/**
 * Created by Martin Styk on 13.11.2015.
 */
public class Arguments {

    private static final Logger logger = LoggerFactory.getLogger(Arguments.class);

    private static final Class DEFAULT_LINK_FINDER_CLASS = PlaydroneLinkFinder.class;
    private static final int DEFAULT_NUMBER_OF_APKS = 1500;
    private static final File DEFAULT_DOWNLOAD_DIRECTORY = new File("D:\\APK_test");
    private static final boolean DEFAULT_OVERWRITE_EXISTING = false;
    private static final int DEFAULT_NUMBER_OF_THREADS = 10;

    private Class linkFinderClass;
    private Integer numberOfApks;
    private File metadataFile;
    private Boolean overwriteExisting;
    private File downloadDirectory;
    private Integer numberOfThreads;

    public int getNumberOfApks() {
        return numberOfApks == null ? DEFAULT_NUMBER_OF_APKS : numberOfApks;
    }

    public void setNumberOfApks(int numberOfApks) {
        this.numberOfApks = numberOfApks;
    }

    public File getMetadataFile() {
        return metadataFile;
    }

    public void setMetadataFile(File metadataFile) {
        this.metadataFile = metadataFile;
    }

    public boolean isOverwriteExisting() {
        return overwriteExisting == null ? DEFAULT_OVERWRITE_EXISTING : overwriteExisting;
    }

    public void setOverwriteExisting(boolean overwriteExisting) {
        this.overwriteExisting = overwriteExisting;
    }

    public File getDownloadDirectory() {
        return downloadDirectory == null ? DEFAULT_DOWNLOAD_DIRECTORY : downloadDirectory;
    }

    public void setDownloadDirectory(File downloadDirectory) {
        this.downloadDirectory = downloadDirectory;
    }

    public int getNumberOfThreads() {
        return numberOfThreads == null ? DEFAULT_NUMBER_OF_THREADS : numberOfThreads;
    }

    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public ApkLinkFinder getLinkFinder() {
        Class clazz = linkFinderClass == null ? DEFAULT_LINK_FINDER_CLASS : linkFinderClass;
        try {
            return ApkLinkFinder.class.cast(clazz.newInstance());
        } catch (InstantiationException e) {
            logger.error(e.toString());
            exit(1);
        } catch (IllegalAccessException e) {
            logger.error(e.toString());
            exit(1);
        }
        return null;
    }

    public void setLinkFinderClass(String linkFinderClassString) {
        if ("AndroidApksFree".equals(linkFinderClassString) || "Playdrone".equals(linkFinderClassString)) {
            String fullClass = "sk.styk.martin.bakalarka.linkfinders." + linkFinderClassString + "LinkFinder";
            try {
                this.linkFinderClass = Class.forName(fullClass);
            } catch (ClassNotFoundException e) {
                logger.error("Class not found " + e.toString());
                exit(1);
            }
        } else {
            logger.warn(linkFinderClassString + "not found, using default : " + DEFAULT_LINK_FINDER_CLASS);
        }

    }


}
