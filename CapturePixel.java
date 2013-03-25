// Prints the position of your mouse cursor 
// and the current rgb values of that position.

package TetrisBattleBot;

import java.awt.*;

public class CapturePixel {
    
    public static void main(String[] args) {
	try {
	    Robot robot = new Robot();
	    Point pt = new Point();
	    while (true) {
		pt = (MouseInfo.getPointerInfo()).getLocation();
		Color col = robot.getPixelColor(pt.x, pt.y);
	    System.out.println(pt.toString());
	    System.out.println(col.toString());
	    }
	}
	catch (AWTException e) {
	    e.printStackTrace();
	}
    }	
}