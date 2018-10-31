import java.awt.*;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;


//contains information about the gameBoard including storing information
//for all of the squares, also initializes the game
public class gameBoard extends JComponent {
   
   private static int BOARDX = 0;
   private static int BOARDY = 0;
   public checkerSquare [] [] allSquares = new checkerSquare [8] [8];
   
   public checkerPiece [] p1_checkerPieces = new checkerPiece [12];
   public checkerPiece [] p2_checkerPieces = new checkerPiece [12];
   
   //booleans to save whether or not any of a player's pieces must take another piece
   //at the start of their turn.
   private boolean p1_mustTakePiece = false;
   private boolean p2_mustTakePiece = false;
   
   private static final String PLAYER1 = drawBoard.PLAYER1;
   private static final String PLAYER2 = drawBoard.PLAYER2;
   
   private final String PLAYER1COLOR = drawBoard.getPlayer1Color ();
   private final String PLAYER2COLOR = drawBoard.getPlayer2Color ();
   
   public int player1Pieces;
   public int player2Pieces;
   
   public boolean player1Lost = false;
   public boolean player2Lost = false;
   
   public static final String COLORWHITE = "white";
   public static final String COLORGRAY = "gray";
   
   public static String getPlayer1String () {
      return PLAYER1;
   }
   
   public static String getPlayer2String () {
      return PLAYER2;
   }
   
   
   /* public boolean p1MustHop () {
      return p1_mustTakePiece;
   }
   
   public boolean p2MustHop () {
      return p2_mustTakePiece;
   } */


   public gameBoard (int boardXsize, int boardYsize) {
      setXY (boardXsize, boardYsize);
      setPreferredSize (new Dimension (boardXsize, boardYsize) );
      initializeGame ();
   }
   
   public void setXY (int newBoardX, int newBoardY) {
      BOARDX = newBoardX;
      BOARDY = newBoardY;
   }
   
   public checkerSquare [] [] getSquares () {
      return allSquares;
   }
   
   
   
   //note: this method may not be necessary and might just be 
   //an attempt to fix a different glitch
   //change the square data so that it reflects the new board
   /* public void alterSquare (checkerSquare newSquare, int row, int col) {
      allSquares [row] [col] = newSquare;
   } */
   
   //initializes all squares and the checker pieces on them
   public void initializeGame () {
      
      resetGameInfo ();
      int xPos = 0;
      int yPos = 0;
      int xSize = BOARDX / 8;
      int ySize = BOARDY / 8;
      
      for (int row = 0; row < 8; row++) {
         for (int col = 0; col < 8; col++) {
            checkerSquare aSquare = new checkerSquare (xPos, yPos, xSize, ySize);
            if( row % 2 == col % 2 ) {
               aSquare.setSquareColor (COLORWHITE);
               //g.setColor(Color.WHITE);
            } else{
               aSquare.setSquareColor (COLORGRAY);
               //g.setColor(Color.GRAY);
            } 
            
            aSquare.setRow (row);
            aSquare.setCol (col);
            
            //add checker pieces in the proper starting positions
            //all pieces start on gray squares
            if ( (row == 0 || row == 1 || row == 2) && (aSquare.getSquareColor ().equals (COLORGRAY) ) ) {
               checkerPiece aPiece = new checkerPiece (xPos, yPos, BOARDX, BOARDY, PLAYER1COLOR);
               //aPiece.setPieceColor (PLAYER1COLOR);
               aSquare.addCheckerPiece (aPiece);
            }
            
            if ( (row == 5 || row == 6 || row == 7) && (aSquare.getSquareColor ().equals (COLORGRAY) ) ) {
               checkerPiece aPiece = new checkerPiece (xPos, yPos, BOARDX, BOARDY, PLAYER2COLOR);
               //aPiece.setPieceColor (PLAYER2COLOR);
               aSquare.addCheckerPiece (aPiece);
            }
            
            
            
            allSquares [row] [col] = aSquare;
            
            //g.fillRect(xPos, yPos, xSize, ySize);
            xPos += xSize;
         }
         
         xPos = 0;
         yPos += ySize;
      }

   }
   
   
   //resets all class variables to their original values each time game is intialized
   private void resetGameInfo () {
      p1_mustTakePiece = false;
      p2_mustTakePiece = false;
      player1Lost = false;
      player2Lost = false;
      player1Pieces = 12;
      player2Pieces = 12;
   }
   
   
   @Override
   public void paintComponent (Graphics g) {
      super.paintComponent (g);
      for (int row = 0; row < 8; row ++) {
         for (int col = 0; col < 8; col ++) {
            checkerSquare tempSquare = allSquares [row] [col];
            checkerSquare aSquare = allSquares [row] [col];
            aSquare.drawSquare (g);
         }
      }
      
      printIf_playerLost (g);
       
      
   }
   
   
   private void printIf_playerLost (Graphics g) {
      
      if (player1Lost) {
         drawVictory (g, PLAYER2COLOR);
      }
      
      else if (player2Lost) {
         g.setColor (Color.BLACK);
         drawVictory (g, PLAYER1COLOR);
      }
      
   }
   
   
   private void drawVictory (Graphics g, String victorColor) {
      
      int fontSize = BOARDX / 8;
      g.setFont (new Font ("TimesRoman", Font.PLAIN, fontSize) );
      
      if (victorColor.equals (PLAYER1COLOR) ) {
         g.setColor (Color.RED);
      } else {
         g.setColor (Color.BLACK);
      }
      
      String victoryString = victorColor + " wins";
      int boardXMiddle = BOARDX / 2;
      int boardYMiddle = BOARDY / 2;
      FontMetrics fontInfo = g.getFontMetrics ();
      int stringHeight = fontInfo.getHeight ();
      int boardYPos = boardYMiddle + (stringHeight / 4);
      int stringWidth = fontInfo.stringWidth (victoryString);
      int boardXPos = boardXMiddle - (stringWidth / 2);
      g.drawString (victoryString, boardXPos, boardYPos);
      
   }
   
   
   //finds the square the user clicked on when they clicked on a square
   public checkerSquare getSquareClicked (int xClick, int yClick) {
      //get x and y values of the size of all of the squares
      int sizeXSquares = allSquares [0] [0].getXsize ();
      int sizeYSquares = allSquares [0] [0].getYsize ();
      for (int row = 0; row < 8; row ++) {
         for (int col = 0; col < 8; col ++) {
            checkerSquare squareToCheck = allSquares [row] [col];
            int squareXstart = squareToCheck.getXcoord ();
            int squareYstart = squareToCheck.getYcoord ();
            int squareXend = squareXstart + sizeXSquares;
            int squareYend = squareYstart + sizeYSquares;
            
            if (xClick >= squareXstart && xClick < squareXend && yClick >= squareYstart && yClick < squareYend) {
               return squareToCheck;
            }
         }
      }
      
      //if no square was found, there was an error
      System.out.println ("There was an error.  One of the squares should have been clicked.");
      System.exit (0);
      return allSquares [0] [0];
   }
   
   
   //note: the validity of the move must already have been determined by this point
   //move a checkerpiece from the square it was on to the square the user clicked
   //performing any hops along the way (giving user option to pick the direction of the hop if necessary)
   //re-evaluates if any of the nearby squares' ability to hop over another square has changed
   //takes as input the 
   /* public void movePiece (checkerSquare startSquare, checkerSquare endSquare) {
   
      if (startSquare.isDiagonalSquare (endSquare) ) {
         movePiece_AcrossSquares (startSquare, endSquare);
      }
      
      else if (startSquare.diagonalHopDistance (endSquare) ) {
         performHop (startSquare, endSquare);
      }
      
   } */
   
   
   //move a checkerPiece from one square (startSquare) to another (endSquare)
   public void movePiece_AcrossSquares (checkerSquare startSquare, checkerSquare endSquare) {
      
      checkerPiece movePiece = startSquare.getChecker ();
      endSquare.addCheckerPiece (movePiece);
      startSquare.removeCheckerPiece ();
      int newRow = endSquare.getRow ();
      if (movePiece.kingThisPiece (newRow) ) {
         movePiece.makeKing ();
      }
      resetCanTakePiece ();

   }
   
   
   //perform a single hop between startSquare and endSquare
   public void performHop (checkerSquare startSquare, checkerSquare endSquare) {
  
      checkerSquare middleSquare = getMiddleSquare (startSquare, endSquare);
      //decrement pieces of the player who lost a piece and check if they lost the game
      decrement_checkGameOver (middleSquare);
      //remove the checker piece that was taken
      middleSquare.removeCheckerPiece ();
      movePiece_AcrossSquares (startSquare, endSquare);
 
   }
   
   
   //decrements the pieces of whoever's piece was taken
   //and checks if either player has lost the game
   //input is the square that is being hopped over
   private void decrement_checkGameOver (checkerSquare middleSquare) {
      checkerPiece pieceTaken = middleSquare.getChecker ();
      String pieceTakenColor = pieceTaken.getPieceColor ();
      
      //decrement the piece count of whichever player lost a piece
      if (pieceTakenColor.equals (PLAYER1COLOR) ) {
         player1Pieces --;
      } else {
         player2Pieces --;
      }
      
      //then check if the player lost the game
      if (player1Pieces == 0) {
         player1Lost = true;
      } else if (player2Pieces == 0) {
         player2Lost = true;
      }
      
   }
   
   
   //input : the color of the player whose turn it is
   //output : whether or not that player can move at the start of their turn
   public boolean playerCanMove (String playerColor) {
      
      for (int row = 0; row < 8; row ++) {
         for (int col = 0; col < 8; col ++) {
            checkerSquare checkSquare = allSquares [row] [col];
            if (checkSquare.hasCheckerPiece () ) {
               checkerPiece checkPiece = checkSquare.getChecker ();
               String checkColor = checkPiece.getPieceColor ();
               if (checkColor.equals (playerColor)  &&  movesPossible (checkSquare) ) {
                  return true;
               }
            }
         }
      }
      return false;
   }
   
   
   //function resets each piece's ability (through the usage of squares, as always) to take
   //a piece on the neighboring square
   //based on the new setup of the board after a move is made 
   private void resetCanTakePiece () {
      //reset every square's ability to take piece to false
      p1_mustTakePiece = false;
      p2_mustTakePiece = false;
      
      for (int row = 0; row < 8; row ++) {
      
         for (int col = 0; col < 8; col ++) {
            checkerSquare checkSquare = allSquares [row] [col];
            
            checkSquare.canTakePiece = false;
            
            if (checkSquare.hasCheckerPiece () ) {
              
              if (hopAvailable (checkSquare) ) {
                  setCanHop (checkSquare);
              }
            }
         }
      }
   }
   
  
   
   
   //takes a checkerSquare as input, alters checkerSquare so it can 
   //take a piece and alters global boolean forcing the player who possesses
   //the square to take a piece during their next move
   //uses global String constant PLAYER1
   private void setCanHop (checkerSquare changeSquare) {
      changeSquare.canTakePiece = true;
      String playerName = changeSquare.getPlayer ();
      
      if (playerName.equals (PLAYER1) ) {
         p1_mustTakePiece = true;
      } else {
         p2_mustTakePiece = true;
      }
   }

   
   // -- > revise function later on to ensure that the player can only click on this if
   //there are no other pieces that they can take at the moment.
   //checks to see if the square the user clicked on has one of their pieces on it
   //accepts as input the square the user clicked on as well as the color of the side the player is on
   public boolean firstClickValid (checkerSquare square, String playerColor) {
   
      System.out.println ("the firstClickValid check is being made.");
   
      boolean pieceMustHop = playerPieceMustHop (playerColor);
      
      //ensure that if another piece can be taken the user
      //has selected a piece that can take one of their opponent's pieces
      if (pieceMustHop == true) {
      
         System.out.println ("The pieceMustHop check is being made");
         
         if (! square.canTakePiece) {
            return false;
         }
      }
      
      //make sure that the square user clicked has a checker piece and that it is the player's color
      //then check if that piece is one of the player's pieces
      if (square.hasCheckerPiece () ) {
         
         System.out.println ("This square has a checker piece");
      
         checkerPiece checker = square.getChecker ();
         String checkerColor = checker.getPieceColor ();
         
         if (checkerColor.equals (playerColor) ) {
         
            System.out.println ("This checkerPiece has the right color -- " + playerColor);
            
            //finally, check if there are any surrounding squares that the player could theoretically move to
            if (movesPossible (square) ) {
            
               System.out.println ("Moves can be made with this piece");
               
               return true;
            }
         }
      }
      
      return false;
   }
   
   
   public boolean secondClickValid (checkerSquare square1, checkerSquare square2, String playerColor) {
   
      boolean pieceMustHop = playerPieceMustHop (playerColor);
      
      //ensure that if another piece can be taken the user
      //has selected a move that is either an invalid move or
      //a valid hop
      if (pieceMustHop == true) {
         if (! square1.diagonalHopDistance (square2) ) {
            return false;
         }
      }

      
      if (isValidMove (square1, square2) ) {
         return true;
      } else {
         return false;
      }
   }
   
   
   
   //eventually put all the below helper functions in a new class called moveFunctions
   
   
   //helper for firstClickValid
   //check whether the user must take one of their opponent's pieces during their turn
   public boolean playerPieceMustHop (String playerColor) {
      String player = drawBoard.getPlayer (playerColor);
      
      //based on the player's string, return the boolean stating whether or not the player
      //must take their opponent's piece
      
      if (player.equals (PLAYER1) ) {
         return p1_mustTakePiece;
      } else {
         return p2_mustTakePiece;
      }
      
   }
   
   
   
   //helper method for the firstClickValid method
   //boolean checks whether or not any moves are actually possible
   //from the square the user clicked
   //takes as input the square the user clicked on  
   private boolean movesPossible (checkerSquare squareClicked) {
      //the order matters -- when hopAvailable is checked all available hop moves will be shown
      //and when regularMoveAvailable is checked all available regular moves will be shown
      //if the user can hop they must hop
      //and only hop moves will be available, so only available hop moves should be shown
      if (hopAvailable (squareClicked) ) {
         return true;
      } else if (regularMoveAvailable (squareClicked) ) {
         return true;
      } else {
         return false;
      }   }
   
   
   //helper function for movesPossible
   //returns whether or not a regular move directly from one square to another
   //is available for the given square
   private boolean regularMoveAvailable (checkerSquare squareClicked) {
      int squareRow = squareClicked.getRow ();
      int squareCol = squareClicked.getCol ();
      
      //the rows above and below the square clicked
      int upperRow = squareRow + 1;
      int lowerRow = squareRow - 1;
      //columns to left and right of the square clicked
      int rightCol = squareCol + 1;
      int leftCol = squareCol - 1;
      
      if (surroundingSquaresAvailable (upperRow, lowerRow, rightCol, leftCol, squareClicked) ) {
         return true;
      } else {
         return false;
      }
   } 
   
   
   public boolean hopAvailable (checkerSquare squareClicked) {
      int squareRow = squareClicked.getRow ();
      int squareCol = squareClicked.getCol ();
      
      //the rows above and below the square clicked
      int upperRow = squareRow + 2;
      int lowerRow = squareRow - 2;
      //columns to left and right of the square clicked
      int rightCol = squareCol + 2;
      int leftCol = squareCol - 2;
      
      if (surroundingSquaresAvailable (upperRow, lowerRow, rightCol, leftCol, squareClicked) ) {
         return true;
      } else {
         return false;
      }
   }
   
   
   
   //Helper function for both regularMovesAvailable and hopAvailable.
   //Checks if the 4 diagonal (either immediately diagonal or reachable by a hop) squares are available
   //for a move.
   //Also adds info to the square which could later allow the user to add a translucent highlight
   private boolean surroundingSquaresAvailable (int upperRow, int lowerRow, int rightCol, int leftCol, checkerSquare squareClicked) {
      
      int upperRowLimit = 8;
      int lowerRowLimit = -1;
      int rightColLimit = 8;
      int leftColLimit = -1;
      
      checkerSquare trySquare;
      //checkerPiece tryPiece = trySquare.getChecker ();
      //String pieceColor = tryPiece.getPieceColor ();
      
      //check to see if there are any available squares to the above and to the right or left the square the user clicked
      if (upperRow < upperRowLimit) {
      
         if (rightCol < rightColLimit) {
            trySquare = allSquares [upperRow] [rightCol];
            
            if (isValidMove (squareClicked, trySquare) ) {
               return true;
            }
         }
         
         if (leftCol > leftColLimit) {
            trySquare = allSquares [upperRow] [leftCol];
            
            if (isValidMove (squareClicked, trySquare) ) {
               return true;
            }
         }
      }
      
      //check same thing below and to the right/left of square user clicked
      if (lowerRow > lowerRowLimit) {
      
         if (rightCol < rightColLimit) {
            trySquare = allSquares [lowerRow] [rightCol];
            
            if (isValidMove (squareClicked, trySquare) ) {
               return true;
            }
         }
         
         if (leftCol > leftColLimit) {
            trySquare = allSquares [lowerRow] [leftCol];
            
            if (isValidMove (squareClicked, trySquare) ) {
               return true;
            }
         }

      }
      
      return false;
   }
   
   
   //note: this function assumes that the first square the user clicked on already had a piece on it.
   //checks whether a move is valid.
   //takes as input the initial square and the square that the piece could potentially be 
   //moved to.
   public boolean isValidMove (checkerSquare startSquare, checkerSquare goalSquare) {
   
      int startingRow = startSquare.getRow ();
      int startingCol = startSquare.getCol ();
      int endingRow = goalSquare.getRow ();
      int endingCol = goalSquare.getCol ();
      //System.out.print ("Testing if it's a valid move.  The row, col of starting square are " + startingRow + ", " + startingCol + ".  ");
      //System.out.println ("The row, col of ending square are " + endingRow + ", " + endingCol);
   
      //first ensure the square user is trying to go to is a black square
      String goalSquareColor = goalSquare.getSquareColor ();
      if (! goalSquareColor.equals (COLORGRAY) ) {
         return false;
      }
      
      //make sure the user is moving their piece in the correct direction
      checkerPiece movePiece = startSquare.getChecker ();
      int startRow = startSquare.getRow ();
      int availableRow = goalSquare.getRow ();
      
      if (! rightDirectionMove (movePiece, startRow, availableRow) ) {
         return false;
      }
      
      //the square being moved to can't have a checker piece
      if (goalSquare.hasCheckerPiece () ) {
         return false;
      }
      
      //if the square being moved to isn't a diagonal square the user must be able to
      //get to the square by taking their opponent's piece
      if (! startSquare.isDiagonalSquare (goalSquare) ) {
         if (! canHop (startSquare, goalSquare) ) {
            return false;
         }
      }
      
      return true;  
   }
   
   
   //helper function for isValidMove, accepts the piece that could be moved,
   //the row of the piece, and the row of the square the piece could potentially move to
   
   //IMPORTANT NOTE ABOUT THIS FUNCTION: THE LOGIC MAY SEEM REVERSED -- THE BOARD WAS GENERATED IN REVERSE -- TOPDOWN
   //SO THE HIGHER THE ROW, THE LOWER ITS ACTUAL POSITION ON THE BOARD
   //THE FUNCTIONS CANMOVEUP () AND CANMOVEDOWN () IN CHECKERPIECE CLASS ARE BASED ON THE VISUAL APPEARANCE OF THE BOARD
   //THIS IS WHY THE GREATER THAN SIGN IS REVERSED FROM ITS LOGICALLY EXPECTED POSITION
   private boolean rightDirectionMove (checkerPiece pieceToMove, int firstRow, int goalRow) {
   
      //if row of starting square is below the other square, the piece must be able to move up
      //for the move to be valid
      if (firstRow > goalRow) {
         if (! pieceToMove.canMoveUp () ) {
            return false;
         }
      }
      
      //if starting square row is below, reverse must be true
      if (firstRow < goalRow) {
         if (! pieceToMove.canMoveDown () ) {
            return false;
         }
      }
      
      return true;
   }
   
   
   
   //helper function for isValidMove
   //input is the initial square clicked and the second square clicked
   //outputs whether or not the user can hop over another player's piece to get to the goalSquare
   //note: for this game the user must click on the first hop first even if there are multiple hops available.
   //note: before this helper function is used, the validity of the direction of the move should
   //already have been tested.
   private boolean canHop (checkerSquare startSquare, checkerSquare goalSquare) {
      
      //the square being hopped to must be a single diagonal hope away
      //and have nothing on it
      if (! startSquare.diagonalHopDistance (goalSquare) ) {
         return false;
      }
      
      if (goalSquare.hasCheckerPiece () ) {
         return false;
      }
      
      checkerSquare squareToHop = getMiddleSquare (startSquare, goalSquare);
      
      //the square being hopped over must have a checker piece of a different color
      if (! squareToHop.hasCheckerPiece () ) {
         return false;
      }
      
      if (! squareToHop.differentColorPiece (startSquare) ) {
         return false;
      }
      
      return true;
     
   }
   
   
   //a helper function for the canHop function
   //takes as input the starting square and the goal square,
   //returns the square in between the two that is being hopped over
   private checkerSquare getMiddleSquare (checkerSquare startSquare, checkerSquare goalSquare) {
      
      int startRow = startSquare.getRow ();
      int startCol = startSquare.getCol ();
      int goalRow = goalSquare.getRow ();
      int goalCol = goalSquare.getCol ();
      
      int middleRow = (startRow + goalRow) / 2;
      int middleCol = (startCol + goalCol) / 2;
      
      checkerSquare middleSquare = allSquares [middleRow] [middleCol];
      return middleSquare;
   }
   


}