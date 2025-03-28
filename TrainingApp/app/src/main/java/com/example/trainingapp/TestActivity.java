package com.example.trainingapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TestActivity extends AppCompatActivity {

    public static String TAG = "TestingActivity";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate:");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Phone Log Button
        Button phoneLogButton = findViewById(R.id.phoneLogButton);
        phoneLogButton.setOnClickListener(view -> {
            Log.d(TAG, "PhoneLog Button Clicked!");
            Toast.makeText(TestActivity.this, "Phone Log Button Clicked!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(TestActivity.this, PhoneLogActivity.class);
            startActivity(intent);
        });

        Button galleryButton = findViewById(R.id.galleryButton);
        phoneLogButton.setOnClickListener(view -> {
            Log.d(TAG, "Gallery Button Clicked!");
            Toast.makeText(TestActivity.this, "Galler Button Clicked!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(TestActivity.this, GalleryActivity.class);
            startActivity(intent);
        });

        // Get a reference to the root view
        ConstraintLayout rootView = findViewById(R.id.main);
        // Set the touch listener on the root view
        rootView.setOnTouchListener((v, event) -> {
            // Get the X and Y coordinates of the touch point
            float x = event.getX();
            float y = event.getY();

            // Print the coordinates to the Logcat
            Log.d(TAG, "Touch at: x=" + x + ", y=" + y);
            // Return false to indicate that we have not consumed the event
            return false;
        });

    }

    @Override
    protected void onStart() {
        Log.d(TAG,"onStart:");
        super.onStart();
    }

    @Override
    protected  void onResume() {
        Log.d(TAG,"onResume:");
        super.onResume();
    }

    @Override
    protected  void onPause() {
        Log.d(TAG,"onPause:");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG,"onStop:");
        super.onStop();
    }

}