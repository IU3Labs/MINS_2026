package ru.iu3.service.interfaces;

import java.util.List;

import ru.iu3.entity.Pass;

public interface PassService {

    void issuePass(int id, String holderName);

    void deactivatePass(int id);

    List<Pass> getAllPasses();

    Pass getPassById(int id);
}
