package Model;
/**
 * Implements the Initialize class.
 * 
 * This class initializes all the pieces in the Chess game.
 * 
 * @author kartikhegde
 *
 */

import java.awt.Point;

import Pieces.Bishop;
import Pieces.King;
import Pieces.Knight;
import Pieces.Pawn;
import Pieces.Piece;
import Pieces.Queen;
import Pieces.Rook;
import Pieces.Space;

public class Initialize {

	public Initialize (Piece [][] board, int boardWidth, int boardLength) {
		initializeSpaces(board, boardWidth, boardLength);
		initializePawns(board, boardWidth, boardLength);
		initializeOtherPieces(board, boardWidth, boardLength);
		//board[6][4] = new Queen(new Point(6,4), 'Q', 0);
		//board[2][2] = new Queen(new Point(2,2), 'q', 1);
	}
	
	private void initializeSpaces(Piece [][] board, int boardWidth, int boardLength) {
		//Initialize Space Pieces
		for (int i = 2; i <= (boardLength-1)-2; i++) {
			for (int j = 0; j < boardWidth; j++) {
				board[i][j] = new Space(new Point(i,j), ' ', -1);
			}
		}	
	}

	private void initializePawns(Piece [][] board, int boardWidth, int boardLength) {
		//Initialize Pawns
		for (int i = 1; i < boardLength; i+=5) {
			for (int j = 0; j < boardWidth; j++) {
				char type = 'P';
				int color = 0;
				if (i == 1) {
					type = 'p';
					color = 1;
				}
				board[i][j] = new Pawn(new Point(i, j), type, color); //TODO: when initializing take into account color
			}
		}
		
	}
	
	private void initializeOtherPieces(Piece [][] board, int boardWidth, int boardLength) {
		//Initialize other defined pieces
		for (int j = 0; j < boardWidth; j++) {
			switch(j) {
				case 0: initializeRooks(board, boardWidth, boardLength, j);
					break;
				case 1: initializeKnights(board, boardWidth, boardLength, j);
					break;
				case 2: initializeBishops(board, boardWidth, boardLength, j);
					break;
				case 3: initializeQueens(board, boardWidth, boardLength, j);
					break;
				case 4: initializeKings(board, boardWidth, boardLength, j);
					break;
			}
		}
	}
	
	private void initializeRooks(Piece [][] board, int boardWidth, int boardLength, int sideOffset) {
		board[0][sideOffset] = new Rook(new Point(0, sideOffset), 'r', 1); 
		board[0][boardWidth-1-sideOffset] = new Rook(new Point(0, boardWidth-1-sideOffset), 'r', 1);
		board[boardLength-1][sideOffset] = new Rook(new Point(boardLength-1, sideOffset), 'R', 0); 
		board[boardLength-1][boardWidth-1-sideOffset] = new Rook(new Point(boardLength-1, boardWidth-1-sideOffset), 'R', 0);	
	}

	private void initializeKnights(Piece [][] board, int boardWidth, int boardLength, int sideOffset) {
		board[0][sideOffset] = new Knight(new Point(0, sideOffset), 'n', 1); 
		board[0][boardWidth-1-sideOffset] = new Knight(new Point(0, boardWidth-1-sideOffset), 'n', 1);
		board[boardLength-1][sideOffset] = new Knight(new Point(boardLength-1, sideOffset), 'N', 0);
		board[boardLength-1][boardWidth-1-sideOffset] = new Knight(new Point(boardLength-1, boardWidth-1-sideOffset), 'N', 0);
	}

	private void initializeBishops(Piece [][] board, int boardWidth, int boardLength, int sideOffset) {
		board[0][sideOffset] = new Bishop(new Point(0, sideOffset), 'b', 1); 
		board[0][boardWidth-1-sideOffset] = new Bishop(new Point(0, boardWidth-1-sideOffset), 'b', 1);
		board[boardLength-1][sideOffset] = new Bishop(new Point(boardLength-1, sideOffset), 'B', 0);
		board[boardLength-1][boardWidth-1-sideOffset] = new Bishop(new Point(boardLength-1, boardWidth-1-sideOffset), 'B', 0);
	}
	
	private void initializeQueens(Piece [][] board, int boardWidth, int boardLength, int sideOffset) {
		board[0][sideOffset] = new Queen(new Point(0, sideOffset), 'q', 1);  
		board[boardLength-1][sideOffset] = new Queen(new Point(boardLength-1, sideOffset), 'Q', 0); 
	}
	
	private void initializeKings(Piece [][] board, int boardWidth, int boardLength, int sideOffset) {
		board[0][sideOffset] = new King(new Point(0, sideOffset), 'k', 1);  
		board[boardLength-1][sideOffset] = new King(new Point(boardLength-1, sideOffset), 'K', 0); 
	}
	
}
