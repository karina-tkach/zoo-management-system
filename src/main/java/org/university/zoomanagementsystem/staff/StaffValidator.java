package org.university.zoomanagementsystem.staff;

import org.springframework.stereotype.Component;
import org.university.zoomanagementsystem.exception.validation.StaffValidationException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class StaffValidator {
    public void validate(Staff staff) {
        validateStaffNotNull(staff);
        validateUserId(staff.getUserId());
        validateHireDate(staff.getHireDate());
        validateSalary(staff.getSalary());
        validateWorkingDays(staff.getWorkingDays());
        validateShiftTimes(staff.getShiftStart(), staff.getShiftEnd());
    }

    public void validateStaffForUpdate(Staff staffToUpdate, Staff updatedStaff) {
        updatedStaff.setUserId(staffToUpdate.getUserId());

        if (updatedStaff.getHireDate() == null) {
            updatedStaff.setHireDate(staffToUpdate.getHireDate());
        }
        if (updatedStaff.getSalary() == null) {
            updatedStaff.setSalary(staffToUpdate.getSalary());
        }
        if (updatedStaff.getWorkingDays() == null) {
            updatedStaff.setWorkingDays(staffToUpdate.getWorkingDays());
        }
        if (updatedStaff.getShiftStart() == null) {
            updatedStaff.setShiftStart(staffToUpdate.getShiftStart());
        }
        if (updatedStaff.getShiftEnd() == null) {
            updatedStaff.setShiftEnd(staffToUpdate.getShiftEnd());
        }

        validate(updatedStaff);
    }

    private void validateStaffNotNull(Staff staff) {
        if (staff == null) {
            throw new StaffValidationException("Staff was null");
        }
    }

    private void validateUserId(int userId) {
        if (userId < 0) {
            throw new StaffValidationException("Staff userId must be non-negative");
        }
    }

    private void validateHireDate(LocalDate hireDate) {
        if (hireDate == null) {
            throw new StaffValidationException("Staff hire date was null");
        }
        if (hireDate.isAfter(LocalDate.now())) {
            throw new StaffValidationException("Staff hire date cannot be in the future");
        }
    }

    private void validateSalary(Long salary) {
        if (salary == null) {
            throw new StaffValidationException("Staff salary was null");
        }
        if (salary < 0) {
            throw new StaffValidationException("Staff salary must be non-negative");
        }
    }

    private void validateWorkingDays(String workingDays) {
        if (workingDays == null) {
            throw new StaffValidationException("Staff working days were null");
        }
        if (workingDays.isBlank()) {
            throw new StaffValidationException("Staff working days were empty");
        }
        if(workingDays.length() > 100) {
            throw new StaffValidationException("Working days length must be less than or equal to 100");
        }
        Pattern pattern = Pattern.compile("^(Mon|Tue|Wed|Thu|Fri|Sat|Sun)(,(Mon|Tue|Wed|Thu|Fri|Sat|Sun))*$");
        if (!pattern.matcher(workingDays).matches()) {
            throw new StaffValidationException("Staff working days format is invalid (must be Mon, Tue etc.)");
        }
        else {
            String[] days = workingDays.split(",");
            Set<String> uniqueDays = new HashSet<>(Arrays.asList(days));

            if (uniqueDays.size() != days.length) {
                throw new StaffValidationException("Duplicate weekdays found in working days");
            }
        }
    }

    private void validateShiftTimes(LocalTime start, LocalTime end) {
        if (start == null || end == null) {
            throw new StaffValidationException("Shift start or end time was null");
        }
        if (start.equals(end) || end.isBefore(start)) {
            throw new StaffValidationException("Shift end time must be after start time");
        }
    }
}
