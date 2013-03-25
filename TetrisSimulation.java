package TetrisBattleBot;

import java.awt.Robot;

// offline simulation
public class TetrisSimulation {
    public static void main(String[] args) throws java.awt.AWTException {
	TetrisBoard TB = new TetrisBoard();
	TetrisStrategy TS = new TetrisStrategy();
	Robot robot = new Robot();
	
	TB.printFullBoard();
	for (int t = 1; t <= 300; t++) {
	    int randomType = TetrisRandom.type();
	    TetrisMove Move = TS.computeBestMove(TB, randomType);
	    
	    System.out.println("BEST MOVE");
	    System.out.println(Move.toString());
	    
	    int ret = TB.dropTetromino(randomType , Move.rot, Move.pos, TB.tilemarker++);
	    TB.clearLinesUpdateHeight();

	    System.out.println("drop " + randomType);
	    if (ret < 0) 
		throw new IllegalArgumentException();
	    TB.printFullBoard();
	    System.out.println("ROUND " + t);
	    System.out.println("SCORE: " + TB.score);       
	    System.out.println();
	    robot.delay(10);
	}

	//	System.out.println("Total Score: " + TB.score);
    }

}
