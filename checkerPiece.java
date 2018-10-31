import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class checkerPiece {
   //constant once constructor is declared
   private int BOARDX = 0;
   private int BOARDY = 0;
   
   //the width and height of the square the piece is placed in
   //are automatically the width and height of the board
   //divided by 8
   private int PIECEXWIDTH = 0;
   private int PIECEYHEIGHT = 0;
   
   //string giving the color of the piece -- either "red" or "black"
   private String pieceColor = "";
   
   //can be changed after constructor is declared
   private int pieceXCoord = 0;
   private int pieceYCoord = 0;
   
   private final String PLAYER1COLOR = drawBoard.getPlayer1Color ();
   private final String PLAYER2COLOR = drawBoard.getPlayer2Color ();
   
   private boolean isKing = false;
   
   public int kingRow; // the row where the piece becomes a king
   
   BufferedImage resizedCrown;
   
   //returns true if the user just clicked on the piece, false otherwise
   boolean userSelectedPiece = false;
   
   //true if the user can use this piece to make a hop
   boolean hopBorder = false;
   
   
   //the checker piece takes as input the leftmost x coordinate of the square it is in and the 
   //topmost y coordinate of the square, as well as the
   //height and width of the board
   public checkerPiece (int squareXCoord, int squareYCoord, int boardXsize, int boardYsize, String color) {
      setBoardSize (boardXsize, boardYsize);
      //must set the booard size before setting the XY coordinates
      setPieceWidth();
      setXYCoord (squareXCoord, squareYCoord);
      pieceColor = color;
      initializeCrown ();
      getKingRow ();
   }
   
   
   
   public void addHopBorder () {
      hopBorder = true;
   }
   
   public void removeHopBorder () {
      hopBorder = false;
   }
   
   public void removeAllBorders () {
      hopBorder = false;
      userSelectedPiece = false;
   }
   
   
   public void makeKing () {
      isKing = true;
   }
   
   public boolean isPieceKing () {
      return isKing;
   }
   
   
   public void addBorder () {
      userSelectedPiece = true;
   }
   
   
   public void removeBorder () {
      userSelectedPiece = false;
   }
   
   
   public boolean canMoveUp () {
      if (pieceColor.equals (PLAYER2COLOR) ) {
         return true;
      }
      if (isKing == true) {
         return true;
      }
      
      return false;
   }
   
   
   public boolean canMoveDown () {
      if (pieceColor.equals (PLAYER1COLOR) ) {
         return true;
      }
      if (isKing == true) {
         return true;
      }
      
      return false;
   }
   
   
   private void setPieceWidth () {
      PIECEXWIDTH = BOARDX / 12;
      PIECEYHEIGHT = BOARDY / 12;
   }
   
   //note: the piece color must be input in lowercase, must be either "red" or "black", or the program will terminate
   //case of the letters does not matter
   /* public void setPieceColor (String color) {
      pieceColor = color.toLowerCase();
   } */
   
   public String getPieceColor () {
      return pieceColor;
   }
   
   //gets the row where the piece will become a king if the row
   //is reached by that piece
   private void getKingRow () {
      int p1KingRow = 7;
      int p2KingRow = 0;
      if (pieceColor.equals (PLAYER1COLOR) ) {
         kingRow = p1KingRow;
      } else {
         kingRow = p2KingRow;
      }
   }
   
   
   
   public void drawPiece (Graphics g) {
      //if the user entered the wrong color or didn't enter a color, end the program
     if ( (! pieceColor.equals (PLAYER1COLOR) )  &&  (! pieceColor.equals (PLAYER2COLOR) ) ) {
         System.out.println("The piece color you entered is " + pieceColor);
         System.out.println ("This color is not accepted.  Please enter \"red\" or \"black\"");
         System.exit (0);
      }
      if (pieceColor.equals (PLAYER1COLOR) ) {
         g.setColor (Color.RED);
      } else {
         g.setColor (Color.BLACK);
      }
      
      g.fillOval (pieceXCoord, pieceYCoord, PIECEXWIDTH, PIECEYHEIGHT);
      g.drawOval (pieceXCoord, pieceYCoord, PIECEXWIDTH, PIECEYHEIGHT);
      
      if (hopBorder) {
         drawHopBorder (g);
      }
      
      if (userSelectedPiece) {
         drawClickBorder (g);
      }
      
      if (isKing) {
         //draw the crown
         Graphics2D g2D = (Graphics2D) g;
         g2D.drawImage (resizedCrown, pieceXCoord, pieceYCoord, null);
      }
   }
   
   
   public void initializeCrown () {
      
      BufferedImage basicCrown = null;
      
      try {
         basicCrown = ImageIO.read (new File ("whiteCrown.png") );
      } catch (IOException e) {
      }
      
      //Graphics2D g2D = (Graphics2D) g;
      
      Image tempCrown = basicCrown.getScaledInstance (PIECEXWIDTH, PIECEYHEIGHT, Image.SCALE_SMOOTH);
      
      resizedCrown = new BufferedImage (PIECEXWIDTH, PIECEYHEIGHT, BufferedImage.TYPE_INT_ARGB);
      
      Graphics2D crownResizer = resizedCrown.createGraphics ();
      
      crownResizer.drawImage (tempCrown, 0, 0, null);
      
      crownResizer.dispose ();
      
      //g2D.drawImage (resizedCrown, pieceXCoord, pieceYCoord, null);
      
   }
   
   
   //draws a border if the user clicks on the checkerPiece
   public void drawClickBorder (Graphics g) {
   
      Graphics2D g2D = (Graphics2D) g;
      double pieceWidth = (double) PIECEXWIDTH;
      float strokeWidth = (float) pieceWidth / 16;     
      g2D.setStroke(new BasicStroke(strokeWidth));  // set stroke width of 10
      g2D.setColor (Color.BLUE);
      g2D.drawOval(pieceXCoord, pieceYCoord, PIECEXWIDTH, PIECEYHEIGHT);
      
   }
   
   //draws a border if the user can use this piece to jump over an opponent's piece
   public void drawHopBorder (Graphics g) {
      Graphics2D g2D = (Graphics2D) g;
      double pieceWidth = (double) PIECEXWIDTH;
      float strokeWidth = (float) pieceWidth / 16;     
      g2D.setStroke(new BasicStroke(strokeWidth));  // set stroke width of 10
      g2D.setColor (Color.CYAN);
      g2D.drawOval(pieceXCoord, pieceYCoord, PIECEXWIDTH, PIECEYHEIGHT);

   }
   
   
   private void setBoardSize (int newBoardX, int newBoardY) {
      BOARDX = newBoardX;
      BOARDY = newBoardY;
   }
   
   //takes as input the left x coordinate and top y coordinate of the square the piece is in
   //uses them to set the class x and y coordinates so that the piece will be centered within the square
   public void setXYCoord (int leftX, int upperY) {
   
      //calculate width and height of square piece is in based on board size
      int squareXWidth = BOARDX / 8;
      int squareYHeight = BOARDY / 8;
      
      //determine center x and y coordinate of the square's center
      int centerXSquare = leftX + (squareXWidth / 2);
      int centerYSquare = upperY + (squareYHeight / 2);
      
      //then determine the position where the piece will be placed so it is centered in the square
      pieceXCoord = centerXSquare - (PIECEXWIDTH / 2);
      pieceYCoord = centerYSquare - (PIECEYHEIGHT / 2);
   }
   
   
   //Checks whether, after moving to a new row (the input of the function),
   //the piece should now become a king.
   public boolean kingThisPiece (int newRow) {
      if (newRow == kingRow) {
         return true;
      } else {
         return false;
      }
   }
   
}