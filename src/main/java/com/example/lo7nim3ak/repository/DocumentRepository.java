package com.example.lo7nim3ak.repository;

import com.example.lo7nim3ak.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    public boolean existsDocumentByTitle(String title);
    List<Document> findByUserId(Long userId);

}
