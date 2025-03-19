package com.funmicode.service;

import com.funmicode.data.model.Apartment;
import com.funmicode.data.repository.ApartmentRepository;
import com.funmicode.dto.request.ApartmentRequest;
import com.funmicode.dto.response.ApartmentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService{
    @Autowired
    private ApartmentRepository apartmentRepository;

    @Override
    public Apartment addApartment(ApartmentRequest request) {
        if (apartmentRepository.existsByBlock(request.getBlock())) {
            throw new IllegalArgumentException("Apartment block already exists!");
        }
        Apartment apartment = new Apartment();
        apartmentRepository.save(apartment);

        ApartmentResponse apartmentResponse = new ApartmentResponse();
        apartmentResponse.setMessage("Apartment added successfully!");
        return apartment;
    }
}