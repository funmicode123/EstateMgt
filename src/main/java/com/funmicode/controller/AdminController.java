package com.funmicode.controller;

import com.funmicode.data.model.Apartment;
import com.funmicode.dto.request.ApartmentRequest;
import com.funmicode.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/apartments")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/addApartment")
    public Apartment addApartment(@Valid @RequestParam ApartmentRequest request) {
        return adminService.addApartment(request);
    }
}
