package student.inti.assignment.photonotes;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import student.inti.assignment.Login;
import student.inti.assignment.R;
import student.inti.assignment.SignUp;
import student.inti.assignment.Task.TaskMainFragment;

import static android.app.DatePickerDialog.*;

public class Upload_notes extends AppCompatActivity {
    Button btnUp;
    EditText tit, desc;
    TextView usr,pst;
    ImageView imgv;
    ImageButton back;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference databaseReference;

    //Folder path for Firebase Storage
    String mStoragePath="Uploads/";
    //Root Database name for firebase database
    String mDatabasePath="PhotoNotes";

    //Creating uri
    Uri mFilePathUri;

    //Create storagereference and database reference
    StorageReference mStorageReference;
    DatabaseReference dbRef;

    //Progress Dialog
    ProgressDialog mProgressDialog;

    //Image request code for choosing image
    int IMAGE_REQUEST_CODE=5;

    @Override
    // public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    //View v= inflater.inflate(R.layout.fragment_task,container,false);
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_upload);

        String name2 = getIntent().getStringExtra("name2");
        mStorageReference = FirebaseStorage.getInstance().getReference();
        dbRef = FirebaseDatabase.getInstance().getReference(name2);//.push();

        tit =findViewById(R.id.title);
        desc =findViewById(R.id.description);
        imgv =findViewById(R.id.imageView);
        btnUp =findViewById(R.id.upload);
        usr=findViewById(R.id.User);
        pst=findViewById(R.id.postedby);
        back=findViewById(R.id.backbutton);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("PhotoNotes");
        mProgressDialog=new ProgressDialog(Upload_notes.this);

        mFirebaseAuth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = getIntent().getStringExtra("name2");
                Intent intent=new Intent(Upload_notes.this,Photo_notes_mainpage.class);
                intent.putExtra("name",name);
                startActivity(intent);
                swapactivity();
            }
            private void swapactivity() {
                Intent i = new Intent(Upload_notes.this, Photo_notes_mainpage.class);
                startActivity(i);
            }
        });

        imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), IMAGE_REQUEST_CODE);
            }
        });
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call method upload data to firebase
                uploadDataToFirebase();
            }
        });
    }
    private void uploadDataToFirebase() {
        //check
        if(mFilePathUri!=null){
            mProgressDialog.setTitle("Uploading Image");
            mProgressDialog.show();
            StorageReference storageReference=mStorageReference.child(mStoragePath+System.currentTimeMillis()+"."+getFileExtension(mFilePathUri));
            //adding SuccessListener
            storageReference.putFile(mFilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        final FirebaseUser user = mFirebaseAuth.getCurrentUser();
                        Calendar now = Calendar.getInstance();
                        final String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(now.getTime());
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            Uri downloadUri=uriTask.getResult();
                            //get title
                            String mPostTitle= tit.getText().toString().trim();
                            String mPostDescription=desc.getText().toString().trim();
                            String mUser=user.getEmail().trim();
                            String mDate=currentDate.trim();
                            mProgressDialog.dismiss();
                            Toast.makeText(Upload_notes.this,"Upload successful",Toast.LENGTH_SHORT).show();
                            PhotoModel photoModel=new PhotoModel(mPostTitle,mPostDescription,downloadUri.toString(),mUser,mDate);
                            String ImageUploadId=dbRef.push().getKey();
                            dbRef.child(ImageUploadId).setValue(photoModel);
                        }
                    })
                    //if network failure
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //hide progress dialog
                            mProgressDialog.dismiss();
                            Toast.makeText(Upload_notes.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            mProgressDialog.setTitle("Uploading Image");
                        }
                    });
        }
        else{
            Toast.makeText(Upload_notes.this,"Please Select a image",Toast.LENGTH_SHORT).show();
        }
    }
    //method to get the selected image file extension from file path uri
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_REQUEST_CODE && resultCode==RESULT_OK && data!=null && data.getData() !=null);
        mFilePathUri= data.getData();
        try{
            //get select image into bitmap
            Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),mFilePathUri);
            //setting bitmap into imageview
            imgv.setImageBitmap(bitmap);
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

}