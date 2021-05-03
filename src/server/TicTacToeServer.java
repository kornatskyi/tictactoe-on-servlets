package server;

import java.net.*;
import java.io.*;


public class TicTacToeServer {

    public static void main(String[] args) {

        final int port = 5000;
        DataOutputStream out1 = null;
        DataInputStream in1 = null;
        DataOutputStream out2 = null;
        DataInputStream in2 = null;
        DataInputStream stdIn = new DataInputStream(System.in);
        try (
                ServerSocket serverSocket = new ServerSocket(port)

        ) {
            System.out.println("Socket created. Waiting for client connection...");
            Socket clientSocket1 = serverSocket.accept();
            Socket clientSocket2 = serverSocket.accept();


            System.out.println("Client connected.");


            out1 = new DataOutputStream(clientSocket1.getOutputStream());
            in1 = new DataInputStream(clientSocket1.getInputStream());
            out2 = new DataOutputStream(clientSocket2.getOutputStream());
            in2 = new DataInputStream(clientSocket2.getInputStream());

            String player1;
            String player2;
            out1.writeUTF("******Hello this is a TicTacToe game!******");
            out2.writeUTF("******Hello this is a TicTacToe game!******");
            out1.writeUTF("Enter a name of Player 1: ");
            out1.writeUTF("n");

            player1 = in1.readUTF();
            System.out.println(player1);
            out1.writeUTF("Hi " + player1 + ".");
            out2.writeUTF("Player 1 chose name " + player1 + ".");
            out2.writeUTF("Now enter a name of Player 2: ");
            out2.writeUTF("n");
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

    public static class TicTacToe {

        private char[] grid;
        private String player1;
        private String player2;
        private DataInputStream p1in;
        private DataOutputStream p1out;
        private DataInputStream p2in;
        private DataOutputStream p2out;

        private boolean playersState;
        private int cellNumber;


        public TicTacToe(String plr1, String plr2,
                         DataInputStream input1, DataOutputStream output1,
                         DataInputStream input2, DataOutputStream output2) {
            grid = new char[]{'#', '#', '#', '#', '#', '#', '#', '#', '#'};
            player1 = plr1;
            player2 = plr2;
            p1in = input1;
            p1out = output1;
            p2in = input2;
            p2out = output2;
            playersState = true;

        }

        public void setPlayersState(boolean playersState) {
            this.playersState = playersState;
        }

        public boolean getPlayerState() {
            return playersState;
        }

        public void startGame() throws IOException {
            while (isThereLine() == 0) {
                printGrid();
                (playersState?p1out:p2out).writeUTF("Player " + getPlayer() + " enter the number of a cell(0-8): ");
                (playersState?p1out:p2out).writeUTF("n");
                while (!putValueInTheCell(Integer.parseInt((playersState?p1in:p2in).readUTF()))) {
                    (playersState?p1out:p2out).writeUTF("Wrong cell number or a value!\nEnter value again(0-8): ");
                    (playersState?p1out:p2out).writeUTF("n");
                }
                playersState = !playersState;
            }
            playersState = !playersState;

            p2out.writeUTF("\n\n$$$**** Player " + getPlayer() + " won. ****$$$");
            p1out.writeUTF("\n\n$$$**** Player " + getPlayer() + " won. ****$$$");
            printGrid();
            //Prevent Eof on client side
            p2out.writeUTF("Over");
            p1out.writeUTF("Over");
        }

        public boolean putValueInTheCell(int cellNumber) {
            if (this.grid[cellNumber] == '#') {
                this.grid[cellNumber] = playersState ? 'X' : 'O';
                return true;
            } else {
                return false;
            }
        }

        private void printGrid() throws IOException {
            p1out.writeUTF(this.grid[0] + "|" + this.grid[1] + "|" + this.grid[2] + "\n-----\n" +
                    this.grid[3] + "|" + this.grid[4] + "|" + this.grid[5] + "\n-----\n" +
                    this.grid[6] + "|" + this.grid[7] + "|" + this.grid[8]);
            p2out.writeUTF(this.grid[0] + "|" + this.grid[1] + "|" + this.grid[2] + "\n-----\n" +
                    this.grid[3] + "|" + this.grid[4] + "|" + this.grid[5] + "\n-----\n" +
                    this.grid[6] + "|" + this.grid[7] + "|" + this.grid[8]);
        }

        public int isThereLine() {
            if (this.grid[0] == this.grid[1] && this.grid[1] == this.grid[2] && this.grid[0] != '#') {
                return this.grid[0];
            }
            if (this.grid[0] == this.grid[3] && this.grid[3] == this.grid[6] && this.grid[0] != '#') {
                return this.grid[0];
            }
            if (this.grid[3] == this.grid[4] && this.grid[4] == this.grid[5] && this.grid[3] != '#') {
                return this.grid[3];
            }
            if (this.grid[1] == this.grid[4] && this.grid[4] == this.grid[7] && this.grid[1] != '#') {
                return this.grid[1];
            }
            if (this.grid[6] == this.grid[7] && this.grid[7] == this.grid[8] && this.grid[6] != '#') {
                return this.grid[6];
            }
            if (this.grid[2] == this.grid[5] && this.grid[5] == this.grid[8] && this.grid[2] != '#') {
                return this.grid[2];
            }
            return 0;
        }

        private String getPlayer() {
            return this.playersState ? this.player1 : this.player2;
        }
    }
}

