package Project03.SpringbootApplication.service;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;

@Service
public class ExampleService {

    @Autowired
    private Firestore firestore;

    /**
     * Save data to Firestore
     * @param collection The collection name
     * @param documentId The document ID
     * @param data The data to save
     * @return Success message with timestamp
     */
    public String saveData(String collection, String documentId, Map<String, Object> data) 
            throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(collection).document(documentId);
        ApiFuture<WriteResult> result = docRef.set(data);
        WriteResult writeResult = result.get();
        return "Data saved successfully at: " + writeResult.getUpdateTime();
    }

    /**
     * Get data from Firestore
     * @param collection The collection name
     * @param documentId The document ID
     * @return The document data as a Map
     */
    public Map<String, Object> getData(String collection, String documentId) 
            throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(collection).document(documentId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        
        if (document.exists()) {
            return document.getData();
        } else {
            throw new RuntimeException("Document not found");
        }
    }
}
