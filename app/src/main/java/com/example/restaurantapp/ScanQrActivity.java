package com.example.restaurantapp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;

public class ScanQrActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //ask for authorisation
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 50);
        }
        else {
            mCodeScanner = new CodeScanner(this, scannerView);
            mCodeScanner.setDecodeCallback(result -> this.runOnUiThread(() -> Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT).show()));
            scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}