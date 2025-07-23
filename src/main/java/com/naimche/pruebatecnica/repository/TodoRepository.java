package com.naimche.pruebatecnica.repository;

import com.naimche.pruebatecnica.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Query("SELECT t FROM Todo t WHERE " +
            "(:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:username IS NULL OR t.user.username = :username)")
    Page<Todo> findAllFiltered(
            @Param("title") String title,
            @Param("username") String username,
            Pageable pageable);
}
