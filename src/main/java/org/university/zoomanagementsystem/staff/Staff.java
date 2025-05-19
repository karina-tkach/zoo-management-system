package org.university.zoomanagementsystem.staff;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Staff {
    private int id;
    private int userId;
    private LocalDate hireDate;
    private Long salary;
    private String workingDays;
    private LocalTime shiftStart;
    private LocalTime shiftEnd;

    public Staff() {
        this.id = -1;
        this.userId = -1;
        this.hireDate = null;
        this.salary = 0L;
        this.workingDays = null;
        this.shiftStart = null;
        this.shiftEnd = null;
    }

    public Staff(int id, int userId, LocalDate hireDate, Long salary, String workingDays, LocalTime shiftStart, LocalTime shiftEnd) {
        this.id = id;
        this.userId = userId;
        this.hireDate = hireDate;
        this.salary = salary;
        this.workingDays = workingDays;
        this.shiftStart = shiftStart;
        this.shiftEnd = shiftEnd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public String getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(String workingDays) {
        this.workingDays = workingDays;
    }

    public LocalTime getShiftStart() {
        return shiftStart;
    }

    public void setShiftStart(LocalTime shiftStart) {
        this.shiftStart = shiftStart;
    }

    public LocalTime getShiftEnd() {
        return shiftEnd;
    }

    public void setShiftEnd(LocalTime shiftEnd) {
        this.shiftEnd = shiftEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Staff staff = (Staff) o;
        return id == staff.id && userId == staff.userId && Objects.equals(hireDate, staff.hireDate) && Objects.equals(salary, staff.salary) && Objects.equals(workingDays, staff.workingDays) && Objects.equals(shiftStart, staff.shiftStart) && Objects.equals(shiftEnd, staff.shiftEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, hireDate, salary, workingDays, shiftStart, shiftEnd);
    }

    @Override
    public String toString() {
        return "Staff{" +
                "id=" + id +
                ", userId=" + userId +
                ", hireDate=" + hireDate +
                ", salary=" + salary +
                ", workingDays='" + workingDays + '\'' +
                ", shiftStart=" + shiftStart +
                ", shiftEnd=" + shiftEnd +
                '}';
    }
}

