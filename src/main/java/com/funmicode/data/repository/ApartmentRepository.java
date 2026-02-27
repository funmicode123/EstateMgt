package com.funmicode.data.repository;

import com.funmicode.data.model.Apartment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ApartmentRepository extends MongoRepository<Apartment, String> {
    Optional<Apartment> findByHouseNoAndBlockAndStreetName(String houseNo, String block, String streetName);
    boolean existsByBlock(String block);
}
