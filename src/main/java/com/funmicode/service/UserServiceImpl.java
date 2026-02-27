package com.funmicode.service;

import com.funmicode.data.model.User;
import com.funmicode.data.repository.UserRepository;
import com.funmicode.dto.request.CreateSignupRequest;
import com.funmicode.dto.request.LoginRequest;
import com.funmicode.dto.response.CreateSignupResponse;
import com.funmicode.dto.response.LoginResponse;
import com.funmicode.utils.Mapper;
import com.funmicode.data.model.Apartment;
import com.funmicode.data.model.UserRole;
import com.funmicode.data.model.AccountStatus;
import com.funmicode.data.repository.ApartmentRepository;
import com.funmicode.utils.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private Mapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private com.funmicode.data.repository.ApartmentRepository apartmentRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public CreateSignupResponse signup(CreateSignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        UserRole role = UserRole.fromString(request.getRole());
        if (role == UserRole.SECURITY || role == UserRole.ADMIN) {
             throw new RuntimeException("This endpoint is for Resident registration only.");
        }

        User user = mapper.mapUserRequest(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(com.funmicode.data.model.AccountStatus.PENDING);

        if (role == UserRole.RESIDENT) {
             if (request.getApartment() == null) {
                 throw new RuntimeException("Apartment details are required for Residents");
             }
             // Link or Create Apartment
             com.funmicode.dto.request.ApartmentRequest aptReq = request.getApartment();
             com.funmicode.data.model.Apartment apartment = apartmentRepository.findByHouseNoAndBlockAndStreetName(
                     aptReq.getHouseNo(), aptReq.getBlock(), aptReq.getStreetName()
             ).orElseGet(() -> {
                 com.funmicode.data.model.Apartment newApt = new com.funmicode.data.model.Apartment();
                 newApt.setHouseNo(aptReq.getHouseNo());
                 newApt.setBlock(aptReq.getBlock());
                 newApt.setStreetName(aptReq.getStreetName());
                 return apartmentRepository.save(newApt);
             });
             user.setApartment(apartment);
        }

        userRepository.save(user);

        String token = jwtProvider.generateToken(user.getEmail(), user.getRole().name());

        CreateSignupResponse response = new CreateSignupResponse();
        response.setMessage("User registration pending approval. Role: " + user.getRole());
        response.setAccessToken(token);
        response.setSuccess(true);
        return response;
    }

    @Override
    public CreateSignupResponse createSecurity(CreateSignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }
        User user = mapper.mapUserRequest(request);
        user.setRole(UserRole.SECURITY);
        user.setStatus(com.funmicode.data.model.AccountStatus.ACTIVE);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        userRepository.save(user);

        CreateSignupResponse response = new CreateSignupResponse();
        response.setMessage("Security account created successfully");
        response.setSuccess(true);
        return response;
    }

    @Override
    public String approveResident(String email) {
        User user = userRepository.findByEmail(email.trim())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (user.getRole() != UserRole.RESIDENT) {
            throw new RuntimeException("Only residents can be approved via this method");
        }
        
        user.setStatus(com.funmicode.data.model.AccountStatus.ACTIVE);
        userRepository.save(user);
        return "Resident approved successfully";
    }

    @Override
    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = String.format("%04d", new java.util.Random().nextInt(10000));
        user.setOtp(otp);
        user.setOtpExpiry(java.time.LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        emailService.sendOtpEmail(email, otp);

        return "OTP sent successfully to your email";
    }

    @Override
    public String resetPassword(com.funmicode.dto.request.ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOtp() == null || !user.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        if (user.getOtpExpiry().isBefore(java.time.LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        return "Password reset successfully";
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User savedUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (savedUser.getStatus() != com.funmicode.data.model.AccountStatus.ACTIVE) {
            throw new RuntimeException("Account is not active. Status: " + savedUser.getStatus());
        }

        if (passwordEncoder.matches(request.getPassword(), savedUser.getPassword())) {
            String token = jwtProvider.generateToken(savedUser.getEmail(), savedUser.getRole().name());
            return new LoginResponse("Login successful", savedUser.getRole(), token, true);
        }

        // Lazy Migration: Check if the password matches as plain text
        if (savedUser.getPassword().equals(request.getPassword())) {
            // It matches plain text! Upgrade to hash.
            savedUser.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(savedUser);
            String token = jwtProvider.generateToken(savedUser.getEmail(), savedUser.getRole().name());
            return new LoginResponse("Login successful", savedUser.getRole(), token, true);
        }    
        throw new RuntimeException("Invalid credentials");
    }

    @Override
    public com.funmicode.dto.response.ProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        com.funmicode.dto.response.ProfileResponse response = new com.funmicode.dto.response.ProfileResponse();
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setRole(user.getRole().name());
        response.setGateLocation(user.getGateLocation());
        response.setApartment(user.getApartment());
        response.setSuccess(true);
        response.setMessage("Profile fetched successfully");
        return response;
    }

    @Override
    public com.funmicode.dto.response.ProfileResponse updateProfile(String email, com.funmicode.dto.request.ProfileUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());

        if (user.getRole() == UserRole.SECURITY && request.getGateLocation() != null) {
            user.setGateLocation(request.getGateLocation());
        }

        if (user.getRole() == UserRole.RESIDENT && request.getApartment() != null) {
            com.funmicode.dto.request.ApartmentRequest aptReq = request.getApartment();
            Apartment apartment = apartmentRepository.findByHouseNoAndBlockAndStreetName(
                    aptReq.getHouseNo(), aptReq.getBlock(), aptReq.getStreetName()
            ).orElseGet(() -> {
                Apartment newApt = new Apartment();
                newApt.setHouseNo(aptReq.getHouseNo());
                newApt.setBlock(aptReq.getBlock());
                newApt.setStreetName(aptReq.getStreetName());
                return apartmentRepository.save(newApt);
            });
            user.setApartment(apartment);
        }

        userRepository.save(user);

        return getProfile(email);
    }

}
