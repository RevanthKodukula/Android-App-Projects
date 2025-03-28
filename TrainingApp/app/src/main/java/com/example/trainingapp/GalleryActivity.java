package com.example.trainingapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GalleryActivity extends AppCompatActivity {

    public static String TAG = "GalleryActivity";
    public ImageView galleryImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate:");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gallery);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        galleryImageView = findViewById(R.id.galleryImageView);
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
