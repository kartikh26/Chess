package Pieces;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Implements the Pawn class.
 * 
 * The specifications of the Pawn's movement
 * are considered when obtaining a list of valid moves.
 * Refer to inherited Piece class for documentation 
 * on how the functions work. The documentation
 * can be found in Piece.java. 
 * 
 * @author kartikhegde
 *
 */

public class Pawn extends Piece {

	int direction;
	boolean promote;
	Point startLoc;
	boolean moved;
	/**
	 * Constructor for the Pawn.
	 */
	public Pawn(Point loc, char t, int c) {
		location = new Point(loc);
		startLoc = new Point(loc);
		type = t;
		color = c; 
		name = "Pawn";	
		promote = false;
		if(color == 0) { //White
			direction = -1; //Backward
			unicodeStr = "\u2659";
		}
		else if (color == 1) {
			direction = 1; //Black
			unicodeStr = "\u265F";
		}
		else {
			direction = 0;
			unicodeStr = "P: No Color";
		}
		moved = false;
	}


	public Vector<String> getValidMoves(Piece[][] board, Point curr) {
		Vector<String> moves = new Vector<String>();
		int stepSize;

		/**If startLoc != curr then the piece did move*/
		if (startLoc.equals(curr)) { 		
			stepSize = 2 * (2 * this.color - 1); //Results in -2 for White Pawn and 2 for Black Pawn because white decreases in row and black increases in row count
		}
		else {
			stepSize = (2 * this.color - 1); //Results in -1 for White Pawn and 1 for Black Pawn because white decreases in row count and black increases in row count
		}
		
		moves.addAll(getForwardMoves(board, curr, stepSize));
		moves.addAll(getUpperMove(board, curr, stepSize, -1));
		moves.addAll(getUpperMove(board, curr, stepSize, 1));
		return moves;
	}

	Vector<String> getForwardMoves(Piece [][] board, Point currLoc, int stepSize) {
		Vector<String> forwardMoves = new Vector<String>();
		int currRow = currLoc.x;
		int currCol = currLoc.y;
		Piece capturedPiece;
		int step = stepSize/Math.abs(stepSize); //This division accounts for sign differences in stepSize
		while (Math.abs(step) <= Math.abs(stepSize)) {
				/**Set up a point for potential location of movement*/
				Point potLoc = new Point(currRow + step, currCol);
				try {
				if (board[potLoc.x][potLoc.y].color == -1) {
					capturedPiece = board[potLoc.x][potLoc.y]; //The captured piece should always be a space when moving forward
					board[currRow][currCol] = new Space(new Point(currRow, currCol), ' ', -1);
					board[potLoc.x][potLoc.y] = this;
					Point temp = this.location;
					this.location = potLoc;
					if (kingIsSafe(board, this)) {
						forwardMoves.add(""+currRow+","+currCol+","+potLoc.x+","+potLoc.y+","+capturedPiece.type);
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
				else {
					break;
				}
			}catch (Exception e) {
				//Do Nothing
			}
			/**The value of step will be incremented if Black and decremented if White*/
			step += (stepSize/Math.abs(stepSize));
		}
		return forwardMoves;
	}
	
	/**
	 *  Upper left move refers to the upper left location
	 *  viewed from the white piece perspective
	 * @param currLoc
	 * @param side Right side has the value +1 and Left side has the value -1
	 * @return
	 */
	Vector<String> getUpperMove(Piece [][] board, Point currLoc, int stepSize, int side) {
		Vector<String> upperMove = new Vector<String>();
		int currRow = currLoc.x;
		int currCol = currLoc.y;
		Piece capturedPiece;
		/**Set up a point for potential location of movement*/
		int direction = stepSize/Math.abs(stepSize); //Computing stepSize/abs(stepSize) gives direction {-1, 1}
		Point potLoc = new Point(currRow + direction, currCol - side);
		try {
			int pieceColor = board[potLoc.x][potLoc.y].color;
			if (pieceColor != this.color && pieceColor != -1) {
				capturedPiece = board[potLoc.x][potLoc.y]; //The captured piece should always be a space when moving forward
				board[currRow][currCol] = new Space(new Point(currRow, currCol), ' ', -1);
				board[potLoc.x][potLoc.y] = this;
				Point temp = this.location;
				this.location = potLoc;
				if (kingIsSafe(board, this)) {
					upperMove.add(""+currRow+","+currCol+","+potLoc.x+","+potLoc.y+","+capturedPiece.type);
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
		}
		catch (Exception e) {
			//Do Nothing
		}
		return upperMove;
	}
	
	Point getPositionIfKing(int color) {
		return null;
	}

}
