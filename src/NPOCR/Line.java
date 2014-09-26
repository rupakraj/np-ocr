/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NPOCR;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class Line {
    
    int Line_Height;
    int Line_Width;
    int Line_Top;
    int Line_Bottom;
    int Line_Left;
    int Line_Right;
    int [][]LineMatrix;
    ArrayList<ConnectedComponent> Diacritics;
    ArrayList<ConnectedComponent> MainBodies;
    ArrayList<Ligature> Ligatures;
    public Line()
    {
           Line_Height=-1;
     Line_Width=-1;
     Line_Top=-1;
     Line_Bottom=-1;
     Line_Left=-1;
     Line_Right=-1;
     Diacritics = new ArrayList<ConnectedComponent>();
     MainBodies=  new ArrayList<ConnectedComponent>();
    }
    
}
