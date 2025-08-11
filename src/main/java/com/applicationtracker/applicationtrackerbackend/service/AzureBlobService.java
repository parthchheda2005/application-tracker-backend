package com.applicationtracker.applicationtrackerbackend.service;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.OffsetDateTime;

@Service
public class AzureBlobService {

    private final BlobContainerClient blobContainerClient;

    public AzureBlobService(@Value("${azure.storage.connection-string}") String connectionString,
                            @Value("${azure.storage.blob-container-name}") String containerName) {
        this.blobContainerClient = new BlobContainerClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .buildClient();
    }

    public void uploadBlob(String blobName, byte[] data) {
        blobContainerClient
                .getBlobClient(blobName)
                .upload(BinaryData.fromBytes(data), true);
    }

    public void deleteBlob(String blobName) {
        blobContainerClient.getBlobClient(blobName).delete();
    }

    public String generateBlobSasUrl(String blobName, int expiryInMinutes) {
        BlobClient blobClient = blobContainerClient.getBlobClient(blobName);

        BlobSasPermission permissions = new BlobSasPermission()
                .setReadPermission(true);

        OffsetDateTime expiryTime = OffsetDateTime.now().plusMinutes(expiryInMinutes);

        BlobServiceSasSignatureValues values = new BlobServiceSasSignatureValues(expiryTime, permissions)
                .setStartTime(OffsetDateTime.now());

        String sasToken = blobClient.generateSas(values);

        return blobClient.getBlobUrl() + "?" + sasToken;
    }

    public String downloadBlobAsText(String blobName) throws IOException {
        BlobClient blobClient = blobContainerClient.getBlobClient(blobName);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blobClient.downloadStream(outputStream);
        byte[] fileBytes = outputStream.toByteArray();

        if (blobName.toLowerCase().endsWith(".pdf")) {
            return extractTextFromPdf(fileBytes);
        } else if (blobName.toLowerCase().endsWith(".docx")) {
            return extractTextFromDocx(fileBytes);
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + blobName);
        }
    }

    private String extractTextFromPdf(byte[] fileBytes) throws IOException {
        try (PDDocument document = PDDocument.load(fileBytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private String extractTextFromDocx(byte[] fileBytes) throws IOException {
        try (XWPFDocument docx = new XWPFDocument(new ByteArrayInputStream(fileBytes))) {
            StringBuilder text = new StringBuilder();
            docx.getParagraphs().forEach(p -> text.append(p.getText()).append("\n"));
            return text.toString();
        }
    }

}
