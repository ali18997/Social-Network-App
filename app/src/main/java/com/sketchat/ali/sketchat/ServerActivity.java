package com.sketchat.ali.sketchat;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerActivity extends AppCompatActivity {

    private ServerSocket serverSocket;

    Handler UIHandler;

    Thread Thread1 = null;

    private EditText  EDITTEXT;
    private EditText SENDTEXT;
    private EditText IPTEXT;
    private Button SendBtn;
    private Button SetIPBtn;

    public static final int SERVERPORT = 6000;
    public String CLIENTIP = null;

    private Server mServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        TextView ipText = (TextView) findViewById(R.id.ip);

        Button clientBtn = (Button) findViewById(R.id.client_btn);

        clientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goIntent = new Intent (ServerActivity.this, ClientActivity.class);
                startActivity(goIntent);
                finish();
            }
        });

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        ipText.setText((CharSequence) ip);

        new connectTask().execute("");

        EDITTEXT = (EditText) findViewById(R.id.incomingMessage);
        SENDTEXT = (EditText) findViewById(R.id.send_message);
        IPTEXT = (EditText) findViewById(R.id.ip_text);
        SendBtn = (Button) findViewById(R.id.send_button);
        SetIPBtn = (Button) findViewById(R.id.ip_set_btn);

        SetIPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CLIENTIP = IPTEXT.getText().toString();
            }
        });

        SendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = SENDTEXT.getText().toString();
                SENDTEXT.setText("");

                if (mServer != null) {
                    mServer.sendMessage(message);
                }

                /*
                new Thread(new Runnable() {

                    Socket socket = null;

                    @Override
                    public void run() {
                        try {
                            InetAddress serverAddr = InetAddress.getByName(CLIENTIP);
                            socket = new Socket(serverAddr, SERVERPORT);

                            PrintWriter printerWriter = new PrintWriter(socket.getOutputStream());
                            printerWriter.write(SendMsg);
                            printerWriter.flush();
                            printerWriter.close();
                            socket.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }).start();

                */
            }

        });


/*
        UIHandler = new Handler();

        this.Thread1 = new Thread(new Thread1());
        this.Thread1.start();
        */



    }








    public class connectTask extends AsyncTask<String,String,Server> {

        @Override
        protected Server doInBackground(String... message) {

            mServer = new Server(new Server.OnMessageReceived() {
                @Override

                public void messageReceived(String message) {

                    publishProgress(message);
                }
            });
            mServer.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            EDITTEXT.setText((EDITTEXT.getText().toString() + "Client Says: " + values[0] + "\n"));




        }
    }










    class Thread1 implements Runnable {

        public void run() {
            Socket socket = null;
            try {
                serverSocket = new ServerSocket(SERVERPORT);
            } catch (IOException e){
                e.printStackTrace();
            }

            while (!Thread.currentThread().isInterrupted()) {

                try {

                    socket = serverSocket.accept();

                    Thread2 commThread = new Thread2(socket);
                    new Thread(commThread).start();
                    return;

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    class Thread2 implements Runnable {

        private Socket clientSocket;

        private BufferedReader input;

        public Thread2(Socket clientSocket) {

            this.clientSocket = clientSocket;

            try {
                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void run() {

            while(!Thread.currentThread().isInterrupted()) {
                try {
                    String read = input.readLine();
                    if(read != null) {
                        UIHandler.post(new updateUIThread(read));
                    }
                    else  {
                        Thread1 = new Thread(new Thread1());
                        Thread1.start();
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        class updateUIThread implements Runnable {
            private String msg;

            public updateUIThread(String str) {
                this.msg = str;
            }

            public void run() {
                EDITTEXT.setText((EDITTEXT.getText().toString() + "Client Says: " + msg + "\n"));
            }

        }


    }


}
