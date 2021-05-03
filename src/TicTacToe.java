
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TicTacToe {

    private char[] grid;
    private String player1;
    private String player2;
    private DataInputStream p1in;
    private DataOutputStream p1out;
    private DataInputStream p2in;
    private DataOutputStream p2out;

    private boolean playersState;

    public TicTacToe() {

    }

    public TicTacToe(String plr1, String plr2, DataInputStream input1, DataOutputStream output1, DataInputStream input2,
            DataOutputStream output2) {
        grid = new char[] { '#', '#', '#', '#', '#', '#', '#', '#', '#' };
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
        p1out.writeUTF("Game created!");
        p2out.writeUTF("Game created!");

        while (isThereLine() == 0) {
            int value;
            printGrid();
            (playersState ? p1out : p2out).writeUTF("Player " + getPlayer() + " enter the number of a cell(0-8): ");
            (!playersState ? p1out : p2out).writeUTF("Waiting when player " + getPlayer() + " input value...");
            (playersState ? p1out : p2out).writeUTF("n");
            while (!putValueInTheCell(value = Integer.parseInt((playersState ? p1in : p2in).readUTF())) || value > 8
                    || value < 0 || value != (int) value) {
                (playersState ? p1out : p2out).writeUTF("Wrong cell number or a value!\nEnter value again(0-8): ");
                (playersState ? p1out : p2out).writeUTF("n");
            }
            playersState = !playersState;
        }
        playersState = !playersState;

        p2out.writeUTF("\n\n$$$**** Player " + getPlayer() + " won. ****$$$");
        p1out.writeUTF("\n\n$$$**** Player " + getPlayer() + " won. ****$$$");
        printGrid();
        // Prevent Eof on client side
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
        p1out.writeUTF(this.grid[0] + "|" + this.grid[1] + "|" + this.grid[2] + "\n-----\n" + this.grid[3] + "|"
                + this.grid[4] + "|" + this.grid[5] + "\n-----\n" + this.grid[6] + "|" + this.grid[7] + "|"
                + this.grid[8] + "\n _____");
        p2out.writeUTF(this.grid[0] + "|" + this.grid[1] + "|" + this.grid[2] + "\n-----\n" + this.grid[3] + "|"
                + this.grid[4] + "|" + this.grid[5] + "\n-----\n" + this.grid[6] + "|" + this.grid[7] + "|"
                + this.grid[8] + "\n_____");
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
