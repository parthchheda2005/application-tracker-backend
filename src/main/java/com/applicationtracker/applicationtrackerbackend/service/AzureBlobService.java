package com.applicationtracker.applicationtrackerbackend.service;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

}
