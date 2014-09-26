/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NPOCR;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class File_Handler {
     public static void Write_Features_in_File(String Message)
    {
    try
    {
      BufferedWriter Log_writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("MBs_Features.txt",true),"UTF8"));
      Log_writer.write(Message);
      Log_writer.newLine();
      Log_writer.close();
    }
    catch (Exception e)
    {
        System.out.println("File cannot be created");
    }
}
       
     public static void WriteFile(String Message, String File_Name) throws FileNotFoundException, IOException
    {
     BufferedWriter Log_writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(File_Name+".txt",true),"UTF8"));
      Log_writer.write(Message);
      Log_writer.newLine();
      Log_writer.close();
    }
    public String ReadFile(String File_Complete_Name) throws FileNotFoundException, IOException
    {        StringBuffer Complete_File= new StringBuffer();
        try {
               BufferedReader Buffered_reader = null;
               Buffered_reader = new BufferedReader(new InputStreamReader(new FileInputStream(File_Complete_Name),"UTF-8"));
               String inLine = null;
               
               inLine = Buffered_reader.readLine();
               while ((inLine = Buffered_reader.readLine()) != null )
               {                              
                    System.out.println(inLine);
                    Complete_File.append(inLine);
                    
               }
               Buffered_reader.close();
              

} catch (UnsupportedEncodingException ex) { 

        }
        
    return Complete_File.toString();
    }
   
}
