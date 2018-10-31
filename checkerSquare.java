//this class contains all the information about a single checker square
//as well as the graphics for the checker square
import java.awt.*;
import java.awt.Color;
public class checkerSquare {
   
   private int XCOORD = 0;
   private int YCOORD = 0;
   private int XSIZE = 0;
   private int YSIZE = 0;
   public String squareColor;
   
   public int boardRow;
   public int boardColumn;
   public boolean containsCheckerPiece = false;
   public checkerPiece checker;
   
   public boolean canTakePiece = false;
   
   private final String PLAYER1COLOR = drawBoard.getPlayer1Color ();
   private final String PLAYER2COLOR = drawBoard.getPlayer2Color ();
   
   
   //determines whether or not the square is a potential move
   //from the player's first click
   //if it is then the color of the square will be changed
   private boolean translucentSquare = false;
   
   
   //don't forget to add arrayList/similar data structure of all squares that are diagonal to the given square
   
   
   public checkerSquare (int xPos, int yPos, int squareXsize, int squareYsize) {
      XCOORD = xPos;
      YCOORD = yPos;
      XSIZE = squareXsize;
      YSIZE = squareYsize;
   }
   
   
   
   
   
   //draws a square and any checkerpiece that is on the square
   public void drawSquare (Graphics g) {
      //ensure that a valid square color has been entered
      
      String whiteColor = gameBoard.COLORWHITE;
      String grayColor = gameBoard.COLORGRAY;
      if (! squareColor.equals (whiteColor)  &&  ! squareColor.equals (grayColor) ) {
         System.out.println ("The color has not been set.  Set a color for the square using the setSquareColor function.");
         System.exit (0);
      }
      
      //draw the square
      if (squareColor.equals (whiteColor) ) {
         g.setColor (Color.WHITE);
      } else {
         g.setColor (Color.GRAY);
      }
      
      g.fillRect (XCOORD, YCOORD, XSIZE, YSIZE);
      
      //draw a checker piece if there is a checker piece on the square
      if (containsCheckerPiece == true) {
        // System.out.println ("Square at row " + boardRow + " and column " + boardColumn + " has a piece");
         checker.drawPiece (g); 
      }
      
      //draw a translucent red square over the square if the user could potentially move
      //to this square.
      if (translucentSquare == true) {
         drawTranslucentSquare (g);
      }
      
   }
   
   
   //check to see if a square that the user clicked is immediately diagonal to this square
   public boolean isDiagonalSquare (checkerSquare otherSquare) {
      int otherRow = otherSquare.getRow ();
      int otherCol = otherSquare.getCol ();
      int thisRow = boardRow;
      int thisCol = boardColumn;
      
      if ( (thisRow == otherRow + 1) || (thisRow == otherRow - 1) ) {
         if ( (thisCol == otherCol + 1) || (thisCol == otherCol - 1) ) {
            return true;
         }
      }
      
      //if it wasn't proven to be diagonal yet then it isn't diagonal
      return false;
   }
   
   
   //check if another square is diagonal to this square
   //but a hop's distance away
   public boolean diagonalHopDistance (checkerSquare otherSquare) {
      int otherRow = otherSquare.getRow ();
      int otherCol = otherSquare.getCol ();
      int thisRow = boardRow;
      int thisCol = boardColumn;
      
      if ( (thisRow == otherRow + 2) || (thisRow == otherRow - 2) ) {
         if ( (thisCol == otherCol + 2) || (thisCol == otherCol - 2) ) {
            return true;
         }
      }
      
      //if it wasn't proven to be diagonal yet then it isn't diagonal
      return false;
   }
   
   
   public boolean hasCheckerPiece () {
      if (containsCheckerPiece == true) {
         return true;
      } else {
         return false;
      }
   }
   
   
   public void removeCheckerPiece () {
      containsCheckerPiece = false;
      checker = null;
   }
   
   
   //makes the checker piece for the class the piece that the user input
   public void addCheckerPiece (checkerPiece piece) {
      containsCheckerPiece = true;
      piece.setXYCoord (XCOORD, YCOORD);
      checker = piece;
   }
   
   
   //check if another square has a piece that is a different color
   //only use this method if the other square is already verified to have a checker piece on it
   public boolean differentColorPiece (checkerSquare anotherSquare) {
      checkerPiece otherPiece = anotherSquare.getChecker ();
      String otherPieceColor = otherPiece.getPieceColor ();
      checkerPiece boardPiece = checker;
      String boardPieceColor = boardPiece.getPieceColor ();
      
      if (! otherPieceColor.equals (boardPieceColor) ) {
         return true;
      } else {
         return false;
      }
   }
   
   
   //check if two square objects are equal
   public boolean squaresIdentical (checkerSquare anotherSquare) {
      if ( (anotherSquare.getRow () == boardRow) && (anotherSquare.getCol () == boardColumn) ) {
         return true;
      } else {
         return false;
      }
   }
   
   
   //returns the String of the player who currently possesses the square
   //either "player1", "player2", or "noplayer"
   public String getPlayer () {
      String checkerColor = checker.getPieceColor ();
      
      if (checkerColor.equals (PLAYER1COLOR) ) {
         return gameBoard.getPlayer1String ();
      }
      
      if (checkerColor.equals (PLAYER2COLOR) ) {
         return gameBoard.getPlayer2String ();
      }
      
      return "noPlayer";
   }
   
   
   //draw a translucent red square over the square if the square
   //is a potential move
   private void drawTranslucentSquare (Graphics g) {
      Color translucentRed = new Color (150, 0, 0, 70);
      g.setColor (translucentRed);
      g.fillRect (XCOORD, YCOORD, XSIZE, YSIZE);
      
   }
   
   //add a translucent square over the square showing that the player
   //can move to the square
   public void addTranslucent () {
      translucentSquare = true;
   }
   
   //remove the translucent square after the player's turn
   public void removeTranslucent () {
      translucentSquare = false;
   }
   
   
   //sets the square color
   public void setSquareColor (String color) {
      squareColor = color.toLowerCase ();
   }
   
   public String getSquareColor () {
      return squareColor;
   }
   
   
   public void setRow (int row) {
      boardRow = row;
   }
   
   public void setCol (int col) {
      boardColumn = col;
   }
   
   public int getRow () {
      return boardRow;
   }
   
   public int getCol () {
      return boardColumn;
   }
   
   public int getXcoord () {
      return XCOORD;
   }
   
   public int getYcoord () {
      return YCOORD;
   }
   
   public int getXsize () {
      return XSIZE;
   }
   
   public int getYsize () {
      return YSIZE;
   }
   
   public void setChecker (checkerPiece aChecker) {
      checker = aChecker;
   }
   
   public checkerPiece getChecker () {
      return checker;
   }
   
}