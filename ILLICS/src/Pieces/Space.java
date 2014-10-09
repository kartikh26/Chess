package Pieces;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Implements the sentinel Space piece class.
 * 
 * The space piece is created to enable 
 * polymorphism without having to check
 * for null in the getValidMoves() function
 * for defined chess pieces.
 */

public class Space extends Piece {

	
	public Space(Point loc, char t, int c) {
		/**Dummy value for Space piece*/
		location = new Point(loc);
		type = t;
		color = c;
		name = "Space";
	}

	public Piece move(Piece[][] board, Point curr, Point dest) {
		return null;
	}

	public Vector<String> getValidMoves(Piece[][] board, Point curr) {
		return null;
	}

	Point getPositionIfKing(int color) {
		return null;
	}

}
