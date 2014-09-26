/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NPOCR;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author rupak
 */
public class Recognizer {

    public static String StartProcess(String filePath, String fileName) throws IOException {

        String result = "";

        Page page = new Page();
        
        page.Read_Image(filePath, fileName);
        page.imageReadGray("Input", fileName);
        page.binarize_(4, 4, 225, -10);
        page.Write_Image(GlobalVars.op, fileName);
        //
        //page = new Page();
        page.Read_Image(GlobalVars.op, fileName);
        
        //clear the fiels
        Files.ClearFiles(GlobalVars.segment_line);        
        GlobalVars.lines = new ArrayList<>();
        page.SegmentLine();
        
        //loop over a lines
        //File Input_Folder = new File(GlobalVars.segment_line);
        //File[] segmentedLines = Input_Folder.listFiles();
        //File[] segmentedLines = dirListByAscendingDate(Input_Folder);
        //Arrays.sort(segmentedLines);
        //for (int i = 0; i < segmentedLines.length; i++) {
        for (int i = 0; i < GlobalVars.lines.size(); i++) {
            //Ligature
            Files.ClearFiles(GlobalVars.segment_lig);
            page = new Page();
            //page.Read_Image(GlobalVars.segment_line, segmentedLines[i].getName());
            String str = GlobalVars.lines.get(i);
            page.Read_Image(GlobalVars.segment_line, str);
            GlobalVars.ligs = new ArrayList<>();
            page.SegmentLigature();
            //loop for on the ligatures 
            //File Lig_Folder = new File(GlobalVars.segment_lig);
            //File[] ligFiles = Lig_Folder.listFiles();
            //Arrays.sort(ligFiles);

            //for (int j = 0; j < ligFiles.length; j++) {
            for (int j = 0; j < GlobalVars.ligs.size(); j++) {
                //CC's
                page = new Page();
                //page.Read_Image(GlobalVars.segment_lig, ligFiles[j].getName());
                page.Read_Image(GlobalVars.segment_lig, GlobalVars.ligs.get(j) );

                Files.ClearFiles(GlobalVars.cc); // claring files
                page.Segment_Page_Into_CCs();
                ArrayList<Integer> ids = page.GetCC_IDS(GlobalVars.cc);

                //for(Integer data: ids){
                //   System.out.println("Found: "+ data+"");
                //}
                //if (!result.equals(null)) {
                result += " " + NEPMapper.GetCharSet(ids);
                //} else {
                //    result += " <NA> ";
                //}
            }
            result += "\n";
        }
        // completed the loop
        System.out.println("Final Result" + result);
        //page.Segment_Page_Into_CCs();
        return result;
    }

    public static File[] dirListByAscendingDate(File folder) {
        if (!folder.isDirectory()) {
            return null;
        }
        File files[] = folder.listFiles();

        class Pair implements Comparable {

            public long t;
            public File f;

            public Pair(File file) {
                f = file;
                t = file.lastModified();
            }

            public int compareTo(Object o) {
                long u = ((Pair) o).t;
                return t < u ? -1 : t == u ? 0 : 1;
            }
        };

// Obtain the array of (file, timestamp) pairs.
//File[] files = directory.listFiles();
        Pair[] pairs = new Pair[files.length];
        for (int i = 0; i < files.length; i++) {
            pairs[i] = new Pair(files[i]);
        }

// Sort them by timestamp.
        Arrays.sort(pairs);

// Take the sorted pairs and extract only the file part, discarding the timestamp.
        for (int i = 0; i < files.length; i++) {
            files[i] = pairs[i].f;
        }
        return files;
    }
}
