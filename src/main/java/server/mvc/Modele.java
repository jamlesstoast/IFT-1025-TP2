package server.mvc;
import server.models.Course;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Modele {

    private List<Course> courses;

    public Modele(){
        courses = new ArrayList<>();
    }

    public List<Course> getCourses(String session) {
        // 'courses' list is cleared before new courses are added
        courses.clear();
        // Get courses from cours.txt for given session
        // 'courseSession' : local variable that represents the session of a course
        // read from 'cours.txt'
        // 'session' : parameter passed to 'getCourses', represents the session for
        // which the list of courses is requested.
        File file = new File("cours.txt");
        try{
            BufferedReader bufferedreader = new BufferedReader(new FileReader(file));
            String line;
            // Each line is read & we compare the 'courseSession' with 'session' parameter
            while ((line = bufferedreader.readLine()) != null) {
                String[] segments = line.split("\t");
                String code = segments[0];
                String name = segments[1];
                String courseSession = segments[2];
                if (courseSession.equals(session)) {
                    Course course = new Course(name, code, courseSession);
                    courses.add(course);
                }
            }
            bufferedreader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return courses;
    }
}
