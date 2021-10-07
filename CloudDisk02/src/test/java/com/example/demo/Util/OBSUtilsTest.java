package com.example.demo.Util;

import org.junit.Test;

import static org.junit.Assert.*;

public class OBSUtilsTest {

    private static OBSUtils obsUtils = null;

    @org.junit.Before
    public void setUp() throws Exception {
        obsUtils = new OBSUtils();
    }

    @Test
    public void should_shareFile(){
        String bucketName = "qtre";
        String objectkey = "yewan.png";
        String url = obsUtils.shareFile(bucketName,objectkey);
        assertNotNull(url);
        System.out.println(url);
    }

    @Test
    public void uploadFile() {
    }
}