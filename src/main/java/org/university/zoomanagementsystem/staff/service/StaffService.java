package org.university.zoomanagementsystem.staff.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.university.zoomanagementsystem.exception.not_found.StaffNotFoundException;
import org.university.zoomanagementsystem.exception.validation.StaffValidationException;
import org.university.zoomanagementsystem.exception.validation.UserValidationException;
import org.university.zoomanagementsystem.exception.validation.ValidationException;
import org.university.zoomanagementsystem.staff.Staff;
import org.university.zoomanagementsystem.staff.StaffDTO;
import org.university.zoomanagementsystem.staff.StaffValidator;
import org.university.zoomanagementsystem.staff.repository.StaffRepository;
import org.university.zoomanagementsystem.user.Role;
import org.university.zoomanagementsystem.user.User;
import org.university.zoomanagementsystem.user.service.UserService;

import java.util.List;

@Service
public class StaffService {
    private final StaffValidator staffValidator;
    private final StaffRepository staffRepository;
    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(StaffService.class);

    public StaffService(StaffValidator staffValidator, StaffRepository staffRepository, UserService userService) {
        this.staffValidator = staffValidator;
        this.staffRepository = staffRepository;
        this.userService = userService;
    }

    public StaffDTO addStaff(StaffDTO dto) {
        try {
            logger.info("Try to add staff");
            if(dto.getRole().equals(Role.VISITOR)) {
                throw new StaffValidationException("Staff can't have role 'VISITOR'");
            }
            User user = new User();
            user.setEmail(dto.getEmail());
            user.setName(dto.getName());
            user.setPassword(dto.getPassword());
            user.setRole(dto.getRole());

            Staff staff = new Staff();
            staff.setUserId(0);
            staff.setHireDate(dto.getHireDate());
            staff.setSalary(dto.getSalary());
            staff.setWorkingDays(dto.getWorkingDays());
            staff.setShiftStart(dto.getShiftStart());
            staff.setShiftEnd(dto.getShiftEnd());

            staffValidator.validate(staff);
            User createdUser = userService.addUser(user);
            staff.setUserId(createdUser.getId());

            int id = staffRepository.addStaff(staff);
            if (id == -1) {
                throw new StaffValidationException("Unable to retrieve the generated key");
            }

            staff.setId(id);
            logger.info("Staff was added:\n{}", staff);
            return getStaffById(id);
        } catch (StaffValidationException | UserValidationException | DataAccessException exception) {
            logger.warn("Staff wasn't added: {}\n{}", dto, exception.getMessage());
            throw exception;
        }
    }

    public StaffDTO getStaffById(int id) {
        try {
            logger.info("Try to get staff by id");
            Staff staff = staffRepository.getStaffById(id);
            if(staff == null) {
                throw new StaffNotFoundException(String.format("Staff with id %d was not found", id));
            }
            User user = userService.getUserById(staff.getUserId());
            logger.info("Staff was fetched successfully");
            return new StaffDTO(staff, user);
        } catch (StaffNotFoundException | DataAccessException exception) {
            logger.warn("Staff wasn't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public StaffDTO updateStaffById(StaffDTO dto, int id) {
        try {
            StaffDTO staffDTOToUpdate = getStaffById(id);
            logger.info("Try to update staff");
            Staff staff = new Staff(-1,-1, dto.getHireDate(),
                    dto.getSalary(), dto.getWorkingDays(), dto.getShiftStart(), dto.getShiftEnd());
            Staff staffToUpdate = new Staff(-1,staffDTOToUpdate.getUserId(), staffDTOToUpdate.getHireDate(),
                    staffDTOToUpdate.getSalary(), staffDTOToUpdate.getWorkingDays(),
                    staffDTOToUpdate.getShiftStart(), staffDTOToUpdate.getShiftEnd());

            staffValidator.validateStaffForUpdate(staffToUpdate, staff);

            User user = new User(-1, dto.getName(), dto.getPassword(), dto.getEmail(), dto.getRole());
            userService.updateUserWithoutPasswordChangeById(user, staffDTOToUpdate.getUserId());

            staffRepository.updateStaffById(staff, id);

            logger.info("Staff was updated:\n{}", dto);
            return getStaffById(id);
        } catch (StaffNotFoundException |StaffValidationException |
                 UserValidationException | DataAccessException exception) {
            logger.warn("Staff wasn't updated: {}\n{}", id, exception.getMessage());
            throw exception;
        }
    }

    public boolean deleteStaffById(int id) {
        try {
            Staff staff = staffRepository.getStaffById(id);
            logger.info("Try to delete staff by id");
            userService.deleteUserById(staff.getUserId());
            logger.info("Staff was deleted:\n{}", staff);
            return true;
        } catch (StaffValidationException | UserValidationException | DataAccessException exception) {
            logger.warn("Staff wasn't deleted: {}\n{}", id, exception.getMessage());
            throw exception;
        }
    }

    public List<StaffDTO> getStaffWithPagination(int pageNumber, int limit) {
        try {
            if (pageNumber < 0 || limit < 0) {
                throw new ValidationException("Page number and limit must be greater than 0");
            }
            logger.info("Try to get staff with pagination");
            List<StaffDTO> staff = staffRepository.getStaffWithPagination(pageNumber, limit);
            logger.info("Staff were fetched with pagination successfully");
            return staff;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Staff weren't fetched with pagination\n{}", exception.getMessage());
            throw exception;
        }
    }

    public int getStaffRowsCount() {
        try {
            logger.info("Try to get staff rows count");
            int count = staffRepository.getStaffRowsCount();
            logger.info("Staff rows count were fetched successfully");
            return count;
        } catch (DataAccessException exception) {
            logger.warn("Staff rows count weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }
}

