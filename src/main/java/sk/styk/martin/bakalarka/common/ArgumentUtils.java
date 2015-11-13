package sk.styk.martin.bakalarka.common;

import org.apache.commons.cli.*;
import org.apache.log4j.pattern.IntegerPatternConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static java.lang.System.exit;

/**
 * Created by Martin Styk on 13.11.2015.
 */
public class ArgumentUtils {

    private static final Logger logger = LoggerFactory.getLogger(ArgumentUtils.class);

    public static Arguments parseArguments(String[] args) throws ParseException {
        // create the command line parser
        CommandLineParser parser = new DefaultParser();

        Options options = new Options();

        // create the Options for link finder
        options.addOption(Option
                .builder("l")
                .longOpt("location")
                .desc("Specifies location from which apks will be downloaded. Supported values : Playdrone, AndroidApksFree")
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
        } catch (ParseException exp)
        {
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
}
