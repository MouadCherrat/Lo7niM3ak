package com.example.lo7nim3ak.services;

import com.example.lo7nim3ak.entities.Document;
import com.example.lo7nim3ak.repository.DocumentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    public void createDocument(Document document){
        try{
            documentRepository.save(document);
        }catch (Exception e){
            e.printStackTrace();
        }
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
}
