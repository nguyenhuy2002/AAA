package com.devops.manager.repository;

import com.devops.manager.model.Role;
import com.devops.manager.model.Sex;
import com.devops.manager.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    @Query(value = """
                SELECT * FROM users WHERE email = :username OR phoneNumber = :username
            """, nativeQuery = true)
    Optional<User> findByUsername(String username);

    @Query(value = """
                SELECT u.* FROM users u WHERE
                       (:id = -1 OR u.id = :id) AND
                       (LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND
                       (CONCAT(LOWER(u.firstName),' ',LOWER(u.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))) AND
                       (:sex IS NULL OR u.sex = :sex) AND
                       (LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :phoneNumber, '%'))) AND
                       (:role IS NULL OR u.role = :role)
            """, countQuery = """
                SELECT COUNT(u.id) FROM users u WHERE
                       (:id = -1 OR u.id = :id) AND
                       (LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND
                       (CONCAT(LOWER(u.firstName),' ',LOWER(u.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))) AND
                       (:sex IS NULL OR u.sex = :sex) AND
                       (LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :phoneNumber, '%'))) AND
                       (:role IS NULL OR u.role = :role)
            """, nativeQuery = true)
    Page<User> findUsersWithPaginationAndSort(
            @Param("id") Long id,
            @Param("email") String email,
            @Param("name") String name,
            @Param("sex") Sex sex,
            @Param("phoneNumber") String phoneNumber,
            @Param("role") Role role,
            Pageable pageable
    );


}