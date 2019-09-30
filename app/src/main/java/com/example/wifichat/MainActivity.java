package com.example.wifichat;

/*support telgram id =@javaprogrammer_eh
 * 05/07/1398
 * creted by elmira hossein zadeh*/

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private TextView text;
    private EditText input;
    private Button btnSend;
    private Handler handler = new Handler();
    WifiManager wifiManager;
    private Socket socket;
    private DataOutputStream outputStream;
    private BufferedReader inputStream;

    /*Server&Client*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        text = findViewById(R.id.txt);
        input = findViewById(R.id.edtinput);
        btnSend = findViewById(R.id.btnsend);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!searchNetwork()) {
                    runChatServer();
                }
                try {
                    outputStream = new DataOutputStream(socket.getOutputStream());
                    inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (Exception e) {
                    log("Error Connection is not stable, exit!");
                    shutDown();
                }

                while (true) {
                    try {
                        String message = inputStream.readLine();
                        if (message != null) {
                            Database.addLoginData(message,getApplicationContext());
                            Database.getDatachat(getApplicationContext());
                            log("Friend: "+message);
                        }

                    } catch (Exception e) {
                    }

                }
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (outputStream == null) {
                    return;
                }
                try {
                    String val = input.getText().toString();
                    Database.addLoginData(val,getApplicationContext());
                    Database.getDatachat(getApplicationContext());
                    log("You: "+val);
                    String message = input.getText().toString() + "\n";
                    outputStream.write(message.getBytes());
                    input.setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }

    private void log(final String message) {
//        Long timeStamp = System.currentTimeMillis();
//        final Long time = timeStamp % 100000;
        handler.post(new Runnable() {
            @Override
            public void run() {
//                text.setText(text.getText() + "\n@" + time + ":" + message);
                  text.setText(text.getText() +"\n"+ message);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            shutDown();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean searchNetwork() {
        log("Connecting...");
        String range = "192.168.1.";
        for (int i = 1; i <= 255; i++) {
            String ip = range + i;
            try {
//                log("Try IP : "+ip);
//                s.setSoTimeout(100);
                socket = new Socket();
                socket.connect(new InetSocketAddress(ip, 9000), 100);
                log("Connected:))");
                return true;
            } catch (IOException e) {
            }
        }
        return false;
    }

    private void runChatServer() {
        try {
            log("Waiting for Client !!!");
            ////
            wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int a = wifiInfo.getIpAddress();
            String sa = Formatter.formatIpAddress(a) + "";
            log(sa);
            ////
            ServerSocket serverSocket = new ServerSocket(9000);
            socket = serverSocket.accept();
            log("A New Client Connected !!!");
        } catch (IOException e) {
        }
    }

    private void shutDown() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);

    }
}

