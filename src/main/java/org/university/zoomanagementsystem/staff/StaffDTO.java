package org.university.zoomanagementsystem.staff;

import org.university.zoomanagementsystem.user.Role;
import org.university.zoomanagementsystem.user.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class StaffDTO {
    private int id; //staff id
    private int userId;
    private String name;
    private String email;
    private String password;
    private Role role;

    private LocalDate hireDate;
    private Long salary;
    private String workingDays;
    private LocalTime shiftStart;
    private LocalTime shiftEnd;

    public StaffDTO() {
        this.id = -1;
        this.userId = -1;
        this.name = null;
        this.email = null;
        this.password = null;
        this.role = null;
        this.hireDate = null;
        this.salary = 0L;
        this.workingDays = null;
        this.shiftStart = null;
        this.shiftEnd = null;
    }

    public StaffDTO(int id, int userId, String name, String email, Role role, LocalDate hireDate, Long salary, String workingDays, LocalTime shiftStart, LocalTime shiftEnd) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = null;
        this.role = role;
        this.hireDate = hireDate;
        this.salary = salary;
        this.workingDays = workingDays;
        this.shiftStart = shiftStart;
        this.shiftEnd = shiftEnd;
    }

    public StaffDTO(Staff staff, User user) {
        this.id = staff.getId();
        this.userId = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = null;
        this.role = user.getRole();
        this.hireDate = staff.getHireDate();
        this.salary = staff.getSalary();
        this.workingDays = staff.getWorkingDays();
        this.shiftStart = staff.getShiftStart();
        this.shiftEnd = staff.getShiftEnd();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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
        StaffDTO staffDTO = (StaffDTO) o;
        return id == staffDTO.id && userId == staffDTO.userId && Objects.equals(name, staffDTO.name) && Objects.equals(email, staffDTO.email) && Objects.equals(password, staffDTO.password) && role == staffDTO.role && Objects.equals(hireDate, staffDTO.hireDate) && Objects.equals(salary, staffDTO.salary) && Objects.equals(workingDays, staffDTO.workingDays) && Objects.equals(shiftStart, staffDTO.shiftStart) && Objects.equals(shiftEnd, staffDTO.shiftEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, name, email, password, role, hireDate, salary, workingDays, shiftStart, shiftEnd);
    }

    @Override
    public String toString() {
        return "StaffDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", hireDate=" + hireDate +
                ", salary=" + salary +
                ", workingDays='" + workingDays + '\'' +
                ", shiftStart=" + shiftStart +
                ", shiftEnd=" + shiftEnd +
                '}';
    }
}
