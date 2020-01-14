/* This file borrows code from the following sources:
https://www.tutorialspoint.com/how-to-write-an-image-file-in-internal-storage-in-android
https://stackoverflow.com/questions/26865787/get-bitmap-from-imageview-in-android-l
https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android
https://stackoverflow.com/questions/4181774/show-image-view-from-file-path
https://stackoverflow.com/questions/2169294/how-to-add-manifest-permission-to-an-application
https://code.tutsplus.com/tutorials/capture-and-crop-an-image-with-the-device-camera--mobile-11458
https://www.cs.dartmouth.edu/~xingdong/Teaching/CS65/web/cs65.html
 */

package com.example.myruns;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;


public class Profile extends AppCompatActivity {
    ImageView profilePic;
    EditText nameText, emailText, phoneText, classText, majorText;
    RadioGroup sexRadio;
    int CAMERA_REQUEST_CODE = 1;
    int PIC_CROP_CODE = 2;
    Uri ImageCaptureUri;
    String profilePicFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Settings");
        nameText = findViewById(R.id.nameText);
        emailText = findViewById(R.id.emailText);
        phoneText = findViewById(R.id.phoneText);
        classText = findViewById(R.id.classText);
        majorText = findViewById(R.id.majorText);
        sexRadio = findViewById(R.id.sexRadio);
        profilePic = findViewById(R.id.profilePicture);
        if(Build.VERSION.SDK_INT < 23) return; if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
        }

        loadProfile(null);
    }

    public void saveProfile(View view){
        SharedPreferences profilePrefs = getSharedPreferences(getString(R.string.profilePref), MODE_PRIVATE);
        SharedPreferences.Editor editor = profilePrefs.edit();

        editor.clear();
        editor.putString(getString(R.string.nameKey), nameText.getText().toString());
        editor.putString(getString(R.string.emailKey), emailText.getText().toString());
        editor.putString(getString(R.string.phoneKey), phoneText.getText().toString());
        editor.putString(getString(R.string.classKey), classText.getText().toString());
        editor.putString(getString(R.string.majorKey), majorText.getText().toString());
        editor.putString("picKey", profilePicFile);
        editor.putInt(getString(R.string.sexKey), sexRadio.indexOfChild(findViewById(sexRadio
                .getCheckedRadioButtonId())));
        editor.commit();

        Toast.makeText(this,"Changes Saved", Toast.LENGTH_SHORT).show();

    }

    public void loadProfile(View view){
        SharedPreferences profilePrefs = getSharedPreferences(getString(R.string.profilePref), MODE_PRIVATE);

        nameText.setText(profilePrefs.getString(getString(R.string.nameKey), ""));
        emailText.setText(profilePrefs.getString(getString(R.string.emailKey), ""));
        phoneText.setText(profilePrefs.getString(getString(R.string.phoneKey), ""));
        classText.setText(profilePrefs.getString(getString(R.string.classKey), ""));
        majorText.setText(profilePrefs.getString(getString(R.string.majorKey), ""));
        int checkedRadioButton = profilePrefs.getInt(getString(R.string.sexKey), -1);
        if(checkedRadioButton != -1){
            ((RadioButton)sexRadio.getChildAt(checkedRadioButton)).setChecked(true);
        }
        String imgPath = profilePrefs.getString("picKey", null);
        if(imgPath != null) {
            profilePic.setImageBitmap(BitmapFactory.decodeFile(imgPath));
            profilePicFile = imgPath;
        }
        else{
            profilePic.setImageResource(R.drawable.default_profile_picture);
        }
    }



    public void changeImage(View view){
        Intent cameraActivity = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues(1);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        ImageCaptureUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Log.d("scott", ImageCaptureUri.getPath());
        cameraActivity.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                ImageCaptureUri);
        cameraActivity.putExtra("return-data", true);
        startActivityForResult(cameraActivity, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(ImageCaptureUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 150);
            cropIntent.putExtra("outputY", 150);
            cropIntent.putExtra("return-data", true);
            cropIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                    ImageCaptureUri);
            startActivityForResult(cropIntent, PIC_CROP_CODE);
        }
        if (requestCode == PIC_CROP_CODE && resultCode == Activity.RESULT_OK){
            profilePic.setImageURI(ImageCaptureUri);
            BitmapDrawable drawable = (BitmapDrawable) profilePic.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File file = new File(directory, "profilePic.jpg");
            profilePicFile = file.getPath();
            if (!file.exists()) {
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
