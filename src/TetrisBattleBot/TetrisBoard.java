// This class deals with everything concerning the Tetris board 
// such as saving the current board state and simulating a Tetromino
// drop and Tetromino rotation. 
//
// Other methods include counting and clearing the number of 
// filled lines (rows).

package TetrisBattleBot;

public class TetrisBoard {
  //
  // Types of Tetrominoes
  //
  // 0  **    1 **     2    **   3    *      4  ****   5 *        6   *
  //    **       **        **        ***                 ***        ***
  //
  public static final int[][][] TETROMINO = { // [i][j][k], Tetromino type: i/4, rotated by (i mod 4)*90 degrees, [j][k] 2D int array with j=0..3, k=0..3
    {{0,0,0,0},{0,0,0,0},{1,1,0,0},{1,1,0,0}}, {{0,0,0,0},{0,0,0,0},{1,1,0,0},{1,1,0,0}}, {{0,0,0,0},{0,0,0,0},{1,1,0,0},{1,1,0,0}}, {{0,0,0,0},{0,0,0,0},{1,1,0,0},{1,1,0,0}}, // 0
    {{0,0,0,0},{0,0,0,0},{1,1,0,0},{0,1,1,0}}, {{0,0,0,0},{0,0,1,0},{0,1,1,0},{0,1,0,0}}, {{0,0,0,0},{0,0,0,0},{1,1,0,0},{0,1,1,0}}, {{0,0,0,0},{0,1,0,0},{1,1,0,0},{1,0,0,0}}, // 1
    {{0,0,0,0},{0,0,0,0},{0,1,1,0},{1,1,0,0}}, {{0,0,0,0},{0,1,0,0},{0,1,1,0},{0,0,1,0}}, {{0,0,0,0},{0,0,0,0},{0,1,1,0},{1,1,0,0}}, {{0,0,0,0},{1,0,0,0},{1,1,0,0},{0,1,0,0}}, // 2
    {{0,0,0,0},{0,0,0,0},{0,1,0,0},{1,1,1,0}}, {{0,0,0,0},{0,1,0,0},{0,1,1,0},{0,1,0,0}}, {{0,0,0,0},{0,0,0,0},{1,1,1,0},{0,1,0,0}}, {{0,0,0,0},{0,1,0,0},{1,1,0,0},{0,1,0,0}}, // 3
    {{0,0,0,0},{0,0,0,0},{0,0,0,0},{1,1,1,1}}, {{0,0,1,0},{0,0,1,0},{0,0,1,0},{0,0,1,0}}, {{0,0,0,0},{0,0,0,0},{0,0,0,0},{1,1,1,1}}, {{0,1,0,0},{0,1,0,0},{0,1,0,0},{0,1,0,0}}, // 4
    {{0,0,0,0},{0,0,0,0},{1,0,0,0},{1,1,1,0}}, {{0,0,0,0},{0,1,1,0},{0,1,0,0},{0,1,0,0}}, {{0,0,0,0},{0,0,0,0},{1,1,1,0},{0,0,1,0}}, {{0,0,0,0},{0,1,0,0},{0,1,0,0},{1,1,0,0}}, // 5
    {{0,0,0,0},{0,0,0,0},{0,0,1,0},{1,1,1,0}}, {{0,0,0,0},{0,1,0,0},{0,1,0,0},{0,1,1,0}}, {{0,0,0,0},{0,0,0,0},{1,1,1,0},{1,0,0,0}}, {{0,0,0,0},{1,1,0,0},{0,1,0,0},{0,1,0,0}}, // 6
  };

  //
  // Board Layout
  //
  // /-----------------------------\
  // |         TMARGIN             |
  // |                             |
  // |        /------\             |
  // |        |BOARDW|             |
  // |        |      |             |
  // |        |      |             |
  // |        |B     |             |
  // |        |O     |             |
  // |        |A     |             |
  // |LMARGIN |R     |    RMARGIN  |
  // |        |D     |             |
  // |        |H     |             |
  // |        |      |             |
  // |        |      |             |
  // |        \------/             |
  // |                             |
  // |         BMARGIN             |
  // |                             |
  // \-----------------------------/
  //
  public static final int BOARDH  = 20; // height
  public static final int BOARDW  = 10; // width 
  public static final int LMARGIN = 20; // left margin
  public static final int RMARGIN = 20; // right margin
  public static final int TMARGIN = 20; // top margin
  public static final int BMARGIN = 20; // bottom margin

  private int[][] Board; // board, 0 unfilled, i > 0 filled
  private int[]  Height; // height at pos  x
  public  int tilemarker = 22;
  // 1 is used for wall boundaries at initialization
  // 10 ... 16 is used by method detectTetrisBoard
  // used in TetrisWebsiteInteraction.java
  public  int score = 0; // sum of all max(number of subsequently cleared lines - 1, 0)
  public  boolean filledlinebef = false; // saves if a line was filled in the previous turn

  // no-argument constructor
  public TetrisBoard() {
    Board = new int [BOARDH + TMARGIN + BMARGIN] [BOARDW + LMARGIN + RMARGIN];

    for (int j = 0; j < BOARDH + TMARGIN + BMARGIN; j++)
      for (int i = 0; i < BOARDW + LMARGIN + RMARGIN; i++) 
        Board[j][i] = 1;
    for (int j = 0; j < BOARDH + TMARGIN; j++)
      for (int i = LMARGIN; i < BOARDW + LMARGIN; i++) 
        Board[j][i] = 0;

    Height = new int [BOARDW + LMARGIN + RMARGIN];
    for (int i = 0; i < BOARDW + LMARGIN + RMARGIN; i++)
      Height[i] = TMARGIN + BOARDH;
  }

  // constructor that initializes the Tetris board with
  // an int array of dimensions BOARDH x BOARDW
  public TetrisBoard(int[][] A) {
    Board = new int [BOARDH + TMARGIN + BMARGIN] [BOARDW + LMARGIN + RMARGIN];

    for (int j = 0; j < BOARDH + TMARGIN + BMARGIN; j++)
      for (int i = 0; i < BOARDW + LMARGIN + RMARGIN; i++) 
        Board[j][i] = 1;
    for (int j = 0; j < TMARGIN; j++)
      for (int i = LMARGIN; i < BOARDW + LMARGIN; i++) 
        Board[j][i] = 0;
    for (int j = TMARGIN; j < BOARDH + TMARGIN; j++)
      for (int i = LMARGIN; i < BOARDW + LMARGIN; i++) 
        Board[j][i] = A[j - TMARGIN][i - LMARGIN];

    Height = new int [BOARDW + LMARGIN + RMARGIN];
    updateHeight();
  }    
  
  // compare current Tetris board to argument,
  // only the contents of the board is compared
  // not included are:
  // * fields within top, left, right and bottom margin
  // * Height array
  // * member variables such as "score", "filledlinebef",
  //   "tilemarker", etc.
  public int compare(TetrisBoard TB) {
    int diff = 0;
    for (int j = TMARGIN; j < BOARDH + TMARGIN; j++)
      for (int i = LMARGIN; i < BOARDW + LMARGIN; i++) 
        if (!( (Board[j][i] == 0 && TB[j][i] == 0) ||
               (Board[j][i]  > 0 && TB[j][i]  > 0) ))
          diff++;
    return diff;
  }
  
  // get height at position "pos"
  public int getHeight(int pos) {
    return Height[pos];
  }

  // count for all rows (from left to right) in TetrisBoard.Board
  // number of changes from value 0 (empty) to a positive value (filled)
  public int horizontalDiff() {
    int sol = 0;
    for (int i = TMARGIN; i < TMARGIN + BOARDH; i++) {
      boolean bef = true;
      for (int j = LMARGIN - 1; j < LMARGIN + BOARDW + 1; j++) {
        if (bef && Board[i][j] == 0) {
          sol++;
          bef = false;
        }
        else if (!bef && Board[i][j] > 0) {
          sol++;
          bef = true;
        }
      }
    }
    return sol;
  }

  // count for all columns (from top to bottom) in TetrisBoard.Board
  // number of changes from value 0 (empty) to a positive value (filled)
  public int verticalDiff() {
    int sol = 0;
    for (int i = LMARGIN; i < LMARGIN + BOARDW; i++) {
      boolean bef = false;
      for (int j = TMARGIN - 1; j < TMARGIN + BOARDH + 1; j++) {
        if (bef && Board[j][i] == 0) {
          sol++;
          bef = false;
        }
        else if (!bef && Board[j][i] > 0) {
          sol++;
          bef = true;
        }
      }
    }
    return sol;
  }

  // prints a Tetromino of type "type"
  public static void printTetromino(int type) {
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        System.out.print( TETROMINO[type*4][i][j]==0?" ":"x" );
      }
      System.out.println("");
    }
    System.out.println("");
  }


  // update the entire array Height
  public void updateHeight() {
    for (int i = LMARGIN; i < BOARDW + LMARGIN; i++) { 
      for (int j = 0; j < TMARGIN + BOARDH + 1; j++) {
        if (Board[j][i] != 0) {
          Height[i] = j;
          break;
        }
      }
    }
  }

  // set all elements of value marking to zero
  // returns true if marking was found
  public boolean deleteTile(int marking) {
    boolean found = false;
    for (int j = 0; j < BOARDH + TMARGIN; j++) 
      for (int i = LMARGIN; i < BOARDW + LMARGIN; i++) 
        if (Board[j][i] == marking) {
          found = true;
          Board[j][i] = 0;
        }
    updateHeight();
    return found;
  }


  // count filled horizontal lines
  public int countLines() {
    int linecount=0;
    for (int i = 0; i < TMARGIN + BOARDH; i++) {
      boolean zero = false;
      for (int j = LMARGIN; j < LMARGIN + BOARDW; j++) {
        if (Board[i][j] == 0) {
          zero = true;
          break;
        }
      }
      if (!zero) linecount++;
    }
    return linecount;
  }

  // update score and filledlinebef
  public void updateScore(int lcount) {
    if (lcount == 0) {
      filledlinebef = false;
      return ;
    }
    filledlinebef = true;
    if (filledlinebef) score += lcount;
    else score += lcount - 1;
  }

  // clear lines and update array Height
  // this function is (typically) used in the main method
  // just after a Tetromino was dropped ultimately
  public void clearLinesUpdateHeight() {
    boolean[] H = new boolean[TMARGIN + BOARDH + BMARGIN];
    for (int i = 0; i < TMARGIN + BOARDH; i++) {
      boolean zero = false;
      for (int j = LMARGIN; j < LMARGIN + BOARDW; j++) {
        if (Board[i][j] == 0) {
          zero = true;
          break;
        }
      }
      if (!zero) H[i] = true;
    }


    int lcount = 0;
    for (int i = TMARGIN + BOARDH - 1; i >= 0; i--) {
      if (H[i]){
        lcount++;
        continue;
      }
      if (lcount > 0) {
        for (int j = LMARGIN; j < LMARGIN + BOARDW; j++)
          Board[i+lcount][j] = Board[i][j];
      }
    }
    for (int i = 0; i < lcount; i++) {
      for (int j = LMARGIN; j < LMARGIN + BOARDW; j++) {
        Board[i][j] = 0;
      }
    }

    updateHeight();
    updateScore(lcount);
  }


  // return the number of "holes"
  // a hole is defined as a zero element in TetrisBoard that contains 
  // a nonzero element above it
  public int hole() {
    int hole = 0;
    for (int i = LMARGIN; i < BOARDW + LMARGIN; i++) {
      boolean found = false;
      for (int j = 0; j < BOARDH + TMARGIN; j++) {
        if (Board[j][i] != 0) found = true;
        else if (found == true) hole++;
      }
    }
    return hole;
  }

  // return the highest filled spot of Board
  // Beware: this is a misnomer, the largest height has the smallest value
  // since we consider e.g. h=1 higher than h=34
  public int maxHeight() { 
    int tmp = Integer.MAX_VALUE;
    for (int i = LMARGIN; i < LMARGIN + BOARDW; i++){
      if (Height[i] < tmp) tmp = Height[i];
    }
    return tmp;
  }

  // return the lowest filled spot of Board
  public int minHeight() { 
    int tmp = 0;
    for (int i = LMARGIN; i < LMARGIN + BOARDW; i++){
      if (Height[i] > tmp) tmp = Height[i];
    }
    return tmp;
  }

  // print the current Tetris board
  // mark wall boundaries as '#'
  public void printFullBoard(){
    for (int j = TMARGIN; j < TMARGIN + BOARDH + 1; j++){
      for (int i = LMARGIN - 1; i < LMARGIN + BOARDW + 1; i++){
        System.out.print(Board[j][i]==1?"#":(Board[j][i]==0?" ":Board[j][i]%10));
      }
      System.out.println("");
    }
    // System.out.println("Score " + score);
    /**/
    System.out.println("HEIGHT");
    for (int i = LMARGIN; i < LMARGIN + BOARDW; i++){
      System.out.print(" "+Height[i]+" ");
    }
    System.out.println("");
    /**/
  }

  // returns true if a Tetromino type i, rotated by j*90 degrees at position x=k, y=h
  // collides with a field on the TetrisBoard
  public boolean collide(int i, int j, int k, int h){
    for (int l = 0; l < 4; l++)
      for (int m = 0; m < 4; m++)
        if (Board[h+l][k+m]!=0 && TETROMINO[i*4+j][l][m]!=0) 
          return true;
    return false;
  }
   
  // insert Tetromino type j, rotated by j*90 degrees at position x=k, y=h in 2D TetrisBoard
  // returns false if there is an overlap or if Tetromino exceeds the boundaries
  // of the Tetris board
  public boolean insertTetromino(int i, int j, int k, int h, int marker){
    if (collide(i, j, k, h)) return false;
    for (int l = 0; l < 4; l++)
      for (int m = 0; m < 4; m++)
        if (TETROMINO[i*4+j][l][m]!=0)
          Board[h+l][k+m] = marker;
    return true;
  }

  // drops a Tetromino type i, rotated by j*90 degrees at position x=k with marking "marker"
  // returns y coordinate of Tetromino
  // returns -1 if Tetromino couldn't be dropped (e.g. overlapped boundary)
  public int dropTetromino(int i, int j, int k, int marker) {
    int h=Math.min(Math.min(Height[k], Height[k+1]), Math.min(Height[k+2], Height[k+3]))-4;

    // there must be a collision 
    for (; ; h++) {
      if (collide(i, j, k, h)) break;
    }
    h--;
    boolean ret = insertTetromino(i, j, k, h, marker);
    //	    System.out.println("problem with dropTetromino: type = " + i + " rot = " + j + " pos = " + k + " h = " + h);
    updateHeight();

    if (!ret) 
      return -1;
    else 
      return h;
  }
}
