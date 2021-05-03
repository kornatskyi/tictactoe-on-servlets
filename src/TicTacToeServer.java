

import java.net.*;
import java.io.*;

public class TicTacToeServer {

    public static void main(String[] args) {

        final int port = 5000;
        DataOutputStream out1 = null;
        DataInputStream in1 = null;
        DataOutputStream out2 = null;
        DataInputStream in2 = null;
        try (ServerSocket serverSocket = new ServerSocket(port)

        ) {
            System.out.println("Socket created. Waiting for client connection...");
            Socket clientSocket1 = serverSocket.accept();
            System.out.println("First client connected.");
            Socket clientSocket2 = serverSocket.accept();
            System.out.println("Second client connected.");

            //Create output and input stream for both players
            out1 = new DataOutputStream(clientSocket1.getOutputStream());
            in1 = new DataInputStream(clientSocket1.getInputStream());
            out2 = new DataOutputStream(clientSocket2.getOutputStream());
            in2 = new DataInputStream(clientSocket2.getInputStream());

            String player1;
            String player2;
            out1.writeUTF("******Hello this is a TicTacToe game!******");
            out2.writeUTF("******Hello this is a TicTacToe game!******");

            out2.writeUTF("Waiting for player one insert name...");

            out1.writeUTF("Enter a name of Player 1: ");
            out1.writeUTF("n"); //trigger for client input
            player1 = in1.readUTF();
            out1.writeUTF("Hi " + player1 + ".");
            out2.writeUTF("Player 1 chose name " + player1 + ".");
            out1.writeUTF("Waiting for player two insert name...");
            out2.writeUTF("Now enter a name of Player 2: ");
            out2.writeUTF("n"); //trigger for client input
            player2 = in2.readUTF();
            out2.writeUTF("Hello " + player2 + ".");
            out1.writeUTF("Player 1 chose name " + player2 + ".");
            TicTacToe game = new TicTacToe(player1, player2, in1, out1, in2, out2);
            game.startGame();

            clientSocket1.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    
}
