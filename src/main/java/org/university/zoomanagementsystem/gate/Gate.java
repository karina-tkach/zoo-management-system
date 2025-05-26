package org.university.zoomanagementsystem.gate;

import java.util.Objects;

public class Gate {
    private int id;
    private String name;
    private String location;

    public Gate() {
        this.id = -1;
        this.name = null;
        this.location = null;
    }

    public Gate(int id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gate gate = (Gate) o;
        return id == gate.id && Objects.equals(name, gate.name) && Objects.equals(location, gate.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location);
    }

    @Override
    public String toString() {
        return "Gate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
