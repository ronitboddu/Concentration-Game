package client.controller;

/**
 * A class that represents the controller in the MVC model for concentration game.
 *
 * @author Shivani Singh ,ss5243@rit.edu
 * @author Ronit Boddu, rb1209@g.rit.edu
 */

import client.model.ConcentrationModel;
import common.ConcentrationProtocol;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConcentrationController implements ConcentrationProtocol {

    /**
     * Listener thread to read the inputs from server and send it to model.
     */
    class Listener extends Thread{
        /**
         * BufferedReader stream to read input from server.
         */
        private BufferedReader in;
        /**
         * Model from MVC
         */
        private ConcentrationModel model;

        /**
         * constructor to initialise variables.
         * @param in BufferedReader stream to read input from server.
         * @param model Model from MVC
         */
        public Listener(BufferedReader in,ConcentrationModel model){
            this.in = in;
            this.model=model;
        }

        /**
         * reads input from server
         */
        @Override
        public void run(){
            String fromServer;
            try{
                while(!((fromServer=in.readLine()).equalsIgnoreCase("GAME_OVER"))){
                    System.out.println(fromServer);
                    model.setCard(fromServer);
                }
                model.setCard(fromServer);
            }catch (IOException ioe){
                System.out.println(ioe.getMessage());
            }
        }
    }

    /**
     * Model from MVC
     */
    private ConcentrationModel model;
    /**
     * Dimension of the board
     */
    private int dim;
    /**
     * clicked button's row
     */
    private int row;
    /**
     * clicked button's column
     */
    private int col;
    /**
     * Socket to create socket connection with server
     */
    private Socket CCSocket;
    /**
     * PrintWriter to send message to server
     */
    private PrintWriter out;
    /**
     * BufferedReader to read messaged from server
     */
    private BufferedReader in;
    /**
     * Listener thread to read messages from server.
     */
    private Listener listener;

    /**
     * Constructor to initialize the controller
     * @param model
     * @param hostName
     * @param portNumber
     * @throws IOException
     */
    public ConcentrationController(ConcentrationModel model,String hostName, int portNumber) throws IOException {
        dim=0;
        row=-1;
        col=-1;
        this.model = model;
        CCSocket = new Socket(hostName, portNumber);
        out = new PrintWriter(CCSocket.getOutputStream(),true);
        in = new BufferedReader(new InputStreamReader(CCSocket.getInputStream()));
        readDimIp(in);
    }

    /**
     * Method to create the board
     * @param dim
     */
    public void create(int dim){
        model.createBoard(dim);
    }

    /**
     * The method to Process Board dimension and invoke the create method
     * @param fromServer
     */
    public void processServerIp(String fromServer){
        String[] arr = fromServer.split(" ");
        if (arr[0].equals("BOARD_DIM")){
            dim = Integer.parseInt(arr[1]);
            create(dim);
        }
    }


    /**
     * Method invoked when a card is clicked
     * @param row
     * @param col
     * @throws IOException
     */
    public void setClickedCard(int row,int col) throws IOException {
        this.row=row;
        this.col=col;
        sendServer(out);
    }

    /**
     * Method to get the clicked card's index
     * @return
     */
    public String getClickedCard(){
        return row +" "+col;
    }

    /**
     * Method to read the dimension of the board from the server
     * @param in
     * @throws IOException
     */
    public void readDimIp(BufferedReader in) throws IOException {
        String fromServer=in.readLine();
        processServerIp(fromServer);
        listener = new Listener(in,model);
        listener.start();
    }

    /**
     * Method to send the server to reveal a card with the following indexes
     * @param out
     */
    public void sendServer(PrintWriter out){
        String fromUser;
        fromUser = REVEAL + " " + getClickedCard();
        System.out.println(fromUser);
        out.println(fromUser);
    }

    /**
     * Method to close the connection with the server
     * @throws IOException
     */
    public void closeConnections() throws IOException {
        CCSocket.close();
        in.close();
        out.close();
    }
}
