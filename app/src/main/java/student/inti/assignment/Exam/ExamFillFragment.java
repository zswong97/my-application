package student.inti.assignment.Exam;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import student.inti.assignment.DurationDialog;
import student.inti.assignment.R;
import student.inti.assignment.Task.TaskFragment2;

import static android.app.DatePickerDialog.OnDateSetListener;

public class   ExamFillFragment extends AppCompatActivity implements NumberPicker.OnValueChangeListener {
    private CalendarView mCalendarView;

    //ArrayList<String> items = new ArrayList<>();
    SearchableSpinner searchableSpinner;
    Spinner spinner;
    TextView textviewdate;
    Button btnShow, btnUp;
    EditText title, details;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference databaseReference;


    TextView subject,date,time,duration,room,notes;
    String day;
    int hour,minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_exam_fill);
       // searchableSpinner = (SearchableSpinner) v.findViewById(R.id.searchspinner);
       // spinner=(Spinner)v.findViewById(R.id.spinner);
        date=(TextView) findViewById(R.id.calendar);
        subject=(TextView) findViewById(R.id.subject);
        time=(TextView) findViewById(R.id.time);
        duration=(TextView) findViewById(R.id.duration);
        room=(TextView) findViewById(R.id.room);
        notes=(TextView) findViewById(R.id.notes);
        btnShow=findViewById(R.id.button);


        final String user = mFirebaseAuth.getInstance().getCurrentUser().getUid();

        final String cSubject, cDate, cTime, cDuration ,cRoom,cNotes,cExamId;

        mFirebaseAuth = FirebaseAuth.getInstance();

        if(date.getText()==null){
            Calendar current = Calendar.getInstance();
            String myFormat = DateFormat.getDateInstance(DateFormat.FULL).format(current.getTime()); //Change as you need
            date.setText(myFormat);
        }

        final Calendar myCalendar = Calendar.getInstance();
        String myFormat = DateFormat.getDateInstance(DateFormat.FULL).format(myCalendar.getTime()); //Change as you need
        date.setText(myFormat);

        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final int year = myCalendar.get(Calendar.YEAR);
                final int month = myCalendar.get(Calendar.MONTH);
                final int day = myCalendar.get(Calendar.DAY_OF_MONTH);

                 DatePickerDialog datePickerDialog= new DatePickerDialog(ExamFillFragment.this,new OnDateSetListener(){

                    @Override
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday){
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = DateFormat.getDateInstance(DateFormat.FULL).format(myCalendar.getTime()); //Change as you need
                        date.setText(myFormat);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c= Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog= new TimePickerDialog(ExamFillFragment.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker,int i, int i1){
                        if(i>=12){
                            day="PM";
                        }
                        else {
                            day = "AM";
                        }
                        time.setText(String.format("%02d:%02d",i,i1)+day);
                    }
                },hour,minute,false);
                timePickerDialog.show();
            }
        });


        duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNumberPicker(view);
            }
        });

        cSubject = getIntent().getStringExtra("cSubject");
        cDate = getIntent().getStringExtra("cDate");
        cTime = getIntent().getStringExtra("cTime");
        cDuration = getIntent().getStringExtra("cDuration");
        cRoom = getIntent().getStringExtra("cRoom");
        cNotes = getIntent().getStringExtra("cNotes");
        cExamId = getIntent().getStringExtra("cExamId");


        subject.setText(cSubject);
        date.setText(cDate);
        time.setText(cTime);
        duration.setText(cDuration);
        room.setText(cRoom);
        notes.setText(cNotes);


        Bundle intent= getIntent().getExtras();

        if (intent!=null){
            btnShow.setText("Update");
        }

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnShow.getText().equals("Update")) {
                    final String nsubject =subject.getText().toString();
                    final String ndate = date.getText().toString();
                    final String ntime = time.getText().toString();
                    final String nduration = duration.getText().toString();
                    final String nroom = room.getText().toString();
                    final String nnotes = notes.getText().toString();


                    FirebaseDatabase mFirbebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = mFirbebaseDatabase.getReference("Exam").child(user);

                    Query query = databaseReference.orderByChild("Ref").equalTo(cExamId);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                ds.getRef().child("Subject").setValue(nsubject);
                                ds.getRef().child("Date").setValue(ndate);
                                ds.getRef().child("Time").setValue(ntime);
                                ds.getRef().child("Duration").setValue(nduration);
                                ds.getRef().child("Room").setValue(nroom);
                                ds.getRef().child("Notes").setValue(nnotes);
                            }
                            Toast.makeText(ExamFillFragment.this, "DatabaseUpdated", Toast.LENGTH_SHORT).show();
                            finish();
                            swapFragment();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Exam").child(user).push();
                    final String refid = databaseReference.getKey();
                    String sub=subject.getText().toString();
                    String dt = date.getText().toString();
                    String tim = time.getText().toString();
                    String dura = duration.getText().toString();
                    String rm = room.getText().toString();
                    String note = notes.getText().toString();
                    if(sub.isEmpty()) {
                        subject.setError("Please enter a subject");
                        subject.requestFocus();
                    }else {
                        Map post = new HashMap();
                        post.put("Subject", sub);
                        post.put("Date", dt);
                        post.put("Time", tim);
                        post.put("Duration", dura);
                        post.put("Room", rm);
                        post.put("Notes", note);
                        post.put("Ref", refid);
                        databaseReference.setValue(post);
                        Toast.makeText(ExamFillFragment.this, "TaskCreated", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
    }


    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
       duration.setText(String.valueOf(numberPicker.getValue())+" "+"minutes");
        /*Toast.makeText(this,
                "selected number " + numberPicker.getValue(), Toast.LENGTH_SHORT).show();*/

    }

    public void showNumberPicker(View view){
        DurationDialog newFragment = new DurationDialog();
        newFragment.setValueChangeListener(this);
        newFragment.show(getSupportFragmentManager(), "time picker");
    }


    private void swapFragment () {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new ExamMainFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
