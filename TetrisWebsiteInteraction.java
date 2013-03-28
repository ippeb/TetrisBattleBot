// This program is responsible for the interaction with the Flash game, i.e.
// Input: Reading the screen 
// Output: Applying key strokes for rotation, drop and hold button, mouse clicks 
// for focusing on the browser window (if necessary)

package TetrisBattleBot;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.MouseInfo;

import java.lang.Math;
import java.awt.image.BufferedImage;

public class TetrisWebsiteInteraction {

  public static Robot robot;

  // key press delay for placing Tetromino
  public static final int SPACEDELAY = 250;
  // key press delay for other movements
  public static final int OTHDELAY   = 50;
  // number of milliseconds this robot sleeps
  // after generating an event
  public static final int AUTODELAY  = 50;

  // types of Tetrominoes
  //
  // 0  **    1 **     2    **   3    *      4  ****   5 *        6   *
  //    **       **        **        ***                 ***        ***
  //
  //    (r,g,b) 
  // 0: 251 184 0
  // 1: 238 26 72
  // 2: 117 208 0
  // 3: 191 47 163
  // 4: 54 174 255
  // 5: 49 69 237
  // 6: 247 106 0
  public static final int[][] PMATCH = {{251,184,0},{238,26,72},{117,208,0},{191,47,163},{54,174,255},{49,69,237},{247,106,0}};

  // countdown for delay
  public static void countdown(int s) {
    for (int i = s; i >= 1; i--) {
      System.out.println(i + "sec...");
      robot.delay(1000);
    }
  }

  // sets appropriate values for pt1 and pt2
  public static void chooseRectangle(Point pt1, Point pt2) { 
    Point tmp;
    tmp = MouseInfo.getPointerInfo().getLocation();
    pt1.x = tmp.x; 
    pt1.y = tmp.y;
    System.out.println(pt1.toString());

    countdown(2);	

    tmp = MouseInfo.getPointerInfo().getLocation();
    pt2.x = tmp.x;
    pt2.y = tmp.y;
    System.out.println(pt2.toString());
  }

  // define a rectangle given points pt1 and pt2 (which form two corners of the rectangle)
  // required: height > 0, width > 0
  public static void setRectangle(Point pt1, Point pt2, Rectangle rect) {
    rect.x =      Math.min(pt1.x, pt2.x);
    rect.y =      Math.min(pt1.y, pt2.y);
    rect.width =  Math.abs(pt1.x - pt2.x);
    rect.height = Math.abs(pt1.y - pt2.y);
    System.out.println("set rect to " + rect.toString());
  }
	
  // match two rgb triples
  // returns matched type of Tetromino
  // if no match return 0
  public static int matchTetromino(int[] rgb) {
    for (int i = 0; i < 7; i++) {
      for (int j = 0; j < 3; j++) {
        if (PMATCH[i][j] != rgb[j]) break;
        if (j == 2) return i;
      }
    }
    return -1; // no match
  }
    
    
  // detect Tetromino using pixels in rect from screen
  public static int detectTetromino(Rectangle rect) {
    BufferedImage Img = robot.createScreenCapture(rect);
    //	Img.getRGB(rect.x, rect.y, rect.width, rect.height, rgb, 0, rect.width);
    for (int x = 0; x < rect.width; x++) {
      for (int y = 0; y <  rect.height; y++) {
        int rgb = Img.getRGB(x,y);
        int[] A = new int[3];
        A[0] = (rgb >> 16) & 0xFF;
        A[1] = (rgb >> 8) & 0xFF;
        A[2] = rgb & 0xFF;
        //		System.out.println(A[0]+" "+A[1]+" "+A[2]);

        int ret = matchTetromino(A);
        if (ret != -1) return ret;
      }
    }
    return -1; // none detected
  }

  public enum MoveType { 
    UP, DOWN, LEFT, RIGHT, SHIFT, SPACE, CLICK, CTRL
        }
    

  // simulate a key press in detail - used in pressKey(..)
  private static void type(int key) {
    robot.delay(40);
    robot.keyPress(key);
    robot.keyRelease(key);
  }


  // simulate a key press
  public static void  pressKey(MoveType t) {

    robot.delay(OTHDELAY);
    System.out.println(t.toString());

    try { 
      switch (t) {       
        case UP:    type(KeyEvent.VK_UP);
          break;
        case DOWN:  type(KeyEvent.VK_DOWN);
          break;
        case LEFT:  type(KeyEvent.VK_LEFT);
          break;
        case RIGHT: type(KeyEvent.VK_RIGHT);
          break;
        case SHIFT: type(KeyEvent.VK_SHIFT);
          break;
        case SPACE: type(KeyEvent.VK_SPACE);
          break;
        case CTRL:  // type(KeyEvent.VK_CONTROL); // control key not working
          type(KeyEvent.VK_SLASH); // corresponds to 'z' (for Dvorak keyboard layout only)
          break;
        case CLICK: // left click
          robot.mousePress(InputEvent.BUTTON1_MASK);
          robot.delay(200);
          robot.mouseRelease(InputEvent.BUTTON1_MASK);
          robot.delay(200);
          break;
      }
	    
    } catch (Throwable trw)
    {
      trw.printStackTrace();
    }
  }

    
  public static void doMove(TetrisMove move, int type) {
    // sets the number of milliseconds this robot sleeps after
    // generating an event.
    robot.setAutoDelay(AUTODELAY);
    // sets whether this robot automatically invokes waitForIdle 
    // after generating an event.
    robot.setAutoWaitForIdle(true);


    // extra delay after pressing "SPACE"
    robot.delay(SPACEDELAY); // must be done in the beginning 
    // because of detection of Tetromino
	
    if (move.rot <= 2) {
      for (int i = 0; i < move.rot; i++) {
        pressKey(MoveType.UP);
      }
    } 
    else {
      pressKey(MoveType.CTRL);
    }

    int pos; // default starting position
    if (type == 0) pos = TetrisBoard.LMARGIN + 4;
    else pos = TetrisBoard.LMARGIN + 3;
	
    if (move.pos >= pos) {
      for (int i = pos; i < move.pos; i++) {
        pressKey(MoveType.RIGHT);
      }
    }
    else {
      for (int i = pos; i > move.pos; i--) {
        pressKey(MoveType.LEFT);
      }
    }

    pressKey(MoveType.SPACE);
  }

    
    
  public static void main(String[] args) throws AWTException {
	
    robot = new Robot();
    // mark the boundaries of the rectangle of the very first Tetromino
    Point ptf1 = new Point(), ptf2 = new Point();
    // rectangle formed by ptf1, ptf2
    Rectangle rectf = new Rectangle();
    // mark the boundaries of the rectangle which is used for looking up 
    // the next Tetromino
    Point pt1 = new Point(), pt2 = new Point();
    // rectangle formed by pt1 and pt2
    Rectangle rect = new Rectangle();
	
    TetrisBoard currBoard = new TetrisBoard();
    TetrisStrategy strategy = new TetrisStrategy();
	
    countdown(3);
    // Capture very first Tetromino
    System.out.println("first rectangle");
    chooseRectangle(ptf1, ptf2);
    setRectangle(ptf1, ptf2, rectf);
    System.out.println("------------------");
    countdown(3);
	
    // Capture pixel in the "hold" box by moving your mouse to that position
    // the color of the tile identifies its type 
    // point  = (MouseInfo.getPointerInfo()).getLocation();
	
    System.out.println("second rectangle");
    chooseRectangle(pt1, pt2);
    setRectangle(pt1, pt2, rect);

    int type;
    for (int t = 1; ; t++) {
      if (t == 1) {
        type = detectTetromino(rectf);
      }
      else {
        type = detectTetromino(rectf);
      }
      if (type == -1) {
        System.out.println("none detected");
        continue;
      }
      TetrisMove move = strategy.computeBestMove(currBoard, type);
      doMove(move, type);
      currBoard.dropTetromino(type, move.rot, move.pos, currBoard.tilemarker++);
      currBoard.clearLinesUpdateHeight();
      currBoard.printFullBoard();
      System.out.println("SCORE: " + currBoard.score);
    }

  }
}