/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NPOCR;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author rupak
 */
public class NEPMapper {
    public static String GetCharSet(ArrayList<Integer> ids) throws IOException{        
        //Map<String, String> map = new HashMap<String, String>();
        BufferedReader in = new BufferedReader(new FileReader( GlobalVars.HashFile ));
        String line=null;
        boolean isfound = false;        
        String found_data = "";
        while ((line = in.readLine()) != null) {
            String id_char[] = line.split(" => ");            
            // 1st element is combination of chars
            // 2nd element is UTF-8            
            String parts[] = id_char[0].split(" ");
            for(String part: parts) {
                for(int id: ids) {
                  if( String.valueOf(id).equals(part) ) {
                      isfound= true;
                      continue;
                  }
                  else { 
                      isfound=false;  
                      break; 
                  }
                }
                if(isfound){
                    found_data = id_char[1];
                    break;
                }
            } 
            if(isfound){ 
                    break;
            }
        }
        // close the file
        in.close();
        //System.out.println("Found char: "+ found_data);
        return found_data;
    }
}
