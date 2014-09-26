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
public class Ligature {
    ConnectedComponent MainBody;
    ArrayList<ConnectedComponent> Diacritics;
    Ligature()
    {
    MainBody= new ConnectedComponent();
    Diacritics= new ArrayList<ConnectedComponent>();
    }
    
}
