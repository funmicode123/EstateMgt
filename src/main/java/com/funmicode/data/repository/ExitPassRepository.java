package com.funmicode.data.repository;

import com.funmicode.data.model.ExitPass;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExitPassRepository extends MongoRepository<ExitPass, String> {
}

