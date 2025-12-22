package eind.trmsmo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eind.trmsmo.entity.ServiceEntity;
import eind.trmsmo.repository.ServiceEntityRepository;
import eind.trmsmo.service.ServiceEntityService;

@Service
@Transactional
public class ServiceEntityServiceImpl implements ServiceEntityService {

    @Autowired
    private ServiceEntityRepository serviceEntityRepository;

    @Override
    public List<ServiceEntity> findAll() {
        return serviceEntityRepository.findAll();
    }

    @Override
    public Optional<ServiceEntity> findById(Long id) {
        return serviceEntityRepository.findById(id);
    }

    @Override
    public ServiceEntity save(ServiceEntity service) {
        return serviceEntityRepository.save(service);
    }

    @Override
    public void deleteById(Long id) {
        serviceEntityRepository.deleteById(id);
    }

    @Override
    public Optional<ServiceEntity> findByName(String name) {
        return serviceEntityRepository.findByName(name);
    }
}