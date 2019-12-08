package student.inti.assignment.Exam;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import student.inti.assignment.R;
import student.inti.assignment.Task.Model;
import student.inti.assignment.Task.TaskFragment2;
import student.inti.assignment.Exam.ExamView;
import student.inti.assignment.Task.post_task;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class ExamMainFragment extends Fragment {
    public ImageButton mFloatingActionButton;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    RecyclerView mRecyclerView;
    FirebaseAuth mFirebaseAuth;
    TextView notas;

    FirebaseRecyclerAdapter<ExamModel, ExamView> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<ExamModel> options;

    String user = mFirebaseAuth.getInstance().getCurrentUser().getUid();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_exam_main,container, false);

        mRecyclerView = v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("Exam").child(user);

        mFloatingActionButton=(ImageButton)v.findViewById(R.id.floatingActionButton);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                swapFragment();
            }
        });
           showData();
        return v;
    }


    private void showData(){
        options=new FirebaseRecyclerOptions.Builder<ExamModel>().setQuery(mDatabaseReference, ExamModel.class).build();
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<ExamModel, ExamView>(options) {
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
                        //ByteArrayOutputStream stream=new ByteArrayOutputStream();
                        //mBitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                        //byte[]bytes=stream.toByteArray();
                        intent.putExtra("subject", msubject);
                        intent.putExtra("date", mdate);
                        intent.putExtra("time", mtime);
                        intent.putExtra("duration", mduration);
                        intent.putExtra("room", mroom);
                        intent.putExtra("notes", mnotes);
                        startActivity(intent);
                    }
                    @Override
                    public void onItemLongClick(final View view, int position) {
                        final String cSubject = getItem(position).getSubject();
                        final String cDate = getItem(position).getDate();
                        final String cTime = getItem(position).getTime();
                        final String cDuration = getItem(position).getDuration();
                        final String cRoom = getItem(position).getRoom();
                        final String cNotes = getItem(position).getNotes();
                        final String cExamId = getItem(position).getRef();
                        //show dialog on long click
                        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                        String[] choices = {"Edit" ,"Delete"};
                        builder.setItems(choices, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i==0){
                                    //FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                    //fragmentTransaction.replace(R.id.fragment_container,new TaskFragment2());
                                    Intent intent=new Intent(view.getContext(), ExamFillFragment.class);
                                    intent.putExtra("cSubject",cSubject);
                                    intent.putExtra("cDate",cDate);
                                    intent.putExtra("cTime",cTime);
                                    intent.putExtra("cDuration",cDuration);
                                    intent.putExtra("cRoom",cRoom);
                                    intent.putExtra("cNotes",cNotes);
                                    intent.putExtra("cExamId",cExamId);
                                    startActivity(intent);
                                }
                                if(i==1){
                                    showDeleteDataDialog(cSubject,cDate,cTime,cDuration,cRoom,cNotes,cExamId);
                                }
                            }
                        });
                        builder.create().show();
                    }
                });
                return examView;
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }


    public void onStart() {
        super.onStart();
        if(firebaseRecyclerAdapter!=null){

        }
    }

    private void showDeleteDataDialog(final String cSubject, String cDate, final String cTime, String cDuration, final String cRoom,final  String cNotes,final String cExamId){
        //alert dialot
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete");
        builder.setMessage("Confirm delete?");
        //set positive yes button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabaseReference.orderByChild("Ref").equalTo(cExamId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postsnapshot :dataSnapshot.getChildren()) {
                            postsnapshot.getRef().removeValue();
                        }
                        Toast.makeText(getActivity(),"Task deleted.",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        //set negative/no button
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        //show dialog
        builder.create().show();
    }





    private void swapFragment(){
        Intent i = new Intent(getActivity(), ExamFillFragment.class);
        startActivity(i);
    }
}
