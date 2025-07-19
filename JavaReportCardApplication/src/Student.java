// Student.java
public class Student {
    private String id;
    private String name;
    private int[] marks;

    public Student(String id, String name, int[] marks) {
        this.id = id;
        this.name = name;
        this.marks = marks;
    }

    public int getTotal() {
        int total = 0;
        for (int mark : marks) {
            total += mark;
        }
        return total;
    }

    public double getAverage() {
        return getTotal() / (double) marks.length;
    }

    public String getGrade() {
        double avg = getAverage();
        if (avg >= 75) return "A";
        else if (avg >= 60) return "B";
        else if (avg >= 50) return "C";
        else if (avg >= 40) return "D";
        else return "F";
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int[] getMarks() { return marks; }
    public void setMarks(int[] marks) { this.marks = marks; }

    public String toCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(",").append(name);
        for (int mark : marks) {
            sb.append(",").append(mark);
        }
        sb.append(",").append(getTotal())
                .append(",").append(String.format("%.2f", getAverage()))
                .append(",").append(getGrade());
        return sb.toString();
    }

    public String toString() {
        return String.format("ID: %s, Name: %s, Total: %d, Average: %.2f, Grade: %s",
                id, name, getTotal(), getAverage(), getGrade());
    }
}