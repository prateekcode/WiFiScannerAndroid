package com.prateekcode.wifiandroid;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.thanosfisherman.wifiutils.WifiUtils;
import com.thanosfisherman.wifiutils.wifiScan.ScanResultsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ConnectivityManager connectivityManager;
    private WifiManager wifiManager;
    private TextView wifiInfo, wifiList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            init();
        }else{
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    123);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                init();
            }else{
                finish();
            }
        }
    }

    private void init(){
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        wifiInfo = findViewById(R.id.wifiInfo);
        wifiList = findViewById(R.id.wifiList);
    }

    public void getWifiInfo(View view) {
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isConnected = networkInfo.isConnected();
        if (isConnected){
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            String ssid = wInfo.getSSID();
            int rssi = wInfo.getRssi();
            wifiInfo.setText(ssid + ":" + rssi);
        }
    }

    public void scanWifi(View view) {
        WifiUtils.withContext(this).scanWifi(new ScanResultsListener() {
            @Override
            public void onScanResults(@NonNull List<ScanResult> scanResults) {
                wifiList.setText("");
                for (ScanResult scanResult : scanResults){
                    wifiList.append(scanResult.SSID + ":" + scanResult.frequency
                            +":" + scanResult.capabilities
                            + "\n\n");
                }
            }
        }).start();
    }

}