package com.example.lo7nim3ak.controllers;

import com.example.lo7nim3ak.entities.Document;
import com.example.lo7nim3ak.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }
    @PostMapping
    public ResponseEntity<String> createDocument(@RequestBody Document document) {
        documentService.createDocument(document);
        return ResponseEntity.ok("Document created successfully.");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable Long id) {
        Document document = documentService.findDocById(id);
        if (document != null) {
            documentService.deleteDocument(document);
            return ResponseEntity.ok("Document deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        List<Document> documents = documentService.getAllDocuments();
        if (documents != null && !documents.isEmpty()) {
            return ResponseEntity.ok(documents);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        Document document = documentService.findDocById(id);
        if (document != null) {
            return ResponseEntity.ok(document);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkDocumentExistsByTitle(@RequestParam String title) {
        boolean exists = documentService.existByTitle(title);
        return ResponseEntity.ok(exists);
    }
}
