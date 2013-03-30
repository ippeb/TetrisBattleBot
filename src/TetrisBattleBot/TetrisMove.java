// This class saves the info associated with 
// a chosen move for a given Tetromino type, i.e.
// rotations and x coordinate.

package TetrisBattleBot;

// saves the info needed for a move
public class TetrisMove {
    TetrisMove(int rot, int pos) {
	update(rot, pos);
    }

    public int rot; // rotated by "rotation*90" degrees
    public int pos; // x coordinate
    
    public void update(int r, int p) {
	rot = r;
	pos = p;
    }

    public String toString(){
	return "rot " + rot + " pos " + pos;
    }
}