package org.university.zoomanagementsystem.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.university.zoomanagementsystem.security.SecurityUserDetails;
import org.university.zoomanagementsystem.user.Role;
import org.university.zoomanagementsystem.user.User;
import org.university.zoomanagementsystem.user.controller.dto.LoginRequest;
import org.university.zoomanagementsystem.user.controller.dto.RegisterRequest;
import org.university.zoomanagementsystem.user.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserRestController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public UserRestController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = httpRequest.getSession();
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            return ResponseEntity.ok(Map.of(
                    "message", "Login successful"
            ));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req, HttpServletRequest servletRequest) {
        if (userService.getUserByEmail(req.getEmail()) != null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already taken"));
        }

        User user = new User();
        user.setName(req.getName());
        user.setPassword(req.getPassword());
        user.setEmail(req.getEmail());
        user.setRole(Role.VISITOR);

        User created = userService.addUser(user);

        SecurityUserDetails details = new SecurityUserDetails(created);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                details, null, details.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
        servletRequest.getSession().setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );

        return ResponseEntity.ok(Map.of(
                "message", "Registration successful"
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return new ResponseEntity<>("Unauthorized: No user is authenticated", HttpStatus.UNAUTHORIZED);
        }

        request.getSession().removeAttribute("SPRING_SECURITY_CONTEXT");
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Logged out successfully"));
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(Authentication authentication) {
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated();

        Map<String, Object> response = new HashMap<>();

        if (isAuthenticated) {
            response.put("username", authentication.getName());
            response.put("roles", authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList());
        } else {
            response.put("username", null);
            response.put("roles", List.of());
        }
        return ResponseEntity.ok(response);
    }

}