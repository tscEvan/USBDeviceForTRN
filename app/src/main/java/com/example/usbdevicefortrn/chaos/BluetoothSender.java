package com.example.usbdevicefortrn.chaos;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

public class BluetoothSender {
    private static final String TAG = BluetoothSender.class.getSimpleName();
    private static final UUID BLUETOOTH_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket mBTSocket;
    private ConnectedThread mConnectedThread;

    BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();

    public BluetoothSender() {
    }

    public ConnectedThread connect(String mac, String version, List<Double> chaosKey) {
        Boolean isSocketCreate = true;
        BluetoothDevice device = mAdapter.getRemoteDevice(mac);
        if (mBTSocket != null) {
            if (mBTSocket.isConnected()) {
                try {
                    mBTSocket.close();
                    if (mConnectedThread.isAlive()) {
                        Log.d(TAG, "connect: alive");
                        mConnectedThread = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            mBTSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Socket: create socket fail");
        }
        try {
            mBTSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Socket: connect fail ");
            try {
                isSocketCreate = false;
                mBTSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
                Log.d(TAG, "Socket: create socket fail");
            }
        }

        if (isSocketCreate) {
            mConnectedThread = new ConnectedThread(mBTSocket, version, chaosKey);
            Log.d(TAG, "BluetoothSender: start listener");
            return mConnectedThread;
        }
        return null;
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        int sdk = Integer.parseInt(Build.VERSION.SDK);
        if (sdk >= 10) {
            return device.createInsecureRfcommSocketToServiceRecord(BLUETOOTH_UUID);
        } else {
            return device.createRfcommSocketToServiceRecord(BLUETOOTH_UUID);
        }
    }

    public class ConnectedThread extends Thread {
        private HenonMap henonMap;
        private List<Double> chaosKey;
        private String version;
        BluetoothSocket mSocket;
        InputStream mInputStream;
        OutputStream mOutputStream;
        boolean lockState = false;
        private int testNum;

        public void setHenonMap(HenonMap henonMap) {
            this.henonMap = henonMap;
        }

        public ConnectedThread(BluetoothSocket mSocket, String version, List<Double> chaosKey) {
            this.mSocket = mSocket;
            this.version = version;
            this.chaosKey = chaosKey;
            InputStream socketInput = null;
            OutputStream socketOutput = null;

            try {
                socketInput = mSocket.getInputStream();
                socketOutput = mSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mInputStream = socketInput;
            mOutputStream = socketOutput;
        }

        @Override
        public void run() {
            super.run();
            int check;
            byte[] buffer = new byte[8];
            int bytes;
            testNum = 0;
            sendChaos(version, 0);
            while (true) {
                try {
                    bytes = mInputStream.available();
                    Log.d(TAG, "run: 1 ="+ bytes);
                    if (bytes!=0) {
//                        bytes = mInputStream.available();
                        check = mInputStream.read();
//                        Log.d(TAG, "run: 2 ="+ bytes);
//                        bytes = mInputStream.read(buffer, 0, 8);
//                        Log.d(TAG, "run: 3 ="+ bytes);
                        //getCheckState
                        testNum++;
                        if(testNum >= 100){
                            break;
                        }
                        getCheckState(version, check, buffer);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
                if (lockState) {
                    cancel();
                    break;
                }
            }
        }

        private void getCheckState(String version, int check, byte[] buffer) {
            if (check == 65) {
                Log.d("MCUReturn", "=============FEELBACK=============");
                sendChaos(version, 0);
            } else if (check == 66) {
//                Log.d("MCUReturn", Float.toString(getMCUreturn(buffer)));
                Log.d("MCUReturn", "=============Lock one Open=============");
                sendChaos(version, 1);
            } else if (check == 67) {
//                Log.d("MCUReturn", Float.toString(getMCUreturn(buffer)));
                Log.d("MCUReturn", "=============Lock two Open=============");
                sendChaos(version, 2);
            } else if (check == 68) {
//                Log.d("MCUReturn", Float.toString(getMCUreturn(buffer)));
                Log.d("MCUReturn", "=============Lock there Open=============");
                lockState = true;
            }
        }

        private void sendChaos(String version, int i) {
            Log.d(TAG, "sendChaos: send==========");
            switch (version) {
                case "1":
                    //old usb
                    ChaosWithBluetooth chaosWithBluetooth = new ChaosWithBluetooth(mOutputStream,mInputStream);
                    lockState = chaosWithBluetooth.start(chaosKey);
                    break;
                case "2":
                    //trn
                    henonMap.chaosMasterMath();
//                    Double x1 = henonMap.getX1();
                    byte[] x1Array = doubleToByteArray(henonMap.getX1());
                    byte[] x2Array = doubleToByteArray(henonMap.getX2());
                    byte[] x3Array = doubleToByteArray(henonMap.getX3());
                    int key1 = TrueNumberFunction(3, x1Array[4] & 0xff, x2Array[4] & 0xff, x3Array[4] & 0xff);
                    int key2 = TrueNumberFunction(3, x1Array[5] & 0xff, x2Array[5] & 0xff, x3Array[5] & 0xff);
                    int key3 = TrueNumberFunction(3, x1Array[6] & 0xff, x2Array[6] & 0xff, x3Array[6] & 0xff);
                    int trnKey = TrueNumberFunction(3,key1,key2,key3);
                    Log.d(TAG, "sendChaos: "+ trnKey);
                    write(henonMap.getU1());
                    write((1 + (trnKey * trnKey)) * chaosKey.get(i));
//                    write((1 + (x1 * x1)) * chaosKey.get(i));
                    break;
            }
        }

        public void write() {
//            byte[] bytes = new byte[];
            try {
                mOutputStream.write((byte) 0b0001111);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void write(float data) {
            int ieee754Data = Float.floatToIntBits(data);
            byte[] bytes = new byte[4];
            bytes[0] = (byte) (ieee754Data >> 24 & 0xFF);
            bytes[1] = (byte) (ieee754Data >> 16 & 0xFF);
            bytes[2] = (byte) (ieee754Data >> 8 & 0xFF);
            bytes[3] = (byte) (ieee754Data & 0xFF);
            try {
                for (int i = 0; i < 4; i++) {
                    Log.d(TAG, "write: " + Integer.toBinaryString(bytes[i]));
                    mOutputStream.write(bytes[i]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void write(double data) {
            long ieee754Data = Double.doubleToLongBits(data);
            byte[] bytes = new byte[8];
            bytes[0] = (byte) (ieee754Data >> 56);
            bytes[1] = (byte) ((ieee754Data >> 48) & 0x0FF);
            bytes[2] = (byte) ((ieee754Data >> 40) & 0x0FF);
            bytes[3] = (byte) ((ieee754Data >> 32) & 0x0FF);
            bytes[4] = (byte) ((ieee754Data >> 24) & 0x0FF);
            bytes[5] = (byte) ((ieee754Data >> 16) & 0x0FF);
            bytes[6] = (byte) ((ieee754Data >> 8) & 0x0FF);
            bytes[7] = (byte) (ieee754Data & 0x0FF);
            try {
                for (int i = 0; i < 8; i++) {
                    Log.d(TAG, "write: " + Integer.valueOf(bytes[i]));
                    mOutputStream.write(bytes[i]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "cancel: " + e.getMessage());
            }
        }
    }

    private final BigInteger modVal =new BigInteger("257");//2^8+1
    private int TrueNumberFunction(int prime_g, int TNF_x, int TNF_r, int TNF_Seed) {
        BigInteger y = new BigInteger(String.valueOf(prime_g)).pow(TNF_x).mod(modVal);// y=g^x mod p
        BigInteger result = y.pow(TNF_r).mod(modVal).multiply(new BigInteger(String.valueOf(TNF_Seed)).mod(modVal)).mod(modVal);// ( Seed * y^r) mod p = ((Seed mod p) * (y^r mod p) ) mod p
        return result.intValue();
    }

    // IEEE754 conversion
    private byte[] doubleToByteArray(double data){
        return ByteBuffer.allocate(8).putDouble(data).array();
    }
}
