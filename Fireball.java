// ------------------------------------
// Author: Sam Starke
// Date: March 15, 2022
// Description: Fireball.java
// ------------------------------------

import java.awt.Graphics;
import java.awt.Image;

public class Fireball extends Sprite {

    // Initialize the fireball images.
    static Image image[] = null;
    Image[] fireball;

    // Initialize integers for frame animation and speed.
    int frame = 0;
    final int frames = 3;
    final int speed = 12;

    // Integer for tracking the fireball's direction.
    int fireballDirection;

    // Integers to keep track of the fireball bouncing aesthetic.
    int bounceFrame = 0;
    int bounce = 0;

    // Fireball constructor. Takes in position and direction.
    Fireball(int x, int y, int dir) {
        this.x = x;
        this.y = y;
        this.fireballDirection = dir;

        // Initialize the different fireball imags.
        fireball = new Image[4];
        if (image == null) {
            for (int i = 0; i < 4; i++)
                fireball[i] = View.loadImage("sprites/fireball/" + i + ".png");
        }
    }

    // Move function that passes through the positions as doubles for precise
    // movement.
    public void move(double x, double y) {
        this.x -= x;
        this.y -= y;
        w = 12;
        h = 12;
    }

    // Marshal function. Had to.
    public Json marshal() {
        Json ob = Json.newObject();
        return ob;
    }

    // Update method.
    public void update() {
        // Depending on the frame of the fireball bounce, simulate a pseudo-realistic
        // bounce. Probably better ways of doing this.
        switch (bounceFrame) {
            case 0 -> bounce += 12;
            case 1 -> bounce += 6;
            case 2 -> bounce += 3;
            case 3 -> bounce += 0;
            case 4 -> bounce -= 3;
            case 5 -> bounce -= 6;
            default -> bounceFrame = 0;
        }
        // Increment the bounceFrame each 50 ms.
        bounceFrame++;
        // Depending on the direction of the fireball move it in that direction as some
        // fixed speed.
        switch (fireballDirection) {
            // 0 = Up, 1 = Down, 2 = Left, 3 = Right
            // 4 = UL, 5 = UR, 6 = DL, 7 = DR
            case 0 -> move(0, speed);
            case 1 -> move(0, -speed);
            case 2 -> move(speed, 0);
            case 3 -> move(-speed, 0);
            case 4 -> move(speed / 1.5, speed / 1.5);
            case 5 -> move(-speed / 1.5, speed / 1.5);
            case 6 -> move(speed / 1.5, -speed / 1.5);
            case 7 -> move(-speed / 1.5, -speed / 1.5);
        }
        // Increment the fireball animation frame.
        frame++;
        if (frame > frames)
            frame = 0;
    }

    // Draw method.
    public void drawYourself(Graphics g) {
        g.drawImage(fireball[frame], x - View.scrollPosX, y - View.scrollPosY - bounce, null);
    }

    // To string method to print out Fireball's coordinates and size when called.
    @Override
    public String toString() {
        return "Fireball (x, y): (" + x + ", " + y + ") Fireball (w, h): (" + w + ", " + h + ") ";
    }

    @Override
    public boolean isFireball() {
        return true;
    }
}