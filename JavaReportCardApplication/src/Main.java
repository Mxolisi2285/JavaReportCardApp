
// StudentReportCardSystem.java
import java.io.*;
import java.util.*;

public class Main {
    private static List<Student> students = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadFromFile();
        int choice;

        do {
            System.out.println("\n===== Student Report Card Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Edit Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Search Student");
            System.out.println("6. Show Class Statistics");
            System.out.println("7. Save to CSV");
            System.out.println("8. Exit");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> viewStudents();
                case 3 -> editStudent();
                case 4 -> deleteStudent();
                case 5 -> searchStudent();
                case 6 -> showClassStats();
                case 7 -> saveToCSV();
                case 8 -> saveToFile();
                default -> System.out.println("Invalid choice!");
            }

        } while (choice != 8);
    }

    private static void addStudent() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine();

        int[] marks = new int[5];
        for (int i = 0; i < 5; i++) {
            while (true) {
                try {
                    System.out.print("Enter mark for subject " + (i + 1) + ": ");
                    marks[i] = Integer.parseInt(scanner.nextLine());
                    if (marks[i] < 0 || marks[i] > 100)
                        throw new IllegalArgumentException("Marks must be between 0 and 100.");
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid input. " + e.getMessage());
                }
            }
        }

        students.add(new Student(id, name, marks));
        System.out.println("Student added successfully!");
    }

    private static void viewStudents() {
        if (students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }
        students.sort(Comparator.comparing(Student::getName));
        for (Student s : students) {
            System.out.println(s);
        }
    }

    private static void editStudent() {
        System.out.print("Enter Student ID to edit: ");
        String id = scanner.nextLine();

        for (Student s : students) {
            if (s.getId().equalsIgnoreCase(id)) {
                System.out.print("Enter new name (leave blank to keep current): ");
                String name = scanner.nextLine();
                if (!name.isBlank()) s.setName(name);

                int[] newMarks = new int[5];
                for (int i = 0; i < 5; i++) {
                    System.out.print("Enter new mark for subject " + (i + 1) + " (current: " + s.getMarks()[i] + "): ");
                    newMarks[i] = Integer.parseInt(scanner.nextLine());
                }
                s.setMarks(newMarks);
                System.out.println("Student updated.");
                return;
            }
        }
        System.out.println("Student ID not found.");
    }

    private static void deleteStudent() {
        System.out.print("Enter Student ID to delete: ");
        String id = scanner.nextLine();
        boolean removed = students.removeIf(s -> s.getId().equalsIgnoreCase(id));
        System.out.println(removed ? "Student deleted." : "Student ID not found.");
    }

    private static void searchStudent() {
        System.out.print("Enter Student ID or Name to search: ");
        String query = scanner.nextLine().toLowerCase();
        boolean found = false;
        for (Student s : students) {
            if (s.getId().toLowerCase().contains(query) || s.getName().toLowerCase().contains(query)) {
                System.out.println(s);
                found = true;
            }
        }
        if (!found) System.out.println("No matching records found.");
    }

    private static void showClassStats() {
        if (students.isEmpty()) {
            System.out.println("No data to show statistics.");
            return;
        }
        double totalAvg = students.stream().mapToDouble(Student::getAverage).average().orElse(0);
        int high = students.stream().mapToInt(Student::getTotal).max().orElse(0);
        int low = students.stream().mapToInt(Student::getTotal).min().orElse(0);
        long gradeA = students.stream().filter(s -> s.getGrade().equals("A")).count();

        System.out.printf("\nClass Average: %.2f\nHighest Total: %d\nLowest Total: %d\nGrade A Students: %d\n",
                totalAvg, high, low, gradeA);
    }

    private static void saveToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("report_cards.csv"))) {
            writer.println("ID,Name,Mark1,Mark2,Mark3,Mark4,Mark5,Total,Average,Grade");
            for (Student s : students) {
                writer.println(s.toCSV());
            }
            System.out.println("Data saved to report_cards.csv");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("students.dat"))) {
            out.writeObject(new ArrayList<>(students));
            System.out.println("Data saved.");
        } catch (IOException e) {
            System.out.println("Save error: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("students.dat"))) {
            students = (List<Student>) in.readObject();
        } catch (Exception e) {
            students = new ArrayList<>();
        }
    }
}
