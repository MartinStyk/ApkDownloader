package sk.styk.martin.bakalarka.common;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.styk.martin.bakalarka.downloaders.ApkDownloader;
import sk.styk.martin.bakalarka.downloaders.CrackApkDownloader;
import sk.styk.martin.bakalarka.linkfinders.ApkLinkFinder;
import sk.styk.martin.bakalarka.linkfinders.CrackApkLinkFinder;
import sk.styk.martin.bakalarka.linkfinders.PlaydroneLinkFinder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

/**
 * Created by Martin Styk on 13.11.2015.
 */
public class Arguments {

    private static final Logger logger = LoggerFactory.getLogger(Arguments.class);

    private static final Class DEFAULT_LINK_FINDER_CLASS = PlaydroneLinkFinder.class;
    private static final Class DEFAULT_DOWNLOADER_CLASS = ApkDownloader.class;
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
        if (availableLocations().contains(linkFinderClassString)) {
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

    public ApkDownloader getApkDownloader() {
        String linkFinderClazzName = linkFinderClass == null ? DEFAULT_LINK_FINDER_CLASS.getCanonicalName() : linkFinderClass.getCanonicalName();
        Class clazz = DEFAULT_DOWNLOADER_CLASS;
        if (linkFinderClazzName.equals(CrackApkLinkFinder.class.getCanonicalName())) {
            clazz = CrackApkDownloader.class;
        }
        try{
            return ApkDownloader.class.cast(clazz.newInstance());
        } catch (InstantiationException e) {
            logger.error(e.toString());
            exit(1);
        } catch (IllegalAccessException e) {
            logger.error(e.toString());
            exit(1);
        }
        return null;
    }


    public Arguments() {
    }

    public static Arguments parseArguments(String[] args) throws ParseException {
        // create the command line parser
        CommandLineParser parser = new DefaultParser();

        Options options = new Options();

        // create the Options for link finder
        options.addOption(Option
                .builder("l")
                .longOpt("location")
                .desc("Specifies location from which apks will be downloaded. Supported values : Playdrone, AndroidApksFree, CrackApk")
                .hasArg()
                .numberOfArgs(1)
                .build()
        );

        options.addOption(Option
                .builder("n")
                .longOpt("number-of-apks")
                .desc("Maximum number of apk files searched.")
                .hasArg()
                .numberOfArgs(1)
                .build()
        );
        options.addOption(Option
                .builder("m")
                .longOpt("metadata-file")
                .desc("Path to metadata file. Only supported for PlaydroneApk link finder")
                .argName("PATH")
                .hasArg()
                .numberOfArgs(1)
                .build()
        );

        //create the Options for downloader
        options.addOption("o", "overwrite-existing", false, "Overwrite already downloaded files");
        options.addOption(Option
                .builder("d")
                .longOpt("download-directory")
                .desc("Path where apks will be downloaded. Directory must exist!")
                .argName("PATH")
                .required()
                .hasArg()
                .numberOfArgs(1)
                .build()
        );
        options.addOption(Option
                .builder("t")
                .longOpt("number-of-threads")
                .desc("Number of concurrent downloads")
                .hasArg()
                .numberOfArgs(1)
                .build()
        );

        CommandLine line = null;

        try {
            // parse the command line arguments
            line = parser.parse(options, args);
        } catch (ParseException exp) {
            logger.error("Unexpected exception during parsing of cli attributes:" + exp.getMessage());
            throw exp;
        }
        Arguments arguments = new Arguments();

        if (line.hasOption("l")) {
            String val = line.getOptionValue("l");
            arguments.setLinkFinderClass(val);
        }


        if (line.hasOption("n")) {
            String val = line.getOptionValue("n");
            Integer intVal = Integer.parseInt(val);
            arguments.setNumberOfApks(intVal);
        }

        if (line.hasOption("m")) {
            String val = line.getOptionValue("m");
            arguments.setMetadataFile(new File(val));
        }
        if (line.hasOption("d")) {
            String val = line.getOptionValue("d");
            arguments.setDownloadDirectory(new File(val));
        }
        if (line.hasOption("o")) {
            arguments.setOverwriteExisting(true);
        }
        if (line.hasOption("t")) {
            String val = line.getOptionValue("t");
            Integer intVal = Integer.parseInt(val);
            arguments.setNumberOfThreads(intVal);
        }
        return arguments;
    }

    private List<String> availableLocations() {
        List<String> loc = new ArrayList<String>();
        loc.add("AndroidApksFree");
        loc.add("Playdrone");
        loc.add("CrackApk");
        return loc;
    }


}
