package com.thedaymarket.repository;

import com.thedaymarket.domain.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  List<Category> findByTagContainingIgnoreCase(String tagQuery, Pageable page);

  Optional<Category> getCategoriesByTag(String tag);
}
