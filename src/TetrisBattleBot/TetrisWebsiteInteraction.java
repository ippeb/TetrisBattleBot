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
  public static final int SPACEDELAY = 150;
  // key press delay for other movements
  public static final int KEYDELAY   = 500; // bef: 50
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
  public static final int[][] PMATCH = {{251,184,0},{238,26,72},{117,208,0},
                                        {191,47,163},{54,174,255},{49,69,237},{247,106,0}};

  public static Rectangle TetrisRect;


  // countdown for delay (in seconds)
  public static void countdown(int s) {
    for (int i = s; i >= 1; i--) {
      System.out.println(i + "sec...");
      robot.delay(1000);
    }
  }

  // define a rectangle given the points pt1 and pt2
  // (which form two corners of the rectangle)
  // required: height > 0, width > 0
  public static Rectangle setRectangle(Point pt1, Point pt2) {
    Rectangle rect = new Rectangle( Math.min(pt1.x, pt2.x),  Math.min(pt1.y, pt2.y),
                                    Math.abs(pt1.x - pt2.x), Math.abs(pt1.y - pt2.y));

    System.out.println("set rectangle to " + rect.toString());
    return rect;
  }
  
  // initializes TetrisRect
  // s1 specifies the duration (in seconds) until the cursor must be moved
  // to the first point. s2 specifies the delay (in seconds) between choosing
  // the first and the second point bounding the rectangle
  public static void chooseRectangle(int s1, int s2) {
    System.out.println("Move your cursor to the upper left corner of the Tetris board. ");
    countdown(s1);
    
    Point pt1 = new Point(MouseInfo.getPointerInfo().getLocation());
    System.out.println(pt1.toString());

    System.out.println("Move your cursor to the lower right corner of the Tetris board.");
    countdown(s2);	

    Point pt2 = new Point(MouseInfo.getPointerInfo().getLocation());
    System.out.println(pt2.toString());
    
    TetrisRect = setRectangle(pt1, pt2);
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
    
  // returns red, green, blue values separately in an
  // array
  private static int[] getRGB(BufferedImage Img, int x, int y) {
    int rgb = Img.getRGB(x,y);
    int[] A = new int[3];
    A[0] = (rgb >> 16) & 0xFF;
    A[1] = (rgb >> 8) & 0xFF;
    A[2] = rgb & 0xFF;
    return A;
  }
  
  // detect Tetromino using pixels in rect from screen
  public static int detectTetrominoSimple() {
    BufferedImage Img = robot.createScreenCapture(TetrisRect);
    for (int x = 0; x < TetrisRect.width; x++) {
      for (int y = 0; y <  TetrisRect.height; y++) {
        int ret = matchTetromino(getRGB(Img, x, y));
        if (ret != -1) return ret;
      }
    }
    return -1; // none detected
  }

  // detect current Tetris board configuration
  // the different types of the Tetrominos are marked with
  // integers i = 10 through 16, i representing Tetromino type i - 10
  public static TetrisBoard detectTetrisBoard() {
    int[][] A = new int[TetrisBoard.BOARDH][TetrisBoard.BOARDH];

    BufferedImage Img = robot.createScreenCapture(TetrisRect);
    int hstep = TetrisRect.width  / TetrisBoard.BOARDW;
    int vstep = TetrisRect.height / TetrisBoard.BOARDH;
    int hmargin = Math.max(hstep / 14, 1);
    int vmargin = Math.max(vstep / 14, 1);

    System.out.println(TetrisRect.toString());
    System.out.println("hstep " + hstep + " vstep " + vstep + " hmargin " + hmargin + " vmargin " + vmargin);
    System.out.println("WIDTH " + Img.getWidth() + " HEIGHT " + Img.getHeight());
    
    for (int j = 0; j < TetrisBoard.BOARDH; j++) {
      for (int i = 0; i < TetrisBoard.BOARDW; i++) {
        int ret = -1;
        for (int y = vmargin + j * vstep; y < (j+1) * vstep - vmargin; y++) {
          for (int x = hmargin + i * hstep; x < (i+1) * hstep - hmargin; x++) {
            ret = matchTetromino(getRGB(Img, x, y));
            System.out.println("("+x+","+y+") ("+i+","+j+")");
            if (ret != -1) {
              // found Tetromino, break condition for loops
              System.out.println("F  x "+x+" y "+y+" i "+i+" j "+j);
              x = TetrisRect.width;
              y = TetrisRect.height;
            }
          }
        }
        A[j][i] = (ret == -1) ? 0 : ret + 10;
      }
    }
    // DEBUG
    for (int j = 0; j < TetrisBoard.BOARDH; j++) {
      for (int i = 0; i < TetrisBoard.BOARDW; i++) {
        System.out.print(A[j][i] + " ");
      }
      System.out.println("");
    }
    System.out.println("");
    
    return new TetrisBoard(A);
  }
  // /DEBUG
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

    robot.delay(KEYDELAY);
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


  // initialize robot
  public static void initRobot() {
    try { 
      robot = new Robot();
    }
    catch (AWTException e) {
      System.out.println("Error while constructing Robot object");
    }
    // sets the number of milliseconds this robot sleeps after
    // generating an event.
    robot.setAutoDelay(AUTODELAY);
    // sets whether this robot automatically invokes waitForIdle 
    // after generating an event.
    robot.setAutoWaitForIdle(true);
  }


  
  public static void main(String[] args) throws AWTException {
    // initialize robot
    initRobot();

    // initialize the Tetris board
    TetrisBoard currBoard = new TetrisBoard();

    // get rectangle TetrisRect specifying the Tetris board
    chooseRectangle(3, 2);

    // currently we only look at the Tetris board for
    // determining the current Tetromino, the
    // "next" window is ignored
    int type;
    for (int t = 1; ; t++) {

      type = detectTetrominoSimple();
      if (type == -1) {
        System.out.println("none detected");
        continue;
      }

      TetrisMove move = TetrisStrategy.computeBestMove(currBoard, type);
      doMove(move, type);
      currBoard.dropTetromino(type, move.rot, move.pos, currBoard.tilemarker++);
      currBoard.clearLinesUpdateHeight();
      currBoard.printFullBoard();
      // for testing :
      detectTetrisBoard().printFullBoard();
      
      System.out.println("SCORE: " + currBoard.score);
    }

  }
}