// ------------------------------------
// Author: Sam Starke
// Date: March 13, 2022
// Description: Sprite.java
// ------------------------------------

import java.awt.Graphics;

abstract class Sprite {

    int x, y, w, h;
    public int brickType;
    public int shellType;
    public int shellDir = -1; // 0 = Up, 1 = Down, 2 = Left, 3 = Right
    public boolean isMoving = false;
    public boolean coinActive = false;
    public boolean flipping = false;
    public boolean bounce = false;
    public boolean broken = false;
    public boolean shellDestroy = false;
    public boolean kicked = false;

    int prevX, prevY;

    // Sprite constructor.
    public Sprite() {
        x = 0;
        y = 0;
        w = 0;
        h = 0;
    }

    abstract public Json marshal();
    abstract public void update();
    abstract public void drawYourself(Graphics g);

    public boolean isMario() {
        return false;
    }

    public boolean isBrick() {
        return false;
    }

    public boolean isFireball() {
        return false;
    }

    public boolean isShell() {
        return false;
    }

    // Set direction function for the shell direction.
    public void setDirection(int dir) {
        shellDir = dir;
    }
}
