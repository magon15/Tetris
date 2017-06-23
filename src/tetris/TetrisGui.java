package tetris;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.border.Border;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import java.io.File;
import java.io.FileReader;

public class TetrisGui extends javax.swing.JFrame {
int ypos;
int xpos;
int rotate;
ArrayList<Integer> intBase = new ArrayList<>();
String rotateVal;
int[][] availVal = new int[21][10];
int[] aboveVal = new int[10];
JButton[][] block = new JButton[20][10];

int rng;
int rngCurrent;
boolean stopPressing = false;
int speed = 1000;
boolean gameOver = true;

ImageIcon none = new ImageIcon("src/Icons/null.jpg");
ImageIcon gray = new ImageIcon("src/Icons/tetrisTheor.jpg");
ImageIcon currentIcon;
Image frameIcon;


                //When you hold a button, for example "a", it will print out: a(2-second pause)aaaaaaaaaa
                //This is the reason why I implemented a timer. If you have suggestions, it would be very much appreciated.
                Timer timerleft = new Timer();
                TimerTask moveleft = new TimerTask(){
                    @Override
                    public void run() {}
            
                };
                
                Timer timerright = new Timer();
                TimerTask moveright = new TimerTask(){
                    @Override
                    public void run() {}
                };
                
                Timer timerdown = new Timer();
                TimerTask movedown = new TimerTask(){
                    @Override
                    public void run() {}
                };

boolean canHold = true;
boolean holdStarted = false;
int rngHold;
ArrayList<Integer> rngList = new ArrayList<>();



//ze shapes
TBlock t = new TBlock();
IBlock i = new IBlock();
RZBlock r = new RZBlock();
GZBlock g = new GZBlock();
BLBlock b = new BLBlock();
BrLBlock br = new BrLBlock();
SqBlock sq = new SqBlock();

    public TetrisGui() {
        initComponents();
        this.setResizable(false);
        loadGUI();
        try{
            frameIcon = ImageIO.read(new File("src/Icons/Tetris_Block.png"));
        }catch(IOException ez){ez.printStackTrace();}
        this.setIconImage(frameIcon);
        this.setTitle("Tetris!!!");
        gameO.setVisible(false);
        dropBlock();
        buttonNewG.requestFocus();
        mainPanel.addKeyListener(new KeyListen());
    }
    
    
    public void loadGUI(){
        
        Border emptyBorder = BorderFactory.createEmptyBorder();
        mainPanel.removeAll();
        for(int i = 0; i < 20; i++){
           for (int j = 0; j < 10; j++){
               block[i][j] = new JButton(none);
               block[i][j].setBorder(emptyBorder);
               availVal[i][j]=0;
               mainPanel.add(block[i][j]);
            }
        }
    
        for (int j = 0; j < 10; j++){
               availVal[20][j]=2;
        } 
        
        gameO.setVisible(false);
        
        try{
            ZipFile zip = new ZipFile("src/score.zip");
            if(zip.isEncrypted()){
                zip.setPassword("secret");
            }
            zip.extractAll("src/");
            
           FileReader read = new FileReader("src/score.txt");
            BufferedReader buffread = new BufferedReader(read);
         
            hiScoreLabel.setText(buffread.readLine());
            buffread.close();
            
            File del = new File("src/score.txt");
            del.delete();

        }catch(Exception ex){ex.printStackTrace();}
        
    }
    
    public void startGame(){
        labelScore.setText("0");
        createRNG();
        updateNextPieces();
        createNewBlock(0);
    }
    
    public synchronized void updateNextPieces(){
        nextBlock1.removeAll();
        nextBlock2.removeAll();
        nextBlock3.removeAll();
        nextBlock4.removeAll();
        nextBlock5.removeAll();
        
        int counter = 0;
        while(counter <= 5){
            try{
                int val = rngList.get(counter);
                String loc = "";
                int x = 0;
                int y = 0;
                
                switch (val) {
                    case 1:
                        loc = t.getLocation();
                        x = t.xscale();
                        y = t.yscale();
                        break;
                    case 2:
                        loc = i.getLocation();
                        x = i.xscale();
                        y = i.yscale();
                        break;
                    case 3:
                        loc = r.getLocation();
                        x = r.xscale();
                        y = r.yscale();
                        break;
                    case 4:
                        loc = g.getLocation();
                        x = g.xscale();
                        y = g.yscale();
                        break;
                    case 5:
                        loc = b.getLocation();
                        x = b.xscale();
                        y = b.yscale();
                        break;
                    case 6:
                        loc = br.getLocation();
                        x = br.xscale();
                        y = br.yscale();
                        break;
                    case 7:
                        loc = sq.getLocation();
                        x = sq.xscale();
                        y = sq.yscale();
                        break;
                    default:
                        break;
                }
                
                
                BufferedImage icon = ImageIO.read(new File(loc));
                ImageIcon imicon = new ImageIcon(icon);
                Image thImage = imicon.getImage();
                Image newimg = thImage.getScaledInstance(x,y,java.awt.Image.SCALE_SMOOTH);  
                
                switch (counter) {
                    case 1:
                        nextBlock1.add(BorderLayout.CENTER, new JLabel(new ImageIcon(newimg)));
                        break;
                    case 2:
                        nextBlock2.add(BorderLayout.CENTER, new JLabel(new ImageIcon(newimg)));
                        break;
                    case 3:
                        nextBlock3.add(BorderLayout.CENTER, new JLabel(new ImageIcon(newimg)));
                        break;
                    case 4:
                        nextBlock4.add(BorderLayout.CENTER, new JLabel(new ImageIcon(newimg)));
                        break;
                    case 5:
                        nextBlock5.add(BorderLayout.CENTER, new JLabel(new ImageIcon(newimg)));
                        break;
                    default:
                        break;
                }
            }catch(Exception ex){ex.printStackTrace();}
            
            counter++;
        }
    }
    
    public synchronized void createNewBlock(int hold){ 
 
        for(int i = 0; i < 20; i++){
                       if(gameOver == false){
                       for(int j = 0; j < 10; j++){
                            if(availVal[i][j]==1){
                                availVal[i][j]=0;
                                block[i][j].setIcon(none);

                            }
                       }
                       }
        }
        
        for(int i = 0; i < 20; i++){
            for(int j = 0; j < 10; j++){
                if(availVal[i][j]==3){
                    availVal[i][j]=0;
                    block[i][j].setIcon(none);

                }
            }
        }
        
        ypos = 0;
        xpos = 0;
        rotate = 1;
        int rng = 0;

        if(hold == 0){
            if(rngList.size() <= 6) { createRNG(); }
            rng = rngList.get(0);
            rngCurrent = rng;
            rngList.remove(0);
        }else if (hold != 0){
            rng = hold;
        }
        
        
        this.rng = rng;
        loadRNG(1,rng);
        
        
        //load block position relative to rng
        loadRNG(rotate,TetrisGui.this.rng);
        intBase.clear();
        for(String base : rotateVal.split(":")){
             intBase.add(Integer.parseInt(base));
        }

        ArrayList<Integer> highest = new ArrayList<>(Arrays.asList(20,20,20,20,20,20,20,20,20,20));
                        for(int j = 0; j < 10; j++){
                            for(int i = 0; i < 20; i++){
                               if(availVal[i][j] == 2){
                                   highest.set(j,i);
                                   break;
                               }
                               
                            }
                        }
        
                        ArrayList<Integer> xposCand = new ArrayList<>();
                        for(int i = 0; i < intBase.size(); i+=2){
                                xposCand.add(intBase.get(i+1));
                        }
                        
                        int lowest = Collections.max(xposCand);
                        ArrayList<Integer> newIntBase = new ArrayList<Integer>();
                        for(int z : intBase){
                            newIntBase.add(z);
                        }
                       
                        counter = 0;
                        for(int v = 0; v < newIntBase.size(); v+=2){
                            newIntBase.set(v+1, newIntBase.get(v+1)-lowest);
                        } 
                         
                        ArrayList<Integer> findLowest = new ArrayList<Integer>();
                        for(int i = 0; i < newIntBase.size(); i+=2){
                                findLowest.add(highest.get(newIntBase.get(i))-newIntBase.get(i+1));
                        }
                        int yposTheor = Collections.min(findLowest) - 1;
                        
                        for(int v = 0; v < newIntBase.size(); v+=2){
                            if(yposTheor > 0 && availVal[newIntBase.get(v+1)+yposTheor][newIntBase.get(v)]!=2){
                            availVal[newIntBase.get(v+1)+yposTheor][newIntBase.get(v)] = 3;
                            block[newIntBase.get(v+1)+yposTheor][newIntBase.get(v)].setIcon(gray);
                            }
                        }

        
        boolean createIcon = true;
        for (int v = 0; v < intBase.size(); v+=2){
                        if( availVal[0][intBase.get(v)] == 2){
                            gameOver();
                            createIcon = false;
                            break;
                        }
        }
                        
        for (int v = 0; v < intBase.size(); v+=2){
                        if(intBase.get(v)>= 0 && intBase.get(v+1)>=0 && gameOver == false && createIcon == true){
                            block[intBase.get(v+1)][intBase.get(v)].setIcon(currentIcon);
                            availVal[intBase.get(v+1)][intBase.get(v)] = 1;
                        }
        }
   
                        for(int i = 0; i < intBase.size(); i+=2){
                            if(intBase.get(i+1) == -1){
                                aboveVal[intBase.get(i)]=1;
                            }
                        }
       
    }
    
    public void createRNG(){
        ArrayList<Integer> seven = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7));
        
            while(!seven.isEmpty()){
                int random = (int) (Math.random()*seven.size());
                rngList.add(seven.get(random));
                seven.remove(random);
            }
            seven = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7));
    }
    
    public void loadRNG(int rotate, int rng){
        
    switch (rng) {
        case 1:
            rotateVal = t.getRotateVal(rotate);
            currentIcon = t.returnIcon();
            break;
        case 2:
            rotateVal = i.getRotateVal(rotate);
            currentIcon = i.returnIcon();
            break;
        case 3:
            rotateVal = r.getRotateVal(rotate);
            currentIcon = r.returnIcon();
            break;
        case 4:
            rotateVal = g.getRotateVal(rotate);
            currentIcon = g.returnIcon();
            break;
        case 5:
            rotateVal = b.getRotateVal(rotate);
            currentIcon = b.returnIcon();
            break;
        case 6:
            rotateVal = br.getRotateVal(rotate);
            currentIcon = br.returnIcon();
            break;
        case 7:
            rotateVal = sq.getRotateVal(rotate);
            currentIcon = sq.returnIcon();
            break;
        default:
            break;
    }
    }
    
    public static void main(String[] args){
          new TetrisGui().setVisible(true);
    }
 
    class KeyListen extends KeyAdapter{
       
        @Override
        public void keyTyped(KeyEvent e) {}
        
        //When you hold a button, for example "a", it will print out: a(2-second pause)aaaaaaaaaa
        //This is the reason why I implemented a timer. If you have suggestions, it would be very much appreciated.
        @Override
        public void keyReleased(KeyEvent e) {
           if(e.getKeyCode() == VK_LEFT && stopPressing == false && gameOver == false){
           timerleft.cancel();
           moveleft.cancel();
           timerleft = new Timer();
           moveleft = new TimerTask(){
                    @Override
                    public void run() {}
            };
           }else if (e.getKeyCode() == VK_RIGHT && stopPressing == false && gameOver == false){
               timerright.cancel();
               moveright.cancel();
               timerright = new Timer();
               moveright = new TimerTask(){
                    @Override
                    public void run() {}
               };
           }else if (e.getKeyCode() == VK_DOWN && stopPressing == false && gameOver == false){
               timerdown.cancel();
               movedown.cancel();
               timerdown = new Timer();
               movedown = new TimerTask(){
                    @Override
                    public void run() {}
               };
           }
        }
        
        @Override
        public void keyPressed(KeyEvent e) {
            
            if(e.getKeyCode() == VK_LEFT && stopPressing == false && gameOver == false){
                timerleft.cancel();
                moveleft.cancel();
                timerleft = new Timer();
                moveleft = new TimerTask(){
                    @Override
                    public void run() {
                        startMovement(-1,0,0,rotate);
                    }
            
                };
                timerleft.scheduleAtFixedRate(moveleft, 0, 200);
                
            }else if(e.getKeyCode() == VK_RIGHT && stopPressing == false && gameOver == false){
                timerright.cancel();
                moveright.cancel();
                timerright = new Timer();
                moveright = new TimerTask(){
                    @Override
                    public void run() {
                        startMovement(0,1,0,rotate);
                    }
            
                };
                timerright.scheduleAtFixedRate(moveright, 0, 200);
            }else if (e.getKeyCode() == VK_DOWN && stopPressing == false && gameOver == false){
                timerdown.cancel();
                movedown.cancel();
                timerdown = new Timer();
                movedown = new TimerTask(){
                    @Override
                    public void run() {
                        startMovement(0,0,1,rotate);
                    }
            
                };
                timerdown.scheduleAtFixedRate(movedown, 0, 175);
            
            }else if(e.getKeyCode() == VK_UP && gameOver == false){
                    
                    boolean nonRotatable = false;
                    
                    rotate++;
                    if(rotate == 5){
                        rotate = 1;
                    }
                    
                    //check if rotatable////////////////////////////////////////////////////////////////////////////////////////////////
                        ArrayList<Integer> existing = new ArrayList();
                        ArrayList<Integer> theoretical = new ArrayList();
                        
                        loadRNG(rotate,TetrisGui.this.rng);
                        theoretical.clear();
                        for(String base : rotateVal.split(":")){
                            theoretical.add(Integer.parseInt(base));
                        }
                    
                        for(int i = 0; i < 21; i++){
                            for (int j = 0; j < 10; j++){
                                if(availVal[i][j]==2){
                                    existing.add(j);
                                    existing.add(i);
                                }
                            }
                        }
                    
                        for (int i = 0; i < theoretical.size(); i+=2){
                            theoretical.set(i, theoretical.get(i)+xpos);
                            theoretical.set(i+1, theoretical.get(i+1)+ypos);
                            if(theoretical.get(i) < 0 || theoretical.get(i) >= 10 || theoretical.get(i+1)>19){
                                nonRotatable = true;                                
                            }
                        }
                    
                        for(int i = 0; i < theoretical.size(); i+=2){
                            for(int j = 0; j < existing.size(); j+=2){
                                if(theoretical.get(i) == existing.get(j) && theoretical.get(i+1) == existing.get(j+1)){
                                    nonRotatable = true;
                                }
                            }
                        }
                ///////////////////////////////////////////////////////////////////////////////////////////////////////              
                    
                    if (nonRotatable == true){
                        rotate--;
                        if(rotate == 0){
                            rotate = 4;
                        }
                    }else if (nonRotatable == false){                        
                        startMovement(0,0,0,rotate);
                    }

            }else if (e.getKeyCode() == VK_SHIFT && gameOver == false){
                
                String loc = "";
                int x = 0;
                int y = 0;
                int val = 0;
                
                val = rngCurrent;
                
                switch (val) {
                    case 1:
                        loc = t.getLocation();
                        x = t.xscale();
                        y = t.yscale();
                        break;
                    case 2:
                        loc = i.getLocation();
                        x = i.xscale();
                        y = i.yscale();
                        break;
                    case 3:
                        loc = r.getLocation();
                        x = r.xscale();
                        y = r.yscale();
                        break;
                    case 4:
                        loc = g.getLocation();
                        x = g.xscale();
                        y = g.yscale();
                        break;
                    case 5:
                        loc = b.getLocation();
                        x = b.xscale();
                        y = b.yscale();
                        break;
                    case 6:
                        loc = br.getLocation();
                        x = br.xscale();
                        y = br.yscale();
                        break;
                    case 7:
                        loc = sq.getLocation();
                        x = sq.xscale();
                        y = sq.yscale();
                        break;
                    default:
                        break;
                }
                
                if(canHold == true && holdStarted == false){
                    
                    try{
                        BufferedImage icon = ImageIO.read(new File(loc));
                        ImageIcon imicon = new ImageIcon(icon);
                        Image thImage = imicon.getImage();
                        Image newimg = thImage.getScaledInstance(x,y,java.awt.Image.SCALE_SMOOTH); 
                        holdBlock.removeAll();
                        holdBlock.add(BorderLayout.CENTER, new JLabel(new ImageIcon(newimg)));
                    }catch(Exception ex){ex.printStackTrace();}
                    
                    for(int i = 0; i < 20; i++){
                        for(int j = 0; j < 10; j++){
                            if(availVal[i][j] == 1 || availVal[i][j] == 3){
                                availVal[i][j]=0;
                                block[i][j].setIcon(none);
                            }
                        }
                    }
                    
                    rngHold = val;
                    updateNextPieces();
                    createNewBlock(0);
                    holdStarted = true;
                    
                    
                }else if(canHold == true && holdStarted == true){
                    
                    for(int i = 0; i < 20; i++){
                        for(int j = 0; j < 10; j++){
                            if(availVal[i][j] == 1 || availVal[i][j] == 3){
                                availVal[i][j]=0;
                                block[i][j].setIcon(none);
 
                            }
                        }
                    }

                    try{
                        BufferedImage icon = ImageIO.read(new File(loc));
                        ImageIcon imicon = new ImageIcon(icon);
                        Image thImage = imicon.getImage();
                        Image newimg = thImage.getScaledInstance(x,y,java.awt.Image.SCALE_SMOOTH); 
                        holdBlock.removeAll();
                        holdBlock.add(BorderLayout.CENTER, new JLabel(new ImageIcon(newimg)));
                    }catch(Exception ex){ex.printStackTrace();}
                
                    createNewBlock(rngHold);
                    rngHold = rngCurrent;
                    
                }
                
                canHold = false;
                    
            
            }else if (e.getKeyCode() == VK_SPACE && gameOver == false){
                             
                int moving = intBase.size()/2;
                int theoretical = 0;
                for(int i = 0; i < 20; i++){
                    for(int j = 0; j < 10; j++){
                        synchronized(this){
                        if(availVal[i][j]==3){;
                            theoretical++;
                        }
                        }
                }
                }
                
                
                if(moving == theoretical){
                for(int t = 0; t < 20; t++){
                    for(int j = 0; j < 10; j++){
                            if(availVal[t][j]==1){
                                availVal[t][j]=0;
                                block[t][j].setIcon(none);

                            }else if(availVal[t][j]==3){
                                availVal[t][j]=2;
                                block[t][j].setIcon(currentIcon);
                            } 
                    }
                    
                }
                }else if(moving != theoretical && theoretical!=0){
                    ArrayList<Integer> theLowestM = new ArrayList<Integer>();
                    ArrayList<Integer> theLowestT = new ArrayList<Integer>();
                    
                        for(int t = 19; t >= 0; t--){
                        for(int j = 0; j < 10; j++){
                            if(availVal[t][j]==3){
                                theLowestT.add(t);
                            }else if(availVal[t][j]==1){
                                theLowestM.add(t);
                            }
                        }
                        }

                    int yMove = Collections.max(theLowestT)-Collections.max(theLowestM);
                    
                    for(int i = 19; i >= 0; i--){
                        for(int j = 0; j < 10; j++){
                            if(availVal[i][j]==1){
                                availVal[i+yMove][j]=2;
                                block[i+yMove][j].setIcon(currentIcon);
                                availVal[i][j]=0;
                                block[i][j].setIcon(none);

                            }
                        }
                    }
                }else if (theoretical == 0 && moving != theoretical){
                    for(int i = 0; i < 20; i++){
                        for(int j = 0; j < 10; j++){
                            if(availVal[i][j]==1){
                                availVal[i][j]=2;
                                block[i][j].setIcon(currentIcon);
                            }
                        }
                    }
                    for(int j = 0; j < 10; j++){
                            if(aboveVal[j]==1){
                                gameOver();
                            }
                    }
                }
                
               
                        
                canHold = true;        
                updateNextPieces();
                checkforClear();
                createNewBlock(0);
                setScore(0,1);

                
            }
            
                
        }
    }
    
    public synchronized void startMovement(int left, int right, int down, int rotate){
                //check if block is movable left, right, or downwards
                boolean isMovableL = true;
                boolean isMovableR = true;
                boolean isMovableD = true;
                
                for(int i = 0; i < 21; i++){
                    for (int j = 0; j < 10; j++){
                        if(j+1 < 10){
                            if(availVal[i][j] == 2 && availVal[i][j+1] == 1){
                                isMovableL = false;
                                break;
                            }else if(availVal[i][j] == 1 && availVal[i][j+1] == 2){
                                isMovableR = false;
                                break;
                            }
                        }
                        
                    }
                   if (availVal[i][0] == 1 && left != 0){
                            isMovableL = false;
                            break;
                   }else if (availVal[i][9] == 1 && right != 0){
                            isMovableR = false;
                            break;
                   }
                }
                
                if(aboveVal[0]==1){
                    isMovableL = false;
                }else if(aboveVal[9]==1){
                    isMovableR = false;
                }
                
                for(int i = 20; i >= 1; i--){
                    for (int j = 0; j < 10; j++){
                        if(availVal[i][j]==2 && availVal[i-1][j]==1){
                            isMovableD = false;
                            break;
                        }
                    }
                }
                
                if(isMovableR == false){
                    right = 0;
                }
                if(isMovableL == false){
                    left = 0;
                }
                if(isMovableD == false){
                    down = 0;
                }
                if(gameOver == true){
                    right = 0;
                    left = 0;
                    down = 0;
                }
                //////////////////////////////////////////////////////////////////////////////////////////////////////////
                
                
                //start movement//////////////////////////////////////////////////////////////////////////////////////////
                int defX = 0;
                underBlock:
                for(int u = 0; u < 2; u++){
                        
                        //check for the highest position of dropped blocks
                        ArrayList<Integer> highest = new ArrayList<>(Arrays.asList(20,20,20,20,20,20,20,20,20,20));
                        for(int j = 0; j < 10; j++){
                            for(int i = defX; i < 20; i++){
                               if(availVal[i][j] == 2){
                                   highest.set(j,i);
                                   break;
                               }
                               
                            }
                        }

                        //check for the distance between lowest position of moving block and highest position of dropped blocks
                        ArrayList<Integer> findLowest = new ArrayList<Integer>();
                        for(int i = 0; i < intBase.size(); i+=2){
                                findLowest.add(highest.get(intBase.get(i)+xpos)-intBase.get(i+1));
                        }
                        int yposTheor = Collections.min(findLowest) - 1; 
                        
                        //to remove the previous "gray icon" position whenever you rotate or move the shape
                        for(int v = 0; v < intBase.size(); v+=2){
                            if(yposTheor!=0 && gameOver == false && availVal[intBase.get(v+1)+yposTheor][intBase.get(v)+xpos] != 2){
                            availVal[intBase.get(v+1)+yposTheor][intBase.get(v)+xpos] = 0;
                            block[intBase.get(v+1)+yposTheor][intBase.get(v)+xpos].setIcon(none);
                            }
                        }
   
                        
                        
                        //remove the blocks of the previous position of the moving shape
                        for (int v = 0; v < intBase.size(); v+=2){
                        
                        if(intBase.get(v)+xpos+left+right < 0){
                            break;
                        }else if(intBase.get(v+1)+ypos+down>19){
                           break;
                        }else if(intBase.get(v+1)+ypos>=0 && gameOver == false && availVal[intBase.get(v+1)+ypos][intBase.get(v)+xpos] != 2){
                           block[intBase.get(v+1)+ypos][intBase.get(v)+xpos].setIcon(none);
                           availVal[intBase.get(v+1)+ypos][intBase.get(v)+xpos] = 0;

                        }
                        
                }
                    //handles the rotation of the new block
                    //(this snippet runs even when you don't rotate the block)
                    loadRNG(rotate,TetrisGui.this.rng);
                    intBase.clear();
                    for(String base : rotateVal.split(":")){
                        intBase.add(Integer.parseInt(base));
                    }

                        //find the position of the lowest block of the moving shape per column, adding all those values to an ArrayList
                        findLowest = new ArrayList<Integer>();
                        for(int i = 0; i < intBase.size(); i+=2){
                                findLowest.add(highest.get(intBase.get(i)+xpos)-intBase.get(i+1));
                        }
                        yposTheor = Collections.min(findLowest) - 1; 
                    
                        //get y-values of the int-base
                        ArrayList<Integer> xposCand = new ArrayList<>();
                        for(int i = 0; i < intBase.size(); i+=2){
                                xposCand.add(intBase.get(i+1));
                        }
                        
                        //get the lowest position that comes from xposCand arraylist, which ironically is the maximum value
                        int lowest = Collections.max(xposCand);
                        ArrayList<Integer> newIntBase = new ArrayList<Integer>();
                        for(int z : intBase){
                            newIntBase.add(z);
                        }
                        
                        for(int v = 0; v < newIntBase.size(); v+=2){
                            newIntBase.set(v+1, newIntBase.get(v+1)-lowest);
                        } 
                         
                        
                        findLowest = new ArrayList<Integer>();
                        for(int i = 0; i < newIntBase.size(); i+=2){
                                findLowest.add(highest.get(newIntBase.get(i)+xpos+left+right)-newIntBase.get(i+1));
                        }
                        yposTheor = Collections.min(findLowest) - 1;
                        
                        //finally after many years of code, we finally get to add the gray shape, which is the theoretical position of the moving block
                        for(int v = 0; v < newIntBase.size(); v+=2){
                            if(yposTheor > 0 && availVal[newIntBase.get(v+1)+yposTheor][newIntBase.get(v)+xpos+left+right] != 2){
                            availVal[newIntBase.get(v+1)+yposTheor][newIntBase.get(v)+xpos+left+right] = 3;
                            block[newIntBase.get(v+1)+yposTheor][newIntBase.get(v)+xpos+left+right].setIcon(gray);
                            }
                        }
                        
                        
                    //add new blocks of the whole shape
                    for (int v = 0; v < intBase.size(); v+=2){
                        if(intBase.get(v)+xpos+left+right < 0){
                            break;
                        }else if(intBase.get(v+1)+ypos+down>19){
                            break;
                        }else if (intBase.get(v+1)+ypos+down>=0 && intBase.get(v+1)+ypos+down<=19){
                            block[intBase.get(v+1)+ypos+down][intBase.get(v)+xpos+left+right].setIcon(currentIcon);
                            availVal[intBase.get(v+1)+ypos+down][intBase.get(v)+xpos+left+right] = 1;
                        }
                    }
                    
                    //check if moving block is under existing block
 
                    ArrayList<Integer> oneCheck = new ArrayList<>();
                    for(int i = 19; i>=0; i--){
                        for(int j = 0; j < 10; j++){
                            if(availVal[i][j]==1){
                                oneCheck.add(i);
                                oneCheck.add(j);
                            }
                        }
                    }
   
                    ArrayList<Integer> external = new ArrayList<>();
                    outerloop:
                    for(int i = 0; i < oneCheck.size(); i+=2){
                        for(int j = oneCheck.get(i)-1; j >= 0; j--){
                            if(availVal[j][oneCheck.get(i+1)]==2){
                                external.add(j);
                            }
                        }
                    }
       
                    if(!external.isEmpty() && u!=1){
                        for(int i = 0; i < 20; i++){
                            for(int j = 0; j < 10; j++){
                                if(availVal[i][j]==3){
                                    availVal[i][j]=0;
                                    block[i][j].setIcon(none);

                                }
                          }
                        }
                        defX = Collections.max(external)+1;
                    }else{
                        break underBlock;
                    }
                }
                
                
         
                    xpos = xpos+left+right;
                    ypos = ypos + down;            
                    
                    //I can't recall if this is a band-aid fix, but this tries to resolve the problem that results when ypos is 0
                    if(ypos == 0){
                        for(int j = 0; j < 10; j++){
                                if(aboveVal[j]==1){
                                    aboveVal[j]=0;
                                }
                            }
                        
                        
                        for(int i = 0; i < intBase.size(); i+=2){
                            if(intBase.get(i+1) == -1){
                                aboveVal[intBase.get(i)+xpos]=1;
                            }
                        }
                    }else{
                         for(int j = 0; j < 10; j++){
                                if(aboveVal[j]==1){
                                   aboveVal[j]=0;
                                }
                         }
                    }
  
    }         
                    
    int counter;
    public void checkforClear(){
        
        int clears = 0;
        for(int i = 19; i >= 0; i--){
        boolean canClear = true;
            for (int j = 0; j < 10; j++){
                if(availVal[i][j] == 0){
                    canClear = false;
                    break;
                }
            }
            
            if(canClear == true){
                  for(int z = 0; z < i; z++){
                    for(int x = 0; x < 10; x++){
                        availVal[i-z][x] = availVal[i-(z+1)][x];
                        block[i-z][x].setIcon(block[i-(z+1)][x].getIcon());
                    }
                  } 
                
                i = 20;
                clears++;
            }
        }
        setScore(clears,0);
    }
    
    public void gameOver(){
        gameOver = true;

        gameO.setVisible(true);
        buttonNewG.setEnabled(true);
            //add score to a password protected zip file. NO CHEATING!
            try{
            ZipFile zip = new ZipFile("src/score.zip");
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            parameters.setPassword("secret");
            
            FileWriter writer = new FileWriter("src/score.txt");
            writer.write(hiScoreLabel.getText());
            writer.close();
            
            zip.addFile(new File("src/score.txt"), parameters);
            
            File delete = new File("src/score.txt");
            delete.delete();
            
            }catch(Exception e){e.printStackTrace();}
        
    }
    
    public void setScore(int clears, int drop){
        labelScore.setText(Integer.toString(Integer.parseInt(labelScore.getText())+clears*100));
        labelScore.setText(Integer.toString(Integer.parseInt(labelScore.getText())+drop*25));
        int score = Integer.parseInt(labelScore.getText());
        int hiscore = Integer.parseInt(hiScoreLabel.getText());
        
        if(score > hiscore){
            hiScoreLabel.setText(labelScore.getText());   
        }
        
        
    }
    
    public void dropBlock(){
        Thread dropping = new Thread(new DropIt());
        dropping.start();
        
    }
    
    public class DropIt implements Runnable{

        @Override
        public void run() {
            
                while(true){
                    counter++;
                    try{
                        if(checkDownwardCollision() == true) speed = 2000;
                        Thread.sleep(speed);
                        speed = 1000;
                    }catch(Exception ex){ex.printStackTrace();}
                    
                    if(gameOver == false){
                    if(checkDownwardCollision() == true){
                    synchronized(this){
                        
                        stopPressing = true;
                        timerdown.cancel();
                        timerleft.cancel();
                        timerright.cancel();
                        movedown.cancel();
                        moveleft.cancel();
                        movedown.cancel();
                        
                        for(int z = 0; z < 21; z++){
                              for(int j = 0; j < 10; j++){ 
                                  if(availVal[z][j]==1){
                                      availVal[z][j]=2;
                                      block[z][j].setIcon(currentIcon);
                                  }
                                  if(aboveVal[j]==1){
                                      gameOver();
                                  }
                              }
                        }
                        
                       
                        checkforClear();
                        updateNextPieces();
                        createNewBlock(0);
                        setScore(0,1);

                        
                        canHold = true;
                        stopPressing = false;
                         
                    } 
                    }else{
                       startMovement(0,0,1,rotate);
                    }
                    }
                }
                
            
        }    
    }
    
    public boolean checkDownwardCollision(){
        boolean doesCollide = false;
        for(int i = 0; i < 20; i++){
            for(int j = 0; j < 10; j++){
                synchronized(this){
                    if(availVal[i][j] == 1 && availVal[i+1][j] == 2){
                        doesCollide = true;
                    }
                }
            }
        }
        
        for(int j = 0; j < 10; j++){
            synchronized(this){
                if(aboveVal[j]==1 && availVal[0][j]==2){
                    doesCollide = true;
                }
            }
        }
        
        return doesCollide;
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        holdBlock = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        nextBlock3 = new javax.swing.JPanel();
        nextBlock2 = new javax.swing.JPanel();
        nextBlock4 = new javax.swing.JPanel();
        nextBlock5 = new javax.swing.JPanel();
        nextBlock1 = new javax.swing.JPanel();
        buttonNewG = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        labelScore = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        gameO = new javax.swing.JLabel();
        hiScoreLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tetris!!!");

        mainPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 5, true));
        mainPanel.setLayout(new java.awt.GridLayout(20, 10));

        jLabel1.setText("Next piece...");

        holdBlock.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        holdBlock.setLayout(new java.awt.BorderLayout());

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Hold");

        nextBlock3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        nextBlock3.setLayout(new java.awt.BorderLayout());

        nextBlock2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        nextBlock2.setLayout(new java.awt.BorderLayout());

        nextBlock4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        nextBlock4.setLayout(new java.awt.BorderLayout());

        nextBlock5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        nextBlock5.setLayout(new java.awt.BorderLayout());

        nextBlock1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        nextBlock1.setLayout(new java.awt.BorderLayout());

        buttonNewG.setText("Start game");
        buttonNewG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNewGActionPerformed(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Score:");

        labelScore.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        labelScore.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelScore.setText("0");
        labelScore.setToolTipText("");

        jLabel8.setText("Hi-score:");

        gameO.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        gameO.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gameO.setText("Game over");

        hiScoreLabel.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        hiScoreLabel.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelScore, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(holdBlock, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(47, 47, 47))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(gameO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonNewG, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(6, 6, 6)
                        .addComponent(hiScoreLabel)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nextBlock2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nextBlock1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nextBlock3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nextBlock4, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nextBlock5, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nextBlock1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nextBlock2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nextBlock3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nextBlock4, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nextBlock5, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(hiScoreLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(holdBlock, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelScore)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonNewG)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(gameO)))))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonNewGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNewGActionPerformed
        gameOver = false;
        loadGUI();
        startGame();
        buttonNewG.setEnabled(false);
        mainPanel.requestFocus();

        for(int i = 0; i < 10; i++){
           aboveVal[i] = 0;
        }
        

    }//GEN-LAST:event_buttonNewGActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonNewG;
    private javax.swing.JLabel gameO;
    private javax.swing.JLabel hiScoreLabel;
    private javax.swing.JPanel holdBlock;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel labelScore;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel nextBlock1;
    private javax.swing.JPanel nextBlock2;
    private javax.swing.JPanel nextBlock3;
    private javax.swing.JPanel nextBlock4;
    private javax.swing.JPanel nextBlock5;
    // End of variables declaration//GEN-END:variables
}
