/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NPOCR;

import java.io.File;

/**
 *
 * @author rupak
 */
public class Files {
 public static void ClearFiles(String Folder){
     File dir = new File(Folder);
    for(File file: dir.listFiles()) file.delete();
 }   
}
