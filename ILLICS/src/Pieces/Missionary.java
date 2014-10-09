package Pieces;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Implements the Missionary class.
 * 
 * Missionary is a fictional Chess piece that has
 * the following functionality: 
 * 
 * 1) A Missionary takes an opponent's piece
 * by converting the piece to the Missionary's
 * color. 
 * 
 * 2) Missionary can move one or two spots in
 * any direction.
 * 
 * 3) When a Missionary converts a piece, both
 * the Missionary and piece leave the board
 * and the piece is replaced by a piece of the
 * same type but of the Missionary's color. 
 * 
 * Example: If Black Missionary attacks White
 * Rook two spots diagonally, then both Black
 * Missionary and White Rook leave the board,
 * and a Black Rook is placed at the previous
 * location of the White Rook.
 * 
 * 4) Converting a King results in a Checkmate.
 * 
 * 5) The only piece a Missionary can kill is
 * another Missionary.
 * 
 * @author kartikhegde
 *
 */
public class Missionary extends Piece{

	int totalDirections = 2;
	public Missionary(Point loc, char t, int c) {
		location = new Point(loc);
		type = t;
		color = c; 
		name = "Missionary";
		
		if (color == 0) //White
			unicodeStr = "\u039C";
		else if (color == 1) //Black
			unicodeStr = "\u03BC";
		else 
			unicodeStr = "M: No Color";
	}

	
	public Vector<String> getValidMoves(Piece[][] board, Point curr) {
		Vector<String> moves = new Vector<String>();
		int currRow = curr.x;
		int currCol = curr.y;
		/**The capturedPiece could be a sentinel Space piece or a defined Chess piece*/
		Piece capturedPiece; 
		for (int horizontal = -1; horizontal < totalDirections; horizontal++) {
			for (int vertical = -1; vertical < totalDirections; vertical++ ) {
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
						 * Restore the position of the King to its original 
						 * location (currRow, currCol) so that we can find 
						 * other possible moves.
						 */
						board[currRow][currCol] = this;
						board[potLoc.x][potLoc.y] = capturedPiece;
						this.location = currLoc;
						
						/**All Space colors are equal to -1*/	
						if (board[potLoc.x][potLoc.y].color != -1 || stepSize == 2) {
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

	/**The Missionary's "move" functionality is more complex
	 * than the default move; hence, it implemented in
	 * Missionary class while other pieces all share the
	 * same move funtionality described in Piece.java.
	 */
	public Piece move(Piece [][] board, Point curr, Point dest) {
		board[curr.x][curr.y] = new Space(curr, ' ', -1);
		Piece piece = board[dest.x][dest.y];
		if (piece.color >= 0) { //Not a space
			changeColor(piece);
			changeType();
			changeName();
		}
		else { //Defaut Behavior
			board[dest.x][dest.y] = this;
			this.location.x = dest.x;
			this.location.y = dest.y;
		}
		return piece;
	}

	private void changeColor(Piece piece) {
		if (piece.color == 1)
			piece.color = 0;
		else
			piece.color = 1;	
	}
	
	private void changeType() {
		//TODO: HashMap 
	}
	
	private void changeName() {
		//TODO: HashMap
	}

}
