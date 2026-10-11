package com.factorypulse.backend.service;

import com.factorypulse.backend.model.Factory;
import com.factorypulse.backend.repository.FactoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FactoryService {

    private final FactoryRepository factoryRepository;

    public FactoryService(FactoryRepository factoryRepository) {
        this.factoryRepository = factoryRepository;
    }

    public Factory createFactory(Factory factory) {
        return factoryRepository.save(factory);
    }

    public List<Factory> getAllFactories() {
        return factoryRepository.findAll();
    }

    public Optional<Factory> getFactoryById(Long id) {
        return factoryRepository.findById(id);
    }

    public void deleteFactory(Long id) {
        factoryRepository.deleteById(id);
    }
}