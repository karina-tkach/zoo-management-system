package org.university.zoomanagementsystem.animal;

import org.university.zoomanagementsystem.animal.enums.AnimalGender;
import org.university.zoomanagementsystem.animal.enums.AnimalGroup;
import org.university.zoomanagementsystem.animal.enums.HealthStatus;
import org.university.zoomanagementsystem.enclosure.Enclosure;
import org.university.zoomanagementsystem.enclosure.HabitatType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Animal {
    private int id;
    private String name;
    private String species;
    private AnimalGroup animalGroup;
    private HabitatType habitatType;
    private AnimalGender gender;
    private LocalDate birthDate;
    private Enclosure enclosure;
    private HealthStatus healthStatus;
    private String image;
    private LocalDateTime lastCheckedUpAt;
    private LocalDateTime lastFedUpAt;

    public Animal() {
        this.id = -1;
        this.name = null;
        this.species = null;
        this.animalGroup = null;
        this.habitatType = null;
        this.gender = null;
        this.birthDate = null;
        this.enclosure = null;
        this.healthStatus = null;
        this.image = null;
        this.lastCheckedUpAt = null;
        this.lastFedUpAt = null;
    }

    public Animal(int id, String name, String species, AnimalGroup animalGroup, HabitatType habitatType, AnimalGender gender, LocalDate birthDate, Enclosure enclosure, HealthStatus healthStatus, String image, LocalDateTime lastCheckedUpAt, LocalDateTime lastFedUpAt) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.animalGroup = animalGroup;
        this.habitatType = habitatType;
        this.gender = gender;
        this.birthDate = birthDate;
        this.enclosure = enclosure;
        this.healthStatus = healthStatus;
        this.image = image;
        this.lastCheckedUpAt = lastCheckedUpAt;
        this.lastFedUpAt = lastFedUpAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public AnimalGroup getAnimalGroup() {
        return animalGroup;
    }

    public void setAnimalGroup(AnimalGroup animalGroup) {
        this.animalGroup = animalGroup;
    }

    public HabitatType getHabitatType() {
        return habitatType;
    }

    public void setHabitatType(HabitatType habitatType) {
        this.habitatType = habitatType;
    }

    public AnimalGender getGender() {
        return gender;
    }

    public void setGender(AnimalGender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Enclosure getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(Enclosure enclosure) {
        this.enclosure = enclosure;
    }

    public HealthStatus getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(HealthStatus healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDateTime getLastCheckedUpAt() {
        return lastCheckedUpAt;
    }

    public void setLastCheckedUpAt(LocalDateTime lastCheckedUpAt) {
        this.lastCheckedUpAt = lastCheckedUpAt;
    }

    public LocalDateTime getLastFedUpAt() {
        return lastFedUpAt;
    }

    public void setLastFedUpAt(LocalDateTime lastFedUpAt) {
        this.lastFedUpAt = lastFedUpAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return id == animal.id && Objects.equals(name, animal.name) && Objects.equals(species, animal.species) && animalGroup == animal.animalGroup && habitatType == animal.habitatType && gender == animal.gender && Objects.equals(birthDate, animal.birthDate) && Objects.equals(enclosure, animal.enclosure) && healthStatus == animal.healthStatus && Objects.equals(image, animal.image) && Objects.equals(lastCheckedUpAt, animal.lastCheckedUpAt) && Objects.equals(lastFedUpAt, animal.lastFedUpAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, species, animalGroup, habitatType, gender, birthDate, enclosure, healthStatus, image, lastCheckedUpAt, lastFedUpAt);
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", species='" + species + '\'' +
                ", animalGroup=" + animalGroup +
                ", habitatType=" + habitatType +
                ", gender=" + gender +
                ", birthDate=" + birthDate +
                ", enclosure=" + enclosure +
                ", healthStatus=" + healthStatus +
                ", image='" + image + '\'' +
                ", lastCheckedUpAt=" + lastCheckedUpAt +
                ", lastFedUpAt=" + lastFedUpAt +
                '}';
    }
}
