package eind.trmsmo.service;

import java.util.List;
import java.util.Optional;

import eind.trmsmo.entity.ServiceEntity;

public interface ServiceEntityService {
    List<ServiceEntity> findAll();
    Optional<ServiceEntity> findById(Long id);
    ServiceEntity save(ServiceEntity service);
    void deleteById(Long id);
    Optional<ServiceEntity> findByName(String name);
}