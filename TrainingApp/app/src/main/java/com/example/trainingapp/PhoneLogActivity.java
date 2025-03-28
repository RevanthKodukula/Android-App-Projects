package com.example.trainingapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

/** @noinspection TryFinallyCanBeTryWithResources*/
public class PhoneLogActivity extends AppCompatActivity {

    public static String TAG = "PhoneLogActivity";
    private TextView callLogTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate:");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_log);

        callLogTextView = findViewById(R.id.callLogTextView);
        EditText numCallsInput = findViewById(R.id.numCallsInput);
        Button fetchLogsButton = findViewById(R.id.fetchLogsButton);
        Button clearButton = findViewById(R.id.clearButton);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS}, 1);
        }

        fetchLogsButton.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                    == PackageManager.PERMISSION_GRANTED) {
                String input = numCallsInput.getText().toString();
                int numCalls = input.isEmpty() ? 10 : Integer.parseInt(input);
                new Thread(() -> fetchCallLogs(numCalls)).start();
            } else {
                Toast.makeText(this, "Permission Denied! Cannot display call logs.", Toast.LENGTH_SHORT).show();
            }
        });

        clearButton.setOnClickListener(view -> {
            runOnUiThread(() -> {
                callLogTextView.animate().alpha(0f).setDuration(150).withEndAction(() -> {
                    callLogTextView.setText("");
                    callLogTextView.animate().alpha(1f).setDuration(150);
                });
            });
        });
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart:");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume:");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause:");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop:");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy:");
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            boolean callLogGranted = false;
            boolean contactsGranted = false;

            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.READ_CALL_LOG) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    callLogGranted = true;
                }
                if (permissions[i].equals(Manifest.permission.READ_CONTACTS) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    contactsGranted = true;
                }
            }

            if (callLogGranted && contactsGranted) {
                Toast.makeText(this, "Permissions Granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchCallLogs(int limit) {
        Log.d(TAG, "fetchCallLogs: Fetching " + limit + " Call logs");
        StringBuilder callDetails = new StringBuilder();
        Map<String, String> contactsMap = getAllContacts();

        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(
                    CallLog.Calls.CONTENT_URI, null, null, null,
                    CallLog.Calls.DATE + " DESC");
            if (cursor != null) {
                int numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER);
                int typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE);
                int durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION);

                int count = 0;
                while (cursor.moveToNext() && count < limit) {
                    String number = cursor.getString(numberIndex);
                    String normalizedNumber = normalizePhoneNumber(number);
                    String contactName = contactsMap.get(normalizedNumber);
                    String type = getCallType(cursor.getInt(typeIndex));
                    String duration = cursor.getString(durationIndex);

                    if (contactName != null) {
                        callDetails.append("Name: ").append(contactName)
                                .append("\nNumber: ").append(number)
                                .append("\nType: ").append(type)
                                .append("\nDuration: ").append(duration).append(" sec\n\n");
                    } else {
                        callDetails.append("Number: ").append(number)
                                .append("\nType: ").append(type)
                                .append("\nDuration: ").append(duration).append(" sec\n\n");
                    }
                    count++;
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        if (callLogTextView != null) {
            runOnUiThread(() -> {
                callLogTextView.animate().alpha(0f).setDuration(150).withEndAction(() -> {
                    callLogTextView.setText("");  // Clear text
                    callLogTextView.animate().alpha(1f).setDuration(150).withEndAction(() -> {
                        callLogTextView.setText(callDetails.length() > 0 ? callDetails.toString() : "No recent calls.");
                    });
                });
            });
        }
    }

    private Map<String, String> getAllContacts() {
        Map<String, String> contactsMap = new HashMap<>();
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                    null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    number = normalizePhoneNumber(number);
                    contactsMap.put(number, name);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return contactsMap;
    }

    private String normalizePhoneNumber(String number) {
        return number.replaceAll("[^0-9]", "");
    }

    private String getCallType(int callType) {
        switch (callType) {
            case CallLog.Calls.INCOMING_TYPE:
                return "Incoming";
            case CallLog.Calls.OUTGOING_TYPE:
                return "Outgoing";
            case CallLog.Calls.MISSED_TYPE:
                return "Missed";
            default:
                return "Unknown";
        }
    }
}
