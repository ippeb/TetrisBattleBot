// This program gets the current mouse location using
// java.awt.MouseInfo

package TetrisBattleBot.Testing;

import java.awt.Point;
import java.awt.MouseInfo;

public class GetMouseLocation {

  public static void main(String args[]) {
    while (true) {
      Point pt = new Point(MouseInfo.getPointerInfo().getLocation());
      System.out.println(pt.toString());
    }
  }
}
 