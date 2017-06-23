/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import java.awt.Image;
import javax.swing.ImageIcon;

public class IBlock{
    private final String baseVal1 = "3:0:4:0:5:0:6:0";
    private final String baseVal2 = "4:-1:4:0:4:1:4:2";
    private final String baseVal3 = "3:-1:4:-1:5:-1:6:-1";
    private final String baseVal4 = "5:-1:5:0:5:1:5:2";
    private ImageIcon blue = new ImageIcon("src/Icons/blue.jpg");
    private Image img = blue.getImage() ;  
    private Image newimg = img.getScaledInstance(18, 18,  java.awt.Image.SCALE_SMOOTH );  
    
    
    //for small icon
    private final String location = "src/Icons/iblock.JPG";
    private final int xscale = 63;
    private int yscale = 28;
    
    public String getLocation(){
        return location;
    }
    
    public int xscale(){
        return xscale;
    }
    
    public int  yscale(){
        return yscale;
    }
    
    public String getRotateVal(int rotate){
        if (rotate == 1){
            return baseVal1;
        }else if (rotate == 2){
            return baseVal2;
        }else if (rotate == 3){
            return baseVal3;
        }else if (rotate == 4){
            return baseVal4;
        }
        return "";
    }
    
    public ImageIcon returnIcon(){
        blue = new ImageIcon(newimg);
        return blue;
    }
    
}
