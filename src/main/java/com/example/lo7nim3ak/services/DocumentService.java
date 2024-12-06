package com.example.lo7nim3ak.services;

import com.example.lo7nim3ak.entities.Document;
import com.example.lo7nim3ak.entities.User;
import com.example.lo7nim3ak.repository.DocumentRepository;
import com.example.lo7nim3ak.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    public void saveDocument(MultipartFile file, String title, Long userId) throws IOException {
        // Retrieve user
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Create document
        Document document = new Document();
        document.setTitle(title);
        document.setContent(file.getBytes()); // Store file content as bytes
        document.setUser(user);

        // Save document to the database
        documentRepository.save(document);
    }
    public void deleteDocument(Document document){
        try{
            documentRepository.delete(document);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public List<Document> getAllDocuments(){
        try{
            return documentRepository.findAll();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public Document findDocById (long docId){
        try {
            documentRepository.findById(docId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public Boolean existByTitle(String title) {
        return documentRepository.existsDocumentByTitle(title);
    }
    public List<Document> getDocumentsByUserId(Long userId) {
        try {
            return documentRepository.findByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public Document getDocumentById(Long id) {
        Optional<Document> document = documentRepository.findById(id);
        if (document.isPresent()) {
            return document.get();
        } else {
            throw new RuntimeException("Document not found with id: " + id);
        }
    }
}
