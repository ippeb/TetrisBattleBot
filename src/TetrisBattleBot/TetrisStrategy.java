// The aim of the game is to maximize the number of subsequent filled lines
// some thoughts on the implemented strategy:
//
// These factors influence the costFunction:
// * Height is bad
// * Holes are bad
// * Stacking more blocks on top of holes is bad
// * Holes that are horizontally adjacent to other holes are not quite as bad
// * Holes that are vertically aligned with other holes are not quite as bad
// * Tall 1-wide troughs are bad
// * 1-wide troughs are not so bad if they are only 1 or 2 deep. 
//   Think about which pieces could fill a 2-deep trough -- 1, 2, or 3 out of the 
//   7 pieces depending on the two sides of the trough. Concentrate on issues that 
//   are near the current top of the pile. Holes that 10 levels below the top edge 
//   are not as important as holes that are immediately below the top edge.

package TetrisBattleBot;

public class TetrisStrategy {
  // returns the cost of placing the Tetromino type i, rotated by j*90 degrees at position x=k 
  public static int costFunction(TetrisBoard TB, int i, int j, int k, boolean DEBUG) {
    if (DEBUG) {
      System.out.println("cost fn ("+i+", "+j+", "+k+")");
      TetrisBoard.printTetromino(i);
    }
    
    int cost = Integer.MAX_VALUE;
    int ret = TB.dropTetromino(i,j,k,TB.tilemarker);
    if (ret >= 0) {
      int countLines = TB.countLines(); // number of full lines
      int hole       = TB.hole();       // number of holes
      // maximal height (value from 0 to BOARDH (exclusive), as seen from the bottom)
      int maxHeight  = TetrisBoard.BOARDH - (TB.maxHeight() - TetrisBoard.TMARGIN) - 1;
      int currHeight = TetrisBoard.BOARDH - (TB.getHeight(k) - TetrisBoard.TMARGIN) - 1; // height at pos k
      // count for all rows (from left to right) in TetrisBoard.Board
      // number of changes from value 0 (empty) to a positive value (filled)
      int horizontalDiff = TB.horizontalDiff(); 
      // count for all columns (from top to bottom) in TetrisBoard.Board
      // number of changes from value 0 (empty) to a positive value (filled)
      int verticalDiff = TB.verticalDiff();
      // the cost function for a board configuration
      cost =  verticalDiff * 100 * 100 + (horizontalDiff+hole) * 100 + maxHeight * 80 + currHeight;
      if (!TB.filledlinebef && countLines == 1) cost += 100;
      if ((TB.filledlinebef && countLines >= 1) || countLines >= 2) {
        cost = 0;
      }
    }
    
    TB.deleteTile(TB.tilemarker);
    
    if (DEBUG) 
      System.out.println("cost " + cost);
    return cost;
  }
  
  // compute best move based on minimizing the costFunction(i,j,k)
  // argument type denotes the Tetromino type (value from 0 to 6, inclusive)
  public static TetrisMove computeBestMove(TetrisBoard TB, int type) {
    int[][][] Cost = new int [TetrisBoard.LMARGIN + TetrisBoard.BOARDW + TetrisBoard.RMARGIN][7][4];
    // among all possible placements of the current Tetromino
    // pick the one which minimizes the cost function.
    
    for (int k = 0; k < 4; k++) { // every rotation
      for (int i = TetrisBoard.LMARGIN - 4; i < TetrisBoard.LMARGIN + TetrisBoard.BOARDW + 4; i++) { // every position
        // System.out.println("|rot " + k + " pos " + i);
        Cost[i][type][k] = costFunction(TB, type, k, i, false);
        // System.out.println("cost " + Cost[i][type][k]);
      }
    }
    
    int minCost = Integer.MAX_VALUE;
    TetrisMove bestMove = new TetrisMove(-1, -1);
    for (int k = 0; k < 4; k++)
      for (int i = TetrisBoard.LMARGIN - 4; i < TetrisBoard.LMARGIN + TetrisBoard.BOARDW + 4; i++) { // every position
        if (Cost[i][type][k] < minCost) {
          minCost = Cost[i][type][k];
          bestMove.update(k, i);
        }
      }
    
    // DEBUG - just for debugging 
    costFunction(TB, type, bestMove.rot, bestMove.pos, true);
    // DEBUG
    
    return bestMove;
  }
}