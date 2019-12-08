package student.inti.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Login extends AppCompatActivity {
    EditText Email, password;
    TextView Signup;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth= FirebaseAuth.getInstance();
        Email =findViewById(R.id.editText);
        password=findViewById(R.id.editText2);
        Signup=findViewById(R.id.textView3);
        CardView buttonsignin=findViewById(R.id.button1);

        mAuthStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if( mFirebaseUser!=null ){
                    Toast.makeText(Login.this,"Log in successful",Toast.LENGTH_SHORT).show();
                    Intent i= new Intent(Login.this,MainActivity.class);
                    startActivity(i);
                }

            }
        };
        buttonsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Email.getText().toString();
                String pwd = password.getText().toString();
                if(email.isEmpty()){
                    Email.setError("Please enter a Email");
                    Email.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(Login.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && pwd.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                FirebaseAuthException e = (FirebaseAuthException )task.getException();
                                Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent intToMain= new Intent(Login.this,MainActivity.class);
                                startActivity(intToMain);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Login.this,"Error!",Toast.LENGTH_SHORT).show();
                }

            }
        });
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intSignup= new Intent(Login.this,SignUp.class);
                startActivity(intSignup);
            }
        });
    }
    @Override
    protected  void onStart(){
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
