/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.styk.martin.bakalarka.linkfinders;

import java.io.File;
import java.util.List;

/**
 *
 * @author Martin Styk
 */
public interface ApkLinkFinder {

    /**
     * Finds links to APK files
     * @return List of URLs to APK files
     */
    List<String> findLinks();

    /**
     * Sets metadata file. Only used with Playdron
     * @param metadataFile metadata file containing links to APKs
     */
    void setMetadataFile(File metadataFile);

    /**
     * @param numberOfApks number of APKs we want to download
     */
    void setNumberOfApks(int numberOfApks);
}
