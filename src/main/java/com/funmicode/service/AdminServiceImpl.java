package com.funmicode.service;

import com.funmicode.data.model.Apartment;
import com.funmicode.data.repository.ApartmentRepository;
import com.funmicode.dto.request.ApartmentRequest;
import com.funmicode.data.repository.BlacklistedVisitorRepository;
import com.funmicode.data.model.BlacklistedVisitor;
import com.funmicode.dto.request.BlacklistRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private BlacklistedVisitorRepository blacklistedVisitorRepository;

    @Override
    public Apartment addApartment(ApartmentRequest request) {
        if (apartmentRepository.existsByBlock(request.getBlock())) {
            throw new IllegalArgumentException("Apartment block already exists!");
        }
        Apartment apartment = new Apartment();
        apartment.setHouseNo(request.getHouseNo());
        apartment.setBlock(request.getBlock());
        apartment.setStreetName(request.getStreetName());
        apartmentRepository.save(apartment);

        return apartment;
    }

    @Override
    public BlacklistedVisitor addToBlacklist(BlacklistRequest request, String adminEmail) {
        if (blacklistedVisitorRepository.findByNameIgnoreCase(request.getName()).isPresent()) {
            throw new IllegalArgumentException("Visitor is already blacklisted!");
        }
        BlacklistedVisitor visitor = new BlacklistedVisitor();
        visitor.setName(request.getName());
        visitor.setPhoneNumber(request.getPhoneNumber());
        visitor.setReason(request.getReason());
        visitor.setAddedBy(adminEmail);
        visitor.setAddedAt(LocalDateTime.now());
        return blacklistedVisitorRepository.save(visitor);
    }

    @Override
    public void removeFromBlacklist(String name) {
        BlacklistedVisitor visitor = blacklistedVisitorRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new IllegalArgumentException("Visitor not found in blacklist"));
        blacklistedVisitorRepository.delete(visitor);
    }

    @Override
    public List<BlacklistedVisitor> getBlacklist() {
        return blacklistedVisitorRepository.findAll();
    }
}