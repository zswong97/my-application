package student.inti.assignment.Task;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import student.inti.assignment.Login;
import student.inti.assignment.R;

import static android.app.DatePickerDialog.*;

public class TaskFragment2 extends AppCompatActivity {
    private CalendarView mCalendarView;

    //ArrayList<String> items = new ArrayList<>();
    //SearchableSpinner searchableSpinner;
    Spinner spinner;
    TextView textviewdate;
    Button btnShow, btnUp;
    EditText title, details,subj;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference databaseReference;

    @Override
    // public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    //View v= inflater.inflate(R.layout.fragment_task,container,false);
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_task);

        subj = (EditText) findViewById(R.id.subject);
        spinner = (Spinner) findViewById(R.id.spinner);
        textviewdate = (TextView) findViewById(R.id.editText4);
        btnShow = (Button) findViewById(R.id.button);
        title = (EditText) findViewById(R.id.editText5);
        details = (EditText) findViewById(R.id.editText7);
        //final String TAG = "YOUR-TAG-NAME";

        final String user = mFirebaseAuth.getInstance().getCurrentUser().getUid();

        final String cSubject, cTask, cDate, cTitle, cDetails, cTaskid;


        mFirebaseAuth = FirebaseAuth.getInstance();

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.Subject, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter2);

        if(textviewdate==null){
            Calendar current = Calendar.getInstance();
            String myFormat = DateFormat.getDateInstance(DateFormat.FULL).format(current.getTime()); //Change as you need
            textviewdate.setText(myFormat);
        }

        final Calendar myCalendar = Calendar.getInstance();
            String myFormat = DateFormat.getDateInstance(DateFormat.FULL).format(myCalendar.getTime()); //Change as you need
            textviewdate.setText(myFormat);


        textviewdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final int year = myCalendar.get(Calendar.YEAR);
                final int month = myCalendar.get(Calendar.MONTH);
                final int day = myCalendar.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog datePickerDialog = new DatePickerDialog(TaskFragment2.this, new OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = DateFormat.getDateInstance(DateFormat.FULL).format(myCalendar.getTime());
                        //Change as you need
                        textviewdate.setText(myFormat);
                        //myCalendar.set(datepicker.getYear(), datepicker.getMonth(), datepicker.getDayOfMonth());

                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });


            cSubject = getIntent().getStringExtra("cSubject");
            cTask = getIntent().getStringExtra("cTask");
            cDate = getIntent().getStringExtra("cDate");
            cTitle = getIntent().getStringExtra("cTitle");
            cDetails = getIntent().getStringExtra("cDetails");
            cTaskid = getIntent().getStringExtra("cTaskid");

           subj.setText(cSubject);

            String compareTask = cTask;
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter2);
            if (compareTask != null) {
                int spinnerPosition = adapter2.getPosition(compareTask);
                spinner.setSelection(spinnerPosition);
            }

            textviewdate.setText(cDate);
            title.setText(cTitle);
            details.setText(cDetails);
            Bundle intent= getIntent().getExtras();

                if (intent!=null){
                    btnShow.setText("Update");
                }

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnShow.getText().equals("Update")) {
                    final String nsubject =subj.getText().toString();
                    final String ntask = spinner.getSelectedItem().toString();
                    final String ndate = textviewdate.getText().toString();
                    final String ntitle = title.getText().toString();
                    final String ndetails = details.getText().toString();

                    FirebaseDatabase mFirbebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = mFirbebaseDatabase.getReference("Task").child(user);

                    Query query = databaseReference.orderByChild("Ref").equalTo(cTaskid);
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
                            Toast.makeText(TaskFragment2.this, "DatabaseUpdated", Toast.LENGTH_SHORT).show();
                            finish();
                            swapFragment();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Task").child(user).push();
                    final String refid = databaseReference.getKey();
                    String subject = subj.getText().toString();
                    String task = spinner.getSelectedItem().toString();
                    String date = textviewdate.getText().toString();
                    String tle = title.getText().toString();
                    String dtls = details.getText().toString();
                    if(subject.isEmpty()) {
                        subj.setError("Please enter a subject");
                        subj.requestFocus();
                    }
                    else if(tle.isEmpty()){
                        title.setError("Please enter a title");
                        title.requestFocus();
                    }
                    else if(subject.isEmpty() && tle.isEmpty()){
                        Toast.makeText(TaskFragment2.this,"Subject and Title are empty!",Toast.LENGTH_SHORT).show();
                    }else {
                        Map post = new HashMap();

                        post.put("Subject", subject);
                        post.put("Task", task);
                        post.put("Date", date);
                        post.put("Title", tle);
                        post.put("Details", dtls);
                        post.put("Ref", refid);

                        databaseReference.setValue(post);
                        Toast.makeText(TaskFragment2.this, "TaskCreated", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
            }
        });
    }


        private void swapFragment () {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new TaskMainFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }