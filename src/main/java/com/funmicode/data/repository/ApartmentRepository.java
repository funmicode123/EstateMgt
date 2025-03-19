package com.funmicode.data.repository;

import com.funmicode.data.model.Apartment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApartmentRepository extends MongoRepository<Apartment, String> {
    boolean existsByBlock(String block);
}

