package it.jaschke.alexandria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;


/**
 * Created by barbarossa on 11/30/15.
 */
public class ScanActivity extends Activity implements ZBarScannerView.ResultHandler {
    static public int SCAN_REQUEST_CODE = 0;
    static public String SCAN_RESULT_KEY = "SCAN_RESULT";

    private ZBarScannerView mScannerView;

    private static final String TAG = "SCAN_ACTIVITY";

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view
        List<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.EAN13);
        mScannerView.setFormats(formats);
        mScannerView.setAutoFocus(true);

        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v(TAG, rawResult.getContents()); // Prints scan results
        Log.v(TAG, rawResult.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)

        Intent returnIntent = new Intent();
        returnIntent.putExtra(SCAN_RESULT_KEY, rawResult.getContents());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

}
