import java.awt.*;

public class Astroid {
    public String name;                //holds the name of the hero
    public int xpos;                //the x position
    public int ypos;                //the y position
    public int dx;                    //the speed of the hero in the x direction
    public int dy;                    //the speed of the hero in the y direction
    public int width;
    public int height;
    public boolean isAlive;//a boolean to denote if the hero is alive or dead.
    public Rectangle hitbox;
    public boolean isCrashing;


    // METHOD DEFINITION SECTION

    // Constructor Definition
    // A constructor builds the object when called and sets variable values.


    //This is a SECOND constructor that takes 3 parameters.  This allows us to specify the hero's name and position when we build it.
    // if you put in a String, an int and an int the program will use this constructor instead of the one above.
    public Astroid(int pXpos, int pYpos) {
        xpos = pXpos;
        ypos = pYpos;
        dx =10;
        dy =0;
        width = 85;
        height = 85;
        isAlive = false;
        hitbox= new Rectangle(xpos, ypos, width, height);
        isCrashing = false;

    } // constructor

    //The move method.  Everytime this is run (or "called") the hero's x position and y position change by dx and dy
    public void move() {
        if(xpos >=950-width){
            xpos=50;

        }

        if(xpos <=0){
            xpos=850;

        }
        if(ypos <=0){
           ypos=1000;
        }
        if(ypos >=700-height){
            ypos = 1;

        }

        xpos = xpos + dx;
        ypos = ypos + dy;
        hitbox= new Rectangle(xpos, ypos, width, height);

    }
}

