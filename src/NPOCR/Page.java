/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NPOCR;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.print.attribute.standard.MediaTray;

/**
 *
 * @author Rupak
 */
public class Page {

    int Height;
    int Width;
    String Image_Name;
    int[][] Image_Matrix;
    ArrayList<Line> Lines;
    int[] vHistogram;
    int[] hHistogram;

    public Page() {
        Height = -1;
        Width = -1;
        Image_Name = null;
        hHistogram = null;
        Lines = new ArrayList<Line>();
    }
    
    
    public ArrayList<Integer> GetCC_IDS(String Types_Path) {
        
        ArrayList<Integer> ids = new ArrayList<Integer>();
        
        ArrayList<Integer> max_dimension = new ArrayList();
        ArrayList<ConnectedComponent> CCs = new ArrayList<ConnectedComponent>();
        //max_dimension = Extract_CC_And_Compute_MaxHeight_Width(Types_Path, CCs);
        Compute_Features(Types_Path, CCs);
        
        int passed = 0, failed =0;
        String result ="";
        for(int i=0; i< CCs.size(); i++){
            ConnectedComponent cc = CCs.get(i);
            cc.BB_Area = cc.CC_Height * cc.CC_Width;    
            
            result = NEPClassifier.ClassifyDecisionTree(cc.CC_Width, cc.CC_Height, cc.BB_Area,cc.Black_PixelsCount);
            
            int featureID = getIDFromString(result);
            
            ids.add(featureID);
        }
        return ids;
         
         
    }
    
    
    public void Verify_Features(String Types_Path) {
        ArrayList<Integer> max_dimension = new ArrayList();
        ArrayList<ConnectedComponent> CCs = new ArrayList<ConnectedComponent>();
        max_dimension = Extract_CC_And_Compute_MaxHeight_Width(Types_Path, CCs);
        
        int passed = 0, failed =0;
        String result ="";
        for(int i=0; i< CCs.size(); i++){
            ConnectedComponent cc = CCs.get(i);
            cc.BB_Area = cc.CC_Height * cc.CC_Width;    
            result = NEPClassifier.ClassifyDecisionTree(cc.CC_Width, cc.CC_Height, cc.BB_Area,cc.Black_PixelsCount);
            int featureID = getIDFromString(result);
            
            if(featureID == cc.CC_Type_ID){
                System.out.println("Verification Passed = "+ featureID);
                passed++;
            }
            else{
                System.out.println("Verification Failed = "+ featureID);
                failed++;
            }
            
        }
        System.out.println("==================================");
            System.out.println("Total Passed:"+passed);
            System.out.println("Total Failed:"+failed);
         
    }

    public void Compute_Features(String Types_Path) {

        ArrayList<Integer> max_dimension = new ArrayList();
        ArrayList<ConnectedComponent> CCs = new ArrayList<ConnectedComponent>();
        max_dimension = Extract_CC_And_Compute_MaxHeight_Width(Types_Path, CCs);
        Write_Features_Values(CCs);
    }

    public static void Write_Features_Values(ArrayList<ConnectedComponent> CCs) {
        File_Handler.Write_Features_in_File("BB_Area,Height,Width,Black Pixels Count,Image Name,CC_Type_ID");
        for (int i = 0; i < CCs.size(); i++) {
            ConnectedComponent cc = CCs.get(i);
            cc.BB_Area = cc.CC_Height * cc.CC_Width;

            String Features = cc.BB_Area + "," + cc.CC_Height + "," + cc.CC_Width + "," + cc.Black_PixelsCount + "," + cc.CC_Name + "," + getStringNumeric(cc.CC_Type_ID);

            File_Handler.Write_Features_in_File(Features);
        }
    }

    public static String getStringNumeric(int id) {
        String[] chars = String.valueOf(id).split("(?!^)");
        String ret_values = "";
        String[] numStr = {"zero","one","two","three","four","five","six","seven","eight", "nine"};
        //String[] numNum ={"0","1","2","3","4","5","6","7","8","9"};
        for (int i = 0; i < chars.length; i++) {
          ret_values += numStr[ Integer.parseInt(chars[i]) ]+"_";
        }
        return ret_values;
    }
    public static int getIDFromString(String id) {
        String[] chars = String.valueOf(id).split("_");
        String ret_values = "";
        String[] numStr = {"zero","one","two","three","four","five","six","seven","eight", "nine"};
        String[] numNum ={"0","1","2","3","4","5","6","7","8","9"};
        for (int i = 0; i < chars.length-1; i++) {
            ret_values +=  numNum[ Arrays.asList(numStr).indexOf(chars[i]) ];
        }
        return Integer.parseInt(ret_values);
    }
    
    //TODO
    public static ArrayList<Integer>  Compute_Features(String Folder_Name, ArrayList<ConnectedComponent> CCs) {
        ArrayList<Integer> Max_Dimension = new ArrayList();
        File CC_Folder = new File(Folder_Name);
        //File[] cc_folders = CC_Folder.listFiles();
        int Current_index = 0;
        int Max_Height = 0, Max_Width = 0;// this will automatically be reintialize in the first iteration
//        for (int i = 0; i < cc_folders.length; i++) { 
//            if (cc_folders[i].isDirectory()) {
                //String CC_ID = cc_folders[i].getName();
                //String CC_Path = CC_Folder + "/" + CC_ID + "/";
                File[] CCImages = CC_Folder.listFiles();
                for (int j = 0; j < CCImages.length; j++) {
                    String CC_Complete_Name = GlobalVars.cc + CCImages[j].getName();
                    ConnectedComponent cc = new ConnectedComponent(CC_Complete_Name);
                    if (Current_index == 0) {
                        Max_Height = cc.CC_Height;
                        Max_Width = cc.CC_Width;
                        Current_index = 1;
                    } else {
                        if (cc.CC_Height > Max_Height) {
                            Max_Height = cc.CC_Height;
                        }
                        if (cc.CC_Width > Max_Width) {
                            Max_Width = cc.CC_Width;
                        }
                    }
                    //cc.CC_Type_ID = Integer.parseInt(CC_ID);
                    CCs.add(cc);
                }
            //}

        //}
        Max_Dimension.add(Max_Height);
        Max_Dimension.add(Max_Width);
        return Max_Dimension;
    }

    
    public static ArrayList<Integer> Extract_CC_And_Compute_MaxHeight_Width(String Folder_Name, ArrayList<ConnectedComponent> CCs) {
        ArrayList<Integer> Max_Dimension = new ArrayList();
        File CC_Folder = new File(Folder_Name);
        File[] cc_folders = CC_Folder.listFiles();
        int Current_index = 0;
        int Max_Height = 0, Max_Width = 0;// this will automatically be reintialize in the first iteration
        for (int i = 0; i < cc_folders.length; i++) {

            if (cc_folders[i].isDirectory()) {
                String CC_ID = cc_folders[i].getName();
                String CC_Path = CC_Folder + "/" + CC_ID + "/";
                File[] CCImages = cc_folders[i].listFiles();
                for (int j = 0; j < CCImages.length; j++) {
                    String CC_Complete_Name = CC_Path + CCImages[j].getName();
                    ConnectedComponent cc = new ConnectedComponent(CC_Complete_Name);
                    if (Current_index == 0) {
                        Max_Height = cc.CC_Height;
                        Max_Width = cc.CC_Width;
                        Current_index = 1;
                    } else {
                        if (cc.CC_Height > Max_Height) {
                            Max_Height = cc.CC_Height;
                        }
                        if (cc.CC_Width > Max_Width) {
                            Max_Width = cc.CC_Width;
                        }
                    }
                    cc.CC_Type_ID = Integer.parseInt(CC_ID);
                    CCs.add(cc);
                }
            }

        }
        Max_Dimension.add(Max_Height);
        Max_Dimension.add(Max_Width);
        return Max_Dimension;
    }

    public void Read_Image(String Folder_Path, String Image_name) {
        Image_Name = Image_name;
        String Complete_Name = Folder_Path + "/" + Image_name;
        BufferedImage image = null;
        try {
            File file = new File(Complete_Name);

            image = ImageIO.read(file);
            Height = image.getHeight();
            Width = image.getWidth();
            Image_Matrix = new int[Height][Width];//Image(height, width);

            for (int r = 0; r < Height; r++) {
                for (int c = 0; c < Width; c++) {
                    //    System.out.println(" "+r+" "+c+"      ");
                    int color = -1;
                    String s = Integer.toHexString(image.getRGB(c, r));
                    int f = Integer.parseInt(s.substring(0, 2), 16);
                    int f1 = Integer.parseInt(s.substring(2, 4), 16);
                    color = f1;
//                    if (f1 == 255) {
//                        color = 1;
//                    } else {
//                        color = 0;
//                    }
                    Image_Matrix[r][c] = color;
                }

            }

        } catch (IOException e) {
            System.out.println("Exception" + e.getMessage());
        }

    }

    public void Write_Image(String Path, String Name) {

        BufferedImage img = new BufferedImage(Width, Height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < Height; i++) {
            for (int j = 0; j < Width; j++) {
                try {

                    int rgb = Image_Matrix[i][j];
                    if (rgb >= 1)// 0xFFFFFF )
                    {
                        rgb = 0xFFFFFF;
                    } else //if(rgb < 0)
                    {
                        rgb = 0;
                    }
                    img.setRGB(j, i, rgb);

                } catch (Exception e) {
                }
            }

        }
        File outputfile = new File(Path + Name); // + ".bmp");
        try {
            ImageIO.write(img, "bmp", outputfile);
        } catch (IOException e) {
            System.out.println(e + "Exception in write of CC function");
        }

    }

    public void Write_inTextFile(String Path, String Name) throws IOException {

        String Complete_Name = Path + "/" + Name + ".txt";
        BufferedWriter output = new BufferedWriter(new FileWriter(Complete_Name));
        for (int i = 0; i < Height; i++) {
            for (int j = 0; j < Width; j++) {
                if (Image_Matrix[i][j] == 0) {
                    output.write(0 + "  ");
                } else {
                    output.write(1 + "  ");
                }
            }
            output.write("\n");

        }
        output.close();

    }

    public void Write(String CompletePathName) throws IOException {
        BufferedImage img = new BufferedImage(Width, Height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < Height; i++) {
            for (int j = 0; j < Width; j++) {
                try {

                    int rgb = Image_Matrix[i][j];
                    if (rgb >= 1)// 0xFFFFFF )
                    {
                        rgb = 0xFFFFFF;
                    } else //if(rgb < 0)
                    {
                        rgb = 0;
                    }
                    img.setRGB(j, i, rgb);
                    //  System.out.print(ImageData[i][j]+" ");//image[i+minX][j+minY];
                } catch (Exception e) {
                }
            }

        }
        File outputfile = new File(CompletePathName + ".bmp");
        try {
            ImageIO.write(img, "bmp", outputfile);
        } catch (IOException e) {
            System.out.println(e + "in write of CC function");
        }

    }

    public void Write(String CompletePathName, int[][] Image_Matrix) throws IOException {
        BufferedImage img = new BufferedImage(Width, Height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < Height; i++) {
            for (int j = 0; j < Width; j++) {
                try {

                    int rgb = Image_Matrix[i][j];
                    if (rgb >= 1)// 0xFFFFFF )
                    {
                        rgb = 0xFFFFFF;
                    } else //if(rgb < 0)
                    {
                        rgb = 0;
                    }
                    img.setRGB(j, i, rgb);
                } catch (Exception e) {
                }
            }

        }
        File outputfile = new File(CompletePathName + ".bmp");
        try {
            ImageIO.write(img, "bmp", outputfile);
        } catch (IOException e) {
            System.out.println(e + "in write of CC function");
        }

    }

    public void WriteLine(String CompletePathName, int startPoint, int endPoint) throws IOException {

        BufferedImage img = new BufferedImage(Width, endPoint - startPoint, BufferedImage.TYPE_INT_RGB);
        int line_height = 0;
        for (int i = startPoint; i <= endPoint; i++) {
            for (int j = 0; j < Width; j++) {
                try {

                    int rgb = Image_Matrix[i][j];
                    if (rgb >= 1)// 0xFFFFFF )
                    {
                        rgb = 0xFFFFFF;
                    } else //if(rgb < 0)
                    {
                        rgb = 0;
                    }
                    img.setRGB(j, line_height, rgb);

                    //  System.out.print(ImageData[i][j]+" ");//image[i+minX][j+minY];
                } catch (Exception e) {
                }
            }
            line_height++;

        }
        File outputfile = new File(CompletePathName + ".bmp");
        try {
            ImageIO.write(img, "bmp", outputfile);
        } catch (IOException e) {
            System.out.println(e + "in write of CC function");
        }

    }

    public void WriteLigature(String CompletePathName, int startPoint, int endPoint) throws IOException {

        BufferedImage img = new BufferedImage(endPoint - startPoint, Height, BufferedImage.TYPE_INT_RGB);

//        for (int i = 0; i < Height; i++) {
//            for (int j = startPoint; j <= endPoint; j++) {
//
//            }
//        }
//        for (int i = startPoint; i <= endPoint; i++) {
//            for (int j = 0; j < Width; j++) {
        int lig_width = 0;
        for (int i = 0; i < Height; i++) {
            lig_width = 0;
            for (int j = startPoint; j <= endPoint; j++) {
                try {

                    int rgb = Image_Matrix[i][j];
                    if (rgb >= 1)// 0xFFFFFF )
                    {
                        rgb = 0xFFFFFF;
                    } else //if(rgb < 0)
                    {
                        rgb = 0;
                    }
                    img.setRGB(lig_width, i, rgb);

                    lig_width++;
                    //  System.out.print(ImageData[i][j]+" ");//image[i+minX][j+minY];
                } catch (Exception e) {
                }
            }

        }
        File outputfile = new File(CompletePathName + ".bmp");
        try {
            ImageIO.write(img, "bmp", outputfile);
        } catch (IOException e) {
            System.out.println(e + "in write of CC function");
        }

    }

    public void Segment_Page_Into_CCs() throws IOException {

        int img_width = this.Width;
        int img_height = this.Height;
        int[][] img_matrix = new int[img_height][img_width];
        int[][] img_matrix2 = new int[img_height][img_width];
        ArrayList<ConnectedComponent> Page_CCs = new ArrayList<>();

        for (int r = 0; r < img_height; r++) {
            for (int c = 0; c < img_width; c++) {
                img_matrix[r][c] = Image_Matrix[r][c];
                img_matrix2[r][c] = Image_Matrix[r][c];
            }
        }

        int CC_Count = 0;
        for (int c = img_width - 1; c >= 0; c--) {
            for (int r = 0; r < img_height; r++) {

                // set left, right, top, bottom and ImageMatrix of CC.  The ID of CC is not assigned here
                ConnectedComponent cc = FindNextConnectedComponent(img_matrix, r, c, img_height, img_width);
                if (cc.CCMatrix != null) {

                    CC_Count++;
                    cc.CC_Number = CC_Count;

                    //cc.CC_Name = Image_Name + "_CC_" + Integer.toString(CC_Count);
                    cc.CC_Name= String.valueOf(CC_Count);
                    cc.Write(GlobalVars.cc, cc.CC_Name);
                    Page_CCs.add(cc);

                }
            }
        }
    }

    public ConnectedComponent FindNextConnectedComponent(int[][] Temp_img, int r, int c, int height, int width) {
        ConnectedComponent CC = new ConnectedComponent();
        int lab = 1;
        int[] pos1;
        Object pos;
        int minX = height;
        int minY = width;
        int maxX = 0;
        int maxY = 0;
        ArrayList points = new ArrayList();
        Stack stack = new Stack();// it dynamically grows so no size is required
        if (Temp_img[r][c] > 0)//    continue;
        {
        } else if (Temp_img[r][c] == 0) {
            stack.push(new int[]{r, c});
            points.add(new int[]{r, c});
            if (minX > r) {
                minX = r;
            }
            if (minY > c) {
                minY = c;
            }
            if (maxX < r) {
                maxX = r;
            }
            if (maxY < c) {
                maxY = c;
            }
            Temp_img[r][c] = lab;

            while (!stack.isEmpty()) {
                pos1 = (int[]) stack.pop();
                int i = pos1[0];
                int j = pos1[1];
                int[] j_dim = {-1, -1, 0, 1, 1, 1, 0, -1};
                int[] i_dim = {0, -1, -1, -1, 0, 1, 1, 1};
                for (int iter = 0; iter < 8; iter++) {
                    int ind_i = i + i_dim[iter];
                    int ind_j = j + j_dim[iter];
                    if ((ind_i) >= 0 && (ind_i) <= height - 1 && (ind_j) >= 0 && (ind_j) <= width - 1) {
                        if (Temp_img[(ind_i)][(ind_j)] == 0)// && label[i-1][j-1] == 1)
                        {
                            Temp_img[(ind_i)][(ind_j)] = lab;
                            points.add(new int[]{(ind_i), (ind_j)});
                            if (minX > (ind_i)) {
                                minX = (ind_i);
                            }
                            if (minY > (ind_j)) {
                                minY = (ind_j);
                            }
                            if (maxX < (ind_i)) {
                                maxX = (ind_i);
                            }
                            if (maxY < (ind_j)) {
                                maxY = (ind_j);
                            }
                            stack.push(new int[]{(ind_i), (ind_j)});
                        }
                    }
                }
            }/* end while */

            CC = new ConnectedComponent(minY, maxY, minX, maxX, points);
        }

        return CC;
    }

    public void imageReadGray(String Folder_Path, String Image_name) {
        Image_Name = Image_name;
        String Complete_Name = Folder_Path + "/" + Image_name;
        BufferedImage image = null;

        File file = new File(Complete_Name);

        try {

            image = ImageIO.read(file);//reading of image file

            Height = image.getHeight();//setting pageHeight of image
            Width = image.getWidth();// setting pageWidth of image

            Image_Matrix = new int[Height][Width];//defining a 2-d array for image

            int array[] = new int[Height * Width];
            image.getRGB(0, 0, Width, Height, array, 0, Width);
            for (int arrayIndex = 0; arrayIndex < array.length; arrayIndex++) {
                //color assignment from image file to 2-D array
                int c = array[arrayIndex];
                int f = (c & 0x00ff0000) >> 16;
                int f1 = (c & 0x0000ff00) >> 8;
                int f2 = c & 0x000000ff;
                int color = ((int) ((0.2989 * (double) f) + (0.5870 * (double) f1) + (0.1140 * (double) f2)));
                Image_Matrix[arrayIndex / Width][arrayIndex % Width] = color;
            }
//            }
        } //catch clause
        catch (Exception ex) {
        }
    }

    public void binarize_(int Window_Height, int Window_Width, int Rval, int Shift) {
        int win_height = Window_Height;
        int win_width = Window_Width;
        float KVAL = (float) 0.1;
        //String Rval = Rval;
        int RVAL = Rval;
        int shift = Shift;
        try {
            //decleration and setting the varibales
            int img_mean = 0;
            int sum = 0;
            int img_count = 0;
            int w_w = 0;
            int w_h = 0;
            if (0 == (Height % win_height)) {
                w_h = (Width / (win_height));
            } else {
                w_h = (Height / (win_height)) + 1;
            }

            if (0 == (Width % win_width)) {
                w_w = (Width / (win_width));
            } else {
                w_w = (Width / (win_width)) + 1;
            }
            int[] s_winmean = new int[w_h * w_w];
            int[] s_winsd = new int[w_h * w_w];

            int win_mean = 0;
            int win_count = 0;
            win_count = 0;
            int sd = 0;// decleration of standard deviation of window variable
            win_mean = 0;// decleration of mean of window variable
            int count = 0;
            //image color extraction, sum and windows sum calculation
            for (int r = 0; r < Height; r = r + (win_height)) {
                for (int c = 0; c < Width; c = c + (win_width)) {
                    count = 0;
                    for (int ind_i = shift; (ind_i < win_height - shift + 1); ind_i++) {
                        for (int ind_j = shift; (ind_j < (win_width) - shift + 1); ind_j++) {
                            if (((r + ind_i) < Height) && ((r + ind_i) > 0) && (c + ind_j) < Width && (c + ind_j) > 0) {
                                win_mean = win_mean + Image_Matrix[r + ind_i][c + ind_j];//adding into window's sum
                                count++;
                            }
                        }
                    }

                    win_mean = win_mean / count;
                    s_winmean[win_count] = win_mean;// storing windows mean into hash table
                    for (int ind_i = shift; (ind_i < win_height - shift + 1); ind_i++) {
                        for (int ind_j = shift; (ind_j < (win_width) - shift + 1); ind_j++) {
                            if (((r + ind_i) < Height) && ((r + ind_i) > 0) && (c + ind_j) < Width && (c + ind_j) > 0) {
                                sd = sd + (Image_Matrix[r + ind_i][c + ind_j] - win_mean) * (Image_Matrix[r + ind_i][c + ind_j] - win_mean);//sum for standard deviation
                            }
                        }
                    }
                    sd = (int) Math.sqrt(sd / count);//standard deviation of current window
                    s_winsd[win_count] = sd;//putting standard deviation of current window into hash table
                    // computing maximum and minimum standard deviation among all windows
                    win_count++;
                    win_mean = 0;//re-setting mean for next window
                    sd = 0;
                }
            }
            int T = 0;//threshold variable decleration
            win_count = 0;

            for (int r = 0; r < Height; r = r + (win_height)) {
                for (int c = 0; c < Width; c = c + (win_width)) {
                    win_mean = s_winmean[win_count];
                    sd = s_winsd[win_count];
                    T = (int) ((double) win_mean * ((double) 1 + (KVAL * (double) ((sd / RVAL) - 1))));
                    for (int ind_i = 0; (ind_i < (win_height)) && ((r + ind_i) < Height); ind_i++) {
                        for (int ind_j = 0; (ind_j < (win_width)) && ((c + ind_j) < Width); ind_j++) {
                            //Threshold value checking
                            if (Image_Matrix[r + ind_i][c + ind_j] < T) {
                                Image_Matrix[r + ind_i][c + ind_j] = 0;// assignment of foreground color
                            } else {
                                Image_Matrix[r + ind_i][c + ind_j] = 1;//assignmentt of background color
                            }
                        }
                    }
                    win_count++;
                }

            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());

        }

        int done = 1;

    }

    public void computeVHistogram() {
        // set the size of the histogram
        vHistogram = new int[Height];
        // loop over the height -- vertical
        for (int h = 0; h < Height; h++) {
            // first set the values to zero
            vHistogram[h] = 0;
            // loop :: compute the black (1) pixel from the width
            for (int w = 0; w < Width; w++) {
                int value = Image_Matrix[h][w];
                if (value < 1) {
                    vHistogram[h]++;
                }
            }
        }
    }

    public void computeHHistogram() {
        // set the size of the histogram
        hHistogram = new int[Width];
        // loop over the height -- vertical
        for (int w = 0; w < Width; w++) {
            // first set the values to zero
            hHistogram[w] = 0;
            // loop :: compute the black (1) pixel from the width
            for (int h = 0; h < Height; h++) {
                int value = Image_Matrix[h][w];
                if (value < 1) {
                    hHistogram[w]++;
                }
            }
        }
    }

    public void SegmentLigature() throws IOException {

        // Compute the V Histogram 
        computeHHistogram();

        boolean isFound = false;
        int totalLine = 0;
        int lineStart = 0, lineEnd = 0, lineHeight = 0;

        if (hHistogram[0] > 0) {
            isFound = true;
            lineStart = 0;
        }

        for (int w = 0; w < Width - 1; w++) {

            if (isFound == true && hHistogram[w + 1] == 0) {
                lineEnd = w;

                lineHeight = lineEnd - lineStart;
                if (lineHeight <= 0) {
                    continue;
                }
                isFound = false;
                totalLine++;
                //DrawLineOnImage(lineStart,100, Width - 100, lineEnd - lineStart, Image_Name);
                //WriteLine(GlobalVars.segment_line + "" + Image_Name + "_" + totalLine, lineStart, lineEnd);
                
                WriteLigature(GlobalVars.segment_lig +  totalLine, lineStart, lineEnd);
                GlobalVars.ligs.add( String.valueOf(totalLine)+".bmp" );
                //System.out.println("Ligature found " + Image_Name + " --- " + totalLine + " " + lineStart + "  " + lineEnd + " width => " + (lineEnd - lineStart));

            } else if (hHistogram[w + 1] != 0 && isFound == false) {
                lineStart = w + 1;
                isFound = true;
            }
        }

        lineHeight = lineEnd - lineStart;

        //if (lineHeight <= 50) {
        if (hHistogram[Width - 1] > 0) {
            lineEnd = Width - 1;
            
            WriteLigature(GlobalVars.segment_lig + "" + Image_Name + "_" + totalLine, lineStart, lineEnd);
            GlobalVars.ligs.add( String.valueOf(totalLine)+".bmp" );
            //System.out.println("Line found " + totalLine + " " + lineStart + "  " + lineEnd);
        }
        //}
    }

    public void SegmentLine() throws IOException {

        // Compute the V Histogram 
        computeVHistogram();

        boolean isFound = false;
        int totalLine = 0;
        int lineStart = 0, lineEnd = 0, lineHeight = 0;

        if (vHistogram[0] > 0) {
            isFound = true;
            lineStart = 0;
        }
        for (int h = 0; h < Height - 1; h++) {

            if (isFound == true && vHistogram[h + 1] == 0) {
                lineEnd = h;
                isFound = false;
                lineHeight = lineEnd - lineStart;
                // 10 for now need to change later
                if (lineHeight <  10) {
                    continue;
                }

                totalLine++;
                //DrawLineOnImage(lineStart,100, Width - 100, lineEnd - lineStart, Image_Name);
                //WriteLine(GlobalVars.segment_line + "" + Image_Name + "_" + totalLine, lineStart, lineEnd);
                
                WriteLine( GlobalVars.segment_line  + totalLine, lineStart, lineEnd ) ;
                GlobalVars.lines.add( String.valueOf( totalLine)+".bmp");
                //System.out.println("Line found " + totalLine + " " + lineStart + "  " + lineEnd + " width => " + (lineEnd - lineStart));

            } else if (vHistogram[h + 1] != 0 && isFound == false) {
                lineStart = h + 1;
                isFound = true;
            }
        }

        lineHeight = lineEnd - lineStart;
        if (lineHeight <= 50) {
            if (vHistogram[Height - 1] > 0) {
                lineEnd = Height - 1;
                //System.out.println("Line found " + totalLine + " " + lineStart + "  " + lineEnd);
                
                WriteLine(GlobalVars.segment_line + "" + Image_Name + "_" + totalLine, lineStart, lineEnd);
                GlobalVars.lines.add( String.valueOf( totalLine)+".bmp");

            }
        }
    }

    public void DrawLineOnImage(int x, int y, int width, int height, String fileName) throws IOException {
        //File imgFile = new File(GlobalVars.ip+"/"+ Image_Name);
        File imgFile = new File(GlobalVars.ip + "/" + fileName);
        BufferedImage bufImg = ImageIO.read(imgFile);
        Graphics2D graph = bufImg.createGraphics();
        graph.setColor(Color.BLACK);
        //graph.drawRect(x,y,this.Width-100, 200);
        graph.drawRect(y, x, width - 100, height);
        graph.dispose();
        ImageIO.write(bufImg, "bmp", new File(GlobalVars.segment_line + "/" + fileName));
    }
}
