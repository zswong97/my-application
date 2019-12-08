package student.inti.assignment.Interface;

import java.util.List;

import student.inti.assignment.Course.Course;

public interface FirebaseLoadSuccess {
    void onFirebaseLoadSuccess(List<Course> courseList);
    void oonFirebaseLoadFailed(String message);
}
