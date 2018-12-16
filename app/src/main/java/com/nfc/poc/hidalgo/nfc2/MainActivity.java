package com.nfc.poc.hidalgo.nfc2;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    // https://www.developer.com/ws/android/nfc-programming-in-android.html

    private TextView mTextView;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mNFCTechLists;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView)findViewById(R.id.txtMain);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        this.imageView = (ImageView) this.findViewById(R.id.imageView);

        if (mNfcAdapter != null) {
            mTextView.setText("POC ANDROID NFC");
        } else {
            mTextView.setText("Activez le NFC");
        }

        // create an intent with tag data and deliver to this activity
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        this.imageView.setImageResource(R.drawable.inconnu);
    }

    @Override
    public void onNewIntent(Intent intent) {
        String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        String s = "";

        // recuperation du serial de la carte
        byte[] extraID = tag.getId();
        StringBuilder sSerialId = new StringBuilder();
        for (byte b : extraID) {
            sSerialId.append(String.format("%02X", b));
        }

        // faire en fonction du serial
        if( sSerialId.toString().equals( "043AE8EA4D5D81" ) ){
            s = "PAUL";
            this.imageView.setImageResource(R.drawable.paul);
        }else if( sSerialId.toString().equals( "04455CEA4D5D81" ) ){
            s = "PIERRE";
            this.imageView.setImageResource(R.drawable.pierre);
        }else if( sSerialId.toString().equals( "047F23EA4D5D80" ) ){
            s = "JACQUES";
            this.imageView.setImageResource(R.drawable.jacque);
        }else if( sSerialId.toString().equals( "CE53E7D0" ) ){
            s = "MANU";
            this.imageView.setImageResource(R.drawable.macif);
        }else if( sSerialId.toString().equals( "8A9134FC" ) ){
            s = "CAFE";
            this.imageView.setImageResource(R.drawable.cafe);
        }else{
            s += "INCONNU : " + sSerialId.toString();
            this.imageView.setImageResource(R.drawable.inconnu);
        }

        mTextView.setText(s);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }
}
