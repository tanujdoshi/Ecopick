package com.example.backend.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import com.amazonaws.services.s3.model.DeleteObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Awsutils {
    private final AmazonS3 amazonS3;

    @Value("${s3.bucket.name}")
    private String s3BucketName;

    @Async
    public File convertMultiPartFileToFile(final MultipartFile multipartFile) {
        final File file = new File(multipartFile.getOriginalFilename());
        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (IOException e) {
            System.out.println("Here??" + e.getLocalizedMessage());
        }
        return file;
    }

    @Async
    public String fetchImageUrl(String s3BucketName, String filename) {
        return amazonS3.getUrl(s3BucketName, filename).toString();
    }

    @Async
    public String uploadFileToS3(final MultipartFile multipartFile, String type, int type_id) {
        try {
            final File file = convertMultiPartFileToFile(multipartFile);
            String fileName = file.getName();
            if (type == "FARM") {
                fileName = "farms/" + type_id + "/" + file.getName();
            } else if (type == "PRODUCT") {
                fileName = "products/" + type_id + "/" + file.getName();
            }
            final PutObjectRequest s3ObjectRequest = new PutObjectRequest(s3BucketName, fileName, file);
            amazonS3.putObject(s3ObjectRequest);
            Files.delete(file.toPath());
            return fileName;
        } catch (AmazonServiceException e) {
            System.out.println("Error {} occurred while uploading file" + e.getLocalizedMessage());
        } catch (IOException ex) {
            System.out.println("Error {} occurred while deleting temporary file" + ex.getLocalizedMessage());
        }
        return null;
    }
    @Async
    public void deleteFilefromS3(String url){
        try{
            final DeleteObjectRequest s3ObjectRequest = new DeleteObjectRequest(s3BucketName, url);
            amazonS3.deleteObject(s3ObjectRequest);
        } catch (AmazonServiceException e){
            System.out.println("Error {} occured while deleting file" + e.getLocalizedMessage());
        }
    }
}
