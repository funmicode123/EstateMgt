package com.funmicode.service;

import com.funmicode.data.model.Apartment;
import com.funmicode.dto.request.ApartmentRequest;

public interface AdminService {
    Apartment addApartment(ApartmentRequest request);
}
