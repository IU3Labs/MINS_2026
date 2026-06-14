package ru.iu3.repository;

import java.util.ArrayList;
import java.util.List;

import ru.iu3.entity.Pass;
import ru.iu3.repository.interfaces.Repository;

public class LPassRepositoryImpl implements Repository<Pass, Integer> {
    private List<Pass> passes = new ArrayList<Pass>();

    @Override
    public void add(Pass pass) {
        passes.add(pass);
    }

    @Override
    public Pass getById(int id) {
        for (Pass pass : passes) {
            if (pass.getId() == id) {
                return pass;
            }
        }
        return null;
    }

    @Override
    public List<Pass> getAll() {
        return passes;
    }

    @Override
    public void update(Pass pass) {
        for (int i = 0; i < passes.size(); i++) {
            if (passes.get(i).getId() == pass.getId()) {
                passes.set(i, pass);
                return;
            }
        }
    }

}
