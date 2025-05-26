package org.university.zoomanagementsystem.gate.repository;

import org.university.zoomanagementsystem.gate.Gate;

import java.util.List;

public interface GateRepository {
    int addGate(Gate gate);

    Gate getGateById(int id);

    Gate getGateByName(String name);

    void updateGateById(Gate gate, int id);

    void deleteGateById(int id);

    List<Gate> getGatesWithPagination(int pageNumber, int limit);

    int getGatesRowsCount();
}
