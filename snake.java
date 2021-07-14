import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Rectangle;

public class MAIN {

    public static void main(String[] args) {
        App newApp = new App(); //MAKE GAME
    }

}
class Character {
    public int gDimension = App.height;
    public int gWidth = App.width;
    public int gheight = App.dimension;


    public ArrayList<Rectangle> charStats;
    public String motion; //W A S D NO ARROW KEYS
    public void W() {
        motion = "UP";
    } //typical movement functions for a game WASD
    public void A() {
        motion = "DOWN";
    } // this will not work for arrow keys!!
    public void S() {
        motion = "LEFT";
    }
    public void D() {
        motion = "RIGHT";
    }
    public Character() {
        charStats = new ArrayList<>();                                            //=============================
        Rectangle XY = new Rectangle(App.dimension, App.dimension);                 //STRUCTURES THE FUNCTIONALITY
        XY.setLocation(App.width/2*App.dimension, App.height/2*App.dimension);// OF THE RECTANGLE BASED
        charStats.add(XY);                                                        //SNAKE MODEL, MOVEMENT,
        //DIRECTION AND WHAT DIRECTIONS
        XY = new Rectangle(gheight, gheight);                                       // THAT THE MODEL CAN BE MOVED IN
        XY.setLocation((gWidth/2-2) * gheight, (gDimension/2) * gheight);     // THROUGHOUT THE GAMEPLAY
        charStats.add(XY);                                                        //
        //
        XY = new Rectangle(gheight, gheight);                                       //=============================
        XY.setLocation((gWidth/2-1) * gheight, (gDimension/2) * gheight);
        charStats.add(XY);
        motion = "NONE";
    }

    public void MOTION() {
        if(motion != "NONE") {                                                        //==============================
            Rectangle rectOne = charStats.get(0);                                   // BLOCK OF CODE USED TO DETERMINE
            Rectangle WASD = new Rectangle(App.dimension, App.dimension);             //THE MOVEMENT AND LOCATION
            if(motion == "UP") {                                                      // OF THE SNAKE MODEL ON THE X Y
                WASD.setLocation(rectOne.x, rectOne.y - App.dimension);            // COORDINATES OF THE GRID DEFINED
            }                                                                         // IN THE GAME APP WINDOW
            else if(motion == "DOWN") {                                               //
                WASD.setLocation(rectOne.x, rectOne.y + App.dimension);            //==============================
            }
            else if(motion == "LEFT") { WASD.setLocation(rectOne.x - App.dimension, rectOne.y);
            }
            else{ WASD.setLocation(rectOne.x + gDimension, rectOne.y); }
            charStats.add(0, WASD);
            charStats.remove(charStats.size()-1);
        }
    }

    public void eatBlock() {//FOOD EAT
        Rectangle rectOne =charStats.get(0);
        Rectangle X = new Rectangle(App.dimension, App.dimension);   //========================
        if(motion == "UP") {                                         //WHEN THE SNAKE ENCOUNTERS
            X.setLocation(rectOne.x, rectOne.y - App.dimension);  // A 'VITAMIN' BLOCK THE
        }                                                            // RECTANGLE MODEL WILL
        else if(motion == "DOWN") {                                 //
            X.setLocation(rectOne.x, rectOne.y + App.dimension);  //==========================
        }
        else if(motion =="RIGHT"){ X.setLocation(rectOne.x + App.dimension, rectOne.y);
        }
        else if(motion =="LEFT"){ X.setLocation(rectOne.x - gDimension, rectOne.y);
        }
        charStats.add(0, X);
    }
    public ArrayList<Rectangle> getCharStats() { return charStats; }
    public void setCharStats(ArrayList<Rectangle> charStats) { this.charStats = charStats; }
    public int xLoc() { return charStats.get(0).x; }
    public int yLoc() { return charStats.get(0).y ; }
    public String getMotion() { return motion; }
}
class    App implements KeyListener{
    public Vitamins vitamins;
    public Character character;
    public GFX GFX;
    public JFrame gameTab; //window var for the window game is in
    public static  int height = 40;  //height of the game tab all of these can be altered to fit the user needs
    public static  int dimension = 20;//dimension of the game tab
    public static  int width = 40;//width of the game tab
    public boolean windowStatus = true;
    public App() {
        gameTab = new JFrame();
        character = new Character();//INST Player class(the snake)
        vitamins = new Vitamins(character);// INST vitamin(food class
        GFX = new GFX(this); //inst GFX class
        gameTab.setTitle("CORONA SNAKE");//Title of the application tab and game
        gameTab.setSize(width * dimension + 2, height * dimension + dimension + 4);
        gameTab.setVisible(windowStatus);
        gameTab.add(GFX);//using inst of 2D graphics class to setup app
        gameTab.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void begin() {
        GFX.status = "ACTIVE";
    }

    public void refresh() {                                 //==================
        if(GFX.status == "ACTIVE") {                        //simple
            if(playerOnVitamin()) {                         //used to check
                character.eatBlock();                 //what the players
                vitamins.spawnPoint(character);       //snake is up to
            }                                               //if it has eaten
            else if(playerHitWall() || playerHitSelf()) {   //the vitamin,hit wall
                GFX.status = "END";                         // or self so can respond
            } else {                                        //appropriately, end game or grow functions basically...
                character.MOTION(); }                 //===================
        }
    }

    public boolean playerHitSelf() {                                                       //-----------
        for(int x = 1; x< character.getCharStats().size(); x++) {                      //as defined in var
            if(character.xLoc()== character.getCharStats().get(x).x &&       //used to just check if
                    character.yLoc()== character.getCharStats().get(x).y) {  //the rectangle of the player
                return true;                                                                //model has self collisions
            }                                                                               //however, isn't fully bug free!
        }                                                                                   //----------
        return false;
    }

    public boolean playerOnVitamin() { //vitamin is our food in this corona themed take  on snake
        if(character.xLoc()==vitamins.getxCoord()*dimension&& character.yLoc()==vitamins.getyCoord()*dimension) {
            return true;
        }return false; }

    public boolean playerHitWall() {
        if(character.xLoc()<0|| character.xLoc()>=width*dimension || character.yLoc()<0|| character.yLoc()>=height*dimension)
        { return true; }
        return false; }

    public void keyTyped(KeyEvent e) {	}
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();//so here we use keycode for keyevent objects to be able to read the input from
        if(GFX.status == "ACTIVE")// so when a key is pressed https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyEvent.html
        { if(keyCode==KeyEvent.VK_W && character.getMotion() != "DOWN") { character.W(); //
        }//  https://docs.oracle.com/javafx/2/api/javafx/scene/input/KeyCode.html#getKeyCode(java.lang.String)
        else if(keyCode == KeyEvent.VK_S && character.getMotion() !="UP") { character.A();
        } //implement the WASD up down left right methods for user input movement
        else if(keyCode == KeyEvent.VK_A && character.getMotion() !="LEFT") { character.S(); }
        else if(keyCode == KeyEvent.VK_D && character.getMotion() !="RIGHT") { character.D(); }
        else if(keyCode == KeyEvent.VK_UP && character.getMotion() !="UP") { character.W(); // ARROW KEY SUPPORT
        }else if(keyCode==KeyEvent.VK_DOWN && character.getMotion() != "DOWN") {
            character.S(); }
        else if(keyCode==KeyEvent.VK_LEFT && character.getMotion() != "LEFT") {
            character.A(); }
        else if(keyCode==KeyEvent.VK_RIGHT && character.getMotion() != "RIGHT") {
            character.S(); }
        } else { this.begin(); }
    }

    public void keyReleased(KeyEvent e) { }
    public Character getPlayer() { return character; }
    public void playerTemp(Character character) { this.character = character; }

    public Vitamins getVitamin() { return vitamins; }
    public void vitTemp(Vitamins vitamins) { this.vitamins = vitamins; }

    public JFrame getGameTab() { return gameTab; }
    public void setGameTab(JFrame gameTab) { this.gameTab = gameTab; }
}

class GFX extends JPanel implements ActionListener { //---------------------
    public String status;                                  //USED TO TRACK STATUS OF THE GAME
    public final App app;                                 //GENERIC APP INST VAR
    public final Vitamins eat;                            //VITAMINS/FOOD MECHANIC USED
    public final Character character;         //PLAYER MODEL INST


    public GFX(App appTemp) {
        Timer clock = new Timer(110, this); //affects refresh rate of the game
        clock.start();                                  //
        status = "START";                               //use status var to start
        app = appTemp;
        eat = appTemp.getVitamin();
        character = appTemp.getPlayer();
        this.addKeyListener(appTemp);                  // key listener functionality to 2d graphics system
        this.setFocusable(true);

    }

    public void paintComponent(java.awt.Graphics gfix) {
        super.paintComponent(gfix);
        Image corona = null; // never got this working but planned to implement a coronavirus image to the end screen
        Graphics2D gameGFX = (Graphics2D) gfix;//setting 2d graphics system in place AS imported

        gameGFX.setColor(Color.black);//sets the background of the grid to black permanently
        gameGFX.fillRect(0,0,App.width*App.dimension+5,App.height*App.dimension +5);//fills the grid
        if(status == "START") {
            gameGFX.setColor(Color.green);//sets the colour of the text on screen
            gameGFX.drawString("ENTER ANY KEY TO BEGIN", App.width/2 * App.dimension -100,
                    App.height/2 *App.dimension -50);//text is centered regardless of dimension changes too the game
            gameGFX.drawString("COLLECT VITAMINS AND SAVE YOURSELF FROM COVID", App.width/2 * App.dimension -100,
                    App.height/2 *App.dimension -80);
            gameGFX.drawString("WELCOME TOO CORONA SNAKE", App.width/2 * App.dimension -100,
                    App.height/2 *App.dimension -100);
            gameGFX.drawString("WASD MOVEMENT KEYS UP DOWN LEFT RIGHT", App.width/2 * App.dimension -100,
                    App.height/2 *App.dimension -40);
        }
        else if(status == "ACTIVE") {
            gameGFX.setColor(Color.white);//the color  is attached the the food cubes representing the vitamins
            gameGFX.fillRect(eat.getxCoord()*App.dimension, eat.getyCoord()*App.dimension,App.dimension,App.dimension);
            gameGFX.setColor(Color.green);//determines colour of the rect representing the player
            for(Rectangle PLAYERCOLOUR : character.getCharStats()) {//uses the playerchar class to set the colour of the snake to green
                gameGFX.fill(PLAYERCOLOUR);// sets green permanently
            }
        }
        else {
            gameGFX.setColor(Color.green);
            gameGFX.drawString("Vitamins collected : " + (character.getCharStats().size() - 2 ), App.width/2 * App.dimension - 30, App.height/2 * App.dimension - 15);
            gameGFX.drawString( "STAY AT HOME " + "PROTECT THE NHS", App.width /2 * App.dimension - 20, App.height / 2 * App.dimension - 5);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) { repaint();app.refresh(); }
}


class Vitamins {
    public int yCoord;
    public int xCoord;
    public Vitamins(Character character) {
        this.spawnPoint(character);
    }
    public void spawnPoint(Character character) {
        boolean foodOnPlayer = true; //conditional put in place to stop the vitamin  from spawning on top of the player
        while(foodOnPlayer) {
            foodOnPlayer = false;
            yCoord=(int)(Math.random() * App.height-1); //using the conditional foodonplayer,
            xCoord=(int)(Math.random() * App.width-1);// it's possible to use xy axis to place food without it spawning on top of the snake
            for(Rectangle T : character.getCharStats()){
                if(T.x==xCoord && T.y==yCoord) {
                    foodOnPlayer = true;
                }
            }
        }
    }
    public int getyCoord() { return yCoord; }
    public void setyCoord(int yCoord) { this.yCoord = yCoord; }
    public int getxCoord() { return xCoord; }
    public void setxCoord(int xCoord) { this.xCoord = xCoord; }
}
