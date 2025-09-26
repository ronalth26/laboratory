package com.laboratory.userservice.config;

import com.laboratory.userservice.model.Role;
import com.laboratory.userservice.model.RoleName;
import com.laboratory.userservice.model.User;
import com.laboratory.userservice.model.Permission;
import com.laboratory.userservice.repository.RoleRepository;
import com.laboratory.userservice.repository.UserRepository;
import com.laboratory.userservice.repository.PermissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PermissionRepository permissionRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Inicializando datos de prueba...");

        // Crear roles si no existen
        initializeRoles();

        // Crear permisos base
        initializePermissions();

        // Asignar permisos a roles
        assignPermissionsToRoles();

        // Crear usuarios de prueba
        initializeUsers();

        logger.info("Datos de prueba inicializados correctamente!");
    }

    private void initializeRoles() {
        Arrays.stream(RoleName.values()).forEach(roleName -> {
            if (!roleRepository.findByName(roleName).isPresent()) {
                Role role = new Role();
                role.setName(roleName);
                role.setDescription(getRoleDescription(roleName));
                roleRepository.save(role);
                logger.info("Rol creado: {}", roleName);
            }
        });
    }

    private void initializePermissions() {
        String[][] basePermissions = {
                {"USER_VIEW", "Puede ver usuarios"},
                {"USER_EDIT", "Puede editar usuarios"},
                {"USER_DELETE", "Puede eliminar usuarios"},
                {"REPORTS_ACCESS", "Puede acceder a los reportes"},
                {"MODULE_LAB_ACCESS", "Puede acceder al módulo de laboratorio"},
                {"BUTTON_EXPORT", "Puede usar el botón de exportar datos"}
        };

        for (String[] perm : basePermissions) {
            String name = perm[0];
            String description = perm[1];

            permissionRepository.findByName(name).orElseGet(() -> {
                Permission p = new Permission();
                p.setName(name);
                p.setDescription(description);
                logger.info("Permiso creado: {}", name);
                return permissionRepository.save(p);
            });
        }
    }

    private void assignPermissionsToRoles() {
        // ADMIN tiene todos los permisos
        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN).get();
        adminRole.setPermissions(new HashSet<>(permissionRepository.findAll()));
        roleRepository.save(adminRole);

        // TÉCNICO: acceso laboratorio + reportes
        Role tecnicoRole = roleRepository.findByName(RoleName.ROLE_TECHNICIAN).get();
        Set<Permission> tecnicoPerms = new HashSet<>();
        tecnicoPerms.add(permissionRepository.findByName("MODULE_LAB_ACCESS").get());
        tecnicoPerms.add(permissionRepository.findByName("REPORTS_ACCESS").get());
        tecnicoRole.setPermissions(tecnicoPerms);
        roleRepository.save(tecnicoRole);

        // SUPERVISOR: reportes + edición de usuarios
        Role supervisorRole = roleRepository.findByName(RoleName.ROLE_SUPERVISOR).get();
        Set<Permission> supervisorPerms = new HashSet<>();
        supervisorPerms.add(permissionRepository.findByName("USER_VIEW").get());
        supervisorPerms.add(permissionRepository.findByName("USER_EDIT").get());
        supervisorPerms.add(permissionRepository.findByName("REPORTS_ACCESS").get());
        supervisorRole.setPermissions(supervisorPerms);
        roleRepository.save(supervisorRole);

        // VIEWER: solo puede ver usuarios
        Role viewerRole = roleRepository.findByName(RoleName.ROLE_VIEWER).get();
        Set<Permission> viewerPerms = new HashSet<>();
        viewerPerms.add(permissionRepository.findByName("USER_VIEW").get());
        viewerRole.setPermissions(viewerPerms);
        roleRepository.save(viewerRole);

        // USER: sin permisos especiales
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER).get();
        userRole.setPermissions(new HashSet<>());
        roleRepository.save(userRole);
    }

    private void initializeUsers() {
        // Usuario ADMIN
        if (!userRepository.findByUsername("admin").isPresent()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@laboratorio.com");
            admin.setFirstName("Administrador");
            admin.setLastName("Del Sistema");
            admin.setEnabled(true);

            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(roleRepository.findByName(RoleName.ROLE_ADMIN).get());
            adminRoles.add(roleRepository.findByName(RoleName.ROLE_USER).get());
            admin.setRoles(adminRoles);

            userRepository.save(admin);
            logger.info("Usuario admin creado: admin / admin123");
        }

        // Usuario TÉCNICO
        if (!userRepository.findByUsername("tecnico").isPresent()) {
            User tecnico = new User();
            tecnico.setUsername("tecnico");
            tecnico.setPassword(passwordEncoder.encode("tecnico123"));
            tecnico.setEmail("tecnico@laboratorio.com");
            tecnico.setFirstName("Juan");
            tecnico.setLastName("Técnico");
            tecnico.setEnabled(true);

            Set<Role> tecnicoRoles = new HashSet<>();
            tecnicoRoles.add(roleRepository.findByName(RoleName.ROLE_TECHNICIAN).get());
            tecnicoRoles.add(roleRepository.findByName(RoleName.ROLE_USER).get());
            tecnico.setRoles(tecnicoRoles);

            userRepository.save(tecnico);
            logger.info("Usuario técnico creado: tecnico / tecnico123");
        }

        // Usuario SUPERVISOR
        if (!userRepository.findByUsername("supervisor").isPresent()) {
            User supervisor = new User();
            supervisor.setUsername("supervisor");
            supervisor.setPassword(passwordEncoder.encode("super123"));
            supervisor.setEmail("supervisor@laboratorio.com");
            supervisor.setFirstName("María");
            supervisor.setLastName("Supervisora");
            supervisor.setEnabled(true);

            Set<Role> supervisorRoles = new HashSet<>();
            supervisorRoles.add(roleRepository.findByName(RoleName.ROLE_SUPERVISOR).get());
            supervisorRoles.add(roleRepository.findByName(RoleName.ROLE_TECHNICIAN).get());
            supervisorRoles.add(roleRepository.findByName(RoleName.ROLE_USER).get());
            supervisor.setRoles(supervisorRoles);

            userRepository.save(supervisor);
            logger.info("Usuario supervisor creado: supervisor / super123");
        }

        // Usuario VIEWER
        if (!userRepository.findByUsername("viewer").isPresent()) {
            User viewer = new User();
            viewer.setUsername("viewer");
            viewer.setPassword(passwordEncoder.encode("viewer123"));
            viewer.setEmail("viewer@laboratorio.com");
            viewer.setFirstName("Carlos");
            viewer.setLastName("Observador");
            viewer.setEnabled(true);

            Set<Role> viewerRoles = new HashSet<>();
            viewerRoles.add(roleRepository.findByName(RoleName.ROLE_VIEWER).get());
            viewerRoles.add(roleRepository.findByName(RoleName.ROLE_USER).get());
            viewer.setRoles(viewerRoles);

            userRepository.save(viewer);
            logger.info("Usuario viewer creado: viewer / viewer123");
        }

        // Usuario REGULAR
        if (!userRepository.findByUsername("usuario").isPresent()) {
            User usuario = new User();
            usuario.setUsername("usuario");
            usuario.setPassword(passwordEncoder.encode("user123"));
            usuario.setEmail("usuario@laboratorio.com");
            usuario.setFirstName("Ana");
            usuario.setLastName("Usuario");
            usuario.setEnabled(true);

            Set<Role> userRoles = new HashSet<>();
            userRoles.add(roleRepository.findByName(RoleName.ROLE_USER).get());
            usuario.setRoles(userRoles);

            userRepository.save(usuario);
            logger.info("Usuario regular creado: usuario / user123");
        }
    }

    private String getRoleDescription(RoleName roleName) {
        switch (roleName) {
            case ROLE_ADMIN:
                return "Administrador completo del sistema con todos los permisos";
            case ROLE_TECHNICIAN:
                return "Técnico de laboratorio con permisos para gestionar análisis";
            case ROLE_SUPERVISOR:
                return "Supervisor que puede aprobar resultados y gestionar técnicos";
            case ROLE_VIEWER:
                return "Usuario con permisos de solo lectura";
            case ROLE_USER:
                return "Usuario básico del sistema";
            default:
                return "Rol del sistema";
        }
    }
}
