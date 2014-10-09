package Model;
import java.awt.Point;
import java.util.Vector;

import javax.swing.JFrame;

import Pieces.Piece;


public class ChessModel{
	
	/*****Fields*****/
	/*Left to Right*/
	private int boardWidth = 8;
	/*Top to Bottom*/
	private int boardLength = 8;
	/*Board array*/
	private Piece [][] board;
	/*Player turn*/
	private int turn;
	/*For the completed moves*/
	private Vector<Piece> capturedPieces;
	private Vector<String> completedMoves;
	/*Depth of search*/
	private int depth;
	/*Declare values of alpha and beta*/
	private int alpha;
	private int beta;
	/*Vectors used for Alpha-Beta Search*/
	private Vector<Piece> alphaBetaCapturedPieces;
	private Vector<String> alphaBetaCompletedMoves;

	/*****Constructors*****/
	public ChessModel() {
		board = new Piece[boardWidth][boardLength];
		Initialize piecesSetup = new Initialize(board, boardWidth, boardLength);
		turn = 0; //If 0, then white else if 1 then black TODO: ask user for color input
		capturedPieces = new Vector<Piece>();
		completedMoves = new Vector<String>();
		alpha = Integer.MIN_VALUE;
		beta = Integer.MAX_VALUE;
		alphaBetaCapturedPieces = new Vector<Piece>();
		alphaBetaCompletedMoves = new Vector<String>();
		depth = 2;
		//drawBoard();
	}

	/*****Methods*****/
	public Piece [][] getChessBoard() {
		return board;
	}
	
	public int getBoardWidth() {
		return boardWidth;
	}
	
	public int getBoardLength() {
		return boardLength;
	}
	
	public int getTurn() {
		return turn;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public int getAlpha() {
		return alpha;
	}
	
	public int getBeta() {
		return beta;
	}
	
	public Vector<Piece> getCapturedPieces() {
		return capturedPieces;
	}
	
	public Vector<String> getCompletedMoves() {
		return completedMoves;
	}
	
	public void setTurn(int newTurnValue) {
		turn = newTurnValue;
	}


	public void printValidMoves(Vector<String> allMoves) {
		System.out.println("-----------------------------------");
		if (allMoves != null) {
			for (int i = 0; i < allMoves.size(); i++) {
				System.out.println(allMoves.get(i));
			}
		}
		System.out.println("-----------------------------------");
	}

	public Vector<String> getAllValidMoves(int turn) { //Turn represent colors - 0: white 1:black 
		Vector<String> moves = new Vector();
		for (int row = 0; row < boardLength; row++) {
			for (int col = 0; col < boardWidth; col++) {
				//System.out.println("Piece: " + board[row][col].type + " Color: " + board[row][col].color); //WORKS UNTIL HERE
				Vector<String> currPieceMoves = null;
				Piece currPiece = board[row][col];
				if (currPiece.color == turn) {
					currPieceMoves = currPiece.getValidMoves(board, new Point(row,col));
				}
				if (currPieceMoves != null) {
					moves.addAll(currPieceMoves);	
				}
			}
		}
		return moves;
	}
	

	private void drawBoard() {
		//Draw top row
		System.out.println(" --- --- --- --- --- --- --- --- ");
		
		//Draw rest of board
		for (int i = 0; i < boardLength; i++) {
			System.out.print("| ");
			for (int j = 0; j < boardWidth; j++)  {
				Piece p = board[i][j];
				char val = p.type;
				System.out.print(val + " | ");
			}
			System.out.println();
			System.out.println(" --- --- --- --- --- --- --- --- ");
		}
	}

	public String getState() {
		Vector<String> moves = getAllValidMoves(turn) ;
		if (moves.size() == 0) {
			if (staleMate()) {
				return "Stalemate";
			}
			else {
				return "Checkmate";	
			}
		}
		return "Neither";
	}
	
	public boolean staleMate() {
		Piece currKing = getcurrKing(turn); //Turn represent color so we can just check the piece to make sure it's a King and then see if color matches
		if (currKing.kingIsSafe(board, currKing))
			return true;  //Game ends in Stalemate
		else
			return false;  //Game ends in Checkmate
	}

	/**
	 * Use the currPlayerTurn to match with the color 
	 * and get the King of that player.
	 * @param currPlayerTurn
	 * @return
	 */
	private Piece getcurrKing(int currPlayerTurn) {
		for (int i = 0; i < boardLength; i++) {
			for (int j = 0; j < boardWidth; j++)  {
				Piece currPiece = board[i][j];
				if (currPiece.name.equals("King") && currPiece.color == turn)
					return currPiece;
			}
		}
		System.out.println("Error in retrieving King..function returns null");
		return null;
	}
	
	/**Since we are playing against the computer,
	 * everytime we undo out move, we must undo computer's
	 * move before we undo our move.
	 */
	public void undoMove() {
		
		if (capturedPieces.size() == 0 && completedMoves.size() == 0) //No moves have occurred
			return;
		//System.out.println("Completed Moves.size: " + completedMoves.size());
		//First undo comp's move (at i = 0) then your move (at i = 1)
		for (int i = 0; i < 2; i++) {
			String lastMove = completedMoves.remove(completedMoves.size()-1);
			
			String currR = lastMove.substring(0,1);
			String currC = lastMove.substring(2,3);
			String destR = lastMove.substring(4,5);
			String destC = lastMove.substring(6,7);
		
			
			int currRow = Integer.parseInt(currR);
			int currCol = Integer.parseInt(currC);
			int destRow = Integer.parseInt(destR);
			int destCol = Integer.parseInt(destC);
			
			
			//Move the piece at destRow, destCol back to currRow, currCol
			Piece atDest = board[destRow][destCol];
			atDest.move(board, new Point(destRow, destCol), new Point(currRow, currCol)); //we're moving the piece backwards
			Piece previousPiece = capturedPieces.remove(capturedPieces.size()-1);
			board[previousPiece.location.x][previousPiece.location.y] = previousPiece;
		}
		
	}
	
	/**
	 * The turn is always called initially for the CPU's turn
	 * @param depth
	 * @param alpha
	 * @param beta
	 * @param currDepth
	 * @param turn
	 * @return
	 */
	public String alphaBetaMax(int alpha, int beta, String move, int currDepth, int currPlayerTurn) { //White turn = 0, Black turn = 1
		Vector<String> possibleMoves = getAllValidMoves(currPlayerTurn);
		if (currDepth == 0 || possibleMoves.size() == 0)
			return move + "," + getHeuristic(); //Append a comma and rating to the end - so rating will be at index 10
		for (int i = 0; i < possibleMoves.size(); i++) {
			String moveToMake = possibleMoves.get(i);
			makeCurrentMove(moveToMake); //Wrapper to move
			String curRet = alphaBetaMin(alpha, beta, moveToMake, currDepth - 1, 1 - currPlayerTurn); //Switch Color
			//System.out.println(curRet);
			int score = Integer.parseInt(curRet.substring(10)); //Rating is at index 10 and anything onwards
			undoMadeMove(moveToMake);
			if (score <= beta) {
				beta = score;
				if (depth == currDepth)
					move = curRet.substring(0,9);
			}
			
		}
		return move + "," +beta; //TODO: verify
	}



	public String alphaBetaMin(int alpha, int beta, String move, int currDepth, int currPlayerTurn) {
		Vector<String> possibleMoves = getAllValidMoves(currPlayerTurn);
		if (currDepth == 0 || possibleMoves.size() == 0)
			return move + "," + (-1 * getHeuristic());
		for (int i = 0; i < possibleMoves.size(); i++) {
			String moveToMake = possibleMoves.get(i);
			makeCurrentMove(moveToMake); //Wrapper to move
			String curRet = alphaBetaMax(alpha, beta, moveToMake, currDepth - 1, 1 - currPlayerTurn); //Switch Color
			int score = Integer.parseInt(curRet.substring(10)); //Rating is at index 10 and anything onwards
			undoMadeMove(moveToMake);
			if (score > alpha) {
				alpha = score; //Max node can only adjust lower bounds
				if (depth == currDepth)
					move = curRet.substring(0,9);
			}
		}
		return move + "," + alpha;
	}
	
	/**
	 * Use this function for any Alpha Beta related moves
	 * 
	 */
	public void makeCurrentMove(String moveToMake) {
		String currR = moveToMake.substring(0,1);
		String currC = moveToMake.substring(2,3);
		String destR = moveToMake.substring(4,5);
		String destC = moveToMake.substring(6,7);

		int currRow = Integer.parseInt(currR);
		int currCol = Integer.parseInt(currC);
		int destRow = Integer.parseInt(destR);
		int destCol = Integer.parseInt(destC);
		
		Piece atCurrLoc = board[currRow][currCol];
		Piece capturedPiece = atCurrLoc.move(board, new Point(currRow, currCol), new Point(destRow, destCol));
		alphaBetaCompletedMoves.add(moveToMake);
		alphaBetaCapturedPieces.add(capturedPiece);
	}
	
	public void makeComputerMove(String moveToMake) {
		String currR = moveToMake.substring(0,1);
		String currC = moveToMake.substring(2,3);
		String destR = moveToMake.substring(4,5);
		String destC = moveToMake.substring(6,7);

		int currRow = Integer.parseInt(currR);
		int currCol = Integer.parseInt(currC);
		int destRow = Integer.parseInt(destR);
		int destCol = Integer.parseInt(destC);
		
		Piece atCurrLoc = board[currRow][currCol];
		Piece capturedPiece = atCurrLoc.move(board, new Point(currRow, currCol), new Point(destRow, destCol));
		completedMoves.add(moveToMake);
		capturedPieces.add(capturedPiece);
	}
	
	public void undoMadeMove(String move) {
		int pieceIndex = -1;
		for (int i = 0; i < alphaBetaCompletedMoves.size(); i++) {
			if (alphaBetaCompletedMoves.get(i).equals(move)) {
				pieceIndex = i;
				alphaBetaCompletedMoves.remove(i);
				String currR = move.substring(0,1);
				String currC = move.substring(2,3);
				String destR = move.substring(4,5);
				String destC = move.substring(6,7);
			
				
				int currRow = Integer.parseInt(currR);
				int currCol = Integer.parseInt(currC);
				int destRow = Integer.parseInt(destR);
				int destCol = Integer.parseInt(destC);
				
				
				//Move the piece at destRow, destCol back to currRow, currCol
				Piece atDest = board[destRow][destCol];
				atDest.move(board, new Point(destRow, destCol), new Point(currRow, currCol)); //we're moving the piece backwards
				Piece previousPiece = alphaBetaCapturedPieces.remove(pieceIndex);
				board[previousPiece.location.x][previousPiece.location.y] = previousPiece;
			}
		}
	}
	
	/**
	 * Important function that determines the
	 * heuristic (i.e value of outcome) of 
	 * each outcome.
	 */
	private int getHeuristic() {
		int gain = 0;
		gain += evaluateAttack(); //How strong is the attack
		gain += evaluatePieces(); //Evalute pieces
		gain += evaluateKingMobility(); //How much can king move freely
		gain += evaluateState(); //Position
		return 0;
	}

	private int evaluateState() {
		// TODO Auto-generated method stub
		return 0;
	}

	private int evaluatePieces() {
		// TODO Auto-generated method stub
		return 0;
	}

	private int evaluateKingMobility() {
		// TODO Auto-generated method stub
		return 0;
	}

	private int evaluateAttack() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
