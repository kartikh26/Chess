package Controller;
/** Implements the MouseEventListener class
 * 
 * Note: This class is a part of the Controller.
 * This class is responsible for dealing with
 * any events that occur when the user clicks the 
 * mouse in attempt to move a piece.
 */

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JOptionPane;

import Model.ChessModel;
import Pieces.Piece;
import View.ChessView;

public class MouseEventListener implements MouseListener {
		
		/*****Fields*****/
		ChessModel chessModel;
		ChessView chessView;
		private int mousePressedX;
		private int mousePressedY;
		private int mouseReleasedX;
		private int mouseReleasedY;
		
		public MouseEventListener(ChessModel cModel, ChessView cView) {
			chessModel = cModel;
			chessView = cView;
		}
	
		public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
		}
	
		public void mousePressed(MouseEvent e) {
			Piece [][] playerViewBoard = chessModel.getChessBoard();
			int horizontalOffset = chessView.getHorizontalOffset();
			int verticalOffset = chessView.getVerticalOffset();
			int squareLength = chessView.getSquareLength();		
			int viewBoardWidth = horizontalOffset+playerViewBoard.length*squareLength;
			int viewBoardHeight = verticalOffset+playerViewBoard.length*squareLength;
			
			boolean withinHorizontalRegion = e.getX() >= horizontalOffset && e.getX() <= viewBoardWidth;
			boolean withinVerticalRegion = e.getY() >= verticalOffset && e.getY() <= viewBoardHeight;
			//add currentturn boolean and check if it's current turn piece
			if (withinHorizontalRegion && withinVerticalRegion) {//If it's in the viewBoard region
				mousePressedX = e.getX();
				mousePressedY = e.getY();
			}
		}

		public void mouseReleased(MouseEvent e) {
			//System.out.println("MOUSE WAS RELEASED");
			Piece [][] playerViewBoard = chessModel.getChessBoard();
			int horizontalOffset = chessView.getHorizontalOffset();
			int verticalOffset = chessView.getVerticalOffset();
			int squareLength = chessView.getSquareLength();
			
			//Must be released inside the viewBoard
			int viewBoardWidth = horizontalOffset+playerViewBoard.length*squareLength;
			int viewBoardHeight = verticalOffset+playerViewBoard.length*squareLength;
			boolean withinHorizontalRegion = e.getX() >= horizontalOffset && e.getX() <= viewBoardWidth;
			boolean withinVerticalRegion = e.getY() >= verticalOffset && e.getY() <= viewBoardHeight;
			//add currentturn boolean and check if it's current turn piece
			if (withinHorizontalRegion && withinVerticalRegion) {//If it's in the viewBoard region
				mouseReleasedX = e.getX();
				mouseReleasedY = e.getY();
			}
			else {
				//Check if Undo was clicked
				int rectWidthOffset = chessView.getRectWidthOffset();
				int rectHeightOffset  = chessView.getRectHeightOffset(); 
				int rectWidth = chessView.getRectWidth();
				int rectHeight = chessView.getRectHeight();
				
				boolean withinUndoMoveHorizontal = e.getX() >= rectWidthOffset && e.getX() <= rectWidthOffset + rectWidth;
				boolean withinUndoMoveVertical = e.getY() >= rectHeightOffset && e.getY() <= rectHeightOffset + rectHeight;
				boolean undoMoveButton = withinUndoMoveHorizontal && withinUndoMoveVertical;
				if (undoMoveButton) {
					chessModel.undoMove();
					chessView.repaintScreen();
				}
				
				//Check if Reset was clicked
				int boxWidthOffset = chessView.getBoxWidthOffset();
				int boxHeightOffset  = chessView.getBoxHeightOffset(); 
				int boxWidth = chessView.getBoxWidth();
				int boxHeight = chessView.getBoxHeight();
				
				boolean withinResetHorizontal = e.getX() >= boxWidthOffset && e.getX() <= boxWidthOffset + boxWidth;
				boolean withinResetVertical = e.getY() >= boxHeightOffset && e.getY() <= boxHeightOffset + boxHeight;
				boolean resetButton = withinResetHorizontal && withinResetVertical;
				if (resetButton) {
					chessModel  = new ChessModel();
					chessView = new ChessView(chessModel);
					chessView.addMouseListener(new MouseEventListener(chessModel, chessView));
				}
				return;
			}
			
			/**Note: The (row, col) notation gets swapped to 
			 * (col, row) when defining the proper (x,y)
			 * coordinates.
	 		 */
			int currXCoord = (mousePressedX - horizontalOffset)/squareLength; 
			int currYCoord = (mousePressedY - verticalOffset)/squareLength;
			int destXCoord = (mouseReleasedX - horizontalOffset)/squareLength;
			int destYCoord = (mouseReleasedY - verticalOffset)/squareLength;
			
			/**Transfer (x,y) to (row,col)*/
			int currRow = currYCoord;
			int currCol = currXCoord;
			int destRow = destYCoord;
			int destCol = destXCoord;
			char capturedPiece = playerViewBoard[destRow][destCol].type;
			String move = "" + ""+currRow+","+currCol+","+destRow+","+destCol+","+capturedPiece;
			int currPlayerTurn = chessModel.getTurn();
			Vector<String> moves = chessModel.getAllValidMoves(currPlayerTurn);
			/*if (moves.size() == 0) {
				String state = chessModel.getState(); // Either returns "Checkmate" or "Stalemate"
				System.out.println(state);
			}*/
			//chessModel.printValidMoves(moves);
			for (int i = 0; i < moves.size(); i++) {
				if (moves.get(i).equals(move)) {
					Point curr = new Point(currRow, currCol);
					Point dest = new Point(destRow, destCol);
					Piece pieceAtDestination = playerViewBoard[currRow][currCol].move(playerViewBoard, curr, dest);
					chessView.repaintScreen();
					Vector<Piece> capturedPieces = chessModel.getCapturedPieces();
					Vector<String> completedMoves = chessModel.getCompletedMoves();
					completedMoves.add(move);
					capturedPieces.add(pieceAtDestination);
					chessModel.setTurn(1-currPlayerTurn); //Toggles turn between 1 and 0
					chessView.repaintScreen();
					
					/*Check game state - we can check it here because we've already changed the turn to other players*/
					String state = chessModel.getState(); // Either returns "Checkmate", "Stalemate", or "Neither" 
					if (state.equals("Neither") == false) {
						System.out.println(state);
						JOptionPane.showMessageDialog(null, state+"!");
						
					}
					//Make Computer's Move here
					int alpha = chessModel.getAlpha();
					int beta = chessModel.getBeta();
					String compMove = chessModel.alphaBetaMax(alpha, beta, "", chessModel.getDepth(), chessModel.getTurn());
					//System.out.println(compMove);
					chessModel.makeComputerMove(compMove); 
					chessModel.setTurn(1-chessModel.getTurn());
					chessView.repaintScreen();
					/*Check game state - we can check it here because we've already changed the turn to other players*/
					state = chessModel.getState(); // Either returns "Checkmate", "Stalemate", or "Neither" 
					if (state.equals("Neither") == false) {
						System.out.println(state);
						JOptionPane.showMessageDialog(null, state+"!");
					}
					
					break;
				}
			}
			//System.out.println("currRow: " + currRow + " currCol: " + currCol +
					//" destRowLoc: " + destRow + " destColLoc: " + destCol);
			
		
		//Set mousePressed and relased to negative values once done
		}

	
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
		
		}

	
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
		
		}
}