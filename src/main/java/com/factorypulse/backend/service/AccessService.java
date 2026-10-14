package com.factorypulse.backend.service;

import com.factorypulse.backend.model.Factory;
import com.factorypulse.backend.model.Machine;
import com.factorypulse.backend.model.ProductionLine;
import com.factorypulse.backend.model.User;
import com.factorypulse.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Single place for "who is calling" and "may they touch this record".
 * ADMIN can access everything. Everyone else only their own factories
 * and the lines, machines, alerts, readings and maintenance under them.
 */
@Service
public class AccessService {

    private final UserRepository userRepository;

    public AccessService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static boolean isAdminRole(String role) {
        if (role == null) {
            return false;
        }
        String r = role.trim().toUpperCase();
        return r.equals("ADMIN") || r.equals("ROLE_ADMIN");
    }

    public String currentEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null
                || "anonymousUser".equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return auth.getName();
    }

    public boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }
        return auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }

    public User currentUser() {
        return userRepository.findByEmail(currentEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }

    /** Email to filter lists by, or null when the caller is ADMIN (no filter). */
    public String ownerFilterEmail() {
        return isAdmin() ? null : currentEmail();
    }

    public boolean canAccessFactory(Factory factory) {
        if (isAdmin()) {
            return true;
        }
        return factory != null
                && factory.getOwner() != null
                && factory.getOwner().getEmail() != null
                && factory.getOwner().getEmail().equalsIgnoreCase(currentEmail());
    }

    public boolean canAccessLine(ProductionLine line) {
        return line != null && canAccessFactory(line.getFactory());
    }

    public boolean canAccessMachine(Machine machine) {
        return machine != null && canAccessLine(machine.getProductionLine());
    }

    /** Not yours looks the same as not found, so ids cannot be probed. */
    public void require(boolean allowed) {
        if (!allowed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }
}
