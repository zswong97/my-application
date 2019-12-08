package student.inti.assignment.photonotes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;
import androidx.recyclerview.widget.RecyclerView;
import student.inti.assignment.Login;
import student.inti.assignment.R;
import student.inti.assignment.Task.TaskFragment2;
import student.inti.assignment.Task.post_task;

public class join extends Fragment {
    private String m_Text = "",m_Pass="";
    CardView btnCreate,btnJoin;
    ImageView imgv;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.photo_join, container, false);
        btnCreate=v.findViewById(R.id.button_create);
        btnJoin=v.findViewById(R.id.button_join);
        imgv=v.findViewById(R.id.imageView3);


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Name");

                // Set up the input
                final EditText input = new EditText(getContext());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        if(m_Text.isEmpty()){
                            Toast.makeText(getActivity(),"Please enter a name",Toast.LENGTH_SHORT).show();
                        }else {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(m_Text).push();
                            Map post = new HashMap();
                            databaseReference.setValue(post);
                            getActivity().finish();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });


        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Enter a name");
                // Set up the input
                final EditText input = new EditText(getContext());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        if(m_Text.isEmpty()){
                            Toast.makeText(getActivity(),"Please enter a name to join",Toast.LENGTH_SHORT).show();
                        }else {
                            Intent intent = new Intent(view.getContext(), Photo_notes_mainpage.class);
                            intent.putExtra("name", m_Text);
                            startActivity(intent);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        ImageButton btnLogout = (ImageButton) v.findViewById(R.id.imageButton);


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to Exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intToLogin = new Intent(getActivity(), Login.class);
                                startActivity(intToLogin);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();

                            }
                        });
                androidx.appcompat.app.AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return v;
    }
}

