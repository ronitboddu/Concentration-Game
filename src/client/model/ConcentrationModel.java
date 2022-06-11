package client.model;

/**
 * A class that represents the Model in the MVC model for concentration game.
 *
 * @author Shivani Singh ,ss5243@rit.edu
 * @author Ronit Boddu, rb1209@g.rit.edu
 */

import client.gui.ConcentrationGUI;
import java.util.LinkedList;
import java.util.List;


public class ConcentrationModel {

    /**
     * class that represents the cards that needs to be updated in the view.
     */
    public class CardUpdate{
        /**
         * row of the clicked card.
         */
        private int row;
        /**
         * column of the clicked card.
         */
        private int col;
        /**
         * face that needs to be updated.
         */
        private String face;

        /**
         * Constructor to initialise variables
         * @param row
         * @param col
         * @param face
         */
        public CardUpdate(int row,int col, String face){
            this.row=row;
            this.col=col;
            this.face=face;
        }

        /**
         * getter for row number.
         * @return row
         */
        public int getRow(){
            return this.row;
        }
        /**
         * getter for column number.
         * @return column
         */
        public int getCol(){
            return this.col;
        }
        /**
         * getter for face letter value.
         * @return face
         */
        public String getFace(){
            return this.face;
        }
    }

    /**
     * Enum to represent the state of the game
     */
    public enum Status {
        OK,
        YOU_WIN
    }

    /**
     * list of observers.
     */
    private List<Observer<ConcentrationModel>> observers;
    /**
     * to represent ptui of the concentration game.
     */
    private String[][] board;
    /**
     * dimension of the board.
     */
    private int dim;
    /**
     * moves played by the user.
     */
    private int moves;
    /**
     * matches found by the player.
     */
    private int matches;
    /**
     * status of the game.
     */
    private Status status;
    /**
     * card that needs to be updated.
     */
    private CardUpdate cardUpdate;

    public ConcentrationModel(){
        this.observers = new LinkedList<>();
        this.moves=0;
        this.matches=0;
        this.status=Status.OK;
        this.dim=0;
        cardUpdate=null;
    }

    /**
     * method to add observers.
     * @param observer observer
     */
    public void addObserver(Observer<ConcentrationModel> observer) {
        this.observers.add(observer);
    }

    /**
     * method the alert the observer when the user flips a card.
     */
    public void alertObservers() {
        for (Observer<ConcentrationModel> obs: this.observers ) {
            obs.update(this,this.cardUpdate);
        }
    }

    /**
     * gets moves.
     * @return moves
     */
    public int getMovesMade(){
        return moves;
    }

    /**
     * gets matches found
     * @return matches
     */
    public int getMatchNum(){
        return matches;
    }

    /**
     * gets the status of the game
     * @return
     */
    public Status getGameStatus(){
        return status;
    }

    /**
     * method to create the ptui of the concentration game.
     * @param dim dimension
     */
    public void createBoard(int dim){
        this.dim = dim;
        board = new String[this.dim][this.dim];
        InitializeBoard();
        displayBoard();
    }

    /**
     * Initialises the board
     */
    public void InitializeBoard(){
        for(int i=0;i< board.length;i++)
            for(int j=0;j<board[i].length;j++)
                board[i][j]=".";
    }

    /**
     * display the ptui board in pretty format.
     */
    public void displayBoard(){
        System.out.print("  ");
        for(int i=0;i<board.length;i++) System.out.print(i);
        System.out.println();
        for(int i=0;i< board.length;i++){
            System.out.print(i+"|");
            for(int j=0;j<board[i].length;j++){
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * gets the dimension
     * @return
     */
    public int getDim() {
        return dim;
    }

    /**
     * flip the user clicked card.
     * @param i the row
     * @param j the column
     * @param face face letter value
     */
    public void flipCard(int i, int j, String face){
        cardUpdate = new CardUpdate(i,j,face);
        board[i][j] = face;
        alertObservers();
    }

    /**
     * gets the board
     * @return board
     */
    public String[][] getBoard() {
        return board;
    }

    /**
     * set the card based on the server's message
     * @param fromServer
     */
    public void setCard(String fromServer){
        String[] arr = fromServer.split(" ");
        if (arr[0].equals("ERROR")){
            System.out.println(fromServer);
            System.exit(0);
        }
        else if (arr[0].equals("CARD")){
            this.moves++;
            this.flipCard(Integer.parseInt(arr[1]),Integer.parseInt(arr[2]),arr[3]);
            this.displayBoard();
        }
        else if (arr[0].equals("MATCH")){
            this.matches++;
            alertObservers();
            this.displayBoard();
        }
        else if (arr[0].equals("MISMATCH")){
            this.flipCard(Integer.parseInt(arr[1]),Integer.parseInt(arr[2]),".");
            this.flipCard(Integer.parseInt(arr[3]),Integer.parseInt(arr[4]),".");
            this.displayBoard();
        }
       else if (arr[0].equals("GAME_OVER")){
            status=Status.YOU_WIN;
            alertObservers();
        }
    }

}
