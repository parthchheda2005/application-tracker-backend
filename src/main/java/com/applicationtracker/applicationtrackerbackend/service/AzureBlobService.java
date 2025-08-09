package com.applicationtracker.applicationtrackerbackend.service;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    public byte[] downloadBlob(String blobName) {
        return blobContainerClient
                .getBlobClient(blobName)
                .downloadContent()
                .toBytes();
    }

    public void deleteBlob(String blobName) {
        blobContainerClient.getBlobClient(blobName).delete();
    }

}
