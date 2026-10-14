package com.factorypulse.backend.service;

import com.factorypulse.backend.model.Factory;
import com.factorypulse.backend.repository.FactoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FactoryService {

    private final FactoryRepository factoryRepository;
    private final AccessService accessService;

    public FactoryService(FactoryRepository factoryRepository,
                          AccessService accessService) {
        this.factoryRepository = factoryRepository;
        this.accessService = accessService;
    }

    public Factory createFactory(Factory factory) {
        factory.setId(null);
        factory.setOwner(accessService.currentUser());
        return factoryRepository.save(factory);
    }

    public List<Factory> getAllFactories() {
        String email = accessService.ownerFilterEmail();
        return email == null
                ? factoryRepository.findAll()
                : factoryRepository.findByOwnerEmail(email);
    }

    public Optional<Factory> getFactoryById(Long id) {
        return factoryRepository.findById(id)
                .filter(f -> accessService.canAccessFactory(f));
    }

    public void deleteFactory(Long id) {
        factoryRepository.findById(id).ifPresent(factory -> {
            accessService.require(accessService.canAccessFactory(factory));
            factoryRepository.delete(factory);
        });
    }
}
