package com.saif.kmpakiadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_ITEM_IMAGE = 113;
    @BindView(R.id.flipper)
    ViewFlipper viewFlipper;
    @BindView(R.id.inpt_item_name)TextInputLayout itemNameInpt;
    @BindView(R.id.inpt_description)TextInputLayout descriptionInpt;
    @BindView(R.id.inpt_phone_number)TextInputLayout phoneNumberInpt;
    @BindView(R.id.img_event)ImageView itemImg;
    @BindView(R.id.btn_send)Button sendBtn;
    String image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addEvent(View view){
        viewFlipper.setDisplayedChild(1);
    }
    public void submit(View view){

    }
    public void addImage(View view){
        pickImage();
    }

    public void pickImage (){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
        startActivityForResult(chooserIntent, PICK_ITEM_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == PICK_ITEM_IMAGE && resultCode == RESULT_OK
                    && data != null) {
                Uri selectedImage = data.getData();
                Bitmap bitmap =
                        MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage);
                itemImg.setImageBitmap(bitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();
                image = Base64.encodeToString(b,Base64.DEFAULT);
                Log.d("saif"+" image",image);
            } else {
                Toast.makeText(getApplicationContext(), "No image chosen!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void submitRequest(String fullName, String itemName, String itemImage, final String phone){

    }
}
