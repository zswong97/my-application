package student.inti.assignment.photonotes;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import student.inti.assignment.R;
import student.inti.assignment.Task.TaskFragment2;
import student.inti.assignment.Task.TaskMainFragment;
import student.inti.assignment.Task.TaskView;
import student.inti.assignment.Task.post_task;

public class Photo_notes_mainpage extends AppCompatActivity {
    public ImageButton mFloatingActionButton,back;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    TextView group;
    RecyclerView mRecyclerView;
    FirebaseAuth mFirebaseAuth;
    EditText search;
    FirebaseRecyclerAdapter<PhotoModel, PhotoView> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<PhotoModel> options;
    FirebaseUser user = mFirebaseAuth.getInstance().getCurrentUser();


    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photonotes_main);

        search=findViewById(R.id.search);
        search.requestFocus();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        back=findViewById(R.id.backbutton);
        group=findViewById(R.id.textView6);

        // delete= v.findViewById(R.id.deletebutton);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        final String name = getIntent().getStringExtra("name");
        group.setText(name);

        mDatabaseReference = mFirebaseDatabase.getReference(name);


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String text=search.getText().toString();
                searchData(text);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text=search.getText().toString();
                searchData(text);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text=search.getText().toString();
                searchData(text);
            }
        });

        mFloatingActionButton = (ImageButton) findViewById(R.id.floatingActionButton);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = getIntent().getStringExtra("name");
                Intent intent=new Intent(Photo_notes_mainpage.this,Upload_notes.class);
                intent.putExtra("name2",name);
                startActivity(intent);
                swapFragment();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                back();
            }
        });
        showDataAll();
    }


    private void showDataAll(){
        options=new FirebaseRecyclerOptions.Builder<PhotoModel>().setQuery(mDatabaseReference, PhotoModel.class).build();
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<PhotoModel, PhotoView>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PhotoView photoView, int i, @NonNull PhotoModel photomodel) {
                photoView.setInfo(getApplicationContext(),photomodel.getTitle(),photomodel.getImage(),photomodel.getDescription(),photomodel.getUser(),photomodel.getDate());
            }
            @NonNull
            @Override
            public PhotoView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //initiating layout row.xml
                View itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.row_photo,parent,false);
                final PhotoView photoView =new PhotoView(itemView);
                photoView.setOnClickListener(new PhotoView.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                    }
                    @Override
                    public void onItemLongClick(final View view, int position) {
                        final String cTitle = getItem(position).getTitle();
                        final String cImage = getItem(position).getImage();
                        final String cDate = getItem(position).getDate();
                        final String cDescription = getItem(position).getDescription();
                        final String cUser = getItem(position).getUser();
                        showDeleteDataDialog(cTitle,cImage,cDate,cDescription,cUser);
                    }
                });
                return photoView;
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    private void searchData(String search){
        String query=search;
        Query query1=mDatabaseReference.orderByChild("title").startAt(query).endAt(query+"\uf8ff");
        options=new FirebaseRecyclerOptions.Builder<PhotoModel>().setQuery(query1, PhotoModel.class).build();
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<PhotoModel, PhotoView>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PhotoView photoView, int i, @NonNull PhotoModel photomodel) {
                photoView.setInfo(getApplicationContext(),photomodel.getTitle(),photomodel.getImage(),photomodel.getDescription(),photomodel.getUser(),photomodel.getDate());
            }
            @NonNull
            @Override
            public PhotoView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //initiating layout row.xml
                View itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.row_photo,parent,false);
                final PhotoView photoView =new PhotoView(itemView);
                photoView.setOnClickListener(new PhotoView.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                    }
                    @Override
                    public void onItemLongClick(final View view, int position) {
                        final String cTitle = getItem(position).getTitle();
                        final String cImage = getItem(position).getImage();
                        final String cDate = getItem(position).getDate();
                        final String cDescription = getItem(position).getDescription();
                        final String cUser = getItem(position).getUser();
                        showDeleteDataDialog(cTitle,cImage,cDate,cDescription,cUser);
                    }
                });
                return photoView;
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null) {

        }
    }

    private void showDeleteDataDialog(final String cTitle, String cImage, final String cDate, String cDescription, final String cUser){
        //alert dialot
        AlertDialog.Builder builder=new AlertDialog.Builder(Photo_notes_mainpage.this);
        builder.setTitle("Delete");
        builder.setMessage("Confirm delete?");
        //set positive yes button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabaseReference.orderByChild("title").equalTo(cTitle).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postsnapshot :dataSnapshot.getChildren()) {
                            postsnapshot.getRef().removeValue();
                        }
                        Toast.makeText(Photo_notes_mainpage.this,"Task deleted.",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Photo_notes_mainpage.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
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
        Intent i = new Intent(Photo_notes_mainpage.this, Upload_notes.class);
        startActivity(i);
    }

    public void back(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new join());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}


