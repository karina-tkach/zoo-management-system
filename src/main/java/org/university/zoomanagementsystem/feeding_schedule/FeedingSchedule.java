package org.university.zoomanagementsystem.feeding_schedule;

import org.university.zoomanagementsystem.animal.Animal;
import org.university.zoomanagementsystem.user.User;

import java.time.LocalTime;
import java.util.Objects;

public class FeedingSchedule {
    private int id;
    private Animal animal;
    private User caretaker;
    private String foodType;
    private LocalTime time;
    private int portionSizeGrams;
    private boolean isDoneToday;

    public FeedingSchedule() {
        this.id = -1;
        this.animal = null;
        this.caretaker = null;
        this.foodType = null;
        this.time = null;
        this.portionSizeGrams = 0;
        this.isDoneToday = false;
    }

    public FeedingSchedule(int id, Animal animal, User caretaker, String foodType, LocalTime time, int portionSizeGrams, boolean isDoneToday) {
        this.id = id;
        this.animal = animal;
        this.caretaker = caretaker;
        this.foodType = foodType;
        this.time = time;
        this.portionSizeGrams = portionSizeGrams;
        this.isDoneToday = isDoneToday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public User getCaretaker() {
        return caretaker;
    }

    public void setCaretaker(User caretaker) {
        this.caretaker = caretaker;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public int getPortionSizeGrams() {
        return portionSizeGrams;
    }

    public void setPortionSizeGrams(int portionSizeGrams) {
        this.portionSizeGrams = portionSizeGrams;
    }

    public boolean isDoneToday() {
        return isDoneToday;
    }

    public void setDoneToday(boolean doneToday) {
        isDoneToday = doneToday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedingSchedule that = (FeedingSchedule) o;
        return id == that.id && portionSizeGrams == that.portionSizeGrams && isDoneToday == that.isDoneToday && Objects.equals(animal, that.animal) && Objects.equals(caretaker, that.caretaker) && Objects.equals(foodType, that.foodType) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, animal, caretaker, foodType, time, portionSizeGrams, isDoneToday);
    }

    @Override
    public String toString() {
        return "FeedingSchedule{" +
                "id=" + id +
                ", animal=" + animal +
                ", caretaker=" + caretaker +
                ", foodType='" + foodType + '\'' +
                ", time=" + time +
                ", portionSizeGrams=" + portionSizeGrams +
                ", isDoneToday=" + isDoneToday +
                '}';
    }
}
