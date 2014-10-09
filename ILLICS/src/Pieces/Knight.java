package Pieces;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Implements the Knight class.
 * 
 * The specifications of the Knight's movement
 * are considered when obtaining a list of valid moves.
 * Refer to inherited Piece class for documentation 
 * on how the functions work. The documentation
 * can be found in Piece.java. 
 * 
 * @author kartikhegde
 *
 */

public class Knight extends Piece {
	/**Total directions*/
	int totalDirections = 2;
	
	/**
	 * Constructor for the Knight.
	 */
	public Knight(Point loc, char t, int c) {
		location = new Point(loc);
		type = t;
		color = c; 
		name = "Knight";
		if (color == 0) //White
			unicodeStr = "\u2658";
		else if (color == 1) //Black
			unicodeStr = "\u265E";
		else 
			unicodeStr = "N: No Color";
	}

	

	
	public Vector<String> getValidMoves(Piece[][] board, Point curr) {
		Vector<String> moves = new Vector<String>();
		int currRow = curr.x;
		int currCol = curr.y;
		moves.addAll(checkFourVerticalSquares(board, currRow, currCol));
		moves.addAll(checkFourHorizontalSquares(board, currRow, currCol));
		return moves;
	}




	Vector<String> checkFourVerticalSquares(Piece [][] board, int currRow, int currCol) {
		
		/**The directions allow the a piece to to:
		 * 1. Move into a region East of the piece's current location but not on the pieces's current row
		 * 2. Move into a region West of the piece's current but not on the pieces's current row
		 * 3. Move along the vertical of the column or horizontal line or the row
		 */
		int totalDirections = 2;
		
		Vector<String> vertMoves = new Vector<String>();
		
		/**The capturedPiece could be a sentinel Space piece or a defined Chess piece*/
		Piece capturedPiece;
		for (int horDist = -1; horDist < totalDirections; horDist++) {
			for (int vertDist = -1; vertDist < totalDirections; vertDist++) {
				if (horDist == 0 || vertDist == 0)
					continue;
				try {
					int stepSize = 2;
					/**Set up a point for potential location of movement*/
					Point potLoc = new Point(currRow + horDist, currCol + stepSize*vertDist);
					if (board[potLoc.x][potLoc.y].color != this.color) {
						capturedPiece = board[potLoc.x][potLoc.y];
						board[currRow][currCol] = new Space(new Point(currRow, currCol), ' ', -1);
						board[potLoc.x][potLoc.y] = this;
						/**Save current location*/
						Point currLoc = this.location;
						this.location = potLoc;
						if (kingIsSafe(board, this)) {
							vertMoves.add(""+currRow+","+currCol+","+potLoc.x+","+potLoc.y+","+capturedPiece.type);
						}
						/**
						 * Restore the position of the Bishop to its original 
						 * location (currRow, currCol) so that we can find 
						 * other possible moves.
						 */
						board[currRow][currCol] = this;
						board[potLoc.x][potLoc.y] = capturedPiece;
						this.location = currLoc;
					}
					
				} catch(Exception e) {
					//Do nothing
				}
			}
		}
		return vertMoves;
	}
	
	Vector<String> checkFourHorizontalSquares(Piece [][] board, int currRow, int currCol) {
		
		/**The directions allow the a piece to to:
		 * 1. Move into a region East of the piece's current location but not on the pieces's current row
		 * 2. Move into a region West of the piece's current but not on the pieces's current row
		 * 3. Move along the vertical of the column or horizontal line or the row
		 */
		int totalDirections = 2;
		
		Vector<String> horMoves = new Vector<String>();
		
		/**The capturedPiece could be a sentinel Space piece or a defined Chess piece*/
		Piece capturedPiece;
		for (int horDist = -1; horDist < totalDirections; horDist++) {
			for (int vertDist = -1; vertDist < totalDirections; vertDist++) {
				if (horDist == 0 || vertDist == 0)
					continue;
				try {
					int stepSize = 2;
					/**Set up a point for potential location of movement*/
					Point potLoc = new Point(currRow + stepSize*horDist, currCol + vertDist);
					if (board[potLoc.x][potLoc.y].color != this.color) {
						capturedPiece = board[potLoc.x][potLoc.y];
						board[currRow][currCol] = new Space(new Point(currRow, currCol), ' ', -1);
						board[potLoc.x][potLoc.y] = this;
						/**Save current location*/
						Point currLoc = this.location;
						this.location = potLoc;
						if (kingIsSafe(board, this)) {
							horMoves.add(""+currRow+","+currCol+","+potLoc.x+","+potLoc.y+","+capturedPiece.type);
						}
						/**
						 * Restore the position of the Bishop to its original 
						 * location (currRow, currCol) so that we can find 
						 * other possible moves.
						 */
						board[currRow][currCol] = this;
						board[potLoc.x][potLoc.y] = capturedPiece;
						this.location = currLoc;	
					}
					
				} catch(Exception e) {
					//Do nothing
				}
			}
		}
		
		return horMoves;
	}

	Point getPositionIfKing(int color) {
		return null;
	}

}
