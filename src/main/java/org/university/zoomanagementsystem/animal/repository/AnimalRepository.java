package org.university.zoomanagementsystem.animal.repository;

import org.university.zoomanagementsystem.animal.Animal;
import org.university.zoomanagementsystem.animal.enums.AnimalGroup;
import org.university.zoomanagementsystem.animal.enums.HealthStatus;
import org.university.zoomanagementsystem.enclosure.HabitatType;

import java.util.List;

public interface AnimalRepository {

    int addAnimal(Animal animal);

    Animal getAnimalById(int id);

    void updateAnimalById(Animal animal, int id);

    void deleteAnimalById(int id);

    List<Animal> getAnimalsWithPagination(int pageNumber, int limit);

    int getAnimalsRowsCount();

    List<Animal> getAnimalsByEnclosureWithPagination(int enclosureId, int pageNumber, int limit);

    int getAnimalsByEnclosureRowsCount(int enclosureId);

    List<Animal> getAnimalsByGroupWithPagination(AnimalGroup animalGroup, int pageNumber, int limit);

    int getAnimalsByGroupRowsCount(AnimalGroup animalGroup);

    List<Animal> getAnimalsByHabitatWithPagination(HabitatType habitatType, int pageNumber, int limit);

    int getAnimalsByHabitatRowsCount(HabitatType habitatType);

    List<Animal> getAnimalsByHealthStatusWithPagination(HealthStatus healthStatus, int pageNumber, int limit);

    int getAnimalsByByHealthStatusRowsCount(HealthStatus healthStatus);
}
