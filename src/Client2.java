

import java.net.*;
import java.io.*;
import java.util.Scanner;


public class Client2 {
    public static void main(String[] args) {
        final int port = 5000;
        final String host = "localhost";

        Scanner scanner = new Scanner(System.in);
        DataOutputStream out = null;
        DataInputStream in = null;
        try {
            Socket clientSocket = new Socket(host, port);
            System.out.println("Socket created.");

            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());

            try {
                String str = "";
                while (!(str = in.readUTF()).equals("Over")) {

                    if (str.equals("n")) {
                        out.writeUTF(scanner.nextLine());
                    } else {
                        System.out.println(str);
                    }
                }
            } catch (EOFException e) {
                System.out.println("Eof");
            }


            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}