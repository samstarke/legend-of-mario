// ------------------------------------
// Author: Sam Starke
// Date: February 2, 2022
// Description: Model.java
// ------------------------------------

import java.util.ArrayList;

class Model extends Object {
	// Initalize bricks and mario.
	// ArrayList<Brick> bricks;
	ArrayList<Sprite> sprites;
	Mario mario;
	Fireball fireball;

	Model() {
		// Brick initialization.
		// bricks = new ArrayList<Brick>();
		// Initalize Mario at 200 X and 140 Y.
		mario = new Mario(this);
	}

	// Copy constructor for strictly unmarshaling.
	public void unmarshal(Json ob) {
		sprites = new ArrayList<Sprite>();
		mario = new Mario(this);
		sprites.add(mario);
		Json brickList = ob.get("bricks");
		Json shellList = ob.get("shells");
		for (int i = 0; i < brickList.size(); i++)
			sprites.add(new Brick(brickList.get(i)));
		for (int i = 0; i < shellList.size(); i++)
			sprites.add(new Shell(shellList.get(i)));
		System.out.println("Successfully loaded " + sprites.size() + " sprites from map.json.\n");
	}

	void addBrick(int x, int y, int t) {
		// Adding a Brick to the ArrayList.
		Brick b = new Brick(x, y, t);
		sprites.add(b);
	}

	void addShell(int x, int y, int t) {
		// Adding a Shell to the ArrayList.
		Shell s = new Shell(x, y, t);
		sprites.add(s);
	}

	// Function that takes in the x and y of a brick and checks if such a brick
	// exists once called, if not, return -1.
	int spriteGetIndex(int x, int y) {
		for (int i = 0; i < sprites.size(); i++) {
			// If the sprite is either a shell or brick and it's position is at where I'm clicking.
			if (sprites.get(i).isBrick() || sprites.get(i).isShell()) {
				if (sprites.get(i).x == x && sprites.get(i).y == y) {
					return i;
				}
			}
		}
		return -1;
	}

	// Function that takes in the index of a sprite and removes it if one already
	// exists at that point.
	void delSprite(int i) {
		sprites.remove(i);
	}

	// Function that creates a fireball object and adds it to the spritelist.
	void shootFireball() {
		fireball = new Fireball(mario.x, mario.y, mario.marioDirection);
		sprites.add(fireball);
	}

	// Marshals the object into Json node. Saves brick data to map.json.
	Json marshal() {
		Json ob = Json.newObject();
		Json marioList = Json.newList();
		ob.add("mario", marioList);
		Json brickList = Json.newList();
		ob.add("bricks", brickList);
		Json shellList = Json.newList();
		ob.add("shells", shellList);
		for (int i = 0; i < sprites.size(); i++) {
			if (sprites.get(i).isMario())
				marioList.add(sprites.get(i).marshal());
			if (sprites.get(i).isBrick())
				brickList.add(sprites.get(i).marshal());
			if (sprites.get(i).isShell())
				shellList.add(sprites.get(i).marshal());
		}
		System.out.println("Successfully saved " + sprites.size() + " sprites to map.json.\n");
		return ob;
	}

	// Model update function.
	public void update() {
		for (int i = 0; i < sprites.size(); i++) {
			sprites.get(i).update();
		}
		for (int i = 0; i < sprites.size(); i++) {
			// If the sprite is a shell.
			if (sprites.get(i) instanceof Shell) {
				for (int j = 0; j < sprites.size(); j++) {
					// If mario collides with a shell, and edit mode is off.
					if (sprites.get(j).isMario() && !Controller.editMode) {
						if (checkCollision(sprites.get(i), sprites.get(j))) {
							// Depending on how the shell interacts with Mario, face the shell in the opposite direction.
							switch (sprites.get(i).shellDir) {
								case 0 -> sprites.get(i).shellDir = 1;
								case 1 -> sprites.get(i).shellDir = 0;
								case 2 -> sprites.get(i).shellDir = 3;
								case 3 -> sprites.get(i).shellDir = 2;
							}
							break;
						}
					}
					// If the shell is not itself and isn't moving and hits another shell, delete the shell that was hit.
					if (sprites.get(j).isShell() && i != j && !sprites.get(j).isMoving) {
						if (checkCollision(sprites.get(i), sprites.get(j))) {
							destroy(j);
							break;
						}
					}
					// If the shell hits a brick, break the shell. Depending on the properties of the brick it will do something funny.
					if (sprites.get(j).isBrick() && !sprites.get(j).flipping && !sprites.get(i).shellDestroy && !sprites.get(i).broken) {
						if (checkCollision(sprites.get(i), sprites.get(j))) {
							if (sprites.get(j).brickType == 3) {
								// Music note blocks bounce.
								sprites.get(j).bounce = true;
								switch (sprites.get(i).shellDir) {
									case 0 -> sprites.get(i).shellDir = 1;
									case 1 -> sprites.get(i).shellDir = 0;
									case 2 -> sprites.get(i).shellDir = 3;
									case 3 -> sprites.get(i).shellDir = 2;
								}
								break;
							}
							else if (sprites.get(j).brickType == 2) {
								// Question blocks shoot out a coin.
								sprites.get(j).brickType = 0;
								destroy(i);
								sprites.get(j).coinActive = true;
								break;
							}
							else if (sprites.get(j).brickType == 1) {
								// Blocks with eyes start flipping and then lose collision properties while it's flipping.
								sprites.get(j).flipping = true;
								destroy(i);
							}
							else {
								destroy(i);
							}
							break;
						}
					}
				}
			}
		}
		// If the sprite is Mario.
		for (int i = 1; i < sprites.size(); i++) {
			if (checkCollision(mario, sprites.get(i))) {
				if (sprites.get(i).isBrick() && !sprites.get(i).flipping) {
					// Exit brick method which takes Mario and moves him out of the bricks.
					mario.exitBrick(sprites.get(i));
				}
				if (sprites.get(i).isShell() && !sprites.get(i).shellDestroy && !sprites.get(i).broken) {
					// Move the shell if Mario kicks it.
					if (!sprites.get(i).shellDestroy && !sprites.get(i).broken)
						mario.kicked = true;
					sprites.get(i).isMoving = true;
					mario.exitBrick(sprites.get(i));
				}
			}
		}
		for (int i = 0; i < sprites.size(); i++) {
			// If the sprite is a fireball.
			if (sprites.get(i) instanceof Fireball) {
				for (int j = 1; j < sprites.size(); j++) {
					// If the sprite is a brick.
					if (sprites.get(j).isBrick() && !sprites.get(j).flipping) {
						// Check collision between a fireball and a brick.
						if (checkCollision(sprites.get(i), sprites.get(j))) {
							sprites.remove(i);
							break;
						}
					}
					if (sprites.get(j).isShell()) {
						// If a fireball collides with a shell destroy both the shell and the fireball.
						if (checkCollision(sprites.get(i), sprites.get(j)) && !sprites.get(j).shellDestroy && !sprites.get(j).broken) {
							sprites.remove(i);
							destroy(j);
							break;
						}
					}
				}
			}
		}
	}

	// Destroy function which allows for time so the shell bursting animation can occur.
	public void destroy(int object) {
		sprites.get(object).shellDir = -1;
		sprites.get(object).broken = true;
		sprites.get(object).isMoving = false;
		if (sprites.get(object).shellDestroy) {
			sprites.remove(object);
		}
	}

	// Check collision method which checks to see if he is NOT colliding. If not,
	// return true.
	boolean checkCollision(Sprite object, Sprite brick) {
		if (object instanceof Shell) {
			// Object right into brick left.
			if (object.x + object.w + 2 < brick.x)
				return false;
			// Object left into brick right.
			if (object.x + 2 > brick.x + brick.w)
				return false;
			// Object bottom into brick top.
			if (object.y + 2 + object.h < brick.y)
				return false;
			// Object top into brick bottom.
			if (object.y + 2 > brick.y + brick.h)
				return false;
			return true;
		} else {
			// Object right into brick left.
			if (object.x + object.w < brick.x)
				return false;
			// Object left into brick right.
			if (object.x > brick.x + brick.w)
				return false;
			// Object bottom into brick top.
			if (object.y + object.h < brick.y)
				return false;
			// Object top into brick bottom.
			if (object.y  > brick.y + brick.h)
				return false;
			return true;
		}
	}
}