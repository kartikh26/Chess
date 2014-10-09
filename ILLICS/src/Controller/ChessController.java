package Controller;
/** 
 * ChessController.java
 * 
 * ChessController is the game's controller. It essentially 
 * provides user the ability to change the view based on the 
 * model. 
 * 
 * The user will provide the Controller with customized options
 * which will be used to render the customized game settings.
 */
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.*;

import Model.ChessModel;
import View.ChessView;

public class ChessController{
	
	/*****Fields*****/
	private int gameType; //TODO: Make this private and have getter methods
	private ChessModel gameModel;
	private ChessView gameView;

	
	/*****Constructors*****/
	public ChessController(ChessModel chessModel, ChessView chessView) {
		gameModel = chessModel;
		gameView = chessView;
		gameView.addMouseListener(new MouseEventListener(gameModel, gameView));
	}

}
