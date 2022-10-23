// ------------------------------------
// Author: Sam Starke
// Date: February 2, 2022
// Description: Controller.java
// ------------------------------------

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.Timer;

class Controller implements ActionListener, MouseListener, KeyListener {

	View view;
	Model model;

	// Mario directions.
	boolean keyUp;
	boolean keyDown;
	boolean keyLeft;
	boolean keyRight;

	// Boolean for checking if controls have been displayed or not.
	boolean dispControls = false;

	// Integer for the number of frames per direction.
	static final int frames = 7;

	// Variables strictly created for the scrolling between rooms.
	boolean isAnimating;
	int animationStepsRemaining = -1;
	Direction animationDirection;
	Timer animationTimer;

	// Boolean which toggles between edit mode.
	public static boolean editMode = false;

	// Custom enums for scroll direction. Used later.
	public enum Direction {
		Up,
		Down,
		Left,
		Right
	}

	Controller(Model m) {
		model = m;
	}

	public void actionPerformed(ActionEvent e) {
	}

	public void setView(View v) {
		view = v;
	}

	public void mousePressed(MouseEvent e) {
		if (editMode) {
			// mousex and mousey gets the position of where the mouse is.
			int mousex = e.getX() + View.scrollPosX;
			int mousey = e.getY() + View.scrollPosY;

			// Grid snaps the brick placement.
			int x = mousex - mousex % 32;
			int y = mousey - mousey % 32;

			// Sets the t variable to wherever the selection is set.
			int t = View.selected;

			// Creates index variable that is equal to my brickGetIndex function in Model.
			int index = model.spriteGetIndex(x, y);
			if (index == -1) {
				// Add brick if it doesn't exist (-1).
				if (View.shells) {
					model.addShell(x, y, t);
				} else {
					model.addBrick(x, y, t);
				}
			} else {
				// Delete brick if such index exists (index != -1).
				model.delSprite(index);
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {

			// Both of these key presses exit the program.
			case KeyEvent.VK_ESCAPE:
				System.exit(0);

			case KeyEvent.VK_Q:
				System.exit(0);

				// Used to debug positioning. Please ignore, it's for my sake, haha.
			case KeyEvent.VK_V:
				System.out.println("View: (" + View.scrollPosX + ", " + View.scrollPosY + ")");
				System.out.println("Room: (" + View.roomX + ", " + View.roomY + ")");
				System.out.println("Bricks loaded: " + model.sprites.size());
				System.out.println(model.mario);
				System.out.println("Animation steps remaining: " + this.animationStepsRemaining);
				System.out.println("Scroll direction: " + this.animationDirection + "\n");
				break;

			// Toggle edit mode on and off
			case KeyEvent.VK_E:
				if (!editMode) {
					View.shells = false;
					View.hide = false;
					editMode = true;
					System.out.println("EDIT MODE IS NOW ACTIVE.\nMario is now hidden and can no longer move.");
					// If the controls have not been displayed yet, display the controls to
					// terminal.
					if (!dispControls) {
						System.out.println("\nControls:");
						System.out.println("[ARROW KEYS] Move Mario in Play Mode\n[B] Throw Fireball\n[# KEYS] Choose Brick Type");
						System.out.println("[CLICK] Place/Break Brick");
						System.out.println("[A/D/W/X] Navigate Screens in Edit Mode");
						System.out.println("[E] Toggle Edit Mode\n[U] Toggle UI\n[S] Save Bricks");
						System.out.println("[L] Load Bricks\n[C] View Controls\n[ESC/Q] Exit Program\n");
					} else {
						// If the controls have been displayed, take up less space by allowing the user
						// to view the controls on their own time.
						System.out.println("Press [C] to view the controls again.\n");
					}
					// Now that the controls have been sent out, change the boolean value to true.
					dispControls = true;
				} else {
					editMode = false;
					System.out.println("EDIT MODE IS NO LONGER ACTIVE.\nMario is now visible and can now move.\n");
				}
				break;

			// Toggle between bricks or shells.
			case KeyEvent.VK_P:
				if (editMode) {
					if (!View.shells)
						View.shells = true;
					else
						View.shells = false;
				}
				break;

			// Toggle UI on and off.
			case KeyEvent.VK_U:
				if (!View.hide) {
					View.hide = true;
					if (editMode)
						System.out.println("Edit Mode UI is now hidden. Press [U] to display the UI again.\n");
				} else {
					View.hide = false;
				}
				break;

			// Directional inputs (Activate).
			case KeyEvent.VK_UP:
				keyUp = true;
				break;

			case KeyEvent.VK_DOWN:
				keyDown = true;
				break;

			case KeyEvent.VK_LEFT:
				keyLeft = true;
				break;

			case KeyEvent.VK_RIGHT:
				keyRight = true;
				break;

			// Each of these set the selected variable to the desired block keypress.
			case KeyEvent.VK_1:
				View.selected = 0;
				break;
			case KeyEvent.VK_2:
				View.selected = 1;
				break;
			case KeyEvent.VK_3:
				View.selected = 2;
				break;
			case KeyEvent.VK_4:
				View.selected = 3;
				break;
			case KeyEvent.VK_5:
				View.selected = 4;
				break;
			case KeyEvent.VK_6:
				View.selected = 5;
				break;
			case KeyEvent.VK_7:
				View.selected = 6;
				break;
			case KeyEvent.VK_8:
				View.selected = 7;
				break;
			case KeyEvent.VK_9:
				View.selected = 8;
				break;
			case KeyEvent.VK_0:
				View.selected = 9;
				break;
		}
	}

	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {

			// Saves the map in its current state to map.json.
			case KeyEvent.VK_S:
				model.marshal().save("map.json");
				break;

			// Loads the map in the state map.json is in.
			case KeyEvent.VK_L:
				Json j = Json.load("map.json");
				model.unmarshal(j);
				break;

			// Directional inputs (Deactivate).
			case KeyEvent.VK_UP:
				keyUp = false;
				model.mario.isRunning = false;
				break;

			case KeyEvent.VK_DOWN:
				keyDown = false;
				model.mario.isRunning = false;
				break;

			case KeyEvent.VK_LEFT:
				keyLeft = false;
				model.mario.isRunning = false;
				break;

			case KeyEvent.VK_RIGHT:
				keyRight = false;
				model.mario.isRunning = false;
				break;

			case KeyEvent.VK_CONTROL:
				break;

			// Allow the user to view the controls view Terminal by pressing C.
			case KeyEvent.VK_C:
				System.out.println("Controls:");
				System.out.println("[ARROW KEYS] Move Mario in Play Mode\n[B] Throw Fireball\n[# KEYS] Choose Brick Type");
				System.out.println("[CLICK] Place/Break Brick");
				System.out.println("[A/D/W/X] Navigate Screens in Edit Mode");
				System.out.println("[E] Toggle Edit Mode\n[U] Toggle UI\n[S] Save Bricks");
				System.out.println("[L] Load Bricks\n[C] View Controls\n[ESC/Q] Exit Program\n");
				break;
		}

		if (!editMode) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_B:
				model.shootFireball();
				break;
			}
		}

		if (editMode) {
			switch (e.getKeyCode()) {
				// If the screen is on the right, and not animating, go left.
				case KeyEvent.VK_A:
					if (View.scrollPosX == View.screenWidth && !this.isAnimating && this.animationStepsRemaining == -1)
						this.animate(Direction.Left);
					break;

				// If the screen is on the left, and not animating, go right.
				case KeyEvent.VK_D:
					if (View.scrollPosX == 0 && !this.isAnimating && this.animationStepsRemaining == -1)
						this.animate(Direction.Right);
					break;

				// If the screen is on the bottom, and not animating, go up.
				case KeyEvent.VK_W:
					if (View.scrollPosY == View.screenHeight && !this.isAnimating && this.animationStepsRemaining == -1)
						this.animate(Direction.Up);
					break;

				// If the screen is on the top, and not animating, go down.
				case KeyEvent.VK_X:
					if (View.scrollPosY == 0 && !this.isAnimating && this.animationStepsRemaining == -1)
						this.animate(Direction.Down);
					break;
			}
		}
	}

	public void loadMap() {
		Json j = Json.load("map.json");
		model.unmarshal(j);
		System.out.println("Press [E] to toggle Edit Mode.\nPress [M] to toggle the map.\n");
	}

	public void keyTyped(KeyEvent e) {
	}

	public void update() {

		// First, I save Mario's previous direction
		model.mario.savePrev();

		// Sets the roomX and roomY variable to the scroll position divided by the
		// screen width or height to rather than get a pixel coordinate grid, we get a
		// coordinate grid for each room.
		View.roomX = View.scrollPosX / View.screenWidth;
		View.roomY = View.scrollPosY / View.screenHeight;

		// Numbers that indicate direction:
		// 0 = Up, 1 = Down, 2 = Left, 3 = Right
		// 4 = UL, 5 = UR, 6 = DL, 7 = DR

		if (!editMode && !this.isAnimating) {
			// First, I check for these two before anything else, this allows me to see if
			// he is moving in two of the opposite directions at once, which lets me make
			// him go nowhere and sets him in the default direction (1).
			if (keyUp && keyDown) {
				model.mario.isRunning = false;
				model.mario.marioDirection = 1;
			} else if (keyLeft && keyRight) {
				model.mario.isRunning = false;
				model.mario.marioDirection = 1;

				// Diagonal Directions.
			} else if (keyUp && keyLeft) {
				frameAdvance(4);
				// Dividing mario's speed by 1.5 in both horizontal and vertical directions so
				// it's not faster than a normal (L/U/R/D) direction.
				model.mario.move(model.mario.speed / 1.5, model.mario.speed / 1.5);
			} else if (keyUp && keyRight) {
				frameAdvance(5);
				model.mario.move(-model.mario.speed / 1.5, model.mario.speed / 1.5);
			} else if (keyDown && keyLeft) {
				frameAdvance(6);
				model.mario.move(model.mario.speed / 1.5, -model.mario.speed / 1.5);
			} else if (keyDown && keyRight) {
				frameAdvance(7);
				model.mario.move(-model.mario.speed / 1.5, -model.mario.speed / 1.5);
			}

			// Normal 4 Directions.
			else if (keyUp) {
				frameAdvance(0);
				model.mario.move(0, model.mario.speed);

			} else if (keyDown) {
				frameAdvance(1);
				model.mario.move(0, -model.mario.speed);

			} else if (keyLeft) {
				frameAdvance(2);
				model.mario.move(model.mario.speed, 0);

			} else if (keyRight) {
				frameAdvance(3);
				model.mario.move(-model.mario.speed, 0);
			}
		}

		// Animates the screen to scroll in the direction he runs to.
		if (!editMode) {
			if (model.mario.y < View.screenHeight + 24 && !this.isAnimating
					&& View.scrollPosY == View.screenHeight)
				this.animate(Direction.Up);

			if (model.mario.y > View.screenHeight - 32 && !this.isAnimating && View.scrollPosY == 0)
				this.animate(Direction.Down);

			if (model.mario.x > View.screenWidth - 32 && !this.isAnimating && View.scrollPosX == 0)
				this.animate(Direction.Right);

			if (model.mario.x < View.screenWidth + 16 && !this.isAnimating && View.scrollPosX == View.screenWidth)
				this.animate(Direction.Left);
		}

		// If the screen is animating and there are still remaining steps to go, call
		// scroll method passing through the direction.
		if (this.isAnimating && this.animationStepsRemaining > 0) {
			this.scroll(this.animationDirection);
			if (!editMode) {
				model.mario.isRunning = true;
				model.mario.frame++;
				if (model.mario.frame > frames)
					model.mario.frame = 0;
			}
			// Otherwise, if it is animating and there are zero steps remaining update the
			// boolean to false.
		} else if (this.isAnimating && this.animationStepsRemaining == 0) {
			this.isAnimating = false;
		}
	}

	// Method that advances Mario's current animation frame along with some other
	// things.
	void frameAdvance(int dir) {
		// Set isRunning to true so Mario knows to stop if he isn't running.
		model.mario.isRunning = true;
		// Pass through an integer for Mario's and the Fireball's direction. These
		// directions are displayed
		// at the top of update.
		model.mario.marioDirection = dir;
		// Iterate his animation frame.
		model.mario.frame++;
		// If his frame goes too high, revert to zero.
		if (model.mario.frame > frames)
			model.mario.frame = 0;
	}

	// Animate method.
	void animate(Direction dir) {
		// Do not do anything if the screen is still scrolling, wait until the scroll is
		// complete.
		if (this.isAnimating && this.animationStepsRemaining > 0) {
			return;
		}

		// Default settings.
		this.isAnimating = true;
		this.animationDirection = dir;
		this.animationTimer = new Timer();

		// Switch statement for direction steps remaining. Multiply these steps by 32
		// (block size) and you will recieve the correct pixels of the room!
		switch (dir) {
			case Up:
				this.animationStepsRemaining = 14;
				break;
			case Down:
				this.animationStepsRemaining = 14;
				break;
			case Left:
				this.animationStepsRemaining = 20;
				break;
			case Right:
				this.animationStepsRemaining = 20;
				break;
		}
		// Calls my AnimationHelper class. Where 0 is the delay before a task is
		// executed, and 100 is the milliseconds between each iteration. This means it
		// scrolls 32 blocks every 0.1 seconds (pretty much).
		this.animationTimer.scheduleAtFixedRate(new AnimationHelper(this), 0, 100);
	}

	// Scroll method.
	public void scroll(Direction dir) {
		// If there are zero steps remaining, the screen is set to "not animating" and
		// the timer is cancelled, which essentially ends it.
		if (this.animationStepsRemaining <= 0) {
			this.animationTimer.cancel();
			this.isAnimating = false;
			model.mario.isRunning = false;
		}
		// Decrement the steps remaining each scroll.
		this.animationStepsRemaining--;

		// Switch case for each enum Direction. 32 stands for how many pixels will
		// advance for each 0.1 seconds. In this case it's the size of the blocks.
		switch (dir) {
			case Up:
				View.scrollPosY -= View.screenHeight / 15;
				// These move mario slightly into the next screen so he doesn't continuously
				// animate the screen up and down when he enters a new room.
				model.mario.y -= 4;
				break;
			case Down:
				View.scrollPosY += View.screenHeight / 15;
				model.mario.y += 4;
				break;
			case Left:
				View.scrollPosX -= View.screenHeight / 15;
				model.mario.x -= 3;
				break;
			case Right:
				View.scrollPosX += View.screenHeight / 15;
				model.mario.x += 3;
				break;
		}
	}
}
