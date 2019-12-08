package student.inti.assignment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_profile,container,false);
       Button btnLogout = (Button) v.findViewById(R.id.logout);


       btnLogout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
               AlertDialog alertDialog = builder.create();
               alertDialog.show();
           }
       });
       return v;

    }
}
