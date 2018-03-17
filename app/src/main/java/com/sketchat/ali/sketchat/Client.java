package com.sketchat.ali.sketchat;

import android.os.StrictMode;
import android.util.Log;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private String serverMessage;
    public static  String SERVERIP ;
    public Socket socket = null;
    public static final int SERVERPORT = 6000;
    private OnMessageReceived mMessageListener = null;
    public boolean mRun = false;

    PrintWriter out;
    BufferedReader in;


    public Client(OnMessageReceived listener) {
        mMessageListener = listener;

    }


    public void sendMessage(String message) {
        if (out != null && !out.checkError()) {
            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                //your codes here
                out.println(message);
                out.flush();
            }

        }
    }

    public void stopClient() {
        mRun = false;
    }

    public void run() {
        mRun = true;
        try {
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);
            Log.e("serverAddr", serverAddr.toString());
            Log.e("TCP Client", "C: Connecting...");
            socket = new Socket(serverAddr, SERVERPORT);
            Log.e("TCP Server IP", SERVERIP);
            try {
                out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())), true);
                Log.e("TCP Client", "C: Sent.");
                Log.e("TCP Client", "C: Done.");
                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                while (mRun) {
                    serverMessage = in.readLine();
                    if (serverMessage != null && mMessageListener != null) {
                        mMessageListener.messageReceived(serverMessage);
                    }
                    serverMessage = null;
                }

                Log.e("RESPONSE FROM SERVER", "S: Received Message: '"
                        + serverMessage + "'");

            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            } finally {

                socket.close();
            }

        } catch (Exception e) {

            Log.e("TCP", "C: Error", e);

        }

    }


    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}