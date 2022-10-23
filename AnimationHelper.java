// ------------------------------------
// Author: Sam Starke
// Date: February 14, 2022
// Description: AnimationHelper.java
// ------------------------------------

// Disclaimer: This is an extra java file I created so I can run my scroll animation.

import java.util.TimerTask;

public class AnimationHelper extends TimerTask {
    Controller controller;

    // Constructor which passes through the controller.
    AnimationHelper(Controller controller) {
        this.controller = controller;
    }

    // This method actually makes the thing go. Lol.
    public void run() {
        controller.scroll(controller.animationDirection);
    }
}