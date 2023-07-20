package com.devops.manager.repository;

import com.devops.manager.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query(value = """
                SELECT p.* FROM projects p WHERE
                       (:id = -1 OR p.id = :id) AND
                       (LOWER(p.repo) LIKE LOWER(CONCAT('%', :repo, '%'))) AND
                       (LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND
                       (:idUser = -1 OR
                            p.id IN (
                                SELECT up.id_project
                                FROM userproject up
                                INNER JOIN users u
                                ON up.id_user =  u.id
                                WHERE u.id = :idUser
                            )
                       )
            """, countQuery = """
                SELECT COUNT(p.id) FROM projects p WHERE
                       (:id = -1 OR p.id = :id) AND
                       (LOWER(p.repo) LIKE LOWER(CONCAT('%', :repo, '%'))) AND
                       (LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND
                       (:idUser = -1 OR
                            p.id IN (
                                SELECT up.id_project
                                FROM userproject up
                                INNER JOIN users u
                                ON up.id_user =  u.id
                                WHERE u.id = :idUser
                            )
                       )
            """, nativeQuery = true)
    Page<Project> findProjectsWithPaginationAndSort(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("repo") String repo,
            @Param("idUser") Long idUser,
            Pageable pageable);

}