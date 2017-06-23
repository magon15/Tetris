package tetris;

import java.awt.Image;
import javax.swing.ImageIcon;
public class SqBlock{
    private final String baseVal1 = "4:-1:5:-1:4:0:5:0";
    private final String baseVal2 = "4:-1:5:-1:4:0:5:0";
    private final String baseVal3 = "4:-1:5:-1:4:0:5:0";
    private final String baseVal4 = "4:-1:5:-1:4:0:5:0";
    private ImageIcon yellow = new ImageIcon("src/Icons/yellow.jpg");
    private Image img = yellow.getImage() ;  
    private Image newimg = img.getScaledInstance(18, 18,  java.awt.Image.SCALE_SMOOTH );  
    
    private final String location = "src/Icons/sqblock.JPG";
    private final int xscale = 38;
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
        yellow = new ImageIcon(newimg);
        return yellow;
    }
    
}
