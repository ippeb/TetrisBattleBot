// This program is responsible for the interaction with the Flash game, i.e.
// Input: Reading the screen 
// Output: Applying key strokes for rotation, drop and hold button, mouse clicks 
// for focusing on the browser window (if necessary)

package TetrisBattleBot;

import java.awt.*;
import java.lang.Math;
import java.awt.image.BufferedImage;

public class WebsiteInteraction {

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
    public static void countdown(int s, Robot robot) {
	for (int i = s; i >= 1; i--) {
	    System.out.println(i + "sec...");
	    robot.delay(1000);
	}
    }

    // sets appropriate values for pt1 and pt2
    public static void chooseRectangle(Robot robot, Point pt1, Point pt2) { 
	//	countdown(2, robot);
	Point tmp;
	tmp = MouseInfo.getPointerInfo().getLocation();
	pt1.x = tmp.x; 
	pt1.y = tmp.y;
	System.out.println(pt1.toString());

	countdown(2, robot);	

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
    public static int detectTetromino(Robot robot, Rectangle rect) {
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
    


    public static void  pressKey(MoveType t, Runtime rt) {
	try {
	    /*
	      AppleScript seems not to be working... because Google Chrome gives each (tab and) plug-in
	      its own process to work the click only affects the current tab but isn't 
	      caught by the flash plug-in. 
	    */
	    
	    System.out.println(t.toString());

	    switch (t) {       
	    case UP:    rt.exec("./TetrisBattleBot/Keyboard/QuartzEventServices/up");
		break;
	    case DOWN:  rt.exec("./TetrisBattleBot/Keyboard/QuartzEventServices/down");
		break;
	    case LEFT:  rt.exec("./TetrisBattleBot/Keyboard/QuartzEventServices/left");
		break;
	    case RIGHT: rt.exec("./TetrisBattleBot/Keyboard/QuartzEventServices/right");
		break;
	    case SHIFT: rt.exec("./TetrisBattleBot/Keyboard/QuartzEventServices/shift");
		break;
	    case SPACE: rt.exec("./TetrisBattleBot/Keyboard/QuartzEventServices/space");
		break;
	    case CTRL:  rt.exec("./TetrisBattleBot/Keyboard/QuartzEventServices/ctrl");
		break;
	    case CLICK: rt.exec("./TetrisBattleBot/Mouse/MouseTools -leftClick");
		break;
	    }
	} catch (Throwable trw)
	    {
		trw.printStackTrace();
	    }
    }
    
    public static void clickPressKey(MoveType t, Runtime rt, Robot robot) {
	// Setting the delays...
	//
	// Version 4
	// no click, 1 time robot.delay in clickPressKey: 200
	// additional delay 200 at beginning of doMove(...) method
	// delay in QuartzEventServices: 50000000L

	// Version 5
	// no click, 1 time robot delay ni cilckPressKey: 100
	// SPACEDELAY 250
	// delay in QuartzEventServices: 50000000L

	// robot.delay(200);
	//pressKey(MoveType.CLICK, rt);
	robot.delay(400); // or: 100
	pressKey(t, rt);
    }
    
    public static void doMove(TetrisMove move, Robot robot, int type) {
	final int SPACEDELAY = 500;
	robot.setAutoWaitForIdle(true);
	Runtime rt = Runtime.getRuntime();
	// extra delay after pressing "SPACE"
	robot.delay(SPACEDELAY); // must be done in the beginning (because of detection of Tetromino)
	
	if (move.rot <= 3) { // instead of 2 - deactivate CTRL
	    for (int i = 0; i < move.rot; i++) {
		clickPressKey(MoveType.UP, rt, robot);
	    }
	} 
	/*	else { // move.rot == 3
	    clickPressKey(MoveType.CTRL, rt, robot);
	}
	*/
	int pos; // default starting position
	if (type == 0) pos = TetrisBoard.LMARGIN + 4;
	else pos = TetrisBoard.LMARGIN + 3;
	
	if (move.pos >= pos) {
	    for (int i = pos; i < move.pos; i++) {
		clickPressKey(MoveType.RIGHT, rt, robot);
	    }
	}
	else {
	    for (int i = pos; i > move.pos; i--) {
		clickPressKey(MoveType.LEFT, rt, robot);
	    }
	}
	clickPressKey(MoveType.SPACE, rt, robot);
    }

    
    
    public static void main(String[] args) throws AWTException {
	
	// mark the boundaries of the rectangle of the very first Tetromino
	Point ptf1 = new Point(), ptf2 = new Point();
	// rectangle formed by ptf1, ptf2
	Rectangle rectf = new Rectangle();
	// mark the boundaries of the rectangle which is used for looking up 
	// the next Tetromino
	Point pt1 = new Point(), pt2 = new Point();
	// rectangle formed by pt1 and pt2
	Rectangle rect = new Rectangle();
	
	Robot robot = new Robot();
	TetrisBoard currBoard = new TetrisBoard();
	TetrisStrategy strategy = new TetrisStrategy();
	
	countdown(3, robot);
	// Capture very first Tetromino
	System.out.println("first rectangle");
	chooseRectangle(robot, ptf1, ptf2);
	setRectangle(ptf1, ptf2, rectf);
	System.out.println("------------------");
	countdown(3, robot);
	
	// Capture pixel in the "hold" box by moving your mouse to that position
	// the color of the tile identifies its type 
	// point  = (MouseInfo.getPointerInfo()).getLocation();
	
	System.out.println("second rectangle");
	chooseRectangle(robot, pt1, pt2);
	setRectangle(pt1, pt2, rect);

	int type;
	for (int t = 1; ; t++) {
	    if (t == 1) {
		type = detectTetromino(robot, rectf);
	    }
	    else {
		type = detectTetromino(robot, rect);
	    }
	    if (type == -1) {
		System.out.println("none detected");
		continue;
	    }
	    TetrisMove move = strategy.computeBestMove(currBoard, type);
	    doMove(move, robot, type);
	    currBoard.dropTetromino(type, move.rot, move.pos, currBoard.tilemarker++);
	    currBoard.clearLinesUpdateHeight();
	    currBoard.printFullBoard();
	    System.out.println("SCORE: " + currBoard.score);
	}

    }
}