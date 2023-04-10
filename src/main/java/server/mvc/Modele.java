package server.mvc;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Modele {

    private final List<Course> courses;

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
            System.out.println("Registration failed due to missing file 'inscription.txt'");
        }
        return courses;
    }

    public void addRegistration(RegistrationForm form) throws IllegalArgumentException {

        // Validate session -> 3 choices
        if (!form.getCourse().getSession().equals("Hiver") && !form.getCourse().getSession().equals("Automne")
                && !form.getCourse().getSession().equals("Été")) {
            throw new IllegalArgumentException("Invalid session");
        }

        // Validate course code -> IFT1025
        if (!form.getCourse().getCode().matches("[A-Z]{3}\\d{4}")) {
            throw new IllegalArgumentException("Invalid course code");
        }

        // Validate matricule -> 20207461
        if (!form.getMatricule().matches("\\d{8}")) {
            throw new IllegalArgumentException("Invalid matricule");
        }

        // Validate first name
        if (!form.getPrenom().matches("^[a-zA-ZÀ-ÿ\\\\-]+$")) {
            throw new IllegalArgumentException("Invalid first name");
        }

        // Validate last name
        if (!form.getNom().matches("^[a-zA-ZÀ-ÿ\\-]+$")) {
            throw new IllegalArgumentException("Invalid last name");
        }

        // Validate email -> celina.zhang@umontreal.ca
        if (!form.getEmail().matches("/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$/")) {
            throw new IllegalArgumentException("Invalid email");
        }

        // Write registration to 'inscription.txt' file
        String line = form.getCourse().getSession() + "\t" + form.getCourse().getCode() + "\t" +
                form.getMatricule() + "\t" + form.getPrenom() + "\t" + form.getNom() + "\t" + form.getEmail();

        try {
            Files.write(Paths.get("inscription.txt"), (line + System.lineSeparator()).getBytes(),
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }}