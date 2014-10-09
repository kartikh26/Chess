package Pieces;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Implements the King class.
 * 
 * The specifications of the King's movement
 * are considered when obtaining a list of valid moves.
 * Refer to inherited Piece class for documentation 
 * on how the functions work. The documentation
 * can be found in Piece.java. 
 * 
 * @author kartikhegde
 *
 */
public class King extends Piece {
	
	boolean moved;
	
	/**There are nine different locations King can move (including it's current location).*/
	int directions = 9; 
	
	/**
	 * Constructor for the King.
	 * @throws  
	 */
	public King(Point loc, char t, int c) {
		location = new Point(loc);
		type = t;
		color = c; 
		moved = false;
		name = "King";
		
		if (color == 0) //White
			unicodeStr = "\u2654";
		else if (color == 1) //Black
			unicodeStr = "\u265A";
		else 
			unicodeStr = "K: No Color";
	}

	
	public Vector<String> getValidMoves(Piece[][] board, Point curr) {
		Vector<String> moves = new Vector<String>();
		int currRow = curr.x;
		int currCol = curr.y;
		/**The capturedPiece could be a sentinel Space piece or a defined Chess piece*/
		Piece capturedPiece; 
		for (int i = 0; i < directions; i++) {
				try {
					/**Set up a point for potential location*/
					Point potLoc = new Point((currRow-1)+(i/3), (currCol-1)+(i%3));
					/**Makes sure King doesn't move to its current location*/
					if (board[potLoc.x][potLoc.y].color != this.color) {
						capturedPiece = board[potLoc.x][potLoc.y];
						board[currRow][currCol] = new Space(new Point(currRow, currCol), ' ', -1);
						board[potLoc.x][potLoc.y] = this;
						Point temp = this.location;
						this.location = potLoc;
						if (kingIsSafe(board, this)) {
							moves.add(""+currRow+","+currCol+","+potLoc.x+","+potLoc.y+","+capturedPiece.type);
						}
						/**
						 * Restore the position of the King to its original 
						 * location (currRow, currCol) so that we can find 
						 * other possible moves.
						 */
						board[currRow][currCol] = this;
						board[potLoc.x][potLoc.y] = capturedPiece;
						this.location = temp;
					}
				} catch(Exception e) {
					//Do nothing
				}
		}
		return moves;
	}


	Point getPositionIfKing(int color) {
		//System.out.println("CURRENT KING: " + this.type + " color we are looking for: " + color);
		if (this.color == color) {
			//System.out.println("RETURNING THIS KINGS LOCATION");
			return location;
		}
		return null;
	}

}
