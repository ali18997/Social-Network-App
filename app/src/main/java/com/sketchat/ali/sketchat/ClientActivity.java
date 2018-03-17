package com.sketchat.ali.sketchat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ClientActivity extends AppCompatActivity {

    private RecyclerView mMessagesList;
    private SwipeRefreshLayout mRefreshLayout;

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager mLinearLayout;
    private ClientMessageAdapter mAdapter;



    private Toolbar mToolbar;



    private EditText sendMsg;
    private TextView connection;
    ImageButton sendBtn;

    public  String SERVERIP2 = null;

    private Client mClient;
    public String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        mToolbar = (Toolbar) findViewById(R.id.client_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Client");

        connection = (TextView) findViewById(R.id.connection);

        new connectTask().execute("");

        if(Client.SERVERIP == null) {
            connection.setText("Connected to: Nothing");
        }
        else {
            connection.setText("Connected to: " + Client.SERVERIP);
        }

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());





        sendMsg = (EditText) findViewById(R.id.client_chat_message_view);




        sendBtn = (ImageButton) findViewById(R.id.client_chat_send_btn);

        mAdapter = new ClientMessageAdapter(messagesList);

        mMessagesList = (RecyclerView) findViewById(R.id.client_messages_list);


        mLinearLayout = new LinearLayoutManager(this);

        mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayout);

        mMessagesList.setAdapter(mAdapter);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = sendMsg.getText().toString();

                if (mClient != null) {
                    mClient.sendMessage(message);
                    Messages c = new Messages();
                    c.setMessage(message);
                    c.setFrom("client");
                    messagesList.add(c);
                    mAdapter.notifyDataSetChanged();

                    mMessagesList.scrollToPosition(messagesList.size() - 1);



                }
                if(!mClient.socket.isOutputShutdown()) {

                    connection.setText("Connected to: " + Client.SERVERIP);
                }
                else {
                    connection.setText("Disconnected from Server");
                }

                sendMsg.setText("");
            }
        });



}

    public class connectTask extends AsyncTask<String,String,Client> {

        @Override
        protected Client doInBackground(String... message) {

            mClient = new Client(new Client.OnMessageReceived() {
                @Override

                public void messageReceived(String message) {

                    publishProgress(message);
                }
            });
            mClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);



            connection.setText("Connected to: " + Client.SERVERIP);

            Messages c = new Messages();
            c.setMessage(values[0]);
            c.setFrom("server");
            messagesList.add(c);
            mAdapter.notifyDataSetChanged();

            mMessagesList.scrollToPosition(messagesList.size() - 1);


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.client_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.client_server_myIp_btn) {

            CharSequence options[] = new CharSequence[] {ip};
            AlertDialog.Builder builder = new AlertDialog.Builder(ClientActivity.this);
            builder.setTitle("Your IP Address is:");
            builder.setItems(options, null);
            builder.show();

        }

        if(item.getItemId() == R.id.client_server_btn){

            Intent serverIntent = new Intent(ClientActivity.this, ServerActivity.class);
            startActivity(serverIntent);
            finish();

        }

        if(item.getItemId() == R.id.client_server_ip_btn){

            Intent setServerIntent = new Intent(ClientActivity.this, SetServerActivity.class);
            startActivity(setServerIntent);
            finish();

        }

        if(item.getItemId() == R.id.client_logout_btn){

            Intent startIntent = new Intent(ClientActivity.this, StartActivity.class);
            startActivity(startIntent);
            finish();
        }

        return true;
    }
}
