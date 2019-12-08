package student.inti.assignment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import student.inti.assignment.Exam.ExamFillFragment;
import student.inti.assignment.Exam.ExamModel;
import student.inti.assignment.Exam.ExamView;
import student.inti.assignment.Exam.post_exam;
import student.inti.assignment.Task.Model;
import student.inti.assignment.Task.TaskView;
import student.inti.assignment.Task.post_task;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;
public class CalendarFragment extends Fragment {
    private CalendarView mCalendarView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    DatabaseReference mDatabaseReference2;
    RecyclerView mRecyclerView,mRecyclerView2;
    FirebaseAuth mFirebaseAuth;
    FirebaseRecyclerAdapter<Model, TaskView> firebaseRecyclerAdapter;
    FirebaseRecyclerAdapter<ExamModel, ExamView> firebaseRecyclerAdapter2;
    FirebaseRecyclerOptions<ExamModel> options2;
    String user = mFirebaseAuth.getInstance().getCurrentUser().getUid();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_calendar, container, false);
        final TextView textview = v.findViewById(R.id.textView2);
        final CalendarView calendarv = v.findViewById(R.id.calendar);
        TextView button = v.findViewById(R.id.textView3);
        mRecyclerView = v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView2=v.findViewById(R.id.recyclerView2);
        mRecyclerView2.setHasFixedSize(true);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("Task").child(user);
        mDatabaseReference2 = mFirebaseDatabase.getReference("Exam").child(user);

        Calendar now = Calendar.getInstance();
        final String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(now.getTime());
        textview.setText(currentDate);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar myCalendar = Calendar.getInstance();
                calendarv.setDate(myCalendar.getTimeInMillis(), true, true);
                String myFormat = DateFormat.getDateInstance(DateFormat.FULL).format(myCalendar.getTime());
                textview.setText(myFormat);
                final String toDate=textview.getText().toString();
                showData(toDate);
            }
        });

        calendarv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                // TODO Auto-generated method stub
                String myFormat = DateFormat.getDateInstance(DateFormat.FULL).format(myCalendar.getTime());
                textview.setText(myFormat);
                String cDate=textview.getText().toString();
               showData(cDate);
               showExam(cDate);
            }

        });
        return v;
    }

    private void showData(String cDate){
        Query query = mDatabaseReference.orderByChild("Date").equalTo(cDate);
        FirebaseRecyclerOptions<Model>options=new FirebaseRecyclerOptions.Builder<Model>().setQuery(query,Model.class).build();
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Model, TaskView>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TaskView taskView, int i, @NonNull Model model) {
                taskView.setInfo(getApplicationContext(), model.getSubject(), model.getTask(), model.getDate(), model.getTitle(), model.getDetails());
            }
            @NonNull
            @Override
            public TaskView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //initiating layout row.xml
                View itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
                final TaskView taskView =new TaskView(itemView);

                taskView.setOnClickListener(new TaskView.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String msubject = getItem(position).getSubject();
                        String mtask = getItem(position).getTask();
                        String mdate = getItem(position).getDate();
                        String mtitle = getItem(position).getTitle();
                        String mdetails = getItem(position).getDetails();

                        Intent intent = new Intent(view.getContext(), post_task.class);
                        intent.putExtra("subject", msubject);
                        intent.putExtra("task", mtask);
                        intent.putExtra("date", mdate);
                        intent.putExtra("title", mtitle);
                        intent.putExtra("details", mdetails);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });

                return taskView;


            }
        };

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }


    private void showExam(String cDate){
        Query query = mDatabaseReference2.orderByChild("Date").equalTo(cDate);
        options2=new FirebaseRecyclerOptions.Builder<ExamModel>().setQuery(query, ExamModel.class).build();
        firebaseRecyclerAdapter2=new FirebaseRecyclerAdapter<ExamModel, ExamView>(options2) {
            @Override
            protected void onBindViewHolder(@NonNull ExamView examView, int i, @NonNull ExamModel model) {
                examView.setInfo(getApplicationContext(), model.getSubject(), model.getDate(), model.getTime(), model.getDuration(), model.getRoom(),model.getNotes());
            }
            @NonNull
            @Override
            public ExamView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //initiating layout row.xml
                View itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.rowexam,parent,false);
                final ExamView examView =new ExamView(itemView);
                examView.setOnClickListener(new ExamView.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String msubject = getItem(position).getSubject();
                        String mdate = getItem(position).getDate();
                        String mtime = getItem(position).getTime();
                        String mduration = getItem(position).getDuration();
                        String mroom = getItem(position).getRoom();
                        String mnotes=getItem(position).getNotes();

                        Intent intent = new Intent(view.getContext(), post_exam.class);
                        intent.putExtra("subject", msubject);
                        intent.putExtra("date", mdate);
                        intent.putExtra("time", mtime);
                        intent.putExtra("duration", mduration);
                        intent.putExtra("room", mroom);
                        intent.putExtra("notes", mnotes);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                return examView;
            }
        };
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseRecyclerAdapter2.startListening();
        mRecyclerView2.setAdapter(firebaseRecyclerAdapter2);
        firebaseRecyclerAdapter2.startListening();
    }

}
