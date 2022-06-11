package server.multiServerThread;

import common.ConcentrationException;
import common.ConcentrationProtocol;
import server.game.ConcentrationBoard;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A class that represents Single Server thread
 *
 * @author RIT CS
 * @author Shivani Singh ,ss5243@rit.edu
 * @author Ronit Boddu, rb1209@g.rit.edu
 */
public class CSMultiServerThread extends Thread implements ConcentrationProtocol {
    private final Socket socket;
    private final int dim;
    private final ConcentrationBoard CB;
    private final int clientNum;
    private final String clientString;

    /**
     * a constructor to initialize the instance of CSMultiServerThread and its variables.
     * @param socket
     * @param dim
     * @param clientNum
     * @throws ConcentrationException
     */
    public CSMultiServerThread(Socket socket,int dim,int clientNum) throws ConcentrationException {
        super("CSMultiServerThread");
        this.socket=socket;
        this.dim = dim;
        this.clientNum=clientNum;
        this.clientString = "Client #"+clientNum+": ";
        this.CB = new ConcentrationBoard(dim);
        System.out.printf(clientString+"Client %d connected: %s\n",clientNum,clientNum,socket.toString());
        System.out.println("Solution:");
        printSolution();
    }

    /**
     * overriden run method for thr thread which calls simulation.
     */
    @Override
    public void run(){
        System.out.printf(clientString + "Client started...\n",clientNum);
        try(
                PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                BufferedReader in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ){
            String outputLine;

            outputLine = String.format(BOARD_DIM_MSG,dim);
            out.println(outputLine);

            simulation(out,in,CB);
            System.out.println(clientString+"Client ending...");
            socket.close();
        } catch (IOException | ConcentrationException e) {
            e.printStackTrace();
        }
    }

    /**
     * A function which does not end until the game is over,
     * contains the error check and processes the revealed card and sends appropriate response to client.
     * @param out
     * @param in
     * @param CB
     * @throws IOException
     * @throws ConcentrationException
     */
    public void simulation(PrintWriter out,BufferedReader in,
                           ConcentrationBoard CB) throws IOException, ConcentrationException {
        String inputLine;
        while (!CB.gameOver()) {
            String msgToClient="";
            System.out.println(clientString);
            System.out.println(CB);
            inputLine=in.readLine();
            System.out.println(clientString+"received: "+inputLine);
            String[] indexes = inputLine.split(" ");
            try{
                if (indexes.length!=3){
                    msgToClient="Please enter 2 integer values in format 'i j'";
                    System.out.println(clientString+"sending: "+ msgToClient);
                    out.println(msgToClient);
                }
                else if (Integer.parseInt(indexes[1])>=dim || Integer.parseInt(indexes[2])>=dim
                        || Integer.parseInt(indexes[1])<0 || Integer.parseInt(indexes[2])<0){
                    msgToClient=String.format(ERROR_MSG,": out of range indexes");
                    System.out.println(clientString+"sending: "+ msgToClient);
                    out.println(msgToClient);
                }
                else if (!(CB.getCard(Integer.parseInt(indexes[1]),
                        Integer.parseInt(indexes[2])).isHidden())){
                    msgToClient=String.format(ERROR_MSG,": Card at "+Integer.parseInt(indexes[1])+" "
                            +Integer.parseInt(indexes[2])+" is " +
                            "already revealed");
                    System.out.println(clientString+"sending: "+ msgToClient);
                    out.println(msgToClient);
                }
                else if (indexes[0].equalsIgnoreCase(REVEAL)){
                    ConcentrationBoard.CardMatch cardMatch = CB.reveal(Integer.parseInt(indexes[1]),
                            Integer.parseInt(indexes[2]));
                    processRevealedCard(cardMatch,out);
                }
                else {
                    throw new ConcentrationException("Prepend 'REVEAL' in the message.");
                }
            }catch (NumberFormatException | InterruptedException nfe){
                out.println(String.format(ERROR_MSG,":Please enter integer values between 0 and "+dim));
            }

        }
        System.out.println(GAME_OVER);
        out.println(GAME_OVER_MSG);
    }

    /**
     * The function to process a card which is to be revealed
     * @param cardMatch
     * @param out
     */
    public void processRevealedCard(ConcentrationBoard.CardMatch cardMatch, PrintWriter out) throws InterruptedException {
        String msgToClient = "";
        if (!cardMatch.isReady()){
            msgToClient = String.format(CARD_MSG,cardMatch.getCard1().getRow()
                    ,cardMatch.getCard1().getCol()
                    ,cardMatch.getCard1().getLetter());
            System.out.println(clientString+"sending: "+ msgToClient);
            out.println(msgToClient);
        }
        else{
            if (cardMatch.isMatch()){
                msgToClient=String.format(CARD_MSG,cardMatch.getCard2().getRow()
                        ,cardMatch.getCard2().getCol()
                        ,cardMatch.getCard2().getLetter());
                System.out.println(clientString+"sending: "+ msgToClient);
                out.println(msgToClient);

                msgToClient=String.format(MATCH_MSG,cardMatch.getCard1().getRow()
                        ,cardMatch.getCard1().getCol()
                        ,cardMatch.getCard2().getRow()
                        ,cardMatch.getCard2().getCol());
                System.out.println(clientString+"sending: "+ msgToClient);
                out.println(msgToClient);
            }
            else{
                msgToClient=String.format(CARD_MSG,cardMatch.getCard2().getRow()
                        ,cardMatch.getCard2().getCol()
                        ,cardMatch.getCard2().getLetter());
                System.out.println(clientString+"sending: "+ msgToClient);
                out.println(msgToClient);
                sleep(1000);

                msgToClient=String.format(MISMATCH_MSG,
                        cardMatch.getCard1().getRow()
                        ,cardMatch.getCard1().getCol()
                        ,cardMatch.getCard2().getRow()
                        ,cardMatch.getCard2().getCol());
                System.out.println(clientString+"sending: "+ msgToClient);
                out.println(msgToClient);
            }
        }
    }

    /**
     * The function to print the board
     * @throws ConcentrationException
     */
    public void printSolution() throws ConcentrationException {
        StringBuilder str = new StringBuilder();
        // build the top row of indices
        str.append("  ");
        for (int col=0; col<dim; ++col) {
            str.append(col);
        }
        str.append("\n");
        // build each row of the actual board
        for (int row=0; row<dim; ++row) {
            str.append(row).append("|");
            // build the columns of the board
            for (int col=0; col<dim; ++col) {
                str.append(CB.getCard(row,col).getLetter());
            }
            str.append("\n");
        }
        System.out.println(str);
    }
}
