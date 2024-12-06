package com.example.lo7nim3ak.controllers;

import com.example.lo7nim3ak.entities.Document;
import com.example.lo7nim3ak.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }
    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("userId") Long userId) {
        try {
            documentService.saveDocument(file, title, userId);
            return ResponseEntity.ok("Document uploaded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload document.");
        }
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
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Document>> getDocumentsByUserId(@PathVariable Long userId) {
        List<Document> documents = documentService.getDocumentsByUserId(userId);
        if (documents != null && !documents.isEmpty()) {
            return ResponseEntity.ok(documents);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
        Document document = documentService.getDocumentById(id);
        if (document == null) {
            return ResponseEntity.notFound().build();
        }
        byte[] fileContent = document.getContent();
        String fileName = document.getTitle() + ".pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_PDF);
        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }




}
