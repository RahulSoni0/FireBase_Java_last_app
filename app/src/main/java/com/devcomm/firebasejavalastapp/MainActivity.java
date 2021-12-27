package com.devcomm.firebasejavalastapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.SnapshotHolder;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {


    private Button select , upload ;
    private TextView filename;
    Uri pdfuri;// urls meant for local storage or path
    FirebaseStorage storage;//used for uploading files
    FirebaseDatabase database;//used to store url of uploaded files
    ProgressDialog progressDialog;
    private static final int PICK_PDF = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        select = findViewById(R.id.button_select);
        upload = findViewById(R.id.button_upload);
        filename  = findViewById(R.id.textView_filename);

        storage = FirebaseStorage.getInstance();//return an obj of firebasestorage
        database = FirebaseDatabase.getInstance();// return an obj of firebasedatabase

      select.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

               selectpdf();



          }
      });


      upload.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(pdfuri != null){
                  uploadfile(pdfuri);
              }

              else{
                  Toast.makeText(MainActivity.this, "Select a file", Toast.LENGTH_SHORT).show();
              }

          }
      });


    }

    private void uploadfile(Uri pdfuri) {
       progressDialog = new ProgressDialog(MainActivity.this);
       progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
       progressDialog.setTitle("Uploading file...");
       progressDialog.setProgress(0);
       progressDialog.show();

        String filename = System.currentTimeMillis() + "";
        StorageReference storageReference = storage.getReference();
        //path in which file will be saved  root path

        //will make a folder in root element and will then upload in the child

        storageReference.child("Uploads").child(filename).putFile(pdfuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                DatabaseReference databaseReference = database.getReference();
                Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadLink = uri.toString();

                        databaseReference.child(filename).setValue(downloadLink).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {



                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this,"Success" , Toast.LENGTH_SHORT).show();

                                }
                                else{

                                    Toast.makeText(MainActivity.this,"Faliure" , Toast.LENGTH_SHORT).show();

                                }
                            }
                        });









                    }
                });

               // String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();






               //DatabaseReference databaseReference = database.getReference();
               /*databaseReference.child(filename).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {



                       if(task.isSuccessful()){
                           progressDialog.dismiss();
                           Toast.makeText(MainActivity.this,"Success" , Toast.LENGTH_SHORT).show();

                       }
                   else{

                           Toast.makeText(MainActivity.this,"Faliure" , Toast.LENGTH_SHORT).show();

                       }
                   }
               });*/
            }
        })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                Toast.makeText(MainActivity.this , "error" , Toast.LENGTH_SHORT).show();



            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                int currentprogress =
                        (int) (100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());

                       progressDialog.setProgress(currentprogress);



            }
        });







    }

    private void selectpdf() {
    // selecting pdf file using file manager
        // intent
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);

        // We will be redirected to choose pdf
        intent.setType("application/pdf");

        startActivityForResult(intent,PICK_PDF);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //check wether user has selected a file or not

        if(resultCode == RESULT_OK &&
                data != null && requestCode == PICK_PDF){

          pdfuri = data.getData();//this will return the uri of selected file
          filename.setText("A file is selected: " + pdfuri.toString());


        }
    else{


            Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show();
        }

    }
}