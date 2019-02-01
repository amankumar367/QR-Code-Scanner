package com.dev.aman.qrcodescanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.dev.aman.qrcodescanner.DBHelper.DatabaseHelper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper mDatabase;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private static final int PERMISSION_REQUEST_CODE = 2;
    private LinearLayout scanQRcode, viewQRcodeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        onClick();

    }

    private void init() {
        scanQRcode = findViewById(R.id.scanQRcode);
        viewQRcodeList = findViewById(R.id.viewQRcodeList);
        mDatabase = new DatabaseHelper(MainActivity.this);
    }

    private void onClick() {
        scanQRcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSelfPermission()){
                    IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                    integrator.setOrientationLocked(false);
                    integrator.initiateScan();
                }else {
                    requestPermission();
                }
            }
        });

        viewQRcodeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ViewScannedCode.class));
            }


        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                    String currentDateTime = dateFormat.format(new Date()); // Find todays date
                    String[] arrayString = currentDateTime.split(" ");
                    String Date = arrayString[0];
                    String Time = arrayString[1];
                    boolean isInserted = mDatabase.insertData(result.getContents(), Date, Time);
                    if(isInserted)
                        Toast.makeText(MainActivity.this,"Data Stored",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(MainActivity.this,"Scanned Data not Stored",Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private Boolean checkSelfPermission() {
        boolean flag = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flag = ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED;
        } else {
            flag = true;
        }
        return flag;
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0) {
                Intent i = new Intent(MainActivity.this, QrCodeActivity.class);
                startActivityForResult(i, REQUEST_CODE_QR_SCAN);
            } else {
                requestPermission();
            }
        }
    }
}
