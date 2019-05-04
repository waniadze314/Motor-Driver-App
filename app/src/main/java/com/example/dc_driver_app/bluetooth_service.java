package com.example.dc_driver_app;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class bluetooth_service extends Service {
    BluetoothAdapter btAdapter;
    BluetoothSocket btSocket;
    static String MACAdress = null;
    boolean connected=false;
    static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //do sprawdzenia, czemu ten UUID działa? nie wiem
    private final IBinder binder = new BtBinder();

    private class BluetoothConnection extends AsyncTask{

        boolean connectedSuccesfully;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(bluetooth_service.this, "Connecting...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                if (btSocket == null || !connected) {
                    btAdapter = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice btTargetDevice = btAdapter.getRemoteDevice(MACAdress);
                    btSocket = btTargetDevice.createInsecureRfcommSocketToServiceRecord(uuid);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                    connectedSuccesfully = true;
                }
            }
            catch(IOException e){
                connectedSuccesfully = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(!connectedSuccesfully){
                Toast.makeText(bluetooth_service.this,"Error while connecting", Toast.LENGTH_LONG).show();
            }
            else{
                connected = true;
                Toast.makeText(bluetooth_service.this,"Connected", Toast.LENGTH_LONG).show();
            }

        }
    }

    public class BtBinder extends Binder{
        bluetooth_service getService(){
            return bluetooth_service.this;
        }
    }
    public bluetooth_service() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void testService(){
        Toast.makeText(bluetooth_service.this, "Test serwisu", Toast.LENGTH_LONG).show();
    }

    public void bluetoothOn() {
        Toast.makeText(bluetooth_service.this, "Turning on Bluetooth...", Toast.LENGTH_LONG).show();
        if (!btAdapter.isEnabled()) {
            btAdapter.enable();
        }
    }

    public void bluetoothOff(){
            Toast.makeText(bluetooth_service.this, "Turning off Bluetooth...", Toast.LENGTH_LONG).show();
            if(btAdapter.isEnabled()) {
                btAdapter.disable();
            }
        }

    public ArrayList getPaired(){

        String deviceName, deviceMAC;

        ArrayList btDevicesList = new ArrayList();
        if(btAdapter.isEnabled()){
            Set<BluetoothDevice> devices = btAdapter.getBondedDevices();
            if(devices.size()>0) {
                for (BluetoothDevice device : devices) {
                    btDevicesList.add("Name: " + device.getName() + " MAC: " + device.getAddress());

                }
            }
        }
        return btDevicesList;
    }



    public boolean connectBtDevice(String adress){
        MACAdress = adress;
        if(MACAdress != null) {
            new BluetoothConnection().execute();
            return true;
        }
        else {
            Toast.makeText(bluetooth_service.this, "MAC adress null", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public boolean disconnectBtDevice(){
        try {
            btSocket.close();
            btSocket = null;
            connected = false;

            return true;

        }
        catch(IOException e){
            return false;
        }

    }

    public void sendCommandViaBluetooth(String command){
        try {
            btSocket.getOutputStream().write(command.toString().getBytes());
        }
        catch(IOException e){
            Toast.makeText(bluetooth_service.this, "Error while sending", Toast.LENGTH_LONG).show();
        }


    }
}

