import utility.ThreadSolveTDoA;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketHandle extends Thread {
    private Socket socket;
    BufferedWriter bufferedWriter;
    BufferedWriter bufferedWriterOfD;

    FileWriter fileWriter = null;
    ExecutorService executor = Executors.newCachedThreadPool();

    public SocketHandle(Socket socket) {

        this.socket = socket;
        SimpleDateFormat sf = new SimpleDateFormat("_MM_dd_HH_mm_ss");// pattern大小写敏感
        try {
            fileWriter = new FileWriter("location" + sf.format(System.currentTimeMillis()) + ".txt", true);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriterOfD = new BufferedWriter(new FileWriter("distance" +
                    sf.format(System.currentTimeMillis()) + ".txt", true));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void out(byte[] bytes) {
        try {
            System.out.print("send:");
            for (int i = 0; i < bytes.length; i++) {
                System.out.printf("%x| ", bytes[i]);
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
        try {
            dis = new DataInputStream(socket.getInputStream());
            String str;
            byte[] bytes = new byte[19];
            int len = 0;
            boolean flag = true;
            while ((len = dis.read(bytes)) != -1) {
                /*获得定位坐标（注释部分）*/
                double[] d = new double[8];
                if (flag) {
                    flag = false;
                    d[0] += ((bytes[2] & 0xff) << 24) + ((bytes[3] & 0xff) << 16) +
                            ((bytes[4] & 0xff) << 8) + (bytes[5] & 0xff);
                    d[1] += ((bytes[6] & 0xff) << 24) + ((bytes[7] & 0xff) << 16) +
                            ((bytes[8] & 0xff) << 8) + (bytes[9] & 0xff);
                    d[2] += ((bytes[10] & 0xff) << 24) + ((bytes[11] & 0xff) << 16) +
                            ((bytes[12] & 0xff) << 8) + (bytes[13] & 0xff);
                    d[3] += ((bytes[14] & 0xff) << 24) + ((bytes[15] & 0xff) << 16) +
                            ((bytes[16] & 0xff) << 8) + (bytes[17] & 0xff);
                    System.out.print("d1 = " + String.valueOf(d[0]) + " d2 = " + String.valueOf(d[1]) + " d3 = "
                            + String.valueOf(d[2]) + " d4 = " + String.valueOf(d[3]));
                } else {
                    flag = true;
                    d[4] += ((bytes[2] & 0xff) << 24) + ((bytes[3] & 0xff) << 16) +
                            ((bytes[4] & 0xff) << 8) + (bytes[5] & 0xff);
                    d[5] += ((bytes[6] & 0xff) << 24) + ((bytes[7] & 0xff) << 16) +
                            ((bytes[8] & 0xff) << 8) + (bytes[9] & 0xff);
                    d[6] += ((bytes[10] & 0xff) << 24) + ((bytes[11] & 0xff) << 16) +
                            ((bytes[12] & 0xff) << 8) + (bytes[13] & 0xff);
                    d[7] += ((bytes[14] & 0xff) << 24) + ((bytes[15] & 0xff) << 16) +
                            ((bytes[16] & 0xff) << 8) + (bytes[17] & 0xff);
                    System.out.println(" d5 = " + String.valueOf(d[4]) + " d6 = " + String.valueOf(d[5]) + " d7 = "
                            + String.valueOf(d[6]) + " d8 = " + String.valueOf(d[7]));

                    for (int i = 0; i < 7; i++) {
                        bufferedWriterOfD.write(String.valueOf(d[i]) + "   ,");
                    }
                    bufferedWriterOfD.write(String.valueOf(d[7]) + "\r\n");
                    bufferedWriterOfD.flush();
/******************************计算TDoA****************************/
                    ThreadSolveTDoA solveTDoA = new ThreadSolveTDoA();
                    solveTDoA.setD(d);
                    solveTDoA.setBufferedWriter(bufferedWriter);
                    executor.execute(solveTDoA);
                }
            }

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
