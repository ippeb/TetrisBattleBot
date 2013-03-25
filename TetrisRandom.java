// This class provides methods for simulating 
// the next (randomly chosen) Tetromino in 
// the input.

package TetrisBattleBot;

public class TetrisRandom {
    
    // generate a random Tetromino type
    public static int type() {
	return (int) (Math.random() * 7);
    }

    // generate a random rotation
    public static int rotation() {
	return (int) (Math.random() * 4);
    }

    // generate a random Tetromino (0 to 6) with random rotation (0 to 3)
    public static int Tetromino() {
	return (int) (Math.random() * 24.0);
    }

    // generate a random placement of a Tetromino
    public static  int placement() {
	return (int) (Math.random() * (TetrisBoard.BOARDW-4)) + TetrisBoard.LMARGIN;
    }
}