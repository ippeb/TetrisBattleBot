package TetrisBattleBot.Testing;

import java.awt.*;
import TetrisBattleBot.WebsiteInteraction;
import TetrisBattleBot.WebsiteInteraction.MoveType;


public class TestKeys {
    public static void program(int delay1, int delay2, Robot robot, Runtime rt) {
	System.out.println("Program START " + delay1 +"  "+ delay2);
	robot.delay(1000);
	for (int j = 0; j < 2; j++) {
	    for (int k = 0; k < 2; k++) {
		for (int i = 0; i < 3; i++) {
		    WebsiteInteraction.pressKey(MoveType.LEFT , rt);
		    robot.delay(delay1);
		}
		for (int i = 0; i < 3; i++) {
		    WebsiteInteraction.pressKey(MoveType.RIGHT , rt);
		    robot.delay(delay1);
		}
	    }
	    WebsiteInteraction.pressKey(MoveType.SPACE , rt);
	    robot.delay(delay2);
	}
    }

    public static void main(String[] args) throws java.awt.AWTException {
	Robot robot = new Robot();
        Runtime rt = Runtime.getRuntime();
	
	program(150, 200, robot, rt);
	program(150, 220, robot, rt);
	program(150, 230, robot, rt);
	program(150, 240, robot, rt);
	program(170, 240, robot, rt);
    }
}
