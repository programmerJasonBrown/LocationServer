package utility;

import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import getLocation.GetLocation;

import java.io.BufferedWriter;
import java.io.IOException;

public class ThreadSolveTDoA implements Runnable {
    private BufferedWriter bufferedWriter;
    private double[] d;

    public void setBufferedWriter(BufferedWriter bufferedWriter) {
        this.bufferedWriter = bufferedWriter;
    }

    public void setD(double[] d) {
        this.d = d;
    }

    @Override
    public void run() {
        /******************************************MATLAB计算定位结果***********************************************/
        GetLocation getLocation = null;
        Object[] rs = null;
        MWNumericArray input = new MWNumericArray(d, MWClassID.DOUBLE);
        try {
            getLocation = new GetLocation();
            rs = getLocation.getX(3, input);
        } catch (MWException e) {
            e.printStackTrace();
        }
/*******************************************输出到控制台和文件*********************************************************/
        System.out.println("++++++++++++++++++++++++++++++++++++");
        System.out.print("x = " + rs[0] + ", ");
        System.out.print("y = " + rs[1] + ", ");
        System.out.println("z = " + rs[2]);
        System.out.println("===================================");
        System.out.println();
        try {
            bufferedWriter.write(rs[0] + "   ," + rs[1] + "   ," + rs[2] + "\r\n");
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
