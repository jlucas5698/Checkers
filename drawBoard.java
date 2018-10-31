import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.*;

//class draws the board frame, adds the graphics and other info by calling the gameBoard class,
//contains the mouse click and everything that happens
//after the mouse is clicked (mostly using functions from the gameBoard class)

public class drawBoard extends MouseAdapter implements ActionListener {   

   
   private final static int SQUAREX = 88;
   private final static int SQUAREY = 88;
   
   
   //for drawing the board
   //because the x and y sizes will be split into 8 squares
   //each one must be divisible by 8 / a multiple of the square size
   private static int BOARDX = 8 * SQUAREX;
   private static int BOARDY = 8 * SQUAREY;
   
   //for the mouse
   //every user gets 2 clicks
   //keeps track of the click that the user is on
   public boolean firstClick = true;
   public boolean secondClick = false;
   
   //checks whether or not the game has ended -- only necessary during forfeits
   private boolean gameOver = false;
   
   //save the first valid square and the second valid square the user clicked
   private checkerSquare firstSquare;
   private checkerSquare secondSquare;
   
   //save the first checker piece in the first checker square
   //private checkerPiece pieceToMove;
   
   private gameBoard theBoard;
   //private checkerSquare [] [] boardSquares;
   
   private JFrame boardFrame;
   
   //the player side can change but it must always be either red or black in lowercase
   private final static String PLAYER1SIDE = "Red";
   private final static String PLAYER2SIDE = "Black";
   
   public static final String PLAYER1 = "player1";
   public static final String PLAYER2 = "player2";
   
   boolean player1turn = true;
   boolean player2turn = false;
   
   //checks whether or not the player must continue a series of hops
   //(if they take one of their opponent's pieces and there are still
   //more pieces to take)
   private boolean continueHop = false;
   
   //handles all user interactive visuals, such as the images that appear when a user
   //clicks on a square containing a checkerPiece, or images showing the player where
   //they can hop over an opponent's piece
   checkerVisuals visuals;
   
   
   
   public drawBoard () {
        
        boardFrame = new JFrame();
        boardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //can be altered during refining process later on if I want board to be resized
        //keeping it simple for now
        
        
        boardFrame.setResizable (false);
        
        initializeToolbar(boardFrame);
        JPanel boardPanel = new JPanel (new FlowLayout (FlowLayout.LEFT, 0, 0) );
        
        theBoard = new gameBoard (BOARDX, BOARDY);
        
        boardPanel.add(theBoard);
        
        boardFrame.add(boardPanel);
        
        boardFrame.pack();
        boardFrame.setVisible(true);
        
        visuals = new checkerVisuals (theBoard);
        
        boardPanel.addMouseListener (this);
        //boardFrame.addMouseListener (this);
        
   }
   
   
   
   public void initializeToolbar(JFrame board) {
   
      JToolBar tools = new JToolBar();
      tools.setFloatable(false);
      board.add(tools, BorderLayout.PAGE_START);
      JButton gameButton = new JButton ("New game");
      gameButton.addActionListener (this);
      tools.add (gameButton);
      //tools.add(new JButton("Save")); 
      JButton forfeitButton = new JButton ("Forfeit");
      forfeitButton.addActionListener (this);
      tools.add (forfeitButton);
      //tools.addSeparator();
      
   }
   
   
   // Event-handler for button-press
    // //Follow instructions for Stage 3
    public void actionPerformed(ActionEvent e) {
        // Follow instructions for Stage 3

        // Follow instructions for Stage 5
         if (e.getActionCommand() == "New game") {
             setNewGame ();
         // } else if (e.getActionCommand() == "Save") { // Follow instructions for Stage 5
         //    text.copy();
         } else if (e.getActionCommand() == "Forfeit") { // Follow instructions for Stage 5
             playerForfeit ();
         }

    }
    
    //reinitialize a new game
    private void setNewGame () {
      gameOver = false;
      firstClick = true;
      secondClick = false;
      firstSquare = null;
      secondSquare = null;
      player1turn = true;
      player2turn = false;
      theBoard.initializeGame ();
      boardFrame.repaint ();
    }
    
    
    private void playerForfeit () {
      if (player1turn) {
         theBoard.player1Lost = true;
      }else {
         theBoard.player2Lost = true;
      }
      //by ensuring it is neither player's turn, means player's can't continue the game
      gameOver = true;
      visuals.removeAllPieceBorders ();
      visuals.removeAllTranslucents ();
      boardFrame.repaint ();
      
    }

   
   
   
   @Override
   public void mouseClicked (MouseEvent e) {
      //if the click was not within the graphics image
      
      //theBoard.removeAllTranslucents ();
      
      int xClick = e.getX ();
      int yClick = e.getY ();
      if (xClick > BOARDX || yClick > BOARDY) {
         System.out.println ("The click was not in the board");
         return;
      }
      
      String playerColor;
      if (player1turn == true) {
         playerColor = PLAYER1SIDE;
      } else {
         playerColor = PLAYER2SIDE;
      }
      
      System.out.println ("The player color is " + playerColor);
      if (player1turn == true) {
         System.out.println ("It is player 1's turn");
      } else {
         System.out.println ("It is player 2's turn");
      }
      
      checkerSquare squareClicked = theBoard.getSquareClicked (xClick, yClick);
      
      //if the user clicks on their own checker piece and could make a move with that piece
      //and if they are not in the middle of a required multi-piece hop (that's what continueHop checks)
      //draw a border around that piece and give them the option to make a second move
       if (! gameOver  &&  continueHop == false  &&  squareClicked.hasCheckerPiece ()  &&  theBoard.firstClickValid (squareClicked, playerColor) ) {
       
         //remove the border from the old first square
         removeBorderFirstSquare ();
         firstClick = false;
         secondClick = true;
         firstSquare = squareClicked;
         checkerPiece piece = firstSquare.getChecker ();
         piece.addBorder ();
         
         //if the player can take opponent's piece with the piece
         //they clicked on then remove all possible other moves and only show this move
         if (theBoard.playerPieceMustHop (playerColor) ) {
            visuals.removeAllTranslucents ();
            visuals.showPossible_HopMoves (boardFrame, squareClicked);
         }else {
            visuals.removeAllTranslucents ();
            visuals.showPossible_RegularMoves (boardFrame, squareClicked);
         }
         
         boardFrame.repaint ();
      }

      
      //otherwise, if the user is continuing a move and puts in a valid move
      //perform the move
      else if (!gameOver  &&  secondClick == true) {
         
         if (theBoard.secondClickValid (firstSquare, squareClicked, playerColor) ) {
         
            visuals.removeAllTranslucents ();
            visuals.removeAllPieceBorders ();
            System.out.println ("secondClickValid");
            secondSquare = squareClicked;
            movePiece (firstSquare, secondSquare);
            
            //if the player's turn is over show the next player's moves
            //else if the player has more pieces to take show their next possible hops
            //and keep the click border around the piece
            if (continueHop == false) {
               showNextPlayerHops ();
            } else {
               checkerPiece jumpPiece = firstSquare.getChecker ();
               jumpPiece.addBorder ();
               visuals.showPossible_HopMoves (boardFrame, firstSquare);
            }
            
            boardFrame.repaint ();
         
         }
      }
   }
   
   
   //if the next player must hop make it visible on the screen
   private void showNextPlayerHops () {
      if (player1turn == true) {
         if (theBoard.playerPieceMustHop (PLAYER1SIDE) ) {
            visuals.showAllPossibleHops (boardFrame, PLAYER1SIDE);
            visuals.drawBorder_HopPieces (boardFrame, PLAYER1SIDE);
         }
      }
      
      else if (player2turn == true) {
         if (theBoard.playerPieceMustHop (PLAYER2SIDE) ) {
            visuals.showAllPossibleHops (boardFrame, PLAYER2SIDE);
            visuals.drawBorder_HopPieces (boardFrame, PLAYER2SIDE);
         }
      }
   }
   
   
   //removes a border from a checker piece that showed that a move could take place
   //then prints the change
   public void removeBorderFirstSquare () {
   
      if (firstSquare != null) {
         checkerPiece squarePiece = firstSquare.getChecker ();
         squarePiece.removeBorder ();
         boardFrame.repaint ();
      }
   }
   
   
   
   //moves the piece from startSquare to endSquare, hopping if necessary
   //if multiple hops are available does not end the player's turn, but rather keeps it on \
   //the second click setting
   private void movePiece (checkerSquare startSquare, checkerSquare endSquare) {
   
      //if it's a single diagonal move, make the move and end the player's turn
      if (startSquare.isDiagonalSquare (endSquare) ) {
      
         System.out.println ("A regular move will be performed");
      
         theBoard.movePiece_AcrossSquares (startSquare, endSquare);
         
         System.out.println ("Player turn will now be switched");
         
         switchPlayerTurn ();
      } 
      
      //if they're taking a piece make the move and check if there are more pieces to take
      else if (startSquare.diagonalHopDistance (endSquare) ) {
         checkerPiece jumpPiece = startSquare.getChecker ();
         boolean kingBeforeJump = jumpPiece.isPieceKing ();
         
         System.out.println ("A hop will be performed");
         
         theBoard.performHop (startSquare, endSquare);
         
         boolean kingAfterJump = jumpPiece.isPieceKing ();
         
         //if the piece wasn't a king before the jump but became a king after the jump then it became king during jump
         boolean pieceBecameKing = (! kingBeforeJump)  &&  kingAfterJump;
         
         //if the user can still take another piece, their turn isn't over
         //(as long as their piece didn't just become a king)
         //make the square they must move from next the square they just landed on
         if (theBoard.hopAvailable (endSquare)  &&  (! pieceBecameKing) ) {
            firstSquare = secondSquare;
            secondSquare = null;
            continueHop = true;
         }
         //otherwise, their turn is over
         else {
            switchPlayerTurn ();
            continueHop = false;
         }
      }
      
   }
   
   
   //changes the player's turn
   //checks if the next player can make any moves -- if they can't, sets teh booleans that they have lost
   private void switchPlayerTurn () {
   
      //reset the global squareClick variables
      firstSquare = null;
      secondSquare = null;
   
      //reset the clicks
      firstClick = true;
      secondClick = false;
   
      //reset the player's turn
      if (player1turn == true) {
         player1turn = false;
         player2turn = true;
         
         //the new player has lost if they can't make any moves
         if (! theBoard.playerCanMove (PLAYER2SIDE) ) {
            theBoard.player2Lost = true;
         }
         
      } 
      else {
        player2turn = false;
        player1turn = true; 
        
        if (! theBoard.playerCanMove (PLAYER1SIDE) ) {
            theBoard.player1Lost = true;
        }
        
      }
      
   }
   
   
   public gameBoard get_gameBoard () {
      return theBoard;
   }
   

   
   
   public static String getPlayer1Color () {
      return PLAYER1SIDE;
   }
   
   public static String getPlayer2Color () {
      return PLAYER2SIDE;
   }
   
   //input color, output is player who possesses that color
   public static String getPlayer (String playerColor) {
   
      if (playerColor.equals (PLAYER1SIDE) ) {
         return PLAYER1;
      }
      if (playerColor.equals (PLAYER2SIDE) ) {
         return PLAYER2;
      }
      
      System.out.println ("error");
      return "";
   }
   
   



}