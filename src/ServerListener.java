import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener extends Thread{
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(3000);
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("有新的连接");
                SocketHandle socketHandle = new SocketHandle(socket);
                socketHandle.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
