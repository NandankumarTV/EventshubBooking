package com.nandan.EventsHub.repo;

import com.nandan.EventsHub.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByAdminName(String adminName);
}
