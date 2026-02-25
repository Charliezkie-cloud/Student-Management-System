package Models;

import java.io.Serializable;
import java.util.UUID;

public class Student implements Serializable {
    private UUID uuid;
    private String firstName;
    private String lastName;
    private String middleName;
    private String course;
    private String year;

    public Student(String firstName, String lastName, String middleName, String course, String year) {
        this.uuid = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.course = course;
        this.year = year;
    }

    // Getters
    public UUID getUuid() { return this.uuid; }
    public String getFirstName() { return this.firstName; }
    public String getLastName() { return this.lastName; }
    public String getMiddleName() { return this.middleName; }
    public String getCourse() { return this.course; }
    public String getYear() { return this.year; }

    // Setters
    public void setUuid(UUID uuid) { this.uuid = uuid; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    public void setCourse(String course) { this.course = course; }
    public void setYear(String year) { this.year = year; }
}
