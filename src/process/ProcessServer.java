package process;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ProcessServer {

    public static void main(String[] args) {

        try{
            Socket socket = new Socket("localhost", 8888);

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            dos.close();
            dis.close();
            socket.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
