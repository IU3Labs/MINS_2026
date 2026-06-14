package ru.iu3.service;

import java.util.List;

import ru.iu3.entity.Pass;
import ru.iu3.exceptions.NotFoundExeption;
import ru.iu3.repository.interfaces.Repository;
import ru.iu3.service.interfaces.PassService;
import ru.iu3.validation.PassValidator;

public class PassServiceImpl implements PassService {
    private  Repository<Pass, Integer> passRepository;
    private  PassValidator passValidator;

    public PassServiceImpl(Repository<Pass, Integer> passRepository, PassValidator passValidator) {
        this.passRepository = passRepository;
        this.passValidator = passValidator;
    }

    @Override
    public void issuePass(int id, String holderName) {
        passValidator.validateForIssue(id, holderName);
        passRepository.add(new Pass(id, holderName));
    }

    @Override
    public void deactivatePass(int id) {
        Pass pass = getPassById(id);
        pass.deactivate();
    }

    @Override
    public List<Pass> getAllPasses() {
        return passRepository.getAll();
    }

    @Override
    public Pass getPassById(int id) {
        Pass pass = passRepository.getById(id);
        if (pass == null) {
            throw new NotFoundExeption("Пропуск с таким ID не найден.");
        }
        return pass;
    }
}
