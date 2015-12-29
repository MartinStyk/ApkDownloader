package sk.styk.martin.bakalarka.downloaders;

import java.io.File;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Martin Styk on 29.12.2015.
 */
public class AppsApkDownloader extends ApkDownloader {

    @Override
    protected String getNameRegex() {
        return "http://.*/(.*apk)";
    }

}

