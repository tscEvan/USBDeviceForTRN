package com.example.usbdevicefortrn.chaos;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

public class ChaosWithBluetooth {
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private float g1, g2, g3, g4;
    private float h1, h2;
    private float j1, j2;
    private float u1, u2;
    private float ax1, ax2, ax3;
    private float dx1, dx2, dx3;
    private float A;
    private float c1, c2;
    private float x1, x2, x3;
    private float x1s, x2s, x3s;
    private float y1, y2, y3;
    private float y1s, y2s, y3s;
    private int testNum;
    OutputStream write;
    InputStream read;
    String log = "test",strSet="0";
    private boolean lockState;

    public ChaosWithBluetooth(OutputStream write, InputStream read){
        this.write = write;
        this.read = read;
        initailizeChaos();
    }

    private void initailizeChaos() {
        ax1 = 1;
        ax2 = 1;
        ax3 = 1;
        dx1 = 1;
        dx2 = 1;
        dx3 = 1;
        c1 = (float) -0.5;
        c2 = (float) 0.06;
        A = (float) 0.1;
        x1 = -0.1f;
        x2 = 0.4f;
        x3 = -0.3f;
    }

    public void chaosMath() { //手機端的混沌公式
        g1 = -(ax1 / (ax2 * ax2));
        g2 = (float) 2 * ax1 * dx2 / (ax2 * ax2);
        g3 = (float) -0.1 * ax1 / ax3;
        g4 = (float) (ax1 * (1.76 - (dx2 * dx2) / (ax2 * ax2) + 0.1 * ax1 * dx3 / ax3) + dx1);

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

    public float getU1() {
        return u1;
    }

    public float getX1() {
        return x1;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    public float getX2() {
        return x2;
    }

    public void setX2(float x2) {
        this.x2 = x2;
    }

    public float getX3() {
        return x3;
    }

    public void setX3(float x3) {
        this.x3 = x3;
    }
    //--------------------bluetooth-------------------------------

    public int inputPort() {
        try {
            return read.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void ieee754Write(float val) {
        int sendVal = Float.floatToIntBits(val);
        Log.i(log, "sendVal = " + sendVal);
        int f1 = Integer.parseInt(strSet), f2 = Integer.parseInt(strSet), f3 = Integer.parseInt(strSet),
                f4 = Integer.parseInt(strSet), f5 = Integer.parseInt(strSet), f6 = Integer.parseInt(strSet),
                f7 = Integer.parseInt(strSet), f8 = Integer.parseInt(strSet), f9 = Integer.parseInt(strSet),
                f10 = Integer.parseInt(strSet), f11 = Integer.parseInt(strSet);
        byte us[] = new byte[11];
        us[0] = (byte) ((sendVal & 0xe0000000) >>> 29);
        f1 = us[0];
        us[1] = (byte) ((sendVal & 0x1c000000) >>> 26);
        f2 = us[1];
        us[2] = (byte) ((sendVal & 0x03800000) >>> 23);
        f3 = us[2];
        us[3] = (byte) ((sendVal & 0x00700000) >>> 20);
        f4 = us[3];
        us[4] = (byte) ((sendVal & 0x000e0000) >>> 17);
        f5 = us[4];
        us[5] = (byte) ((sendVal & 0x0001c000) >>> 14);
        f6 = us[5];
        us[6] = (byte) ((sendVal & 0x00003800) >>> 11);
        f7 = us[6];
        us[7] = (byte) ((sendVal & 0x00000700) >>> 8);
        f8 = us[7];
        us[8] = (byte) ((sendVal & 0x000000e0) >>> 5);
        f9 = us[8];
        us[9] = (byte) ((sendVal & 0x0000001c) >>> 2);
        f10 = us[9];
        us[10] = (byte) ((sendVal & 0x00000003));
        f11 = us[10];
        try {
            write.write(f1);
            Log.i(log, "f1 = " + f1);
            write.write(f2);
            Log.i(log, "f2 = " + f2);
            write.write(f3);
            Log.i(log, "f3 = " + f3);
            write.write(f4);
            Log.i(log, "f4 = " + f4);
            write.write(f5);
            Log.i(log, "f5 = " + f5);
            write.write(f6);
            Log.i(log, "f6 = " + f6);
            write.write(f7);
            Log.i(log, "f7 = " + f7);
            write.write(f8);
            Log.i(log, "f8 = " + f8);
            write.write(f9);
            Log.i(log, "f9 = " + f9);
            write.write(f10);
            Log.i(log, "f10 = " + f10);
            write.write(f11);
            Log.i(log, "f11 = " + f11);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void ieee754Write(float val,int version) {
        int sendVal = Float.floatToIntBits(val);
        Log.i(log, "sendVal = " + sendVal);
        int f1 = Integer.parseInt(strSet), f2 = Integer.parseInt(strSet), f3 = Integer.parseInt(strSet),
                f4 = Integer.parseInt(strSet);
        byte us[] = new byte[4];
        us[0] = (byte) ((sendVal >>> 24) & 0xFF);
        f1 = us[0];
        us[1] = (byte) ((sendVal >>> 16) & 0xFF);
        f2 = us[1];
        us[2] = (byte) ((sendVal >>> 8) & 0xFF);
        f3 = us[2];
        us[3] = (byte) (sendVal & 0xFF);
        f4 = us[3];
        try {
            write.write(f1);
            Log.i(log, "f1 = " + f1);
            write.write(f2);
            Log.i(log, "f2 = " + f2);
            write.write(f3);
            Log.i(log, "f3 = " + f3);
            write.write(f4);
            Log.i(log, "f4 = " + f4);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public float getMCUreturn() {
        int floatS = Integer.parseInt(strSet);
        try {
            floatS |= ((read.read() & 0x07)) << 29;
            floatS |= ((read.read() & 0x07)) << 26;
            floatS |= ((read.read() & 0x07)) << 23;
            floatS |= ((read.read() & 0x07)) << 20;
            floatS |= ((read.read() & 0x07)) << 17;
            floatS |= ((read.read() & 0x07)) << 14;
            floatS |= ((read.read() & 0x07)) << 11;
            floatS |= ((read.read() & 0x07)) << 8;
            floatS |= ((read.read() & 0x07)) << 5;
            floatS |= ((read.read() & 0x07)) << 2;
            floatS |= ((read.read() & 0x03));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return floatS;
    }

    private void mathLoop(float key) {
        float x1;
//        Log.d("chaosTest", "Start");
        testNum++;
        if(testNum >= 100){
            return;
        }
        chaosMath();
        ieee754Write(getU1(),1);
//        ieee754Write(getU1());
        x1 = getX1();
//        Log.d("chaosTest", "u1\t"+getU1());
        ieee754Write((1 + (x1 * x1)) * key,1);
//        ieee754Write((1 + (x1 * x1)) * key);
        int check = inputPort();
        if (check == 65) {
            Log.d("MCUReturn", Float.toString(getMCUreturn()));
            mathLoop(key);
        } else if (check == 66) {
            Log.d("MCUReturn", Float.toString(getMCUreturn()));
            Log.d("MCUReturn", "=============Lock one Open=============");
        } else if (check == 67) {
            Log.d("MCUReturn", Float.toString(getMCUreturn()));
            Log.d("MCUReturn", "=============Lock two Open=============");
        } else if (check == 68) {
            Log.d("MCUReturn", Float.toString(getMCUreturn()));
            Log.d("MCUReturn", "=============Lock there Open=============");
            lockState = true;
        }
    }
//
    public boolean start(List<Double> chaosKey){//Context context) {
        lockState = false;
        testNum = 0;
        mathLoop(-12345f);
        mathLoop( -543.21f);
        mathLoop(21.354f);
//        Toast.makeText(context, "lock open state is "+lockState, Toast.LENGTH_SHORT).show();
        return lockState;
    }
}