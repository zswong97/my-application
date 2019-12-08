package student.inti.assignment.Task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import student.inti.assignment.R;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

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

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class TaskMainFragment extends Fragment {
    public ImageButton mFloatingActionButton;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    RecyclerView mRecyclerView;
    FirebaseAuth mFirebaseAuth;
    Button delete;

    FirebaseRecyclerAdapter<Model, TaskView> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Model> options;

    String user = mFirebaseAuth.getInstance().getCurrentUser().getUid();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task_main, container, false);

        mRecyclerView = v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("Task").child(user);

        mFloatingActionButton = (ImageButton) v.findViewById(R.id.floatingActionButton);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                swapFragment();
            }
        });
        showData();

        return v;
    }

    private void showData(){
        options=new FirebaseRecyclerOptions.Builder<Model>().setQuery(mDatabaseReference,Model.class).build();
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
                        //String mrefid=getItem(position).getRef();

                        Intent intent = new Intent(view.getContext(), post_task.class);
                        //ByteArrayOutputStream stream=new ByteArrayOutputStream();
                        //mBitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                        //byte[]bytes=stream.toByteArray();
                        intent.putExtra("subject", msubject);
                        intent.putExtra("task", mtask);
                        intent.putExtra("date", mdate);
                        intent.putExtra("title", mtitle);
                        intent.putExtra("details", mdetails);
                        startActivity(intent);
                    }
                    @Override
                    public void onItemLongClick(final View view, int position) {
                        final String cSubject = getItem(position).getSubject();
                        final String cTask = getItem(position).getTask();
                        final String cDate = getItem(position).getDate();
                        final String cTitle = getItem(position).getTitle();
                        final String cDetails = getItem(position).getDetails();
                        final String cTaskid = getItem(position).getRef();


                        //show dialog on long click
                        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                        String[] choices = {"Edit" ,"Delete"};
                        builder.setItems(choices, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i==0){
                                   //FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                    //fragmentTransaction.replace(R.id.fragment_container,new TaskFragment2());
                                    Intent intent=new Intent(view.getContext(), TaskFragment2.class);
                                    intent.putExtra("cSubject",cSubject);
                                    intent.putExtra("cTask",cTask);
                                    intent.putExtra("cDate",cDate);
                                    intent.putExtra("cTitle",cTitle);
                                    intent.putExtra("cDetails",cDetails);
                                    intent.putExtra("cTaskid",cTaskid);
                                    startActivity(intent);
                                }
                                if(i==1){
                                    showDeleteDataDialog(cSubject,cTask,cDate,cTitle,cDetails,cTaskid);

                                }
                            }
                        });
                        builder.create().show();
                    }

                });

                return taskView;


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

    private void showDeleteDataDialog(final String cSubject, String cTask, final String cDate, String cTitle, final String cDetails,final  String cTaskId){
        //alert dialot
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete");
        builder.setMessage("Confirm delete?");
        //set positive yes button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabaseReference.orderByChild("Ref").equalTo(cTaskId).addListenerForSingleValueEvent(new ValueEventListener() {
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
        Intent i = new Intent(getActivity(), TaskFragment2.class);
        startActivity(i);
    }

}
