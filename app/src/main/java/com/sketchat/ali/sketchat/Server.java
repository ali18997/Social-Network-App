package com.sketchat.ali.sketchat;

import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ALI on 12/24/2017.
 */

public class Server {
    private String serverMessage;
    public static  String SERVERIP ;

    public static final int SERVERPORT = 6000;
    private OnMessageReceived mMessageListener = null;
    public boolean mRun = false;

    PrintWriter out;
    BufferedReader in;


    public Server(OnMessageReceived listener) {
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

    public void stopServer() {
        mRun = false;
    }

    public void run() {
        //Socket socket = null;
        mRun = true;

        try {

            ServerSocket serverSocket = new ServerSocket(SERVERPORT);


            Socket client = serverSocket.accept();

            try {


                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);


                 in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                while (mRun) {
                    serverMessage = in.readLine();

                    if (serverMessage != null && mMessageListener != null) {

                        mMessageListener.messageReceived(serverMessage);
                    }
                    serverMessage = null;
                }

            } catch (Exception e) {

                e.printStackTrace();
            } finally {
                client.close();

            }

        } catch (Exception e) {

            e.printStackTrace();
        }


    }


    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}