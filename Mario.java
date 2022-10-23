// ------------------------------------
// Author: Sam Starke
// Date: February 27, 2022
// Description: Mario.java
// ------------------------------------

import java.awt.Image;
import java.awt.Graphics;

public class Mario extends Sprite {

    Model model;

    // Previous position variables.
    int prevX, prevY;

    // Double variable for speed.
    double speed = 8;

    // Custom variables to fix Mario's drawn position and hitbox.
    int marioDrawX = 182;
    int marioDrawY = 102;

    // All image arrays for Mario.
    static Image image = null;
    // Still image array.
    Image[] marioStill;

    // Up, down, left, right image arrays.
    Image[] marioU;
    Image[] marioD;
    Image[] marioL;
    Image[] marioR;

    // Diagonal image arrays.
    Image[] marioUL;
    Image[] marioUR;
    Image[] marioDL;
    Image[] marioDR;

    // Image for shadow to emulate 2.5D.
    Image shadow;
    Image kick;

    // Changes depending on the key press.
    int frame = 0;
    int kickFrame = 0;
    public int marioDirection = 1;

    // Mario direction variable hardcoded as down.
    boolean isRunning = false;

    // Mario constructor.
    public Mario(Model m) {

        model = m;

        x = 200;
        y = 350;

        w = 24;
        h = 32;

        // All for loops to load in Mario images.
        if (image == null) {
            marioStill = new Image[8];
            marioStill[0] = View.loadImage("sprites/S/1.png");
            for (int i = 0; i < marioStill.length; i++) {
                marioStill[i] = View.loadImage("sprites/S/" + i + ".png");
            }

            marioU = new Image[8];
            for (int i = 0; i < marioU.length; i++) {
                marioU[i] = View.loadImage("sprites/U/" + i + ".png");
            }

            marioD = new Image[8];
            for (int i = 0; i < marioD.length; i++) {
                marioD[i] = View.loadImage("sprites/D/" + i + ".png");
            }

            marioL = new Image[8];
            for (int i = 0; i < marioL.length; i++) {
                marioL[i] = View.loadImage("sprites/L/" + i + ".png");
            }

            marioR = new Image[8];
            for (int i = 0; i < marioR.length; i++) {
                marioR[i] = View.loadImage("sprites/R/" + i + ".png");
            }

            marioUL = new Image[8];
            for (int i = 0; i < marioUL.length; i++) {
                marioUL[i] = View.loadImage("sprites/UL/" + i + ".png");
            }

            marioUR = new Image[8];
            for (int i = 0; i < marioUR.length; i++) {
                marioUR[i] = View.loadImage("sprites/UR/" + i + ".png");
            }

            marioDL = new Image[8];
            for (int i = 0; i < marioDL.length; i++) {
                marioDL[i] = View.loadImage("sprites/DL/" + i + ".png");
            }

            marioDR = new Image[8];
            for (int i = 0; i < marioDR.length; i++) {
                marioDR[i] = View.loadImage("sprites/DR/" + i + ".png");
            }
            shadow = View.loadImage("sprites/misc/shadow.png");
            kick = View.loadImage("sprites/misc/kick.png");
        }
    }

    public Json marshal() {
        Json mario = Json.newObject();
        mario.add("marioX", x);
        mario.add("marioY", y);
        return mario;
    }

    // Method to save the previous position of Mario.
    public void savePrev() {
        prevX = x;
        prevY = y;
    }
    
    // Method to move Mario wherever.
    public void move(double x, double y) {
        this.x -= x;
        this.y -= y;
    }

    // Drawing all Mario graphics depending on the direction Mario is facing.
    public void drawYourself(Graphics g) {
        if (isRunning) {
            switch (marioDirection) {
                // Each of these draw the image depending on the direction of Mario as the
                // direction types were listed in Controller. The key is a variable passed
                // through Mario from Controller that increments the animation frame. Mario is
                // being drawn at his custom Draw variables rather than his actual coordinates.
                // Read more about his Draw variables below in the update function.
                case 0 -> g.drawImage(marioU[frame], marioDrawX, marioDrawY, null);
                case 1 -> g.drawImage(marioD[frame], marioDrawX, marioDrawY, null);
                case 2 -> g.drawImage(marioL[frame], marioDrawX, marioDrawY, null);
                case 3 -> g.drawImage(marioR[frame], marioDrawX, marioDrawY, null);
                case 4 -> g.drawImage(marioUL[frame], marioDrawX, marioDrawY, null);
                case 5 -> g.drawImage(marioUR[frame], marioDrawX, marioDrawY, null);
                case 6 -> g.drawImage(marioDL[frame], marioDrawX, marioDrawY, null);
                case 7 -> g.drawImage(marioDR[frame], marioDrawX, marioDrawY, null);
            }
        } else {
            // If he is not moving draw the still image.
            g.drawImage(marioStill[marioDirection], marioDrawX, marioDrawY, null);
        }
        // Draw the shadow underneath Mario at a hardcoded position.
        g.drawImage(shadow, marioDrawX + 5, marioDrawY + 56, null);
        if (kicked) {
            g.drawImage(kick, x - View.scrollPosX, y - View.scrollPosY, null);
        }
    }

    // Continuously update Mario's draw. This is to align him within his hitbox
    // perfectly.
    public void update() {
        if (kicked) {
            if (kickFrame > 0) {
                kickFrame = 0;
                kicked = false;
            }
            kickFrame++;
        }
        marioDrawX = x - View.scrollPosX - 18;
        marioDrawY = y - View.scrollPosY - 38;
    }

    void exitBrick(Sprite sprite) {
        // Mario left to brick right.
        if (x <= sprite.x + sprite.w && prevX >= sprite.x + sprite.w) {
            if (sprite.isShell()) {
                sprite.setDirection(2);
                x = prevX;
            }
            x = prevX;
        }
        // Mario right to brick left.
        else if (x + w >= sprite.x && prevX + w <= sprite.x) {
            if (sprite.isShell()) {
                sprite.setDirection(3);
                x = prevX;
            }
            x = prevX;
        }
        // Mario bottom to brick top.
        else if (y + h >= sprite.y && prevY + h <= sprite.y) {
            if (sprite.isShell()) {
                sprite.setDirection(1);
                y = prevY;
            }
            y = prevY;
        }
        // Mario top to brick bottom.
        else if (y <= sprite.y + sprite.h && prevY >= sprite.y + sprite.h) {
            if (sprite.isShell()) {
                sprite.setDirection(0);
                y = prevY;
            }
            y = prevY;
        }
    }

    // To string method to print out Mario's coordinates and size when called.
    @Override
    public String toString() {
        return "Mario (x, y): (" + x + ", " + y + ") Mario (w, h): (" + w + ", " + h + ") ";
    }

    @Override
    public boolean isMario() {
        return true;
    }

}
