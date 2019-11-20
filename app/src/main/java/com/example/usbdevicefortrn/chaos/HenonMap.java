package com.example.usbdevicefortrn.chaos;

import android.util.Log;

import java.math.BigDecimal;

public class HenonMap {
    private HenonMap henonMap;

    double g1, g2, g3, g4;
    double h1, h2;
    double j1, j2;
    double u1, u2;
    double ax1, ax2, ax3;
    double dx1, dx2, dx3;
    double A;
    double c1, c2;
    double x1, x2, x3;
    double x1s, x2s, x3s;
    double y1, y2, y3;
    double y1s, y2s, y3s;
    int testTime = 0;

    public HenonMap() {
    }

    //set inti
    public HenonMap getInstance() {
        if (henonMap == null) {
            henonMap = new HenonMap();
            henonMap.inti();
        }
        return henonMap;
    }

    private void inti() {
        ax1 = 1;
        ax2 = 1;
        ax3 = 1;
        dx1 = 1;
        dx2 = 1;
        dx3 = 1;
        c1 = (double) -0.5;
        c2 = (double) 0.06;
        A = (double) 0.1;
        x1 = -0.1;
        x2 = 0.4;
        x3 = -0.3;
        y1 = -0.8;
        y2 = 0.8;
        y3 = 2;
    }

    public void chaosSystemMath() {
        g1 = -(ax1 / (ax2 * ax2));
        g2 = (double) 2 * ax1 * dx2 / (ax2 * ax2);
        g3 = (double) -0.1 * ax1 / ax3;
        g4 = (double) (ax1 * (1.76 - (dx2 * dx2) / (ax2 * ax2) + 0.1 * ax1 * dx3 / ax3) + dx1);
        h1 = ax2 / ax1;
        h2 = -(ax2 * dx1) / ax1 + dx2;
        j1 = ax3 / ax2;
        j2 = -(ax3 * dx2) / ax2 + dx3;

        u1 = (x2 * x2 * g1) + (x2 * g2) + (x3 * g3) + (x1 * c1 * h1) + x2 * c2 * j1 - x1 * A - x2 * c1 * A - x3 * c2 * A;
        u2 = -(y2 * y2) * g1 - y2 * g2 - y3 * g3 - y1 * c1 * h1 - y2 * c2 * j1 + y1 * A + y2 * c1 * A + y3 * c2 * A;

        x1s = g1 * x2 * x2 + g2 * x2 + g3 * x3 + g4;
        x2s = h1 * x1 + h2;
        x3s = j1 * x2 + j2;


        y1s = g1 * y2 * y2 + g2 * y2 + g3 * y3 + g4 + u1 + u2;//
        y2s = h1 * y1 + h2;
        y3s = j1 * y2 + j2;
        x1 = x1s;
        x2 = x2s;
        x3 = x3s;
        y1 = y1s;
        y2 = y2s;
        y3 = y3s;
        testTime++;
//        Log.d("Math", "x1s： "+x1s+"  y1s： "+y1s);
        if (sub(x1s,y1s)==0) {
//            Log.d("ChaosMath", "x1s： " + x1s + "  y1s： " + y1s + " testTime ：" + testTime + " ");
            System.out.println("x1s：" + x1s + "\ty1s： " + y1s + "\ttestTime ：" + testTime + " ");
        } else {
//            Log.d("ChaosMath", "x1s： " + x1s + "  y1s： " + y1s + " testTime ：" + testTime + " ****");
            System.out.println("x1s：" + x1s + "\ty1s： " + y1s + "\ttestTime ：" + testTime + " ****");
        }
    }
    //    doLoopState = 0;
    //手機端的混沌公式
    public void chaosMasterMath() {
        g1 = -(ax1 / (ax2 * ax2));
        g2 = (double) 2 * ax1 * dx2 / (ax2 * ax2);
        g3 = (double) -0.1 * ax1 / ax3;
        g4 = (double) (ax1 * (1.76 - (dx2 * dx2) / (ax2 * ax2) + 0.1 * ax1 * dx3 / ax3) + dx1);

        h1 = ax2 / ax1;
        h2 = -(ax2 * dx1) / ax1 + dx2;

        j1 = ax3 / ax2;
        j2 = -(ax3 * dx2) / ax2 + dx3;

        u1 = x2 * x2 * g1 + x2 * g2 + x3 * g3 + x1 * c1 * h1 + x2 * c2 * j1 - x1 * A - x2 * c1 * A - x3 * c2 * A;

        x1s = g1 * x2 * x2 + g2 * x2 + g3 * x3 + g4;
        x2s = h1 * x1 + h2;
        x3s = j1 * x2 + j2;
        x1 = x1s;
        x2 = x2s;
        x3 = x3s;
        Log.d("Math", "x1=：\t" + x1+"\tx1s=：\t" + x1s);
    }

    public double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.setScale(5, 0).subtract(b2.setScale(5, 0)).doubleValue();
    }

    public double getU1() {
        return u1;
    }

    public double getX1() {
        return x1;
    }

    public double getX2() {
        return x2;
    }

    public double getX3() {
        return x3;
    }
}