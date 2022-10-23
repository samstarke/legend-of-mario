#!/bin/bash
set -u -e
clear
javac Game.java View.java Controller.java Model.java Brick.java Json.java AnimationHelper.java Mario.java Sprite.java Fireball.java Shell.java
java Game

