// ------------------------------------
// Author: Sam Starke
// Date: March 28, 2022
// Description: Shell.java
// ------------------------------------

import java.awt.Graphics;
import java.awt.Image;
// import java.awt.Color;

public class Shell extends Sprite {

    // Shell image arrays.
    static Image image[] = null;
    Image[][] shell;
    Image[] smoke;

    // Initialize frame animation variables for things.
    int shellFrame = 0;
    int smokeFrame = 0;

    // Amount of frames and speed.
    final int frames = 4;
    final int speed = 12;

    Shell(int x, int y, int t) {
        // Sets type of shell variable to wherever the selection is set.
        shellType = View.selected;

        // Width and height of shell smaller than the actual image to allow it to not contact with the corner of bricks since they are same size.
        w = 28;
        h = 28;

        this.x = x;
        this.y = y;


        // Initialize the different shell images.
        shell = new Image[10][4];
        if (image == null) {
            for (int type = 0; type < 10; type++) {
                for (int frame = 0; frame < 4; frame++) {
                    shell[type][frame] = View.loadImage("sprites/shells/" + type + "/" + frame + ".png");
                }
            }
        }
        // Initialize the different smoke animation frame images.
        smoke = new Image[6];
        if (image == null) {
            for (int i = 0; i < 4; i++) {
                smoke[i] = View.loadImage("sprites/misc/smoke/" + i + ".png");
            }
        }
    }

    // Copy constructor for strictly unmarshaling.
    Shell(Json ob) {
        x = (int) ob.getLong("shellX");
        y = (int) ob.getLong("shellY");
        shellType = (int) ob.getLong("shellType");
        
        w = 28;
        h = 28;

        // Initalize the different brick images in the copy constructor as well.
        shell = new Image[10][4];
        if (image == null) {
            for (int type = 0; type < 10; type++) {
                for (int frame = 0; frame < 4; frame++) {
                    shell[type][frame] = View.loadImage("sprites/shells/" + type + "/" + frame + ".png");
                }
            }
        }
        // Initialize the different smoke animation frame images in the copy constructor as well.
        smoke = new Image[6];
        if (image == null) {
            for (int i = 0; i < 4; i++) {
                smoke[i] = View.loadImage("sprites/misc/smoke/" + i + ".png");
            }
        }
    }

    // Move function similar to the one in Mario. Except this one doesn't use doubles since it only goes in the 4 ULDR directions.
    public void move(int x, int y) {
        if (isMoving) {
            this.x -= x;
            this.y -= y;
        }
    }

    // Public function that actually moves the shell and animates it.
    void shellDirection() {
        switch (shellDir) {
            case 0 -> move(0, speed);
            case 1 -> move(0, -speed);
            case 2 -> move(speed, 0);
            case 3 -> move(-speed, 0);
        }
        if (isMoving) {
            shellFrame = (shellFrame + 1) % frames;
        }
    }

    // Marshals the shell's position and type.
    public Json marshal() {
        Json ob = Json.newObject();
        ob.add("shellX", x);
        ob.add("shellY", y);
        ob.add("shellType", shellType);
        return ob;
    }

    // Draw yourself method for if it's either broken, moving, or not.
    public void drawYourself(Graphics g) {
        if (!broken && !shellDestroy) {
            if (isMoving) {
                g.drawImage(shell[shellType][shellFrame], x - View.scrollPosX, y - View.scrollPosY, null);
            } else {
                g.drawImage(shell[shellType][0], x - View.scrollPosX, y - View.scrollPosY, null);
            }
        }
        else if (broken) {
            g.drawImage(smoke[smokeFrame], x - View.scrollPosX, y - View.scrollPosY, null);
        }
    }

    // Update function that updates the shell direction then checks if it's broken then animate the smoke.
    public void update() {
        shellDirection();
        if (broken) {
            if (smokeFrame > 4) {
                smokeFrame = 0;
                shellDestroy = true;
                broken = false;
            }
            smokeFrame++;
        }
    } 
    
    // To string method to print out Shell's coordinates and size when called.
    @Override
    public String toString() {
        return "Shell (x, y): (" + x + ", " + y + ") Shell (w, h): (" + w + ", " + h + ") ";
    }

    @Override
    public boolean isShell() {
        return true;
    }
}
