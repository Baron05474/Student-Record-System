import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private static Database instance;
    private final Map<String, Student> students;
    private final String adminUsername = "admin";
    private final String adminPassword = "12345";

    private Database() {
        students = new HashMap<>();
        
        // Sample Student Seed Data
        Student s1 = new Student("Baron26", "pass26", "BARON BHOWMICK", 20, "7742626590");
        s1.addOrUpdateCourse("Computer Organization: ", 8.0);
        s1.addOrUpdateCourse("Theory of Computation: ", 9.0);
        s1.addOrUpdateCourse("Data Structures and Algorithms: ", 8.5);
        s1.addOrUpdateCourse("Discrete Mathematics: ", 9.0);
        s1.addOrUpdateCourse("Programming Methodology: ", 9.0);
        s1.addOrUpdateCourse("IT Workshop: ", 9.0);
        s1.setAttendancePercentage(99.0);

        Student s2 = new Student("2025BTCSE040", "pass40", "Rohan Sharma", 21, "9123456789");
        s2.addOrUpdateCourse("Operating Systems", 3.8);
        s2.addOrUpdateCourse("Software Engineering", 4.0);
        s2.setAttendancePercentage(88.0);

        students.put(s1.getId(), s1);
        students.put(s2.getId(), s2);
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public boolean validateAdmin(String username, String password) {
        return adminUsername.equals(username) && adminPassword.equals(password);
    }

    public Student authenticateStudent(String id, String password) {
        Student s = students.get(id);
        if (s != null && s.getPassword().equals(password)) {
            return s;
        }
        return null;
    }

    public Collection<Student> getAllStudents() {
        return students.values();
    }

    public Student getStudent(String id) {
        return students.get(id);
    }

    public void addStudent(Student student) {
        students.put(student.getId(), student);
    }

    public void removeStudent(String id) {
        students.remove(id);
    }
}