import java.util.HashMap;
import java.util.Map;

public class Student {
    private final String id;
    private final String password;
    private String name;
    private int age;
    private String mobileNumber;
    private double attendancePercentage;
    // Map of Course Name Letter Grade or Numerical Grade (10.0 scale)
    private final Map<String, Double> courseGrades;

    public Student(String id, String password, String name, int age, String mobileNumber) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.age = age;
        this.mobileNumber = mobileNumber;
        this.attendancePercentage = 100.0;
        this.courseGrades = new HashMap<>();
    }

    // Calculation of Overall GPA
    public double calculateCGPA() {
        if (courseGrades.isEmpty()) return 0.0;
        double total = 0.0;
        for (double grade : courseGrades.values()) {
            total += grade;
        }
        return total / courseGrades.size();
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    public double getAttendancePercentage() { return attendancePercentage; }
    public void setAttendancePercentage(double attendancePercentage) { this.attendancePercentage = attendancePercentage; }
    public Map<String, Double> getCourseGrades() { return courseGrades; }

    public void addOrUpdateCourse(String courseName, double gradePoint) {
        courseGrades.put(courseName, gradePoint);
    }

    public void removeCourse(String courseName) {
        courseGrades.remove(courseName);
    }
}