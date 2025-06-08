package com.shortlink.repository;

import com.shortlink.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    Optional<Link> findByOriginal(String code);
    Optional<Link> findByLink(String link);

    List<Link> findAllByOrderByCountDesc();
}
