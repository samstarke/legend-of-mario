// ------------------------------------
// Author: Sam Starke
// Date: February 10, 2022
// Description: Brick.java
// ------------------------------------

import java.awt.Graphics;
import java.awt.Image;

public class Brick extends Sprite {

    // This should be fun to add comments to...
    // Brick image arrays.
    static Image image[] = null;
    static Image[] brickSelect;
    static Image[] brick;

    // Arrays for special effects.
    static Image[] brickAnim;
    static Image[] musicAnim;
    static Image[] flipAnim;
    static Image[] coin;
    static Image[] sparks;

    // Frame animation total integers.
    final int brickFrames = 8;
    final int coinFrames = 4;

    // Different integers for different types of frames. Yes there are probably
    // better ways to do this.
    static int frameCounter = 0;
    int frame = 0;
    int jumpFrame = 0;
    int sparkFrame = 0;
    int coinFrame = 0;
    int flipFrame = 0;
    int bounceFrame = 0;

    // Special effect variables.
    int coinJump = 0;
    int musicBounceY = 0;
    int powBounce = 0;
    boolean spark = false;

    Brick(int x, int y, int t) {
        // Sets type of brick variable to wherever the selection is set.
        brickType = View.selected;

        this.x = x;
        this.y = y;

        // Width and height of the brick.
        w = 32;
        h = 32;

        // Initalize the different brick images.
        brick = new Image[10];
        if (image == null) {
            for (int i = 0; i < 10; i++)
                brick[i] = View.loadImage("sprites/bricks/" + i + ".png");
        }
        // Initialize the different question block animation frame images.
        brickAnim = new Image[brickFrames];
        if (image == null) {
            for (int i = 0; i < brickFrames; i++)
                brickAnim[i] = View.loadImage("sprites/misc/blockAnim/" + i + ".png");
        }
        // Initialize the different music block animation frame images.
        musicAnim = new Image[brickFrames];
        if (image == null) {
            for (int i = 0; i < brickFrames; i++)
                musicAnim[i] = View.loadImage("sprites/misc/musicAnim/" + i + ".png");
        }
        // Initialize the different flip block animation frame images.
        flipAnim = new Image[brickFrames];
        if (image == null) {
            for (int i = 0; i < brickFrames; i++)
                flipAnim[i] = View.loadImage("sprites/misc/flipAnim/" + i + ".png");
        }
        // Initialize the different coin frame images.
        coin = new Image[brickFrames];
        if (image == null) {
            for (int i = 0; i < coinFrames; i++)
                coin[i] = View.loadImage("sprites/misc/coinAnim/" + i + ".png");
        }
        // Initialize the different spark images.
        sparks = new Image[brickFrames];
        if (image == null) {
            for (int i = 0; i < coinFrames; i++)
                sparks[i] = View.loadImage("sprites/misc/sparks/" + i + ".png");
        }
    }

    // Copy constructor for strictly unmarshaling.
    Brick(Json ob) {
        x = (int) ob.getLong("brickX");
        y = (int) ob.getLong("brickY");
        brickType = (int) ob.getLong("brickType");

        w = 32;
        h = 32;

        // Initalize the different brick images in the copy constructor as well.
        brick = new Image[10];
        if (image == null) {
            for (int i = 0; i < 10; i++) {
                brick[i] = View.loadImage("sprites/bricks/" + i + ".png");
            }
        }
        // Initialize the different question block animation frame images in the copy
        // constructor as well.
        brickAnim = new Image[8];
        if (image == null) {
            for (int i = 0; i < 8; i++)
                brickAnim[i] = View.loadImage("sprites/misc/blockAnim/" + i + ".png");
        }
        // Initialize the different music block animation frame images in the copy
        // constructor as well.
        musicAnim = new Image[brickFrames];
        if (image == null) {
            for (int i = 0; i < brickFrames; i++)
                musicAnim[i] = View.loadImage("sprites/misc/musicAnim/" + i + ".png");
        }
        // Initialize the different coin frame images in the copy constructor as well.
        coin = new Image[brickFrames];
        if (image == null) {
            for (int i = 0; i < coinFrames; i++)
                coin[i] = View.loadImage("sprites/misc/coinAnim/" + i + ".png");
        }
        // Initialize the different spark images in the copy constructor as well.
        sparks = new Image[brickFrames];
        if (image == null) {
            for (int i = 0; i < coinFrames; i++)
                sparks[i] = View.loadImage("sprites/misc/sparks/" + i + ".png");
        }
        // Initialize the different flip block animation frame images.
        flipAnim = new Image[brickFrames];
        if (image == null) {
            for (int i = 0; i < brickFrames; i++)
                flipAnim[i] = View.loadImage("sprites/misc/flipAnim/" + i + ".png");
        }
    }

    // Marshals together the brick objects.
    public Json marshal() {
        Json ob = Json.newObject();
        ob.add("brickX", x);
        ob.add("brickY", y);
        ob.add("brickType", brickType);
        return ob;
    }

    // Call the drawYourself function in brick which passes through the brickType to
    // the brick.
    // Each of these can either draw a silly little coin above the block, sparks
    // when the coin disappears, or animate the block when it's on the screen.
    public void drawYourself(Graphics g) {
        if (coinActive) {
            g.drawImage(coin[coinFrame], x - View.scrollPosX, y - View.scrollPosY - h - coinJump, null);
        }
        if (spark) {
            g.drawImage(sparks[sparkFrame], x - View.scrollPosX, y - View.scrollPosY - h - coinJump, null);
        }
        if (brickType == 2) {
            g.drawImage(brickAnim[frame], x - View.scrollPosX, y - View.scrollPosY, null);
        } else if (brickType == 3) {
            g.drawImage(musicAnim[frame], x - View.scrollPosX, y - View.scrollPosY - musicBounceY, null);
        } else if (brickType == 1 && flipping) {
            g.drawImage(flipAnim[frame], x - View.scrollPosX, y - View.scrollPosY, null);
        } else {
            g.drawImage(brick[brickType], x - View.scrollPosX, y - View.scrollPosY, null);
        }
    }

    // Animate method to have the coin flutter up and back down then disappear and sparks to exist.
    public void coinAnimate() {
        coinFrame++;
        jumpFrame++;
        if (jumpFrame >= 0 && jumpFrame <= 3)
            coinJump += 10;
        else if (jumpFrame >= 4 && jumpFrame <= 5)
            coinJump += 3;
        else if (jumpFrame >= 6 && jumpFrame <= 7)
            coinJump -= 6;
        if (coinFrame > 2) {
            coinFrame = 0;
            frameCounter++;
            if (frameCounter > 2) {
                frameCounter = 0;
                coinActive = false;
                spark = true;
                jumpFrame = 0;
            }
        }
    }

    // Silly update method.
    public void update() {
        frame = (frame + 1) % brickFrames;
        // Coins.
        if (coinJump > 16)
            coinJump = 24;
        if (coinActive) {
            coinAnimate();
        }
        // Spark animation.
        if (spark) {
            sparkFrame++;
            if (sparkFrame > 7) {
                sparkFrame = 0;
                spark = false;
            }
        }
        // Block flip animation.
        if (flipping) {
            flipFrame++;
            if (flipFrame > 150) {
                flipFrame = 0;
                flipping = false;
            }
        }
        // Music bounce animation.
        if (bounce) {
            bounceFrame++;
            if (bounceFrame > 5) {
                bounceFrame = 0;
                bounce = false;
            }
            if (bounceFrame >= 0 && bounceFrame < 3) {
                musicBounceY += 8;
            } else if (bounceFrame >= 3 && bounceFrame < 6) {
                musicBounceY -= 8;
            }
        }
    }

    // To string method to print out the Bricks coordinates and size when called.
    @Override
    public String toString() {
        return "Brick (x, y): (" + x + ", " + y + ") Brick (w, h): (" + w + ", " + h + ") ";
    }

    @Override
    public boolean isBrick() {
        return true;
    }
}
