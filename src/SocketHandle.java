import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import getLocation.GetLocation;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;

public class SocketHandle extends Thread {
    private Socket socket;
    BufferedWriter bufferedWriter;
    FileWriter fileWriter = null;

    public SocketHandle(Socket socket) {

        this.socket = socket;
        SimpleDateFormat sf = new SimpleDateFormat("_MM_dd_HH_mm_ss");// pattern大小写敏感
        try {
            fileWriter = new FileWriter("location" + sf.format(System.currentTimeMillis()) + ".txt", true);
            bufferedWriter = new BufferedWriter(fileWriter);
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
//                for (int i = 0; i < len; i++) {
//                    System.out.printf("%x| ", bytes[i]);
//                }
//                System.out.println();
//                System.out.println("=====");

                /*获得定位坐标（注释部分）*/
                double[] d = new double[8];
                if (flag) {
                    flag = false;
                    d[0] = ((bytes[2] & 0xff) << 24) + ((bytes[3] & 0xff) << 16) +
                            ((bytes[4] & 0xff) << 8) + (bytes[5] & 0xff);
                    d[1] = ((bytes[6] & 0xff) << 24) + ((bytes[7] & 0xff) << 16) +
                            ((bytes[8] & 0xff) << 8) + (bytes[9] & 0xff);
                    d[2] = ((bytes[10] & 0xff) << 24) + ((bytes[11] & 0xff) << 16) +
                            ((bytes[12] & 0xff) << 8) + (bytes[13] & 0xff);
                    d[3] = ((bytes[14] & 0xff) << 24) + ((bytes[15] & 0xff) << 16) +
                            ((bytes[16] & 0xff) << 8) + (bytes[17] & 0xff);
                    System.out.print("d1 = " + String.valueOf(d[0]) + " d2 = " + String.valueOf(d[1]) + " d3 = "
                            + String.valueOf(d[2]) + " d4 = " + String.valueOf(d[3]));
                }
                if (!flag) {
                    flag = true;
                    d[4] = ((bytes[2] & 0xff) << 24) + ((bytes[3] & 0xff) << 16) +
                            ((bytes[4] & 0xff) << 8) + (bytes[5] & 0xff);
                    d[5] = ((bytes[6] & 0xff) << 24) + ((bytes[7] & 0xff) << 16) +
                            ((bytes[8] & 0xff) << 8) + (bytes[9] & 0xff);
                    d[6] = ((bytes[10] & 0xff) << 24) + ((bytes[11] & 0xff) << 16) +
                            ((bytes[12] & 0xff) << 8) + (bytes[13] & 0xff);
                    d[7] = ((bytes[14] & 0xff) << 24) + ((bytes[15] & 0xff) << 16) +
                            ((bytes[16] & 0xff) << 8) + (bytes[17] & 0xff);
                    System.out.println(" d5 = " + String.valueOf(d[4]) + " d6 = " + String.valueOf(d[5]) + " d7 = "
                            + String.valueOf(d[6]) + " d8 = " + String.valueOf(d[7]));
                    GetLocation getLocation = null;
                    Object[] rs = null;

                    MWNumericArray input = new MWNumericArray(d, MWClassID.DOUBLE);


                    try {
                        getLocation = new GetLocation();
                        rs = getLocation.getX(3, input);
                    } catch (MWException e) {
                        e.printStackTrace();
                    }
                    System.out.println("++++++++++++++++++++++++++++++++++++");
                    System.out.print("x = " + rs[0] + ", ");
                    System.out.print("y = " + rs[1] + ", ");
                    System.out.println("z = " + rs[2]);
                    System.out.println("===================================");
                    System.out.println();
                    bufferedWriter.write(rs[0] + "," + rs[1] + "," + rs[2]);
                    bufferedWriter.flush();
                }
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
