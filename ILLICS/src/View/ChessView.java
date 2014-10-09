package View;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import Controller.MouseEventListener;
import Model.ChessModel;

import com.sun.media.sound.ModelAbstractChannelMixer;


public class ChessView extends JFrame {
	
	/*****Fields*****/
	/*The GUI to draw the board*/
	ChessGui gui;
	
	/*Get values of the applet*/
	private int screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
	private int screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
	
	/*Set values for offset and square length*/
	private int horOffset = 20;
	private int vertOffset = 20;
	private int squareLength = 80;
	
	/*Piece size*/
	private int pieceSize = 50;
	
	/*Box for undo-ing move*/
	private int rectWidthOffset = (int) (3.2 * screenWidth) / 6; //Arbitrary values set based on appearance
	private int rectHeightOffset  = (int) (2.5 * 100); //Arbitrary values set based on appearance
	private int rectWidth = 200;
	private int rectHeight = 65;
	
	/*Box for resetting game*/
	private int boxWidthOffset = (int) (3.2 * screenWidth) / 6; //Arbitrary values set based on appearance
	private int boxHeightOffset  = (int) (4 * 100); //Arbitrary values set based on appearance
	private int boxWidth = 200;
	private int boxHeight = 65;
	
	/*Set up the score coordinates*/
	private int scoreXLoc = (int) (3.2 * screenWidth) / 6; //Arbitrary values set based on appearance;
	private int scoreYLoc = (int) (5.5 * 100); //Arbitrary values set based on appearance
	
	/*****Constructors*****/
	public ChessView(ChessModel chessModel) {
		init(chessModel);
	}

	/*****Methods*****/
	private void init(ChessModel chessModel) {
		this.setTitle("ILLICS"); 
		this.setSize(screenWidth, screenHeight);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		gui = new ChessGui(chessModel, this);
		this.add(gui);
		this.setVisible(true);
	}

	public void addMouseEventListener(MouseEventListener mouseListener) {
		this.addMouseListener(mouseListener);
	}
	
	
	public int getScreenWidth() {
		return screenWidth;
	}
	
	public int getScreenHeight() {
		return screenHeight;
	}
	
	public int getHorizontalOffset() {
		return horOffset;
	}
	
	public int getVerticalOffset() {
		return vertOffset;
	}
	
	public int getSquareLength() {
		return squareLength;
	}
	
	public int getPieceSize() {
		return pieceSize;
	}
	
	public int getRectWidthOffset() {
		return rectWidthOffset;
	}
	
	public int getRectHeightOffset() {
		return rectHeightOffset;
	}
	
	public int getRectWidth() {
		return rectWidth;
	}
	
	public int getRectHeight() {
		return rectHeight;
	}
	
	public int getBoxWidthOffset() {
		return boxWidthOffset;
	}
	
	public int getBoxHeightOffset() {
		return boxHeightOffset;
	}
	
	public int getBoxWidth() {
		return boxWidth;
	}
	
	public int getBoxHeight() {
		return boxHeight;
	}
	
	public void repaintScreen() {
		gui.repaintScreen();
	}

	public int getScoreXLoc() {
		return scoreXLoc;
	}
	
	public int getScoreYLoc() {
		return scoreYLoc;
	}
}
