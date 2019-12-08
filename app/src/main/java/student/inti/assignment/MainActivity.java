package student.inti.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import student.inti.assignment.Exam.ExamMainFragment;
import student.inti.assignment.Task.TaskMainFragment;
import student.inti.assignment.photonotes.join;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav= findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new join()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem ) {
                    Fragment selectedFragment= null;

                    switch(menuItem.getItemId()){
                        case R.id.nav_home:
                            selectedFragment= new join();
                            break;
                        case R.id.nav_calendar:
                            selectedFragment= new CalendarFragment();
                            break;
                        case R.id.nav_assignment:
                            selectedFragment= new TaskMainFragment();
                            break;
                        case R.id.nav_exam:
                            selectedFragment= new ExamMainFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    return true;
                }
            } ;

}
