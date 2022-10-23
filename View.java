// ------------------------------------
// Author: Sam Starke
// Date: February 2, 2022
// Description: View.java
// ------------------------------------

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;
import java.io.IOException;

class View extends JPanel {

	Model model;

	// Background images.
	BufferedImage sand;
	BufferedImage palace;
	BufferedImage castle;
	BufferedImage bonus;

	// User interface image.
	BufferedImage ui;

	// Map image. Currently not modular.
	BufferedImage mapImage;
	BufferedImage marioIcon;

	// Public container image, this is what holds the edit mode brick containers.
	public static Image[] container;
	// Spacing of the containers apart from each other.
	static final int containerSpacing = 62;
	public static final int containers = 10;

	// Different bricks that appear for choices of bricks.
	Image[] brickSelect;

	// Different shells that appear for choices of shells.
	Image[] shellSelect;

	// Public static variables for scroll position.
	public static int scrollPosX;
	public static int scrollPosY;

	// Integers for screen width and screen height to minimize magic numbers.
	public static final int screenWidth = 672;
	public static final int screenHeight = 480;

	// Integers for checking what room you're in.
	public static int roomX;
	public static int roomY;

	// Selected variable is what changes depending on number key press.
	public static int selected = 0;

	// Hide boolean is what either hides the UI or not.
	public static boolean hide = false;

	// Select shells boolean.
	public static boolean shells = false;

	View(Controller c, Model m) {
		c.setView(this);
		model = m;

		scrollPosX = 0;
		scrollPosY = 0;

		// Loads in brick images.
		try {
			// Different backgrounds for the different rooms.
			this.sand = ImageIO.read(new File("sprites/bg/sand.png"));
			this.palace = ImageIO.read(new File("sprites/bg/palace.png"));
			this.castle = ImageIO.read(new File("sprites/bg/castle.png"));
			this.bonus = ImageIO.read(new File("sprites/bg/bonus.png"));

			// Miscellaneous sprites for different objects.
			this.ui = ImageIO.read(new File("sprites/misc/ui.png"));

			// Loads in two container images. Selected and not selected.
			container = new Image[2];
			container[0] = loadImage("sprites/misc/container1.png");
			container[1] = loadImage("sprites/misc/container0.png");

			// Loads in all brick images to display inside the containers.
			brickSelect = new Image[10];
			for (int i = 0; i < 10; i++)
				brickSelect[i] = View.loadImage("sprites/bricks/" + i + ".png");

			// Loads in all shell images to display inside the containers.
			shellSelect = new Image[10];
			for (int i = 0; i < 10; i++)
				shellSelect[i] = View.loadImage("sprites/shells/" + i + "/0.png");

		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}

	// loadImage method which you can pass through the filepath of the image, and it
	// loads.
	static Image loadImage(String filepath) {
		Image image = null;
		try {
			return ImageIO.read(new File(filepath));
		} catch (IOException e) {
			e.printStackTrace(System.err);
			System.exit(1);
		}
		return image;
	}

	// Public paintComponent method which redraws the screen each frame.
	public void paintComponent(Graphics g) {
		// Background color.
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		// Drawing background images.
		g.drawImage(this.bonus, -scrollPosX, -scrollPosY, null);
		g.drawImage(this.sand, screenWidth - scrollPosX, -scrollPosY, null);
		g.drawImage(this.castle, -scrollPosX, screenHeight - scrollPosY, null);
		g.drawImage(this.palace, screenWidth - scrollPosX, screenHeight - scrollPosY, null);

		// Draw bricks. Start at 1 since Mario is at sprite index 0.
		for (int i = 1; i < model.sprites.size(); i++)
			model.sprites.get(i).drawYourself(g);

		// If edit mode is not active, draw Mario.
		if (!Controller.editMode) {
			model.mario.drawYourself(g);
		}

		// If edit mode is active but hide is not.
		if (!hide && Controller.editMode) {
			for (int i = 0; i < containers; i++) { 
				// Initialize container position 33 pixels from the left and 62 pixels apart
				// (containerSpacing) from each other.
				int containerPosX = (i * containerSpacing) + 33;
				int containerPosY = screenHeight - 81;
				// If the selected brick is equal to the iteration.
				if (selected == i) {
					g.drawImage(container[0], containerPosX, containerPosY, null);
				} else {
					g.drawImage(container[1], containerPosX, containerPosY, null);
				}
			}
			for (int i = 0; i < containers; i++) {
				// Initialize the brick position 41 pixels from the left and 62
				// (containerSpacing) pixels apart from each other.
				int brickSelectPosX = (i * containerSpacing) + 41;
				int brickSelectPosY = screenHeight - 73;
				if (!shells) {
					// Draw all different kinds of bricks.
					g.drawImage(brickSelect[i], brickSelectPosX, brickSelectPosY, null);
				} else {
					// Draw all different kinds of shells.
					g.drawImage(shellSelect[i], brickSelectPosX, brickSelectPosY, null);
				}
			}
			// Draw the UI Menu.
			g.drawImage(ui, 3, -22, null);
		}
		// Display hitbox. Debug.
		// g.setColor(new Color(255, 255, 255));
		// g.fillRect(model.mario.x - scrollPosX, model.mario.y - scrollPosY,
		// model.mario.w,
		// model.mario.h);
	}
}