package com.laboratory.userservice.service;

import com.laboratory.user.service.model.Role;
import com.laboratory.user.service.model.RoleName;
import com.laboratory.user.service.model.User;
import com.laboratory.user.service.repository.RoleRepository;
import com.laboratory.user.service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Crear usuario con roles básicos
    public User createUser(User user) {
        return createUserWithRoles(user, Set.of(RoleName.ROLE_USER));
    }

    // Crear usuario con roles específicos
    public User createUserWithRoles(User user, Set<RoleName> roleNames) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists: " + user.getUsername());
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        // Asignar roles
        Set<Role> roles = roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(roles);
        return userRepository.save(user);
    }

    // Obtener usuario por username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Obtener todos los usuarios
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // Verificar si usuario tiene un rol específico
    public boolean hasRole(Long userId, RoleName roleName) {
        return userRepository.findById(userId)
                .map(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName() == roleName))
                .orElse(false);
    }

    // Asignar rol adicional a usuario
    public User addRoleToUser(Long userId, RoleName roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        user.getRoles().add(role);
        return userRepository.save(user);
    }

    // Remover rol de usuario
    public User removeRoleFromUser(Long userId, RoleName roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        user.getRoles().removeIf(role -> role.getName() == roleName);
        return userRepository.save(user);
    }

    // Actualizar usuario
    public User updateUser(Long userId, User userDetails) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        user.setEmail(userDetails.getEmail());
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEnabled(userDetails.isEnabled());

        return userRepository.save(user);
    }

    // Eliminar usuario
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        userRepository.delete(user);
    }

    // Cambiar contraseña
    public User changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    // Buscar usuarios por rol
    public List<User> findUsersByRole(RoleName roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        return userRepository.findAll().stream()
                .filter(user -> user.getRoles().contains(role))
                .collect(Collectors.toList());
    }
}