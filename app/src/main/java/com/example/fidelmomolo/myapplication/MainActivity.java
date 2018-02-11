package com.example.fidelmomolo.myapplication;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {



    private static final int PICK_REQUEST=1;

     private DatabaseReference myDatabase;
     private StorageReference myStorage;
     private StorageTask uploadTask;


    Button Upload,picture;
    TextView show_details;
    ImageView imageView;
    EditText editText;



    ProgressBar progressBar;

    Uri imageuri;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Upload=findViewById(R.id.upload);
        picture=findViewById(R.id.file);
        show_details=findViewById(R.id.show_detail);
        editText=findViewById(R.id.editText);
        progressBar=findViewById(R.id.progressBar);
        imageView=findViewById(R.id.imageView);


          progressDialog=new ProgressDialog(this);


         FirebaseDatabase database = FirebaseDatabase.getInstance();
         FirebaseStorage storage=FirebaseStorage.getInstance();


         myDatabase = database.getReference().child("uploads");
         myStorage=storage.getReference("uploads");


        DatabaseReference myRef1 = database.getReference("message");
//        myRef.setValue("Hello, World!");
//        myRef1.setValue("Luther king");





        show_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Recycle.class);
                startActivity(intent);

            }
        });


        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(uploadTask !=null && uploadTask.isInProgress()){

                    //prevents user from uploading same image severally to the data base when the task is in progress of uploading
                    Toast.makeText(MainActivity.this,"Upload is in progress",Toast.LENGTH_LONG).show();

                }

                else

             upLoadFile();

            }
        });


        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                openfileChooser();

            }
        });













        // Read from the database
        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d("TheBeast", "Value is: " + value);





            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Guardian", "Failed to read value.", error.toException());
            }
        });





    }




    private void openfileChooser() {
       // It opens a folder containing images
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_REQUEST);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //it handles selected image

        if (requestCode==PICK_REQUEST && resultCode==RESULT_OK && data !=null && data.getData() !=null){

            imageuri=data.getData();//the path of selected image is stored here

            Picasso.with(this).load(imageuri).into(imageView);//uses the picasso library to load image to the image view



//            Picasso.with(this)
//                    .load(imageuri)
//                    .resize(50, 50)
//                    .centerCrop()
//                    .into(imageView);

        }
    }





    private  String getFileExtension(Uri uri){
        //used for getting extension from our file
        ContentResolver cR=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cR.getType(uri));

    }

    private void upLoadFile() {

        if(imageuri!=null){
           //the code gives the image files uploaded to firebase a name similar to current milliseconds
          StorageReference fileReference=myStorage.child(System.currentTimeMillis()+"."+getFileExtension(imageuri));

            //the code uploads actual image file to firebase
            uploadTask=fileReference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            progressBar.setProgress(0);
                        }
                    },100);


                    Toast.makeText(MainActivity.this,"Image Uploaded successfully",Toast.LENGTH_LONG).show();

                    Upload upload=new Upload(editText.getText().toString().trim(),taskSnapshot.getDownloadUrl().toString());

                    String UploadId=myDatabase.push().getKey();
                    myDatabase.child(UploadId).setValue(upload);

                    progressDialog.dismiss();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int)progress);
//                    progressDialog.setMessage("Uploading photo");
//                    progressDialog.show();
                }
            });


        }


        else{

            Toast.makeText(this,"No Image has been selected",Toast.LENGTH_LONG).show();


        }


    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
