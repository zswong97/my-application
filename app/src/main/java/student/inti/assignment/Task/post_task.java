package student.inti.assignment.Task;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import student.inti.assignment.R;

public class post_task extends AppCompatActivity {

    TextView mSubject, mTask, mDate, mTitle, mDetails;
    Button mButtonDelete, mButtonEdit;

    String cSubject, cTask, cDate, cTitle, cDetails, cButton;
    FirebaseAuth mFirebaseAuth;

    String user = mFirebaseAuth.getInstance().getCurrentUser().getUid();
    DataSnapshot mDataSnapshot;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_post_task);

        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("WHAT");
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        //mButtonEdit=findViewById(R.id.editbutton);
        //mButtonDelete = findViewById(R.id.deletebutton);
        mSubject = findViewById(R.id.Subject);
        mTask = findViewById(R.id.Task);
        mDate = findViewById(R.id.Calendar);
        mTitle = findViewById(R.id.EditText5);
        mDetails = findViewById(R.id.EditText7);

        final String subject = getIntent().getStringExtra("subject");
        final String task = getIntent().getStringExtra("task");
        final String date = getIntent().getStringExtra("date");
        final String title = getIntent().getStringExtra("title");
        final String details = getIntent().getStringExtra("details");

        mSubject.setText(subject);
        mTask.setText(task);
        mDate.setText(date);
        mTitle.setText(title);
        mDetails.setText(details);


   /*     mButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskFragment2 fragment = new TaskFragment2();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                Intent intent=new Intent(post_task.this, TaskFragment2.class);
                intent.putExtra("cSubject",subject);
                intent.putExtra("cTask",task);
                intent.putExtra("cDate",date);
                intent.putExtra("cTitle",title);
                intent.putExtra("cDetails",details);
                transaction.commit();
            }
        });*/

       /* Bundle intent = getIntent().getExtras();
        if (intent != null) {
            cSubject = intent.getString("cSubject");
            cTask = intent.getString("cTask");
            cDate = intent.getString("cDate");
            cTitle = intent.getString("cTitle");
            cDetails = intent.getString("cDetails");


            mSubject.setText(cSubject);
            mTask.setText(cTask);
            mDate.setText(cDate);
            mTitle.setText(cTitle);
            mDetails.setText(cDetails);

        }

            final String nsubject = mSubject.getText().toString();
            final String ntask = mTask.getText().toString();
            final String ndate = mDate.getText().toString();
            final String ntitle = mTitle.getText().toString();
            final String ndetails = mDetails.getText().toString();


            FirebaseDatabase mFirbebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = mFirbebaseDatabase.getReference("Task").child(user);

            Query query = databaseReference.orderByChild("Title").equalTo(cTitle);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ds.getRef().child("Subject").setValue(nsubject);
                        ds.getRef().child("Task").setValue(ntask);
                        ds.getRef().child("Date").setValue(ndate);
                        ds.getRef().child("Title").setValue(ntitle);
                        ds.getRef().child("Details").setValue(ndetails);
                    }
                    //startActivity(new Intent(post_task.this,))
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/

        }


    }

