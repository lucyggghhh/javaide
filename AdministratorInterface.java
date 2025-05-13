import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// Student class
class Student {
    private String name;
    private String id;
    private Map<Course, Double> enrolledCourses;

    public Student(String name, String id) {
        this.name = name;
        this.id = id;
        this.enrolledCourses = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<Course, Double> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void enrollCourse(Course course) {
        enrolledCourses.put(course, null);
        course.enrollStudent();
    }

    public void assignGrade(Course course, double grade) {
        enrolledCourses.put(course, grade);
    }
}

// Course class
class Course {
    private String courseCode;
    private String name;
    private int maxCapacity;
    private static int totalEnrolledStudents = 0;

    public Course(String courseCode, String name, int maxCapacity) {
        this.courseCode = courseCode;
        this.name = name;
        this.maxCapacity = maxCapacity;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getName() {
        return name;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public static int getTotalEnrolledStudents() {
        return totalEnrolledStudents;
    }

    public void enrollStudent() {
        totalEnrolledStudents++;
    }

    @Override
    public String toString() {
        return "Course{" +
               "courseCode='" + courseCode + '\'' +
               ", name='" + name + '\'' +
               '}';
    }
}

// CourseManagement class
class CourseManagement {
    private static List<Course> courses = new ArrayList<>();
    private static Map<Student, Map<Course, Double>> studentGrades = new HashMap<>();
    private static List<Student> students = new ArrayList<>();

    public static void addCourse(String courseCode, String name, int maxCapacity) {
        courses.add(new Course(courseCode, name, maxCapacity));
        System.out.println("Course " + name + " (" + courseCode + ") added.");
    }

    public static void addStudent(Student student) {
        students.add(student);
        System.out.println("Student " + student.getName() + " with ID " + student.getId() + " added.");
    }

    public static Student findStudent(String studentId) {
        for (Student student : students) {
            if (student.getId().equals(studentId)) {
                return student;
            }
        }
        return null;
    }

    public static Course findCourse(String courseCode) {
        for (Course course : courses) {
            if (course.getCourseCode().equals(courseCode)) {
                return course;
            }
        }
        return null;
    }

    public static void enrollStudent(Student student, Course course) {
        if (course != null && student != null) {
            student.enrollCourse(course);
            if (!studentGrades.containsKey(student)) {
                studentGrades.put(student, new HashMap<>());
            }
            studentGrades.get(student).put(course, null);
            System.out.println("Student " + student.getName() + " enrolled in " + course.getName() + ".");
        } else {
            System.out.println("Invalid student ID or course code.");
        }
    }

    public static void assignGrade(Student student, Course course, double grade) {
        if (student != null && course != null && student.getEnrolledCourses().containsKey(course)) {
            student.assignGrade(course, grade);
            studentGrades.get(student).put(course, grade);
            System.out.println("Grade " + grade + " assigned to student " + student.getName() + " for course " + course.getName() + ".");
        } else {
            System.out.println("Invalid student ID or course code, or student not enrolled in the course.");
        }
    }

    public static double calculateOverallGrade(Student student) {
        double totalGrade = 0;
        int courseCount = 0;
        for (Map.Entry<Course, Double> entry : student.getEnrolledCourses().entrySet()) {
            if (entry.getValue() != null) {
                totalGrade += entry.getValue();
                courseCount++;
            }
        }
        return courseCount > 0 ? totalGrade / courseCount : 0;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Add Course");
            System.out.println("2. Add Student");
            System.out.println("3. Enroll Student in Course(s)");
            System.out.println("4. Assign Grade to Student for a Course");
            System.out.println("5. Calculate Overall Grade for a Student");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over

            switch (option) {
                case 1:
                    System.out.print("Enter course code: ");
                    String courseCode = scanner.nextLine();
                    System.out.print("Enter course name: ");
                    String courseName = scanner.nextLine();
                    System.out.print("Enter max capacity: ");
                    int maxCapacity = scanner.nextInt();
                    scanner.nextLine(); // Consume newline left-over
                    CourseManagement.addCourse(courseCode, courseName, maxCapacity);
                    break;
                case 2:
                    System.out.print("Enter student name: ");
                    String studentName = scanner.nextLine();
                    System.out.print("Enter student ID: ");
                    String studentId = scanner.nextLine();
                    Student student = new Student(studentName, studentId);
                    CourseManagement.addStudent(student);
                    break;
                case 3:
                    System.out.print("Enter student ID to enroll: ");
                    String studentIdToEnroll = scanner.nextLine();
                    Student studentToEnroll = CourseManagement.findStudent(studentIdToEnroll);
                    if (studentToEnroll != null) {
                        System.out.print("Enter course code(s) to enroll (comma-separated): ");
                        String courseCodesToEnroll = scanner.nextLine();
                        String[] courseCodes = courseCodesToEnroll.split(",");
                        for (String code : courseCodes) {
                            Course courseToEnroll = CourseManagement.findCourse(code.trim());
                            CourseManagement.enrollStudent(studentToEnroll, courseToEnroll);
                        }
                    } else {
                        System.out.println("Student with ID " + studentIdToEnroll + " not found.");
                    }
                    break;
                case 4:
                    System.out.print("Enter student ID to assign grade: ");
                    String studentIdToAssign = scanner.nextLine();
                    Student studentToGrade = CourseManagement.findStudent(studentIdToAssign);
                    if (studentToGrade != null) {
                        System.out.print("Enter course code to assign grade: ");
                        String courseCodeToGrade = scanner.nextLine();
                        Course courseToGrade = CourseManagement.findCourse(courseCodeToGrade);
                        if (courseToGrade != null) {
                            System.out.print("Enter grade for " + courseToGrade.getName() + ": ");
                            double grade = scanner.nextDouble();
                            scanner.nextLine(); // Consume newline left-over
                            CourseManagement.assignGrade(studentToGrade, courseToGrade,
