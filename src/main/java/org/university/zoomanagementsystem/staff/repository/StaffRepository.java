package org.university.zoomanagementsystem.staff.repository;

import org.university.zoomanagementsystem.staff.Staff;
import org.university.zoomanagementsystem.staff.StaffDTO;

import java.util.List;

public interface StaffRepository {
    int addStaff(Staff staff);

    Staff getStaffById(int id);

    void updateStaffById(Staff staff, int id);

    List<StaffDTO> getStaffWithPagination(int pageNumber, int limit);

    int getStaffRowsCount();
}
