package Pieces;
import java.awt.Image;
import java.awt.Point;
import java.util.Vector;

import javax.swing.ImageIcon;

/**
 * Implements the Bishop class.
 * 
 * The specifications of the Bishop's movement
 * are considered when obtaining a list of valid moves.
 * Refer to inherited Piece class for documentation 
 * on how the functions work. The documentation
 * can be found in Piece.java. 
 * 
 * @author kartikhegde
 *
 */
public class Bishop extends Piece {

	/**The directions allow the Bishop to to:
	 * 1. Move into a region East of the Bishop's current location but not on the pieces's current row
	 * 2. Move into a region West of the Bishop's current but not on the pieces's current row
	 * 3. Move along the vertical of the column or horizontal line or the row
	 * 
	 * A Bishop cannot move along the vertical and horizontal line and so we place 
	 * those restrictions below when calculating the valid moves of the Bishop in the
	 * getValidMoves() function.
	 * 
	 * There are three directions altogether, but since we count the first direction as index 0,
	 * the total number of directions is 2.
	 */
	int totalDirections = 2;
	
	/**
	 * Constructor for the Bishop.
	 */
	public Bishop(Point loc, char t, int c) {
		location = new Point(loc);
		type = t;
		color = c; 
		name = "Bishop";
		if (color == 0) //White
			unicodeStr = "\u2657";
		else if (color == 1) //Black
			unicodeStr = "\u265D";
		else 
			unicodeStr = "B: No Color";
	}

	
	/** There are 4 directions in total (denoted by a horizontal and vertical direction):
	 *  NE  = horizontal = 1;  vertical = 1
	 *  NW  = horizontal = -1; vertical = 1
	 *  SE  = horizontal = 1;  vertical = -1
	 *  SW  = horizontal = -1; vertical = -1
	 */ 
	public Vector<String> getValidMoves(Piece[][] board, Point curr) {
		Vector<String> moves = new Vector<String>();
		int currRow = curr.x;
		int currCol = curr.y;
		/**The capturedPiece could be a sentinel Space piece or a defined Chess piece*/
		Piece capturedPiece; 
		
		for (int horizontal = -1; horizontal < totalDirections; horizontal++) {
			for (int vertical = -1; vertical < totalDirections; vertical++ ) {
				/**If either of the directions are 0, the Bishop can't perform the move*/
				if (horizontal == 0 || vertical == 0)
					continue;
				/**The distance we will be away from the Queen.*/
				int stepSize = 1;
				try {
					/**Set up a point for potential location of movement*/
					Point potLoc = new Point(currRow + stepSize*horizontal, currCol + stepSize*vertical);
					/**The while check takes care of the fact that the piece won't move to its current location*/
					while (board[potLoc.x][potLoc.y].color != this.color) {
						capturedPiece = board[potLoc.x][potLoc.y];
						board[currRow][currCol] = new Space(new Point(currRow, currCol), ' ', -1);
						board[potLoc.x][potLoc.y] = this;
						/**Save current location*/
						Point currLoc = this.location;
						this.location = potLoc;
						if (kingIsSafe(board, this)) {
							moves.add(""+currRow+","+currCol+","+potLoc.x+","+potLoc.y+","+capturedPiece.type);
						}
						/**
						 * Restore the position of the Bishop to its original 
						 * location (currRow, currCol) so that we can find 
						 * other possible moves.
						 */
						board[currRow][currCol] = this;
						board[potLoc.x][potLoc.y] = capturedPiece;
						this.location = currLoc;
						
						/**All Space colors are equal to -1*/	
						if (board[potLoc.x][potLoc.y].color != -1) {
							break;
						}
						/**Increase stepSize by 1*/
						stepSize++;
						/**Find the new potential location point after increasing stepSize*/
						potLoc = new Point(currRow + stepSize*horizontal, currCol + stepSize*vertical);
					}
				} catch (Exception e) {
					//Do nothing
				}
			}
		}
		return moves;
	}


	
	Point getPositionIfKing(int color) {
		return null;
	}


}
