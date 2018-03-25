package com.saif.kmpakiadmin;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements TimeAdapter.TodoItemEditListener {
    private static final int PICK_ITEM_IMAGE = 113;
    public static final String TAG = "admin-tag";
    @BindView(R.id.flipper)
    ViewFlipper viewFlipper;
    @BindView(R.id.inpt_item_name)TextInputLayout itemNameInpt;
    @BindView(R.id.inpt_description)TextInputLayout descriptionInpt;
    @BindView(R.id.et_date)EditText dateInpt;
    @BindView(R.id.img_event)ImageView itemImg;
    @BindView(R.id.btn_send)Button sendBtn;
    @BindView(R.id.txt_no_data)TextView noDataTv;
    @BindView(R.id.rv_dates)RecyclerView eventsRv;
    @BindView(R.id.progress)ProgressBar progressBar;
    String image,title,desc,date;
    Calendar calendar;
    DatePicker datePicker;
    DatabaseReference eventRef;
    FirebaseDatabase database;
    ArrayList<ModelData> events = new ArrayList<>();
    TimeAdapter timeAdapter;
    boolean isEdit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        database = FirebaseDatabase.getInstance();
        eventRef = database.getReference();
        calendar = Calendar.getInstance();
        Log.d("event",eventRef.child("events").push()+"");
        eventRef.child("events").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                progressBar.setVisibility(View.INVISIBLE);
                noDataTv.setVisibility(View.INVISIBLE);
                ModelData event = dataSnapshot.getValue(ModelData.class);
                events.add(event);
                Log.d(TAG+"data",dataSnapshot.toString()+"/size"+events.size());
                timeAdapter = new TimeAdapter(events,MainActivity.this,MainActivity.this);
                eventsRv.setAdapter(timeAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG+"Changed",dataSnapshot.toString()+"/size"+events.size());

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG+"Removed",dataSnapshot.toString()+"/size"+events.size());

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG+"Moved",dataSnapshot.toString()+"/size"+events.size());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG+"Cancell",databaseError.toString()+"/size"+events.size());

            }
        });
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (!Utils.isConnected(MainActivity.this)){
                    noDataTv.setVisibility(View.VISIBLE);
                    noDataTv.setText("لايوجد اتصال بالانترنت!");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }else
                if (events==null||events.size()==0){
                    progressBar.setVisibility(View.INVISIBLE);
                    noDataTv.setVisibility(View.VISIBLE);
                }
            }
        }.start();
    }

    public void addEvent(View view){
        viewFlipper.setDisplayedChild(1);
    }
    public void submit(View view){
        itemNameInpt.setErrorEnabled(false);
        descriptionInpt.setErrorEnabled(false);
        title = itemNameInpt.getEditText().getText().toString();
        desc = descriptionInpt.getEditText().getText().toString();
        date = dateInpt.getText().toString();
        if (title==null||title.length()<2){
            itemNameInpt.setError("يجب ادخال عنوان الحدث اولا");
        }else if (date==null||date.length()<3){
            Toast.makeText(getApplicationContext(),"يجب ادخال تاريخ الحدث اولا" ,Toast.LENGTH_LONG).show();
        }else {
            submitRequest(title,desc,image,date);
        }
    }
    public void addImage(View view){
        pickImage();
    }
    public void pickDate (View view){
        seleceDateDialog();
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

    public void submitRequest(String title, String desc, String image, final String date){
        progressBar.setVisibility(View.VISIBLE);
        eventRef = FirebaseDatabase.getInstance().getReference().child("events");
        String key = eventRef.child("events").push().getKey();
        Map newPost = new HashMap();
        newPost.put("title", title);
        newPost.put("desc", desc);
        newPost.put("date", date);
        newPost.put("key",key);
        eventRef.child(key).setValue(newPost, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(getApplicationContext(),"تم إضافة الحدث" ,Toast.LENGTH_LONG).show();
                dateInpt.getText().clear();
                descriptionInpt.getEditText().getText().clear();
                itemNameInpt.getEditText().getText().clear();
                itemImg.setImageDrawable(getResources().getDrawable(R.drawable.add_image));
            }
        });
        eventRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("saifAdd",s+"/"+dataSnapshot.toString());
                ModelData modelData = dataSnapshot.getValue(ModelData.class);
                //if (timeAdapter!=null)timeAdapter.add(modelData);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("saifChange",s+"");

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("saifMove",dataSnapshot.toString()+"");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (viewFlipper.getDisplayedChild()==1){
            viewFlipper.setDisplayedChild(0);
        }else
        super.onBackPressed();
    }

    private void seleceDateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.dialoge_select_date, null);
        datePicker = subView.findViewById(R.id.date_picker);
        Button okBtn = subView.findViewById(R.id.date_time_set);
        builder.setView(subView);
        final AlertDialog alertDialog = builder.show();
        alertDialog.setCancelable(true);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tiemPicker();
                alertDialog.dismiss();
            }
        });
    }
    private void tiemPicker(){

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int year =datePicker.getYear();
                        int month = datePicker.getMonth();
                        int day = datePicker.getDayOfMonth();
                        Log.d("saif"+"time",hourOfDay+"");
                        calendar.set(year,month,day,hourOfDay,00);
                        calendar.setTimeZone(TimeZone.getTimeZone("UTC+02:00"));
                        String myFormat = "yyyy-MM-dd HH:mm";
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(myFormat, Locale.US);
                        sdf.setCalendar(calendar);
                        String date = sdf.format(calendar.getTime());
                        dateInpt.setText(date);
                        date = sdf.format(calendar.getTime());
                        Log.d("date",date);
                    }
                }, calendar.HOUR_OF_DAY, calendar.MINUTE, false);

        timePickerDialog.show();
    }

    @Override
    public void itemClicked(int position) {
        ModelData event = events.get(position);
        viewFlipper.setDisplayedChild(1);
        itemNameInpt.getEditText().setText(event.getTitle());
        descriptionInpt.getEditText().setText(event.getDesc());
        dateInpt.setText(event.getDate());
        sendBtn.setText("حفظ التغيرات");

    }
}
