package com.example.hasan.btcontrolledcar;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;

public class MainActivity extends Activity {
    private Button setip, forward, backward, left, right, stop, Aut, Manua, connec;
    private EditText debug, editText;
    public String command;
    public String ipaddress;
    private android.bluetooth.BluetoothDevice device;
    private android.bluetooth.BluetoothSocket socket;
    private java.io.OutputStream outputStream;
    private java.io.InputStream inputStream;


    private final String DEVICE_ADDRESS = "00:18:E4:34:EF:68"; //MAC Address of Bluetooth Module
    private final java.util.UUID PORT_UUID = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setip = findViewById(R.id.set_ip);
        forward = findViewById(R.id.forward);
        backward = findViewById(R.id.backward);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        stop = findViewById(R.id.stop);
        Aut = findViewById(R.id.auto);
        Manua = findViewById(R.id.manual);
        connec = findViewById(R.id.connect);
        editText = findViewById(R.id.editText);



        debug = findViewById(R.id.debug);
        debug.setVisibility(View.INVISIBLE);

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("forward");

                command = "1";

                try
                {
                    outputStream.write(command.getBytes()); //transmits the value of command to the bluetooth module
                    outputStream.flush();
                }
                catch (java.io.IOException e)
                {
                    e.printStackTrace();

                }
            }
        });

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("backwarddd");

                command = "4";

                try
                {
                    outputStream.write(command.getBytes());
                    outputStream.flush();
                }
                catch (java.io.IOException e)
                {
                    e.printStackTrace();
                }

            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("right");

                command = "2";

                try
                {
                    outputStream.write(command.getBytes());
                    outputStream.flush();
                }
                catch (java.io.IOException e)
                {
                    e.printStackTrace();
                }
            }

        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("left");

                command = "3";

                try
                {
                    outputStream.write(command.getBytes());
                    outputStream.flush();
                }
                catch (java.io.IOException e)
                {
                    e.printStackTrace();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("stop");

                command = "5";

                try
                {
                    outputStream.write(command.getBytes());
                    outputStream.flush();
                }
                catch (java.io.IOException e)
                {
                    e.printStackTrace();
                }
            }
        });

        setip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("ip set");

                ipaddress = editText.getText().toString();

                String frameVideo = "<html><body>Youtube video .. <br> <iframe width=\"320\" height=\"315\" src=\"http://" + ipaddress + ":8080/browserfs.html\" frameborder=\"0\" allowfullscreen></iframe></body></html>";

                WebView displayVideo = findViewById(R.id.webView);
                displayVideo.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        return false;
                    }
                });
                WebSettings webSettings = displayVideo.getSettings();
                webSettings.setJavaScriptEnabled(true);
                displayVideo.loadData(frameVideo, "text/html", "utf-8");


            }
        });

        Aut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("auto");
                command = "6";

                try
                {
                    outputStream.write(command.getBytes());
                    outputStream.flush();
                }
                catch (java.io.IOException e)
                {
                    e.printStackTrace();
                }
                forward.setEnabled(false);
                backward.setEnabled(false);
                right.setEnabled(false);
                left.setEnabled(false);
                stop.setEnabled(false);
            }
        });

        Manua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command = "7";

                try
                {
                    outputStream.write(command.getBytes());
                    outputStream.flush();
                }
                catch (java.io.IOException e)
                {
                    e.printStackTrace();
                }
                System.out.println("manual");
                forward.setEnabled(true);
                backward.setEnabled(true);
                right.setEnabled(true);
                left.setEnabled(true);
                stop.setEnabled(true);
            }
        });

        connec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("connect");

                if(BTinit())
                {
                    System.out.println("baal");
                    BTconnect();
                }
            }
        });




    }

    public boolean BTinit()
    {
        boolean found = false;

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter == null) //Checks if the device supports bluetooth
        {
            Toast.makeText(getApplicationContext(), "Device doesn't support bluetooth", Toast.LENGTH_SHORT).show();
        }

        if(!bluetoothAdapter.isEnabled()) //Checks if bluetooth is enabled. If not, the program will ask permission from the user to enable it
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter,0);

            try
            {
                Thread.sleep(1000);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

        if(bondedDevices.isEmpty()) //Checks for paired bluetooth devices
        {
            Toast.makeText(getApplicationContext(), "Please pair the device first", Toast.LENGTH_SHORT).show();
        }
        else
        {
            for(BluetoothDevice iterator : bondedDevices)
            {
                if(iterator.getAddress().equals(DEVICE_ADDRESS))
                {
                    device = iterator;
                    found = true;
                    break;
                }
            }
        }

        return found;
    }

    public boolean BTconnect()
    {
        boolean connected = true;

        try
        {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID); //Creates a socket to handle the outgoing connection
            socket.connect();

            Toast.makeText(getApplicationContext(),
                    "Connection to bluetooth device successful", Toast.LENGTH_LONG).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            connected = false;
        }

        if(connected)
        {
            try
            {
                outputStream = socket.getOutputStream(); //gets the output stream of the socket
                inputStream = socket.getInputStream();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        return connected;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }



    private class innerThread implements Runnable{
        Thread t;

        byte[] str = new byte[99999];

        innerThread(){
            t = new Thread();
            t.start();
        }

        @Override
        public void run() {
            while(true)
            {
                try{
                    if(inputStream.available()>0)
                    {
                        int aa = inputStream.read(str);
                        debug.setText(str.toString());
                        System.out.println("str is ajaira: ");

                    }
                }
                catch (Exception e){}
            }
        }
    }


}
