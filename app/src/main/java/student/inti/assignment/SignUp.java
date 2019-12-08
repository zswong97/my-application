package student.inti.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class SignUp extends AppCompatActivity {
    EditText Email, password;
    TextView login,buttonsignup;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirebaseAuth= FirebaseAuth.getInstance();

        Email =findViewById(R.id.editText);
        password=findViewById(R.id.editText2);
        login=findViewById(R.id.textView3);
        CardView buttonsignup=findViewById(R.id.button1);
        buttonsignup.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(SignUp.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && pwd.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                FirebaseAuthException e = (FirebaseAuthException )task.getException();
                                Toast.makeText(SignUp.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                            else{
                                startActivity(new Intent(SignUp.this,Login.class));
                            }
                        }
                    });
                }else{
                    Toast.makeText(SignUp.this,"Error!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUp.this,Login.class);
                startActivity(i);
            }
        });

    }
}
