package be.pielambr.minerva4j.parsers;

import be.pielambr.minerva4j.beans.Document;
import be.pielambr.minerva4j.parsers.json.JSONDocument;
import be.pielambr.minerva4j.utility.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DocumentParser {

    /**
     * @param json Json blob to parse
     * @return A list of documents
     */
    public static List<Document> parseDocuments(String json) {
        List<Document> documents = new ArrayList<Document>();
        JSONDocument jsonDocument = new Gson().fromJson(json, JSONDocument.class);
        if(jsonDocument!= null && jsonDocument.getType().equals(Constants.TYPE_ROOT)){
            if(jsonDocument.getItems() != null) {
                documents.addAll(parseDirectory(jsonDocument.getItems()));
            }
        }
        return documents;
    }

    /**
     * Parses a directory and searches for documents within
     * @param items A map of documents in a directory
     * @return A list of documents
     */
    private static List<Document> parseDirectory(Map<String, JSONDocument> items) {
        List<Document> documents = new ArrayList<Document>();
        for(JSONDocument document : items.values()) {
            if (document.getType() == null) {
                continue;
            }

            if (document.getType().equals(Constants.TYPE_FILE)) {
                Document doc = new Document(document.getId(), document.getFilename());
                documents.add(doc);
            } else if (document.getType().equals(Constants.TYPE_FOLDER) && document.getItems() != null) {
                documents.addAll(parseDirectory(document.getItems()));
            }
        }
        return documents;
    }
}