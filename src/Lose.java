import java.awt.*;
public class Lose {


        public String name;                //holds the name of the hero
        public int xpos;                //the x position
        public int ypos;                //the y position
        public int width;
        public int height;
        public boolean isAlive;//a boolean to denote if the hero is alive or dead.



        // METHOD DEFINITION SECTION

        // Constructor Definition
        // A constructor builds the object when called and sets variable values.


        //This is a SECOND constructor that takes 3 parameters.  This allows us to specify the hero's name and position when we build it.
        // if you put in a String, an int and an int the program will use this constructor instead of the one above.
        public Lose(int pXpos, int pYpos) {
            xpos = pXpos;
            ypos = pYpos;
            width = 1000;
            height = 700;
            isAlive = false;


        }
    }





