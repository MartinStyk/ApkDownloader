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

    public List<String> findLinks();

    public void setMetadataFile(File metadataFile);

    public void setNumberOfApks(int numberOfApks);
}
