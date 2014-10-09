package View;
/** 
 * Implements the ChessGui Class.
 *  
 * This is a part of the ChessView
 * 
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import Model.ChessModel;
import Pieces.Piece;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessGui extends JComponent{
	
	/*****Fields*****/
	ChessModel chessModel;
	ChessView chessView;

	
	/*****Constructors*****/
	public ChessGui(ChessModel cModel, ChessView cView) {
		chessModel = cModel;
		chessView = cView;
	}

	/*****Methods*****/
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D twoDim = (Graphics2D) g;
		drawLayout(twoDim);
		drawPlayerViewBoard(twoDim);
	}
	

	public void repaintScreen() {
		repaint();
	}

	//TODO: ADD Comments before functions
	void drawLayout(Graphics2D twoDim) {
		//Draw Background
		twoDim.setColor(new Color(0xFF, 0x66, 0x00));
		twoDim.fillRect(0, 0, 5 * chessView.getScreenWidth() / 6, chessView.getScreenHeight());
		twoDim.setColor(new Color(0, 0, 200));
		twoDim.fillRect(5 * chessView.getScreenWidth() / 6, 0, chessView.getScreenWidth() / 6, chessView.getScreenHeight());
		
		//Draw Text
		int verticalStringOffset = 100;
		twoDim.setColor(new Color(0, 0, 0));
		twoDim.setFont(new Font("TimesRoman", Font.BOLD, 40)); //TODO:Change TimesRoman to a variable
		twoDim.drawString("ILLICS", (int) (3.2 * chessView.getScreenWidth()) / 6, verticalStringOffset);
		twoDim.setFont(new Font("TimesRoman", Font.BOLD, 30));
		twoDim.drawString("Illinois Computer System", (int) (3.2 * chessView.getScreenWidth()) / 6, (int) (1.5 * verticalStringOffset)); //TODO: Add a line of comments
		twoDim.drawString("Kartik Hegde", (int) (3.2 * chessView.getScreenWidth()) / 6, (int) (2 * verticalStringOffset));
	
		//Draw Undo Move Button
		int rectWidthOffset = chessView.getRectWidthOffset(); //Arbitrary values set based on appearance
		int rectHeightOffset  = chessView.getRectHeightOffset(); //Arbitrary values set based on appearance
		int rectWidth = chessView.getRectWidth();
		int rectHeight = chessView.getRectHeight();
		twoDim.setColor(Color.BLACK);
		twoDim.fillRect(rectWidthOffset, rectHeightOffset, rectWidth, rectHeight);
		twoDim.setColor(Color.WHITE);
		twoDim.setFont(new Font("TimesRoman", Font.BOLD, 30)); //TODO:Change TimesRoman to a variable
		twoDim.drawString("Undo Move", rectWidthOffset + 25, rectHeightOffset + 40);
		
		//Draw Reset Button
		int boxWidthOffset = chessView.getBoxWidthOffset(); //Arbitrary values set based on appearance
		int boxHeightOffset  = chessView.getBoxHeightOffset(); //Arbitrary values set based on appearance
		int boxWidth = chessView.getBoxWidth();
		int boxHeight = chessView.getBoxHeight();
		twoDim.setColor(Color.BLACK);
		twoDim.fillRect(boxWidthOffset, boxHeightOffset, boxWidth, boxHeight);
		twoDim.setColor(Color.WHITE);
		twoDim.setFont(new Font("TimesRoman", Font.BOLD, 30)); //TODO:Change TimesRoman to a variable
		twoDim.drawString("Reset", boxWidthOffset + 60, boxHeightOffset + 40);
		
		//Draw Score
		twoDim.setColor(Color.BLACK);
		twoDim.setFont(new Font("TimesRoman", Font.BOLD, 40)); //TODO:Change TimesRoman to a variable
		twoDim.drawString("Human: 0	    CPU: 0", chessView.getScoreXLoc(), chessView.getScoreYLoc());
	}
	
	void drawPlayerViewBoard(Graphics2D twoDim) {
			drawNumbers(twoDim);
			drawCheckers(twoDim);
			drawPieces(twoDim);		
	}

	void drawNumbers(Graphics2D twoDim) {
		// TODO Auto-generated method stub
		
	}

	void drawPieces(Graphics2D twoDim) {
		int pieceHorOffset = 15;
		int pieceVertOffset = 55;
		Piece [][] playerViewBoard = chessModel.getChessBoard();
		int pieceSize = chessView.getPieceSize();
		int horizontalOffset = chessView.getHorizontalOffset();
		int verticalOffset = chessView.getVerticalOffset();
		int squareLength = chessView.getSquareLength();
		
		for (int row = 0; row < playerViewBoard.length; row++) {
			for (int col = 0; col < playerViewBoard[0].length; col++) {
				Piece currPiece = playerViewBoard[row][col];
				Point currLoc = playerViewBoard[row][col].location;
				twoDim.setColor(Color.BLACK);
				twoDim.setFont(new Font("Arial Unicode MS", Font.PLAIN, pieceSize)); //Save Arial Unicode MS in a variable
				/**Transfer (row,col) to (x,y)*/
				int currXCoord = currLoc.y;
				int currYCoord = currLoc.x;
				//Draw string takes into account x and y not row and col
				twoDim.drawString(""+currPiece.unicodeStr, pieceHorOffset +horizontalOffset+currXCoord*squareLength, pieceVertOffset + verticalOffset+currYCoord*squareLength);
			}
		}	
	}

	void drawCheckers(Graphics2D twoDim) {
		Piece [][] playerViewBoard = chessModel.getChessBoard();
		int horizontalOffset = chessView.getVerticalOffset();
		int verticalOffset = chessView.getVerticalOffset();
		int squareLength = chessView.getSquareLength();
		for (int row = 0; row < playerViewBoard.length; row++) {
			for (int col = 0; col < playerViewBoard[0].length; col++) {
				if (row % 2 == 0) { //Start with light square
					if (col % 2 == 0) { //Light square comes first
						twoDim.setColor(new Color(255, 255, 255)); //LIGHT
						twoDim.fillRect(horizontalOffset+row*squareLength, verticalOffset+col*squareLength, squareLength, squareLength);
					}
					else { //Dark square comes second
						twoDim.setColor(new Color(100, 100, 100)); //DARK
						twoDim.fillRect(horizontalOffset+row*squareLength, verticalOffset+col*squareLength, squareLength, squareLength);
					}
				}
				else { //Start with dark square
					if (col % 2 == 0) {
						twoDim.setColor(new Color(100, 100, 100)); //DARK
						twoDim.fillRect(horizontalOffset+row*squareLength, verticalOffset+col*squareLength, squareLength, squareLength);
					}
					else {
						twoDim.setColor(new Color(255, 255, 255)); //LIGHT
						twoDim.fillRect(horizontalOffset+row*squareLength, verticalOffset+col*squareLength, squareLength, squareLength);
					}
				}
			}
		}
	}
	
}
