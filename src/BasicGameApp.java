//Basic Game Application
//Version 2
// Basic Object, Image, Movement
// Astronaut moves to the right.
// Threaded

//K. Chun 8/2018

//*******************************************************************************
//Import Section
//Add Java libraries needed for the game
//import java.awt.Canvas;

//Graphics Libraries

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.*;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JPanel;


//*******************************************************************************
// Class Definition Section
//step 1: implement keyLister
//step 1: implement MouseLister

public class BasicGameApp implements Runnable, KeyListener, MouseListener {

    //Variable Definition Section
    //Declare the variables used in the program
    //You can set their initial values too

    //Sets the width and height of the program window
    final int WIDTH = 1000;
    final int HEIGHT = 700;

    //Declare the variables needed for the graphics
    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;

    public BufferStrategy bufferStrategy;
    public Image cowboyPic;
    public Image zombiePic;
    public Image backGroundPic;
    public Image curePic;
    public Image bulletPic;
    public Image bossPic;
    public Image winPic;
    public Image losePic;


    //Declare the objects used in the program
    //These are things that are made up of more than one variable type
    private Cowboy cowboy;
    private Zombie zombie1;
    private Zombie zombie2;
    public Zombie[] zombies;
    public Cure cure;
    public Bullet bullet;
    private Boss boss;
    private boolean bossSpawned = false;
    private boolean secondWaveSpawned = false;
    private boolean started = false;
    private Win win;
    private Lose lose;


    // Main method definition
    // This is the code that runs first and automatically
    public static void main(String[] args) {
        BasicGameApp ex = new BasicGameApp();   //creates a new instance of the game
        new Thread(ex).start();                 //creates a threads & starts up the code in the run( ) method
    }


    // Constructor Method
    // This has the same name as the class
    // This section is the setup portion of the program
    // Initialize your variables and construct your program objects here.
    public BasicGameApp() {

        setUpGraphics();

        //random structure
        //(int)(Math.random() *range) + start
        //range is 1-10
        int randx = (int) (Math.random() * 10) + 1;

        randx = (int) (Math.random() * 999) + 1;

        int randy = (int) (Math.random() * 699) + 1;
        randy = (int) (Math.random() * 699) + 1;

        //variable and objects
        //create (construct) the objects needed for the game and load up
        cowboyPic = Toolkit.getDefaultToolkit().getImage("Cowboy.jpg");
        zombiePic = Toolkit.getDefaultToolkit().getImage("Zombie.jpg");
        backGroundPic = Toolkit.getDefaultToolkit().getImage("City.jpg");//load the picture
        curePic = Toolkit.getDefaultToolkit().getImage("Cure.jpeg");
        bulletPic = Toolkit.getDefaultToolkit().getImage("Bullet.jpg");
        bossPic = Toolkit.getDefaultToolkit().getImage("Boss.jpg");
        winPic = Toolkit.getDefaultToolkit().getImage("Win2.jpg");
        losePic = Toolkit.getDefaultToolkit().getImage("Lose2.jpg");
        cowboy = new Cowboy(WIDTH / 2, HEIGHT / 2);
        zombie1 = new Zombie(100, 300);
        zombie1.dx = -zombie1.dx;
        zombie2 = new Zombie(250, 250);
        zombies = new Zombie[12];
        boss = new Boss(100, 300);
        cure = new Cure(100, 100);
        bullet = new Bullet(100, 100);
        win = new Win(0,0);
        lose = new Lose(0,0);
        for (int q = 0; q < zombies.length; q++) {
            zombies[q] = new Zombie((int) (Math.random() * 1000), (int) (Math.random() * 100));


        }


    }// BasicGameApp()


//*******************************************************************************
//User Method Section
//
// put your code to do things here.

    // main thread
    // this is the code that plays the game after you set things up
    public void run() {

        //for the moment we will loop things forever.
        while (true) {

            moveThings();  //move all the game objects
            render();  // paint the graphics
            pause(20); // sleep for 10 ms
        }
    }


    public void moveThings() {
        //calls the move( ) code in the objects
        if (started==true) {
            cowboy.move();
            zombie1.move();
            zombie2.move();
            bullet.move();
            boss.move();
            crashing();
            for (int i = 0; i < zombies.length; i++) {
                zombies[i].move();

            }


        }

    }

    public void crashing() {
        //check to see if things crash into eachother


        if (zombie1.hitbox.intersects(zombie2.hitbox)) {
            //System.out.println("no intersection");
            zombie1.isCrashing = false;

        }
        //kills cowboy :(
        if (cowboy.hitbox.intersects(zombie1.hitbox) && zombie1.isAlive == true) {
            cowboy.isAlive = false;

        }
        //kills cowboy
        if (cowboy.hitbox.intersects(zombie2.hitbox) && zombie2.isAlive == true) {
            cowboy.isAlive = false;

        }
        //cure kills zombie
        if (cowboy.hitbox.intersects(cure.hitbox) && cowboy.isAlive == true) {
            zombie1.isAlive = false;
        }
        //kills cowboy
        for (int x = 0; x < zombies.length; x++) {
            if (zombies[x].hitbox.intersects(cowboy.hitbox) && zombies[x].isAlive == true) {
                cowboy.isAlive = false;
                System.out.println("Crashing");
            }


        }
        //bullet kills zombie
        if (bullet.hitbox.intersects(zombie1.hitbox) && bullet.isAlive == true) {
            zombie1.isAlive = false;
        }
        //bullet kills zombie
        if (bullet.hitbox.intersects(zombie2.hitbox) && bullet.isAlive == true) {
            zombie2.isAlive = false;
        }
        //

        //gives a chance to kill the boss so he not unkillable
        if (bullet.isAlive && boss.isAlive && bullet.hitbox.intersects(boss.hitbox)) {
            boss.health -= 5;   // bullet does 5 damage

        }
        //kills boss
        if (boss.health <= 0) {
            boss.health = 0;
            boss.isAlive = false;
        }
        //kills cowboy
        if (boss.hitbox.intersects(cowboy.hitbox) && boss.isAlive == true) {
            cowboy.isAlive = false;
        }
        boolean allZombiesDead = true;
        for (int x = 0; x < zombies.length; x++) {
            if (zombies[x].isAlive == true) {
                allZombiesDead = false;
            }
        }
        // SPAWN SECOND WAVE
        if (!secondWaveSpawned && zombie1.isAlive == false && zombie2.isAlive == false && allZombiesDead) {

            for (int i = 0; i < zombies.length; i++) {
                zombies[i] = new Zombie((int) (Math.random() * 1000), (int) (Math.random() * 700));
            }

            secondWaveSpawned = true;
        }
        //spawns boss
        if (!bossSpawned && zombie1.isAlive == false && zombie2.isAlive == false && allZombiesDead == true) {
            boss.isAlive = true;
            bossSpawned = true;
        }
        //kills bullet
        if (cowboy.isAlive == false) {
            bullet.isAlive = false;
        }
        for (int x=0; x<zombies.length; x++) {
            //makes sure cure works
            if (cowboy.hitbox.intersects(cure.hitbox) && cowboy.isAlive == true) {
                boss.isAlive = false;
                zombie1.isAlive = false;
                zombie2.isAlive = false;
                zombies[x].isAlive=false;
            }
        }
        //bullet kills array zombies
        for (int i = 0; i < zombies.length; i++) {
            if (bullet.hitbox.intersects(zombies[i].hitbox) && bullet.isAlive==true) {
                zombies[i].isAlive = false;
            }
        }//allows the win screen to appear need to fix so that the array zombies are included
        for (int x=0; x<zombies.length; x++) {
            if (zombie1.isAlive == false && zombie2.isAlive == false && boss.isAlive == false && cowboy.isAlive == true && bossSpawned == true && zombies[x].isAlive==false) {

                win.isAlive = true;
            }
        }
        //makes so just win screen
        if (win.isAlive==true){
            bullet.isAlive=false;
            cure.isAlive=false;
        }
        //makes lose screen appear
        if (cowboy.isAlive==false){
            lose.isAlive=true;
            cure.isAlive=false;
        }






    }
    //make sure the game can restart when you lose method just remakes everything
    public void resetGame() {
        //all code below just makes everything new so that the game can restart after
        cowboy = new Cowboy(700, 610);
        zombie1 = new Zombie(100, 100);
        zombie2 = new Zombie(250, 100);
        for (int i = 0; i < zombies.length; i++) {
            zombies[i] = new Zombie((int)(Math.random() * 1000), (int)(Math.random() * 400));
        }
        boss = new Boss(100, 300);
        cure = new Cure(100, 100);
        bullet = new Bullet(100, 100);
        win.isAlive = false;
        lose.isAlive = false;
        bossSpawned = false;
        secondWaveSpawned = false;
        started = true;
        cowboy.isAlive=true;
        bullet.isAlive=true;
    }

    //Pauses or sleeps the computer for the amount specified in milliseconds
    public void pause(int time) {
        //sleep
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
    }

    //Graphics setup method
    private void setUpGraphics() {
        frame = new JFrame("Application Template");   //Create the program window or frame.  Names it.

        panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
        panel.setLayout(null);   //set the layout

        // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
        // and trap input events (Mouse and Keyboard events)
        canvas = new Canvas();

        //step 2: set canvas as the key listener
        canvas.addKeyListener(this);
        //step 2: set canvas the mouse listner
        canvas.addMouseListener(this);

        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);  // adds the canvas to the panel.

        // frame operations
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
        frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
        frame.setResizable(false);   //makes it so the frame cannot be resized
        frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!

        // sets up things so the screen displays images nicely.
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
        System.out.println("DONE graphic setup");

    }


    //paints things on the screen using bufferStrategy
    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.drawImage(backGroundPic, 0, 0, WIDTH, HEIGHT, null);

        //draw the image of the cowboy if hes alive
        if (cowboy.isAlive == true) {
            g.drawImage(cowboyPic, cowboy.xpos, cowboy.ypos, cowboy.width, cowboy.height, null);
            g.drawRect(cowboy.hitbox.x, cowboy.hitbox.y, cowboy.hitbox.width, cowboy.hitbox.height);
        }
        //all stuff like this makes sure things can die since they are only drawn if they are alive
        if (zombie1.isAlive == true) {
            g.drawImage(zombiePic, zombie1.xpos, zombie1.ypos, zombie1.width, zombie1.height, null);
            g.drawRect(zombie1.hitbox.x, zombie1.hitbox.y, zombie1.hitbox.width, zombie1.hitbox.height);
        }
        if (zombie2.isAlive == true) {
            g.drawImage(zombiePic, zombie2.xpos, zombie2.ypos, zombie2.width, zombie2.height, null);
            g.drawRect(zombie2.hitbox.x, zombie2.hitbox.y, zombie2.hitbox.width, zombie2.hitbox.height);
        }
        //same things here just with the array
        for (int z = 0; z < zombies.length; z++) {
            if (zombies[z].isAlive == true) {
                g.drawImage(zombiePic, zombies[z].xpos, zombies[z].ypos, zombies[z].width, zombies[z].height, null);
            }

        }
        if (win.isAlive==true){
            g.drawImage(winPic, win.xpos, win.ypos, win.width, win.height, null);

        }
        if (lose.isAlive==true){
            g.drawImage(losePic, lose.xpos, lose.ypos, lose.width, lose.height, null);

        }
        if (cure.isAlive==true){
        g.drawImage(curePic, cure.xpos, cure.ypos, cure.width, cure.height, null);}
        if (bullet.isAlive == true) {
            g.drawImage(bulletPic, bullet.xpos, bullet.ypos, bullet.width, bullet.height, null);
        }
        if (boss.isAlive) {
            //slightly different since this also draws a health bar

            g.drawImage(bossPic, boss.xpos, boss.ypos, boss.width, boss.height, null);
            //sets the health car width and height and were the health bar wll be
            int hbarWidth = 200;
            int hbarHeight = 20;
            int barX = boss.xpos + 100;
            int barY = boss.ypos - 30;
            //sets color
            g.setColor(Color.RED);
            g.fillRect(barX, barY, hbarWidth, hbarHeight);

            g.setColor(Color.GREEN);
            g.fillRect(barX, barY, (boss.health * hbarWidth) / boss.maxHealth, hbarHeight);

            g.setColor(Color.BLACK);
            g.drawRect(barX, barY, hbarWidth, hbarHeight);

        }
        //adding a start button
        if (started == false){
            g.setColor(Color.green);
            g.fillRect(400, 250, 200, 200); // green square
            g.setColor(Color.black);
            g.drawString("Click Here", 445,350);
        }
        //makes so that the same button appears when you lose though it is technically a different button
        if (lose.isAlive==true){
            g.setColor(Color.green);
            g.fillRect(400, 500, 200, 50); // green square
            g.setColor(Color.black);
            g.drawString("Click Here", 445,525);

        }


        g.dispose();

        bufferStrategy.show();
    }
    //step 3: add key listener methods

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //all below allow so that when you press arrow for bullet and wasd for cowboy they move that direction
        System.out.println("key typed " + e.getKeyCode());
        if (e.getKeyCode() == 38) {
            System.out.println("pressed up arrow");
            bullet.dy = -7;
        }
        if (e.getKeyCode() == 40) {
            System.out.println("pressed down arrow");
            bullet.dy = 7;
        }
        if (e.getKeyCode() == 37) {
            System.out.println("pressed left arrow");
            bullet.dx = -10;
        }
        if (e.getKeyCode() == 39) {
            System.out.println("pressed right arrow");
            bullet.dx= 10;
        }
        if (e.getKeyCode() == 87){
            cowboy.dy=-10;
        }
        if (e.getKeyCode() == 83){
            cowboy.dy=10;
        }
        if (e.getKeyCode() == 65){
            cowboy.dx=-10;
        }
        if (e.getKeyCode() == 68){
            cowboy.dx=10;
        }


    }

    @Override
    public void keyReleased(KeyEvent e) {

        //all below allow you to control bullet with arrow keys and cowboy with wasd and when you release they stop moving
        System.out.println("key typed " + e.getKeyCode());
        if (e.getKeyCode() == 38) {
            System.out.println(" not pressed up arrow");

            bullet.dy = 0;

        }
        System.out.println("key typed " + e.getKeyCode());
        if (e.getKeyCode() == 40) {
            System.out.println(" not pressed down arrow");

            bullet.dy = 0;
        }
        System.out.println("key typed " + e.getKeyCode());
        if (e.getKeyCode() == 37) {
            System.out.println(" not pressed down arrow");

            bullet.dx = 0;
        }
        System.out.println("key typed " + e.getKeyCode());
        if (e.getKeyCode() == 39) {
            System.out.println(" not pressed down arrow");

            bullet.dx = 0;
        }
        if (e.getKeyCode() == 87){
            cowboy.dy=0;
        }
        if (e.getKeyCode() == 83){
            cowboy.dy=0;
        }
        if (e.getKeyCode() == 65){
            cowboy.dx=0;
        }
        if (e.getKeyCode() == 68){
            cowboy.dx=0;
        }
        //cheats n
        //****this cheat no longer works because of win screen
        if (e.getKeyCode() == 78){
            cowboy.isAlive=true;
            bullet.isAlive=true;
        }

        //cheats m
        if (e.getKeyCode() == 77){
            boss.isAlive=false;
        }

    }

    // step 3 : add methods
    @Override
    public void mouseClicked(MouseEvent e) {


    }

    @Override
    public void mousePressed(MouseEvent e) {
        //when you press the mouse the bullet goes there a little easier than using arrow keys though those are still a choice
        System.out.println(e.getPoint());
        if (cowboy.isAlive == true){
        bullet.isAlive = true;
        bullet.xpos = e.getX();
        bullet.ypos = e.getY();}

        //if the game hasn't started see if rect has been clicked
        if (started == false){
            if (e.getX() > 400 && e.getX() < 600 &&
                    e.getY() > 250 && e.getY() < 450){
                started=true;
            }
        }
        //making the reset game work when they click rect game starts
        if (lose.isAlive == true) {
            if (e.getX() > 400 && e.getX() < 600 &&
                    e.getY() > 100 && e.getY() < 600) {
                resetGame();
            }
        }
        else if(started==false){
            started=true;
        }




    }

    @Override
    public void mouseReleased(MouseEvent e) {


    }

    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println("entered!!");


    }

    @Override
    public void mouseExited(MouseEvent e) {


    }
}



