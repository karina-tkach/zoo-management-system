package org.university.zoomanagementsystem.enclosure;

import java.util.Objects;

public class Enclosure {
    private int id;
    private String name;
    private String location;
    private HabitatType environmentType;
    private int areaM2;

    public Enclosure() {
        this. id = -1;
        this.name = null;
        this.location = null;
        this.environmentType = null;
        this.areaM2 = -1;
    }

    public Enclosure(int id, String name, String location, HabitatType environmentType, int areaM2) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.environmentType = environmentType;
        this.areaM2 = areaM2;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public HabitatType getEnvironmentType() {
        return environmentType;
    }

    public void setEnvironmentType(HabitatType environmentType) {
        this.environmentType = environmentType;
    }

    public int getAreaM2() {
        return areaM2;
    }

    public void setAreaM2(int areaM2) {
        this.areaM2 = areaM2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enclosure enclosure = (Enclosure) o;
        return id == enclosure.id && areaM2 == enclosure.areaM2 && Objects.equals(name, enclosure.name) && Objects.equals(location, enclosure.location) && environmentType == enclosure.environmentType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location, environmentType, areaM2);
    }

    @Override
    public String toString() {
        return "Enclosure{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", environmentType=" + environmentType +
                ", areaM2=" + areaM2 +
                '}';
    }
}
