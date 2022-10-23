// ------------------------------------
// Author: Sam Starke
// Date: February 2, 2022
// Description: Game.java
// ------------------------------------

import javax.swing.JFrame;
import java.awt.Toolkit;

public class Game extends JFrame {

	Model model;
	Controller controller;
	View view;

	public Game() {
		model = new Model();
		controller = new Controller(model);
		view = new View(controller, model);
		controller.loadMap();
		this.setTitle("The Legend of Mario");
		// 672 x 508 since my bricks are 32 x 32. This exact window size lines up evenly
		// with my bricks.
		this.setSize(672, 508);
		this.setFocusable(true);
		this.getContentPane().add(view);
		this.setVisible(true);
		view.addMouseListener(controller);
		this.addKeyListener(controller);
	}

	public static void main(String[] args) {
		Game g = new Game();
		g.run();
	}

	public void run() {
		while (true) {
			controller.update();
			model.update();
			view.repaint(); // Indirectly calls View.paintComponent.
			Toolkit.getDefaultToolkit().sync(); // Updates screen.
			// Go to sleep for 50 milliseconds.
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
