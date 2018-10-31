//import java.awt.*;
import javax.swing.*;

//handles all user interactive visuals, such as the images that appear when a user
//clicks on a square containing a checkerPiece, or images showing the player where
//they can hop over an opponent's piece
public class checkerVisuals {
   
   private checkerSquare [] [] allSquares;
   //used for the functions in gameBoard (which will
   //hopefully eventually be moved to another class
   //which can then be passed in instead of a gameBoard
   gameBoard boardInfo;

   public checkerVisuals (gameBoard board) {
      allSquares = board.getSquares ();
      boardInfo = board;
   }
   
   
   //based on a user click show all places that they can move to
   public void showPossible_RegularMoves (JFrame board, checkerSquare squareClicked) {
   
      int squareRow = squareClicked.getRow ();
      int squareCol = squareClicked.getCol ();
      
      //the rows above and below the square clicked
      int upperRow = squareRow + 1;
      int lowerRow = squareRow - 1;
      //columns to left and right of the square clicked
      int rightCol = squareCol + 1;
      int leftCol = squareCol - 1;
      
      makeMovesVisible (upperRow, lowerRow, rightCol, leftCol, squareClicked);
      board.repaint ();
   }
   
   
   //shows the hops the user can make after clicking on a piece
   public void showPossible_HopMoves (JFrame board, checkerSquare squareClicked) {
   
      int squareRow = squareClicked.getRow ();
      int squareCol = squareClicked.getCol ();
      
      //the rows above and below the square clicked
      int upperRow = squareRow + 2;
      int lowerRow = squareRow - 2;
      //columns to left and right of the square clicked
      int rightCol = squareCol + 2;
      int leftCol = squareCol - 2;
      
      makeMovesVisible (upperRow, lowerRow, rightCol, leftCol, squareClicked);
      board.repaint ();
      
   }
   
   //at the start of the user's turn makes visible all the hops that they can make
   public void showAllPossibleHops (JFrame board, String playerColor) {
      for (int row = 0; row < 8; row ++) {
      
         for (int col = 0; col < 8; col ++) {
            checkerSquare checkSquare = allSquares [row] [col];
            
            if (checkSquare.hasCheckerPiece () ) {
              checkerPiece piece = checkSquare.getChecker ();
              String pieceColor = piece.getPieceColor ();
              
              if (pieceColor.equals (playerColor) ) {
                  showPossible_HopMoves (board, checkSquare);
              }
            }
         }
      }

   }
   
   
   //draw a border around the pieces that can take their opponent's piece
   public void drawBorder_HopPieces (JFrame board, String playerColor) {
   
      for (int row = 0; row < 8; row ++) {
      
         for (int col = 0; col < 8; col ++) {
            checkerSquare checkSquare = allSquares [row] [col];
            
            if (checkSquare.hasCheckerPiece () ) {
              checkerPiece piece = checkSquare.getChecker ();
              String pieceColor = piece.getPieceColor ();
              
              if (pieceColor.equals (playerColor) ) {
                  
                  if (boardInfo.hopAvailable (checkSquare) ) {
                     piece.addHopBorder ();
                  }
              }
            }
         }
      }
      
      board.repaint ();

   }
   
   
   
   public void removeAllPieceBorders () {
      for (int row = 0; row < 8; row ++) {
      
         for (int col = 0; col < 8; col ++) {
            checkerSquare square = allSquares [row] [col];
            
            if (square.hasCheckerPiece () ) {
              checkerPiece piece = square.getChecker ();
              piece.removeAllBorders ();
            }
         }
      }

   }
   
   
   //removes any translucent outer squares from possible square moves
   //after the user has made their move
   public void removeAllTranslucents () {
      for (int row = 0; row < 8; row ++) {
         for (int col = 0; col < 8; col ++) {
            checkerSquare square = allSquares [row] [col];
            square.removeTranslucent ();
         }
      }
   }

   
   
   private void makeMovesVisible (int upperRow, int lowerRow, int rightCol, int leftCol, checkerSquare squareClicked) {
      int upperRowLimit = 8;
      int lowerRowLimit = -1;
      int rightColLimit = 8;
      int leftColLimit = -1;
      
      checkerSquare trySquare;
      
      //check to see if there are any available squares to the above and to the right or left the square the user clicked
      if (upperRow < upperRowLimit) {
      
         if (rightCol < rightColLimit) {
            trySquare = allSquares [upperRow] [rightCol];
            
            if (boardInfo.isValidMove (squareClicked, trySquare) ) {
               trySquare.addTranslucent ();
            }
         }
         
         if (leftCol > leftColLimit) {
            trySquare = allSquares [upperRow] [leftCol];
            
            if (boardInfo.isValidMove (squareClicked, trySquare) ) {
               trySquare.addTranslucent ();
            }
         }
      }
      
      //check same thing below and to the right/left of square user clicked
      if (lowerRow > lowerRowLimit) {
      
         if (rightCol < rightColLimit) {
            trySquare = allSquares [lowerRow] [rightCol];
            
            if (boardInfo.isValidMove (squareClicked, trySquare) ) {
               trySquare.addTranslucent ();
            }
         }
         
         if (leftCol > leftColLimit) {
            trySquare = allSquares [lowerRow] [leftCol];
            
            if (boardInfo.isValidMove (squareClicked, trySquare) ) {
               trySquare.addTranslucent ();
            }
         }

      }

   }





   
}