package server;

import java.net.*;
import java.io.*;



public class TicTacToeServer {

    public static void main(String[] args) {

        final int port = 5000;
        DataOutputStream out = null;
        DataInputStream in = null;
        DataInputStream stdIn = new DataInputStream(System.in);
        try (
                ServerSocket serverSocket = new ServerSocket(port)

        ) {
            System.out.println("Socket created. Waiting for client connection...");
            Socket clientSocket = serverSocket.accept();

            System.out.println("Client connected.");


            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());

        String player1;
        String player2;
        out.writeUTF("******Hello this is a TicTacToe game!******");
        out.writeUTF("Enter a name of Player 1: ");
        out.writeUTF("n");

        player1 = in.readUTF();
            System.out.println(player1);
        out.writeUTF("Hi " + player1 + ".");
        out.writeUTF("Now enter a name of Player 2: ");
            out.writeUTF("n");
        player2 = in.readUTF();
        out.writeUTF("Hello " + player2 + ".");
        TicTacToe game = new TicTacToe(player1, player2, in, out);
        game.startGame();


        clientSocket.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


    }
    public static class TicTacToe {

        private char[] grid;
        private String player1;
        private String player2;
        private DataInputStream in;
        private DataOutputStream out;

        private boolean playersState;
        private int cellNumber;


        public TicTacToe(String plr1, String plr2, DataInputStream input, DataOutputStream output) {
            grid = new char[]{'#', '#', '#', '#', '#', '#', '#', '#', '#'};
            player1 = plr1;
            player2 = plr2;
            in = input;
            out = output;

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
                out.writeUTF("Player " + getPlayer() + " enter the number of a cell(0-8): ");
                out.writeUTF("n");
                while (!putValueInTheCell(Integer.parseInt(in.readUTF()))) {
                    out.writeUTF("Wrong cell number or a value!\nEnter value again(0-8): ");
                    out.writeUTF("n");
                }
                playersState = !playersState;
            }
            playersState = !playersState;

            out.writeUTF("\n\n$$$**** Player " + getPlayer() + " won. ****$$$");
            printGrid();
            out.writeUTF("Over");
        }

        public boolean putValueInTheCell(int cellNumber) {
            if(this.grid[cellNumber] == '#') {
                this.grid[cellNumber] = playersState?'X':'O';
                return true;
            } else {
                return false;
            }
        }

        private void printGrid() throws IOException {
            out.writeUTF(this.grid[0] + "|" + this.grid[1] + "|" + this.grid[2] + "\n-----\n" +
                    this.grid[3] + "|" + this.grid[4] + "|" + this.grid[5] + "\n-----\n" +
                    this.grid[6] + "|" + this.grid[7] + "|" + this.grid[8]);
        }

        public int isThereLine() {
            if(this.grid[0] == this.grid[1] && this.grid[1] == this.grid[2] && this.grid[0] != '#') {
                return this.grid[0];
            }
            if(this.grid[0] == this.grid[3] && this.grid[3] == this.grid[6] && this.grid[0] != '#') {
                return this.grid[0];
            }
            if(this.grid[3] == this.grid[4] && this.grid[4] == this.grid[5] && this.grid[3] != '#') {
                return this.grid[3];
            }
            if(this.grid[1] == this.grid[4] && this.grid[4] == this.grid[7] && this.grid[1] != '#') {
                return this.grid[1];
            }
            if(this.grid[6] == this.grid[7] && this.grid[7] == this.grid[8] && this.grid[6] != '#') {
                return this.grid[6];
            }
            if(this.grid[2] == this.grid[5] && this.grid[5] == this.grid[8] && this.grid[2] != '#') {
                return this.grid[2];
            }
            return 0;
        }

        private String getPlayer() {
            return this.playersState?this.player1:this.player2;
        }
    }
}

