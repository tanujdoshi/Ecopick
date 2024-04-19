package com.example.backend.utils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.net.URL;

import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import com.amazonaws.services.s3.AmazonS3;

public class AwsUtilsTest {

    @Mock
    private AmazonS3 amazonS3;


    private Awsutils awsUtils;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        awsUtils = new Awsutils(amazonS3);
    }

//    @Test
//    public void testConvertMultiPartFileToFile() throws IOException {
//        File file = awsUtils.convertMultiPartFileToFile(multipartFile);
//        // Assert file is not null and other necessary assertions
//    }

    @Test
    public void testFetchImageUrl() throws MalformedURLException {
        String s3BucketName = "testBucket";
        String filename = "testFile.jpg";
        String expectedUrl = "https://testBucket.s3.amazonaws.com/testFile.jpg";

        when(amazonS3.getUrl(s3BucketName, filename)).thenReturn(new URL(expectedUrl));

        String imageUrl = awsUtils.fetchImageUrl(s3BucketName, filename);

        assertEquals(expectedUrl, imageUrl);
    }

//    @Test
//    public void testDeleteFileFromS3() {
//        String url = "testUrl";
//        awsUtils.deleteFilefromS3(url);
//
//        verify(amazonS3, times(1)).deleteObject(any());
//    }
}