package com.factorypulse.backend.service;

import com.factorypulse.backend.dto.RegisterRequest;
import com.factorypulse.backend.model.Factory;
import com.factorypulse.backend.model.User;
import com.factorypulse.backend.repository.FactoryRepository;
import com.factorypulse.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {

    public static final String ROLE_OWNER = "FACTORY_OWNER";
    public static final String ROLE_ADMIN = "ADMIN";

    private static final Pattern EMAIL_RE =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$");

    private final UserRepository userRepository;
    private final FactoryRepository factoryRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       FactoryRepository factoryRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.factoryRepository = factoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /** Public registration. Role is ALWAYS FACTORY_OWNER. Creates the first factory too. */
    @Transactional
    public User registerFactoryOwner(RegisterRequest req) {
        String name = clean(req.getName());
        String email = clean(req.getEmail()).toLowerCase();
        String factoryName = clean(req.getFactoryName());

        validate(name, email, req.getPassword());
        if (factoryName.length() < 2 || factoryName.length() > 100) {
            throw new IllegalArgumentException("Please enter your factory name.");
        }

        User user = new User(name, email,
                passwordEncoder.encode(req.getPassword()), ROLE_OWNER);
        user = userRepository.save(user);

        Factory factory = new Factory();
        factory.setName(factoryName);
        factory.setOwner(user);
        factoryRepository.save(factory);

        return user;
    }

    /** Only reachable through the key-protected admin endpoint. */
    public User createAdmin(String name, String email, String password) {
        name = clean(name);
        email = clean(email).toLowerCase();
        validate(name, email, password);
        return userRepository.save(new User(name, email,
                passwordEncoder.encode(password), ROLE_ADMIN));
    }

    private void validate(String name, String email, String password) {
        if (name.length() < 2 || name.length() > 100) {
            throw new IllegalArgumentException("Please enter your full name.");
        }
        if (!EMAIL_RE.matcher(email).matches() || email.length() > 150) {
            throw new IllegalArgumentException("Please enter a valid email address.");
        }
        if (password == null || password.length() < 8 || password.length() > 72
                || !password.matches(".*[A-Za-z].*") || !password.matches(".*\\d.*")) {
            throw new IllegalArgumentException(
                    "Password must be 8-72 characters with a letter and a number.");
        }
        if (userRepository.findByEmailIgnoreCase(email).isPresent()) {
            throw new IllegalStateException("This email is already registered.");
        }
    }

    private static String clean(String s) {
        return s == null ? "" : s.trim();
    }

    public Optional<User> findByEmail(String email) {
        Optional<User> exact = userRepository.findByEmail(email);
        return exact.isPresent() ? exact : userRepository.findByEmailIgnoreCase(email);
    }
}
