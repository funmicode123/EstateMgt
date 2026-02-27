package com.funmicode.controller;

import com.funmicode.data.model.Apartment;
import com.funmicode.dto.request.ApartmentRequest;
import com.funmicode.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private com.funmicode.service.UserService userService;

    @PostMapping("/apartments/add")
    public Apartment addApartment(@Valid @RequestBody ApartmentRequest request) { // changed RequestParam to RequestBody for complex object
        return adminService.addApartment(request);
    }
    
    @PostMapping("/create-security")
    public com.funmicode.dto.response.CreateSignupResponse createSecurity(@Valid @RequestBody com.funmicode.dto.request.CreateSignupRequest request) {
        return userService.createSecurity(request);
    }
    
    @PutMapping("/approve-resident/{email:.+}")
    public String approveResident(@PathVariable String email) {
        return userService.approveResident(email);
    }

    @PostMapping("/blacklist/add")
    public com.funmicode.data.model.BlacklistedVisitor addToBlacklist(@Valid @RequestBody com.funmicode.dto.request.BlacklistRequest request, @RequestParam String adminEmail) {
        return adminService.addToBlacklist(request, adminEmail);
    }

    @DeleteMapping("/blacklist/remove")
    public String removeFromBlacklist(@RequestParam String name) {
        adminService.removeFromBlacklist(name);
        return "Visitor removed from blacklist successfully";
    }

    @GetMapping("/blacklist")
    public java.util.List<com.funmicode.data.model.BlacklistedVisitor> getBlacklist() {
        return adminService.getBlacklist();
    }
}
