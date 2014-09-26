/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NPOCR;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author Administrator
 */
public class ConnectedComponent {

   int CC_Height;
    int CC_Width;
    int CC_Top;
    int CC_Bottom;
    int CC_Left;
    int CC_Right;
    int [][]CCMatrix;
    int CC_Number;
    String CC_Name;
    // features
    int CC_Type_ID;
    int BB_Area;
    int Black_PixelsCount;
        ConnectedComponent() {
            CC_Height=-1;
     CC_Width=-1;
     CC_Top=-1;
     CC_Bottom=-1;
     CC_Left=-1;
     CC_Right=-1;
 CC_Number=-1;
    CC_Name=null;
    CC_Type_ID=-1;
    BB_Area=-1;
    Black_PixelsCount=-1;
    }
public ConnectedComponent(String Complete_Name)//int left, int right, int top, int bottom, ArrayList points, int[][] imgMatrix, int baseline) {
    {

    CC_Name=Complete_Name.substring(Complete_Name.lastIndexOf("/")+1,Complete_Name.length());
     String Path = Complete_Name;
        BufferedImage image = null;
        int height=0,width=0;
int black_pixels=0;
        
        try {
            File file = new File(Path);
            
            image = ImageIO.read(file);
            height = image.getHeight();
             width = image.getWidth();
             CCMatrix = new int [height][width];//Image(height, width);
            for (int r = 0; r < height; r++) {
                for (int c = 0; c < width; c++) {
                    int color = image.getRGB(c, r);

                    String s = Integer.toHexString(image.getRGB(c, r));
                    int f = Integer.parseInt(s.substring(0, 2), 16);
                    int f1 = Integer.parseInt(s.substring(2, 4), 16);
                    color = 0;
                    if (f1 == 255) {
                        color = 1;
                    } else {
                        black_pixels++;
                        color = 0;
                    }
                    CCMatrix[r][c] = color;
                }

            }
            this.Black_PixelsCount=black_pixels;
            BB_Area=CC_Height*CC_Width;
        } catch (IOException e) {
            System.out.println("Exception" + e.getMessage());
        }

        CC_Left = 0;
        CC_Right = width;
        CC_Top = 0;
        CC_Bottom = height;
        CC_Height = height;
        CC_Width = width;
    }

    public ConnectedComponent(int left, int right, int top, int bottom, ArrayList points) {

        CC_Left = left;
        CC_Right = right;
        CC_Top = top;
        CC_Bottom = bottom;
        CC_Height = CC_Bottom - CC_Top + 1 + 2;// 2 for pading
        CC_Width = CC_Right - CC_Left + 1 + 2;
        CCMatrix = new int[CC_Height][CC_Width];
                for (int i = 0; i < CC_Height; i++) {
            for (int j = 0; j < CC_Width; j++) {
                CCMatrix[i][j] = 1;
            }
        }

        for (int i = 0; i < points.size(); i++) {
            int[] p = (int[]) points.get(i);

            int x = p[0] - CC_Left + 1;
            int y = p[1] - CC_Top + 1;
            
            CCMatrix[p[0] - CC_Top + 1][p[1] - CC_Left + 1] = 0;//image[i+minX][j+minY];
        }

    }

       public void Write(String Path, String Name) throws IOException {
        //////////////Printing//////////////////////
        BufferedImage img = new BufferedImage(CC_Width, CC_Height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < CC_Height; i++) {
            for (int j = 0; j < CC_Width; j++) {
                try {

                    int rgb = CCMatrix[i][j];
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
        File outputfile = new File(Path + Name + ".bmp");
        try {
            ImageIO.write(img, "bmp", outputfile);
        } catch (IOException e) {
            System.out.println(e + "in write of CC function");
        }


    }

}
