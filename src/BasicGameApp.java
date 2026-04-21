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
        cowboy = new Cowboy(WIDTH / 2, HEIGHT / 2);
        zombie1 = new Zombie(100, 300);
        zombie1.dx = -zombie1.dx;
        zombie2 = new Zombie(250, 250);
        zombies = new Zombie[12];
        boss = new Boss(100, 300);
        cure = new Cure(100, 100);
        bullet = new Bullet(100, 100);
        for (int q = 0; q < zombies.length; q++) {
            zombies[q] = new Zombie((int) (Math.random() * 1000), (int) (Math.random() * 100));


        }


        //todo: make a variable randy that generated a random number between 1-699
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

    public void crashing() {
        //check to see if my astros crash into eachother


        if (zombie1.hitbox.intersects(zombie2.hitbox)) {
            //System.out.println("no intersection");
            zombie1.isCrashing = false;

        }
        if (cowboy.hitbox.intersects(zombie1.hitbox) && zombie1.isAlive == true) {
            cowboy.isAlive = false;

        }
        if (cowboy.hitbox.intersects(zombie2.hitbox) && zombie2.isAlive == true) {
            cowboy.isAlive = false;

        }

        if (cowboy.hitbox.intersects(cure.hitbox) && cowboy.isAlive == true) {
            zombie1.isAlive = false;
        }

        for (int x = 0; x < zombies.length; x++) {
            if (zombies[x].hitbox.intersects(cowboy.hitbox) && zombies[x].isAlive == true) {
                cowboy.isAlive = false;
                System.out.println("Crashing");
            }


        }
        if (bullet.hitbox.intersects(zombie1.hitbox) && bullet.isAlive == true) {
            zombie1.isAlive = false;
        }
        if (bullet.hitbox.intersects(zombie2.hitbox) && bullet.isAlive == true) {
            zombie2.isAlive = false;
        }
        for (int x = 0; x < zombies.length; x++) {
            if (zombies[x].isAlive == true && bullet.isAlive == true && bullet.hitbox.intersects(zombies[x].hitbox)) {
                zombies[x].isAlive = false;
                bullet.isAlive = false;   // bullet disappears after one hit
                // stop after killing one zombie
            }
        }

        if (bullet.isAlive && boss.isAlive && bullet.hitbox.intersects(boss.hitbox)) {
            boss.health -= 5;   // bullet does 5 damage
            bullet.isAlive = false;
        }

        if (boss.health <=0) {
            boss.health=0;
            boss.isAlive = false;
        }
        if (boss.hitbox.intersects(cowboy.hitbox)&& boss.isAlive==true){
            cowboy.isAlive=false;
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
                zombies[i] = new Zombie((int)(Math.random()*1000), (int)(Math.random()*700));
            }

            secondWaveSpawned = true;
        }
        if (!bossSpawned && zombie1.isAlive == false && zombie2.isAlive == false && allZombiesDead == true) {
            boss.isAlive = true;
            bossSpawned = true;
        }
    if (cowboy.isAlive==false){
        bullet.isAlive=false;
    }


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

        //draw the image of the astronaut
        if (cowboy.isAlive == true) {
            g.drawImage(cowboyPic, cowboy.xpos, cowboy.ypos, cowboy.width, cowboy.height, null);
            g.drawRect(cowboy.hitbox.x, cowboy.hitbox.y, cowboy.hitbox.width, cowboy.hitbox.height);
        }

        if (zombie1.isAlive == true) {
            g.drawImage(zombiePic, zombie1.xpos, zombie1.ypos, zombie1.width, zombie1.height, null);
            g.drawRect(zombie1.hitbox.x, zombie1.hitbox.y, zombie1.hitbox.width, zombie1.hitbox.height);
        }
        if (zombie2.isAlive == true) {
            g.drawImage(zombiePic, zombie2.xpos, zombie2.ypos, zombie2.width, zombie2.height, null);
            g.drawRect(zombie2.hitbox.x, zombie2.hitbox.y, zombie2.hitbox.width, zombie2.hitbox.height);
        }
        for (int z = 0; z < zombies.length; z++) {
            if (zombies[z].isAlive == true) {
                g.drawImage(zombiePic, zombies[z].xpos, zombies[z].ypos, zombies[z].width, zombies[z].height, null);
            }

        }
        g.drawImage(curePic, cure.xpos, cure.ypos, cure.width, cure.height, null);
        if (bullet.isAlive == true) {
            g.drawImage(bulletPic, bullet.xpos, bullet.ypos, bullet.width, bullet.height, null);
        }
        if (boss.isAlive) {

            g.drawImage(bossPic, boss.xpos, boss.ypos, boss.width, boss.height, null);

            int hbarWidth = 200;
            int hbarHeight = 20;
            int barX = boss.xpos + 100;
            int barY = boss.ypos - 30;

            g.setColor(Color.RED);
            g.fillRect(barX, barY, hbarWidth, hbarHeight);

            g.setColor(Color.GREEN);
            g.fillRect(barX, barY, (boss.health * hbarWidth) / boss.maxHealth, hbarHeight);

            g.setColor(Color.BLACK);
            g.drawRect(barX, barY, hbarWidth, hbarHeight);

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
        System.out.println("key typed " + e.getKeyCode());
        if (e.getKeyCode() == 38) {
            System.out.println("pressed up arrow");
            // astro.ypos =astro.ypos-50;
            cowboy.dy = -7;
        }
        if (e.getKeyCode() == 40) {
            System.out.println("pressed down arrow");
            // .ypos =.ypos-50;
            cowboy.dy = 7;
        }
        if (e.getKeyCode() == 37) {
            System.out.println("pressed left arrow");
            // co.ypos =astro.ypos-50;
            cowboy.dx = -10;
        }
        if (e.getKeyCode() == 39) {
            System.out.println("pressed right arrow");
            // astro.ypos =astro.ypos-50;
            cowboy.dx = 10;
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
        System.out.println("key typed " + e.getKeyCode());
        if (e.getKeyCode() == 38) {
            System.out.println(" not pressed up arrow");
            // astro.ypos =astro.ypos-50;
            cowboy.dy = 0;

        }
        System.out.println("key typed " + e.getKeyCode());
        if (e.getKeyCode() == 40) {
            System.out.println(" not pressed down arrow");
            // astro.ypos =astro.ypos-50;
            cowboy.dy = 0;
        }
        System.out.println("key typed " + e.getKeyCode());
        if (e.getKeyCode() == 37) {
            System.out.println(" not pressed down arrow");
            // astro.ypos =astro.ypos-50;
            cowboy.dx = 0;
        }
        System.out.println("key typed " + e.getKeyCode());
        if (e.getKeyCode() == 39) {
            System.out.println(" not pressed down arrow");
            // astro.ypos =astro.ypos-50;
            cowboy.dx = 0;
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

    }

    // step 3 : add methods
    @Override
    public void mouseClicked(MouseEvent e) {


    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println(e.getPoint());
        if (cowboy.isAlive == true){
        bullet.isAlive = true;
        bullet.xpos = e.getX();
        bullet.ypos = e.getY();}


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



