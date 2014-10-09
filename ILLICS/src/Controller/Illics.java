package Controller;
import Model.ChessModel;
import View.ChessView;

/**
 * Implements the Illics class. 
 * 
 * 
 * @author kartikhegde
 *
 */

//TODO: Add a folder structure.
public class Illics {
	
		public static void main(String[] args) {
			ChessModel chessModel = new ChessModel();
			ChessView chessView = new ChessView(chessModel);	
			ChessController chessController = new ChessController(chessModel, chessView);			
		}
}
