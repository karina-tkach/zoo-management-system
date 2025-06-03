package org.university.zoomanagementsystem.feeding_schedule;

import org.springframework.stereotype.Component;
import org.university.zoomanagementsystem.animal.Animal;
import org.university.zoomanagementsystem.exception.validation.FeedingScheduleValidationException;
import org.university.zoomanagementsystem.user.User;

import java.time.LocalTime;

@Component
public class FeedingScheduleValidator {

    public void validate(FeedingSchedule feedingSchedule) {
        validateFeedingScheduleIsNotNull(feedingSchedule);
        validateAnimal(feedingSchedule.getAnimal());
        validateCaretaker(feedingSchedule.getCaretaker());
        validateFoodType(feedingSchedule.getFoodType());
        validateTime(feedingSchedule.getTime());
        validatePortionSizeGrams(feedingSchedule.getPortionSizeGrams());

    }

    public void validateFeedingScheduleForUpdate(FeedingSchedule feedingScheduleToUpdate, FeedingSchedule feedingSchedule) {
        if(feedingSchedule.getAnimal() == null) {
            feedingSchedule.setAnimal(feedingScheduleToUpdate.getAnimal());
        }
        if(feedingSchedule.getCaretaker() == null) {
            feedingSchedule.setCaretaker(feedingScheduleToUpdate.getCaretaker());
        }
        if (feedingSchedule.getFoodType() == null) {
            feedingSchedule.setFoodType(feedingScheduleToUpdate.getFoodType());
        }
        if (feedingSchedule.getTime() == null) {
            feedingSchedule.setTime(feedingScheduleToUpdate.getTime());
        }

        validate(feedingSchedule);
    }

    private void validateFeedingScheduleIsNotNull(FeedingSchedule feedingSchedule) {
        if (feedingSchedule == null) {
            throw new FeedingScheduleValidationException("Feeding schedule was null");
        }
    }

    private void validateAnimal(Animal animal) {
        if (animal == null) {
            throw new FeedingScheduleValidationException("Feeding schedule animal was null");
        }
        else if (animal.getId() < 0) {
            throw new FeedingScheduleValidationException("Feeding schedule animal id was negative");
        }
    }

    private void validateCaretaker(User caretaker) {
        if (caretaker == null) {
            throw new FeedingScheduleValidationException("Feeding schedule caretaker was null");
        }
        else if (caretaker.getId() < 0) {
            throw new FeedingScheduleValidationException("Feeding schedule caretaker id was negative");
        }
    }


    private void validateFoodType(String foodType) {
        if (foodType == null) {
            throw new FeedingScheduleValidationException("Feeding schedule food type was null");
        }
        if (foodType.isBlank()) {
            throw new FeedingScheduleValidationException("Feeding schedule food type was empty");
        }
        if (foodType.length() > 150 || foodType.length() < 2) {
            throw new FeedingScheduleValidationException("Feeding schedule food type had wrong length (must be 2 to 150 characters)");
        }

    }

    private void validateTime(LocalTime time) {
        if(time == null) {
            throw new FeedingScheduleValidationException("Feeding schedule time was null");
        }
    }

    private void validatePortionSizeGrams(int portionSizeGrams) {
        if (portionSizeGrams <= 0) {
            throw new FeedingScheduleValidationException("Feeding schedule portion size must be positive");
        }
    }
}
