import java.awt.*;

/**
 * Created by chales on 11/6/2017.
 */
public class Cowboy {


    //VARIABLE DECLARATION SECTION
    //Here's where you state which variables you are going to use.
    public String name;                //holds the name of the hero
    public int xpos;                //the x position
    public int ypos;                //the y position
    public int dx;                    //the speed of the hero in the x direction
    public int dy;                    //the speed of the hero in the y direction
    public int width;
    public int height;
    public boolean isAlive; //a boolean to denote if the hero is alive or dead.
    public Rectangle hitbox;


    // METHOD DEFINITION SECTION

    // Constructor Definition
    // A constructor builds the object when called and sets variable values.


    //This is a SECOND constructor that takes 3 parameters.  This allows us to specify the hero's name and position when we build it.
    // if you put in a String, an int and an int the program will use this constructor instead of the one above.
    public Cowboy(int pXpos, int pYpos) {
        xpos = pXpos;
        ypos = pYpos;
        dx =0;
        dy =0;
        width = 60;
        height = 60;
        isAlive = true;
        hitbox= new Rectangle(xpos, ypos, width, height);

    } // constructor

    //The move method.  Everytime this is run (or "called") the hero's x position and y position change by dx and dy
    public void move() {
        if(xpos >= 1050-width){
            xpos=50;


        }
       if (xpos <= -50){
           xpos=950;

       }
       if (ypos >=750-height){
           ypos=50;


       }
        if (ypos <= -50){
            ypos=650;

        }
        xpos = xpos + dx;
        ypos = ypos + dy;
        hitbox= new Rectangle(xpos, ypos, width, height);

    }
}






