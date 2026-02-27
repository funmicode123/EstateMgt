package com.funmicode.data.repository;

import com.funmicode.data.model.BlacklistedVisitor;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface BlacklistedVisitorRepository extends MongoRepository<BlacklistedVisitor, String> {
    Optional<BlacklistedVisitor> findByNameIgnoreCase(String name);
}
