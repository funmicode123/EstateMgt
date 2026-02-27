package com.funmicode.service;

import com.funmicode.data.model.Apartment;
import com.funmicode.dto.request.ApartmentRequest;

public interface AdminService {
    Apartment addApartment(ApartmentRequest request);
    
    com.funmicode.data.model.BlacklistedVisitor addToBlacklist(com.funmicode.dto.request.BlacklistRequest request, String adminEmail);
    void removeFromBlacklist(String name);
    java.util.List<com.funmicode.data.model.BlacklistedVisitor> getBlacklist();
}
