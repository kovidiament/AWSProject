import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class projectClient
{

    public static void main(String[] arg){
        Scanner scan = new Scanner(System.in);
        try {
            String serverName = "ec2-54-244-205-173.us-west-2.compute.amazonaws.com";
            int port = 3000;

            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            PrintWriter out = new PrintWriter(outToServer, true);
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);


            while(true)
            {
                System.out.println(in.readUTF());
                out.println(scan.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


