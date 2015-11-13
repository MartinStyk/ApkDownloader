package sk.styk.martin.bakalarka.downloaders;

import java.io.File;
import java.lang.reflect.Constructor;

/**
 * Created by Martin Styk on 13.11.2015.
 */
public class CrackApkDownloader extends ApkDownloader {
    @Override
    protected Constructor getDownloadThreadClass(){
        Class classToLoad = WebClientDownloadTask.class;

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

    @Override
    protected String getNameRegex(){
        return ".*?pkg=(.*)";
    }
}

