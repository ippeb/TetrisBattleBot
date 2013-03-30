// This program prints the RGB values positioned at your 
// Mouse cursor to standard output.

package TetrisBattleBot.Testing;

import java.awt.Point;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.Color;

public class GetRGBValues {

  public static void main(String args[]) {
    try {
      Robot robot = new Robot();
      
      while (true) {
        Point pt = new Point(MouseInfo.getPointerInfo().getLocation());
        Color color = robot.getPixelColor(pt.x, pt.y);
        System.out.println("Position (" + pt.x + ", " + pt.y + ") RGB (" +  color.getRed()  +
                           ", " + color.getGreen() + ", " + color.getBlue() + ")");
      }
      
    }
    catch (java.awt.AWTException e) {
      
    }
    
  }
}
