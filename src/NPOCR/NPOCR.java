/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NPOCR;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Administrator
 */
public class NPOCR {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        // Generate the Feature Set
//Page page = new Page();                 
//page.Compute_Features( GlobalVars.mb );
        
        //Recognizer.StartProcess(GlobalVars.RecImagePath , "01.bmp" );
//        File Binary_Folder = new File(GlobalVars.op);
//        File[] binaries = Binary_Folder.listFiles();  
//        for (int i = 0; i < binaries.length; i++) { 
//            Page P1 = new Page();
//            //P1.imageReadGray("Input", binaries[i].getName());
//            P1.Read_Image(GlobalVars.op, binaries[i].getName()); 
//            P1.SegmentLine(); 
//        }
     
//        File Lines_Folder = new File(GlobalVars.segment_line);
//        File[] lines= Lines_Folder.listFiles();
//        for(int i=0; i<lines.length;i++){
//            Page P1 = new Page();
//            P1.Read_Image(GlobalVars.segment_line, lines[i].getName());
//            P1.SegmentLigature();
//        }
        

//
//        
        
 /*
        //stopped
        File Input_Folder = new File(GlobalVars.ip);
        File[] Images = Input_Folder.listFiles();
        for (int i = 0; i < Images.length; i++) {

//                Page p= new Page();
//                p.Read_Image("Input", Images[i].getName());
//                p.Segment_Page_Into_CCs();
            Page P1 = new Page();
            P1.imageReadGray("Input", Images[i].getName());
            P1.Read_Image(GlobalVars.ip, Images[i].getName());
            //P1.Write_Image(GlobalVars.op, Images[i].getName());
            P1.binarize_(4, 4, 225, -10); //P1.binarize_(4, 4,128, -20);
            P1.Write_Image(GlobalVars.op, Images[i].getName());
            // P1.Segment_Page_Into_CCs();
            //P1.SegmentLine();
        } 
         */
        
        // Generate the Feature Set
        //Page page = new Page();                 
        //page.Compute_Features( GlobalVars.mb );
        
        //Verify Decision Tree
         Page page = new Page();
         page.Verify_Features(GlobalVars.mbTest);
            
    }
}