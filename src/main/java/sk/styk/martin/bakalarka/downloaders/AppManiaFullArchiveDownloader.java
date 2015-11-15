package sk.styk.martin.bakalarka.downloaders;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Martin Styk on 13.11.2015.
 */
public class AppManiaFullArchiveDownloader extends ApkDownloader {

    @Override
    protected String getNameRegex(){
        return ".*/download/.*/(.*)";
    }

    @Override
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
}

