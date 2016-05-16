package logic;

import javax.swing.JOptionPane;

import graphics.*;
import pieces.*;

public class MakeMove
{
	public static pieces.Piece [][]newBoardMachine;
	
	public MakeMove(){}
	
	public static void movePiece(Piece piece, String coordenatesToMove)
	{
		String []coord = coordenatesToMove.split(",");
		int newCoordenateX = Integer.parseInt(coord[0]);
		int newCoordenateY = Integer.parseInt(coord[1]);
						
		int oldPieceCoordenates[] = piece.getCoordenates();
		
		if(!PossibleMoves.castling){
			reviewIfIsEating(newCoordenateX, newCoordenateY);
			generateNewPiece(newCoordenateX, newCoordenateY, piece);
			StartGame.board.removePiece(oldPieceCoordenates[0], oldPieceCoordenates[1]);
		} else{
			castlingMove();
		}
		
    	Board.cleanBoard();
		StartGame.board.repaintBoard();
    	PossibleMoves.possibleMoves.clear();
		PossibleMoves.castling = false;
		//changeTurn();
		
		/* Machine turn */
		if(!Board.isHumanTurn)
		{			
			/* Generating the tree */
			AlphaBethaPruning prune = new AlphaBethaPruning(makeArrayForTree());
			newBoardMachine = prune.YourTurn("black");
			removeAllPieces();
			newBoard();
        	PossibleMoves.possibleMoves.clear();
			graphics.StartGame.board.repaintBoard();
			changeTurn();
		}
		
	}
	
	public static void generateNewPiece(int newCoordenateX, int newCoordenateY, Piece piece)
	{
				
		String pieceType;
		pieceType = piece.getType();
		if(piece.getType() == "Pawn"){
			if(piece.getTeam() == "white" && newCoordenateX == 0){
				pieceType = JOptionPane.showInputDialog(null, "�En qu� te quieres convertir?");
			}
			
			if(piece.getTeam() == "black" && newCoordenateX == 7){
				pieceType = JOptionPane.showInputDialog(null, "�En qu� te quieres convertir?");
			}
		}
		
		String colorImage = piece.getTeam().equals("white") ? "W" : "B";
		
        switch(pieceType){
	        case "Pawn":
	            StartGame.board.addPiece(new pieces.Pawn("src/pieces_images/" + colorImage + "pawn.png", piece.getTeam(), new int[]{newCoordenateX, newCoordenateY}), newCoordenateX, newCoordenateY);
	    		
	            if(!(newCoordenateX == 1 || newCoordenateX == 6)){
	    			((Piece) Board.squares[newCoordenateX][newCoordenateY].getAccessibleContext().getAccessibleChild(0)).setMovesCounter();
	    		} 
	            
	            break;
	        
	        case "Knight":
	            StartGame.board.addPiece(new pieces.Knight("src/pieces_images/" + colorImage + "knight.png", piece.getTeam(), new int[]{newCoordenateX, newCoordenateY}), newCoordenateX, newCoordenateY);
	            break;
	            
	        case "King":
	            StartGame.board.addPiece(new pieces.King("src/pieces_images/" + colorImage + "king.png", piece.getTeam(), new int[]{newCoordenateX, newCoordenateY}), newCoordenateX, newCoordenateY);
	            break;
	            
	        case "Bishop":
	            StartGame.board.addPiece(new pieces.Bishop("src/pieces_images/" + colorImage + "bishop.png", piece.getTeam(), new int[]{newCoordenateX, newCoordenateY}), newCoordenateX, newCoordenateY);
	            break;
	            
	        case "Rook":
	            StartGame.board.addPiece(new pieces.Rook("src/pieces_images/" + colorImage + "rook.png", piece.getTeam(), new int[]{newCoordenateX, newCoordenateY}), newCoordenateX, newCoordenateY);
	            break;
	            
	        case "Queen":
	            StartGame.board.addPiece(new pieces.Queen("src/pieces_images/" + colorImage + "queen.png", piece.getTeam(), new int[]{newCoordenateX, newCoordenateY}), newCoordenateX, newCoordenateY);
	            break;
        }
        
	}
	
	public static void reviewIfIsEating(int x, int y)
	{
        int squarePiece = Board.squares[x][y].getAccessibleContext().getAccessibleChildrenCount();
        if(squarePiece != 0){
    		graphics.StartGame.board.removePiece(x, y);
        }
	}
	
	public static void changeTurn()
	{
		Board.isHumanTurn = Board.isHumanTurn ? false : true;
	}
	
	public static Piece[][] makeArrayForTree()
	{
		Piece[][] pieceMatrix = new Piece[8][8];
		
		for (int i = 0; i < 8; i++) 
		{
			for (int j = 0; j < 8; j++) 
			{
				pieceMatrix[i][j] = (pieces.Piece) Board.squares[i][j].getAccessibleContext().getAccessibleChild(0);
			}
		}
		
		return pieceMatrix;
	}
	
	public static void removeAllPieces()
	{
		for (int i = 0; i < 8; i++) 
		{
			for (int j = 0; j < 8; j++) 
			{
		    	int squareHasPiece = Board.squares[i][j].getAccessibleContext().getAccessibleChildrenCount();
		    	
		    	if(squareHasPiece != 0){
					graphics.StartGame.board.removePiece(i, j);
		    	}
			}
		}
	}

	public static void newBoard()
	{		
		for (int i = 0; i < 8; i++) 
		{
			for (int j = 0; j < 8; j++) 
			{
				if(newBoardMachine[i][j] != null){
					generateNewPiece(i, j, newBoardMachine[i][j]);
				}
			}
		}
	}
	
	public static void castlingMove()
	{
		Piece rook, queen;
		
		switch(PossibleMoves.castlingType){
			case 1:
				rook = (pieces.Piece) Board.squares[7][0].getAccessibleContext().getAccessibleChild(0);
				queen = (pieces.Piece) Board.squares[7][4].getAccessibleContext().getAccessibleChild(0);
				generateNewPiece(7, 3, rook);
				generateNewPiece(7, 2, queen);
				StartGame.board.removePiece(7, 0);
				StartGame.board.removePiece(7, 4);
				break;
				
			case 2:
				rook = (pieces.Piece) Board.squares[7][7].getAccessibleContext().getAccessibleChild(0);
				queen = (pieces.Piece) Board.squares[7][4].getAccessibleContext().getAccessibleChild(0);
				generateNewPiece(7, 5, rook);
				generateNewPiece(7, 6, queen);
				StartGame.board.removePiece(7, 7);
				StartGame.board.removePiece(7, 4);
				break;
		}
	}
}
