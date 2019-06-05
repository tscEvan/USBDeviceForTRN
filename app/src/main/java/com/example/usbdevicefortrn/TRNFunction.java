package com.example.usbdevicefortrn;

import com.example.usbdevicefortrn.Chaos.HenonMap;

import java.io.DataInputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;

public class TRNFunction {
    public static final BigInteger modVal =new BigInteger("257");//2^8+1
    public static void main(String[] args) {
        HenonMap henonMap = new HenonMap();
        henonMap.inti();
        int j = 3;
        int j1 = 4;
        int j2 = 5;

        for (int i = 0; i < 10000000; i++) {
//            int data = Float.floatToIntBits(256);
            byte[] dataByteX1 = floatToByteArray(henonMap.getX1());
            byte[] dataByteX2 = floatToByteArray(henonMap.getX2());
            byte[] dataByteX3 = floatToByteArray(henonMap.getX3());

            int dataTRN1 = Integer.valueOf(String.valueOf(TrueNumberFunction(new BigInteger("3"), dataByteX1[j] & 0xff, dataByteX2[j] & 0xff, BigInteger.valueOf(dataByteX3[j] & 0xff))));
            System.out.print("----" + dataTRN1);
            int dataTRN2 = Integer.valueOf(String.valueOf(TrueNumberFunction(new BigInteger("3"), dataByteX1[j1] & 0xff, dataByteX2[j1] & 0xff, BigInteger.valueOf(dataByteX3[j1] & 0xff))));
            System.out.print("----" + dataTRN2);
            BigInteger dataTRN3 = TrueNumberFunction(new BigInteger("3"), dataByteX1[j2] & 0xff, dataByteX2[j2] & 0xff, BigInteger.valueOf(dataByteX3[j2] & 0xff));
            System.out.println("----" + dataTRN3);
//            int newTRN = dataTRN1+dataTRN2+Integer.valueOf(String.valueOf(dataTRN3));
//            System.out.println("new:" + newTRN);
//            System.out.println("TRN: " + TrueNumberFunction(new BigInteger("3"),dataTRN1,dataTRN2,dataTRN3));
            henonMap.chaosSystemMath();
        }
        //乘
//        System.out.println(new BigInteger("4").multiply(new BigInteger("4")));
        //次方
//        System.out.println(getPow("2", 9));
        //取次方後餘數
//        System.out.println(getPow(2, 9).mod(modVal));
        //大數運算取次方後取餘數
//        System.out.println(new BigInteger("2").modPow(new BigInteger("9"), modVal));
        //
//        System.out.println(TrueNumberFunction(new BigInteger("3"), 100, 100, new BigInteger("2")));

    }

    //T.R.N function
    public static BigInteger getPow(BigInteger base, int pow){
        BigInteger bigInteger = base;
        return bigInteger.pow(pow);
    }
    public static BigInteger getPow(String base, int pow){
        BigInteger bigInteger = new BigInteger(base);
        return bigInteger.pow(pow);
    }
    public static BigInteger getPow(int base, int pow){
        BigInteger bigInteger = new BigInteger(String.valueOf(base));
        return bigInteger.pow(pow);
    }
    public static BigInteger TrueNumberFunction(BigInteger prime_g, int TNF_x, int TNF_r, BigInteger TNF_Seed) {
        BigInteger y = prime_g.pow(TNF_x).mod(modVal);// y=g^x mod p
        BigInteger result = y.pow(TNF_r).mod(modVal).multiply(TNF_Seed.mod(modVal)).mod(modVal);// ( Seed * y^r) mod p = ((Seed mod p) * (y^r mod p) ) mod p
        return result;
    }

    // IEEE754 conversion
    public static byte[] floatToByteArray(double data){
        return ByteBuffer.allocate(8).putDouble(data).array();
    }

    public static double byteArrayToFloat(byte[] data){
        return ByteBuffer.wrap(data).getDouble();
    }
}
