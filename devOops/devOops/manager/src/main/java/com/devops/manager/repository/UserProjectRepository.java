package com.devops.manager.repository;

import com.devops.manager.model.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserProjectRepository extends JpaRepository<UserProject, Long> {
    @Modifying
    @Query(value = """
            DELETE FROM userproject WHERE id_project = :projectId AND id NOT IN :ids
            """, nativeQuery = true)
    void deleteAllNotInListId(@Param("projectId") Long projectId, @Param("ids") List<Long> ids);

}