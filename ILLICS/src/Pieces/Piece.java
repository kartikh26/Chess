package Pieces;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Vector;

/**
 * Abstract Piece class.
 * 
 * Specifies behavior and attributes of 
 * any chess piece.
 * 
 * @author kartikhegde
 */
public abstract class Piece {
	
	/**Member variables of each piece*/
	public Point location;
	public char type; 
	public int color;
	public String name;
	public BufferedImage pieceImage;
	public String unicodeStr = "";
	
	/**
	 * Moves the piece from current location to specified location.
	 * 
	 * @param board: The chess board (passed as reference)
	 * @param curr: The current location of the piece (curr = current)
	 * @param dest: The location the piece will be moved to (dest = destination)
	 */
	public Piece move(Piece [][] board, Point curr, Point dest) {
		board[curr.x][curr.y] = new Space(curr, ' ', -1);
		Piece piece = board[dest.x][dest.y];
		board[dest.x][dest.y] = this;
		this.location.x = dest.x;
		this.location.y = dest.y;
		return piece;
	}

	/**
	 * Returns a vector of valid moves.
	 * 
	 * Every index of the vector will contain a move. The 
	 * move will be in the form of "x1,y1,x2,y2,capturedPiece",
	 * in which x1,y1 represents the piece's current location and x2,y2
	 * represent the destination of the piece after the move. If the
	 * destination contains a piece, then the type of the piece will be 
	 * recorded by capturedPiece. If there exists no piece at the
	 * destination, capturedPiece will be a black space ' '.
	 */
	public abstract Vector<String> getValidMoves(Piece [][] board, Point curr);
	
	/**
	 * Returns the position of the King on team color.
	 * 
	 * This function allows convenient acces to each piece
	 * returning valid coordinates only if it is the King. All
	 * the work of checking whether the piece is the King is 
	 * packaged into this function.
	 * 
	 * @param color Denotes the team player
	 * @return The King's location belonging to the team color
	 * or null if the Piece is not a king
	 */
	abstract Point getPositionIfKing(int color);
	
	/**
	 * @param board
	 * @param currPiece We pass in the currPiece so the function can make use of its
	 * 		  properties like its color.
	 * @return
	 */
	public boolean kingIsSafe(Piece [][] board, Piece currPiece) { //TODO::KINGSAFE NEEDS TO ACCOUNT FOR ALTERNATING SIDES NOT ALL PIECES WILL CALL KING SAFE - I.E. IF THE BISHOP CANNOT MOVE ANYWHERE IT WILL NEVER REACH THE CALL
		//if (currPiece.type == 'k')
			//System.out.println("BLACK KING IS CHECKING FOR CHECK");
		//System.out.println("currPiece : " + currPiece.type);
		//**Position of the current player's King*//*
		Point currKingPos = getKingPosition(board, currPiece.color); //gets YOUR kings position CALLED EVERYTIME KINGISSAFE IS CALLED
		//System.out.println("row: " + currKingPos.x + " col: " + currKingPos.y);
		//System.out.println("6,3: " + board[6][3].name);
		//KINGSAFE MOVES THE currPiece TO ITS POTENTIAL LOCATION 
		// THEN CHECKS IF THE KING IS SAFE THEN TAKES THAT INTO ACCOUNT IN VALID MOVES
		//System.out.println("currPiece " + currPiece.name + " at: " + currPiece.location.x + ", " + currPiece.location.y);
		boolean notInCheck = true; //If notInCheck is false, then King is in check
		notInCheck = safeFromQueen(board, currKingPos, currPiece.color); //Returns true if King is safe from Queen
		//System.out.println("notInCheck: " + notInCheck);
		notInCheck = notInCheck && safeFromRook(board, currKingPos);
		notInCheck = notInCheck && safeFromBishop(board, currKingPos);
		notInCheck = notInCheck && safeFromKnight(board, currKingPos);
		notInCheck = notInCheck && safeFromPawn(board, currKingPos);
		notInCheck = notInCheck && safeFromKing(board, currKingPos);
		//notInCheck = notInCheck && safeFromMissionary(board, currKingPos);
		//notInCheck = notInCheck && safeFromSuperPawn(board, currKingPos);
		//**If King is in check, then it is not safe*//*
		/*if (notInCheck)
			System.out.println("King is NOT in check");
		else
			System.out.println("King is in check");*/
		return notInCheck;
	}


	private boolean safeFromKing(Piece[][] board, Point currKingPos) {
		boolean safeFromKing = true;  
		int currRow = currKingPos.x;
		int currCol = currKingPos.y;
		/**There are nine different locations King can move (including it's current location).*/
		int directions = 9; 
		for (int i = 0; i < directions; i++) {
				try {
					/**Set up a point for potential location*/
					Point potLoc = new Point((currRow-1)+(i/3), (currCol-1)+(i%3));
					/**Makes sure King doesn't move to its current location*/
					if (board[potLoc.x][potLoc.y].name.equals("King") && board[potLoc.x][potLoc.y].color != this.color ) {
						safeFromKing = false;
					}
				} catch(Exception e) {
					//Do nothing
				}
		}
		return safeFromKing;
	}

	private boolean safeFromPawn(Piece[][] board, Point currKingPos) {
		boolean safeFromPawn = true;  
		/** Setting the correct stepSize is identical to how we did it in getValidMoves
		 * for a pawn. We assign the stepSize to account for the direction and the diagonal
		 * distance moveable by a Pawn when attacking. Since the diagonal distance
		 * is only 1, the only real thing we have to account for is the direction. 
		 * When determining check, we start at the King's current position and trace 
		 * backwards the direction we came from. 
		 * 
		 * For example, we need to set the direction of white King to -1 because we 
		 * would check to the left and right sides one row upwards to see whether a pawn
		 * exists.  
		 */
		int stepSize = (2 * this.color - 1); //Results in  for White Pawn and 1 for Black Pawn - Accounts for direction of Pawn Movement
		safeFromPawn = checkUpperMove(board, currKingPos, stepSize, 1); //Check right side attack - denoted by 1
		safeFromPawn = safeFromPawn && checkUpperMove(board, currKingPos, stepSize, -1); //Check left side attack - denoted by -1
		return safeFromPawn;
	}

	/**
	 *  We only have to check Upper Moves because that is the direction the king could be attacked
	 * @param board
	 * @param currKingPos
	 * @param stepSize
	 * @param side
	 * @return
	 */
	private boolean checkUpperMove(Piece[][] board, Point currKingPos,  int stepSize, int side ) {
		boolean safeFromPawn = true;
		int currRow = currKingPos.x;
		int currCol = currKingPos.y;
		/**Set up a point for potential location of movement*/
		int direction = stepSize/Math.abs(stepSize); //Computing stepSize/abs(stepSize) gives direction {-1, 1}
		Point potLoc = new Point(currRow + direction, currCol - side);
		try {
			int pieceColor = board[potLoc.x][potLoc.y].color;
			if ( board[potLoc.x][potLoc.y].name.equals("Pawn") && pieceColor != this.color) {
				safeFromPawn = false;

			}
		}
		catch (Exception e) {
			//Do Nothing
		}
		return safeFromPawn;
	}
	
	private boolean safeFromKnight(Piece[][] board, Point currKingPos) {
		boolean safeFromKnight = true;  
		safeFromKnight = checkFourVerticalSquares(board, currKingPos);
		safeFromKnight = safeFromKnight && checkFourHorizontalSquares(board, currKingPos);
		return safeFromKnight;
	}


	private boolean checkFourVerticalSquares(Piece[][] board, Point currKingPos) {
		boolean safeFromVertical = true;
		/**The directions allow the a piece to to:
		 * 1. Move into a region East of the piece's current location but not on the pieces's current row
		 * 2. Move into a region West of the piece's current but not on the pieces's current row
		 * 3. Move along the vertical of the column or horizontal line or the row
		 */
		int totalDirections = 2;
		int currRow = currKingPos.x;
		int currCol = currKingPos.y;
		
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
					if (board[potLoc.x][potLoc.y].name.equals("Knight") && board[potLoc.x][potLoc.y].color != this.color) {
						safeFromVertical = false;
					}
					
				} catch(Exception e) {
					//Do nothing
				}
			}
		}
		return safeFromVertical;
	}

	private boolean checkFourHorizontalSquares(Piece[][] board, Point currKingPos) {
		boolean safeFromHorizontal = true;
		/**The directions allow the a piece to to:
		 * 1. Move into a region East of the piece's current location but not on the pieces's current row
		 * 2. Move into a region West of the piece's current but not on the pieces's current row
		 * 3. Move along the vertical of the column or horizontal line or the row
		 */
		int totalDirections = 2;
		int currRow = currKingPos.x;
		int currCol = currKingPos.y;
		
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
					if (board[potLoc.x][potLoc.y].name.equals("Knight") && board[potLoc.x][potLoc.y].color != this.color) {
						safeFromHorizontal = false;
					}
					
				} catch(Exception e) {
					//Do nothing
				}
			}
		}
		return safeFromHorizontal;
	}
	
	private boolean safeFromBishop(Piece[][] board, Point currKingPos) {
		boolean safeFromBishop = true;  
		int totalOrientations = 2;
		int currRow = currKingPos.x;
		int currCol = currKingPos.y;
		for (int horizontal = -1; horizontal < totalOrientations; horizontal++) {
			for (int vertical = -1; vertical < totalOrientations; vertical++ ) {
				if (horizontal == 0 || vertical == 0)
					continue;
				try {
					/**The distance we will be away from the Queen.*/
					int stepSize = 1;
					Point potLoc = new Point(currRow + stepSize*horizontal, currCol + stepSize*vertical);
					/**Spaces are given the dummy value of -1*/
					while (board[potLoc.x][potLoc.y].color == -1 ) {
						stepSize++;
						/*System.out.println("COLOR: " + board[potLoc.x][potLoc.y].color + " Step Size: " + stepSize);
						System.out.println("potLoc.x: " + potLoc.x + " potLoc.y: " + potLoc.y);
						System.out.println(board[potLoc.x][potLoc.y].name);*/
						potLoc = new Point(currRow + stepSize*horizontal, currCol + stepSize*vertical);
					}
					
					/**We break out of the while loop and come to return
					 * statement if we hit a piece. If we don't hit a
					 * piece, that means we have iterated enough so that
					 * we go out of bounds and encounter the OutOfBoundsException.
					 * This is caught by the genereal Exception e in the try-catch 
					 * block.
					 */
					String currPieceName = board[potLoc.x][potLoc.y].name;
					int currPieceColor = board[potLoc.x][potLoc.y].color;
					boolean safe = !((currPieceName.equals("Bishop") && currPieceColor != color)); //If the piece is a bishop then we are not safe
					safeFromBishop = safeFromBishop && safe; //Makes sure it's the other color's bishop
				} catch(Exception e) {
					//Do nothing
				}
			}
		}
		//System.out.println("REACHED OUTSIDE OF LOOP" + ++count);
		return safeFromBishop;
	}

	private boolean safeFromRook(Piece[][] board, Point currKingPos) {
		boolean safeFromRook = true;  
		int totalOrientations = 2;
		int currRow = currKingPos.x;
		int currCol = currKingPos.y;
		for (int horizontal = -1; horizontal < totalOrientations; horizontal++) {
			for (int vertical = -1; vertical < totalOrientations; vertical++ ) {
				if (horizontal != 0 && vertical != 0)
					continue;
				try {
					/**The distance we will be away from the Queen.*/
					int stepSize = 1;
					Point potLoc = new Point(currRow + stepSize*horizontal, currCol + stepSize*vertical);
					/**Spaces are given the dummy value of -1*/
					while (board[potLoc.x][potLoc.y].color == -1 ) {
						stepSize++;
						/*System.out.println("COLOR: " + board[potLoc.x][potLoc.y].color + " Step Size: " + stepSize);
						System.out.println("potLoc.x: " + potLoc.x + " potLoc.y: " + potLoc.y);
						System.out.println(board[potLoc.x][potLoc.y].name);*/
						potLoc = new Point(currRow + stepSize*horizontal, currCol + stepSize*vertical);
					}
					
					/**We break out of the while loop and come to return
					 * statement if we hit a piece. If we don't hit a
					 * piece, that means we have iterated enough so that
					 * we go out of bounds and encounter the OutOfBoundsException.
					 * This is caught by the genereal Exception e in the try-catch 
					 * block.
					 */
					String currPieceName = board[potLoc.x][potLoc.y].name;
					int currPieceColor = board[potLoc.x][potLoc.y].color;
					boolean safe = !((currPieceName.equals("Rook") && currPieceColor != color)); //If the piece is a rook then we are not safe
					safeFromRook = safeFromRook && safe; //The purpose of currPieceColor != color is to make sure we are talking about the other team's color
				} catch(Exception e) {
					//Do nothing
				}
			}
		}
		//System.out.println("REACHED OUTSIDE OF LOOP" + ++count);
		return safeFromRook;
	}

	//safeFromQueen is STUCK IN AN INFINITE LOOP note that this checks whether the new state is in check or not
	private boolean safeFromQueen(Piece[][] board, Point currKingPos, int color) { //Everytime kingSafe is called this function is called
		boolean safeFromQueen = true;  
		int totalOrientations = 2;
		int currRow = currKingPos.x;
		int currCol = currKingPos.y;
		for (int horizontal = -1; horizontal < totalOrientations; horizontal++) {
			for (int vertical = -1; vertical < totalOrientations; vertical++ ) {
				try {
					/**The distance we will be away from the Queen.*/
					int stepSize = 1;
					Point potLoc = new Point(currRow + stepSize*horizontal, currCol + stepSize*vertical);
					/**Spaces are given the dummy value of -1*/
					while (board[potLoc.x][potLoc.y].color == -1 ) {
						stepSize++;
						/*System.out.println("COLOR: " + board[potLoc.x][potLoc.y].color + " Step Size: " + stepSize);
						System.out.println("potLoc.x: " + potLoc.x + " potLoc.y: " + potLoc.y);
						System.out.println(board[potLoc.x][potLoc.y].name);*/
						potLoc = new Point(currRow + stepSize*horizontal, currCol + stepSize*vertical);
					}
					
					/**We break out of the while loop and come to return
					 * statement if we hit a piece. If we don't hit a
					 * piece, that means we have iterated enough so that
					 * we go out of bounds and encounter the OutOfBoundsException.
					 * This is caught by the genereal Exception e in the try-catch 
					 * block.
					 */
					String currPieceName = board[potLoc.x][potLoc.y].name;
					int currPieceColor = board[potLoc.x][potLoc.y].color;
					boolean safe = !((currPieceName.equals("Queen") && currPieceColor != color)); //If the piece is a queen then we are not safe
					safeFromQueen = safeFromQueen && safe; //Makes sure it's the other queen)
				} catch(Exception e) {
					//Do nothing
				}
			}
		}
		//System.out.println("REACHED OUTSIDE OF LOOP" + ++count);
		return safeFromQueen;
	}

	
	
	public Point getKingPosition(Piece [][] board, int color) { //WORKS
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				Piece curr = board[row][col];
				Point kingPos = curr.getPositionIfKing(color);
				if (kingPos != null) {
					//System.out.println("KING X: " + kingPos.x + " KING Y: " + kingPos.y);
					return kingPos;
				}
			}
		}
		return null;
	}
}
