import com.mathworks.toolbox.javabuilder.MWException;
import getLocation.GetLocation;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;

public class SocketHandle2 extends Thread {
    private Socket socket;

    public SocketHandle2(Socket socket) {
        this.socket = socket;
    }

    public void out(byte[] bytes) {
        try {
            System.out.print("send:");
            for (int i = 0; i < bytes.length; i++) {
                System.out.printf("%x|  ", bytes[i]);
            }
            System.out.println();
            socket.getOutputStream().write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        DataInputStream dis;
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        SimpleDateFormat sf = new SimpleDateFormat("HHmmss");// pattern大小写敏感
        try {
            fileWriter = new FileWriter("location" + sf.format(System.currentTimeMillis()) + ".txt", true);
            bufferedWriter = new BufferedWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            dis = new DataInputStream(socket.getInputStream());
            String str;
            byte[] bytes = new byte[19];
            while ((dis.read(bytes)) != -1) {
                for (int i = 0; i < bytes.length; i++) {
                    System.out.printf("%x| ", bytes[i]);
                }
                System.out.println("=====");

                /*获得定位坐标（注释部分）*/
                int d1 = 0;
                int d2 = 0;
                int d3 = 0;
                int d4 = 0;
                d1 = ((bytes[2] & 0xff) << 24) + ((bytes[3] & 0xff) << 16) +
                        ((bytes[4] & 0xff) << 8) + (bytes[5] & 0xff);
                d2 = ((bytes[6] & 0xff) << 24) + ((bytes[7] & 0xff) << 16) +
                        ((bytes[8] & 0xff) << 8) + (bytes[9] & 0xff);
                d3 = ((bytes[10] & 0xff) << 24) + ((bytes[11] & 0xff) << 16) +
                        ((bytes[12] & 0xff) << 8) + (bytes[13] & 0xff);
                d4 = ((bytes[14] & 0xff) << 24) + ((bytes[15] & 0xff) << 16) +
                        ((bytes[16] & 0xff) << 8) + (bytes[17] & 0xff);
                System.out.println("d1=" + String.valueOf(d1) + " d2=" + String.valueOf(d2) + " d3=" + String.valueOf(d3) + " d4=" + String.valueOf(d4));
                double L = (d2 - d1) / 1000.0;  //L 表示里第一个参考节点的距离
                double R = (d3 - d1) / 1000.0;
                double Q = (d4 - d1) / 1000.0;

                GetLocation getLocation = null;
                Object[] rs = null;
                try {
                    getLocation = new GetLocation();
                    rs = getLocation.getX(3, L, Q, R);
                } catch (MWException e) {
                    e.printStackTrace();
                }
                System.out.print("x = " + rs[0] + ", ");
                System.out.print("y = " + rs[1] + ", ");
                System.out.println("z = " + rs[2]);
                bufferedWriter.write(rs[0].toString() + " " + rs[1].toString() + " " + rs[2].toString());
                bufferedWriter.flush();
            }
            //socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

