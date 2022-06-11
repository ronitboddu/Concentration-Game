package server.multiServerThread;

import common.ConcentrationException;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * A class to represent the server which will check the arguments,
 * create the socket and accept the connection.
 *
 * @author RIT CS
 * @author Shivani Singh ,ss5243@rit.edu
 * @author Ronit Boddu, rb1209@g.rit.edu
 */
public class ConcentrationServer {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java ConcentrationServer <port number> <dimension>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        int dim = Integer.parseInt(args[1]);
        int clientNum = 1;
        boolean listening=true;
        try(ServerSocket serverSocket = new ServerSocket(portNumber)){
            System.out.printf("Concentration server starting on port %d, DIM=%d\n",portNumber,dim);
            while(listening){
                new CSMultiServerThread(serverSocket.accept(),dim,clientNum).start();
                clientNum++;
            }
        }catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        } catch (ConcentrationException e) {
            e.printStackTrace();
        }
    }
}
