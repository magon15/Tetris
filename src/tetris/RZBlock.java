package tetris;

import java.awt.Image;
import javax.swing.ImageIcon;

public class RZBlock{
    private final String baseVal1 = "3:-1:4:-1:4:0:5:0";
    private final String baseVal2 = "4:0:4:1:5:0:5:-1";
    private final String baseVal3 = "3:0:4:0:5:1:4:1";
    private final String baseVal4 = "3:0:3:1:4:0:4:-1";
    private ImageIcon red = new ImageIcon("src/Icons/red.jpg");
    private Image img = red.getImage();  
    private Image newimg = img.getScaledInstance(18, 18,  java.awt.Image.SCALE_SMOOTH );  
    
    private final String location = "src/Icons/rzblock.JPG";
    private final int xscale = 49;
    private int yscale = 37;
    
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
        red = new ImageIcon(newimg);
        return red;
    }
    
}
