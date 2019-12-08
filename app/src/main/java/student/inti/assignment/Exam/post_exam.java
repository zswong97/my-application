package student.inti.assignment.Exam;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import androidx.appcompat.app.AppCompatActivity;
import student.inti.assignment.R;

public class post_exam extends AppCompatActivity {
    TextView mSubject, mDate, mTime, mDuration, mRoom,mNotes;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_exam);

        mSubject = findViewById(R.id.Subject);
        mDate = findViewById(R.id.Date);
        mTime = findViewById(R.id.Time);
        mDuration = findViewById(R.id.Duration);
        mRoom = findViewById(R.id.Room);
        mNotes = findViewById(R.id.Notes);

        final String subject = getIntent().getStringExtra("subject");
        final String date = getIntent().getStringExtra("date");
        final String time = getIntent().getStringExtra("time");
        final String duration = getIntent().getStringExtra("duration");
        final String room = getIntent().getStringExtra("room");
        final String notes = getIntent().getStringExtra("notes");

        mSubject.setText(subject);
        mDate.setText(date);
        mTime.setText(time);
        mDuration.setText(duration);
        mRoom.setText(room);
        mNotes.setText(notes);

    }


}

