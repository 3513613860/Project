package com.example.demo.Util;

import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.*;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class OBSUtils {
    private static final Logger LOGGER = Logger.getLogger(OBSUtils.class);
    private static final String endPoint = "obs.cn-east-3.myhuaweicloud.com";
    private static final String ak = "6N2DHIICNFMV8CNOGTDS";
    private static final String sk = "3MFnNsfaIuEXp4gjBkSOAuYEKyu1V62f4qRq8Nqp";
    public static String BUCKET_NAME = "qtre";

    /**
     * 创建桶;返回桶信息
     *
     * @param bucketName
     * @return
     */
    public String createBucket(String bucketName) {
        // 创建ObsClient实例
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);
        ObsBucket obsBucket = new ObsBucket();
        // 设置桶区域位置
        obsBucket.setLocation("cn-north-4");//北京一不需要设置，其他必须设置，此处obs应用的是北京四
        // 设置桶访问权限为公共读，默认是私有读写//AccessControlList.REST_CANNED_PUBLIC_READ
        obsBucket.setAcl(AccessControlList.REST_CANNED_PRIVATE);//私有
        // 设置桶的存储类型为标准存储的；COLD则为归档存储
        obsBucket.setBucketStorageClass(StorageClassEnum.STANDARD);
        boolean exists = obsClient.headBucket(bucketName);

        // 创建桶
        try {
            if (!exists) {
                obsBucket.setBucketName(bucketName);
                // 创建桶成功
                obsClient.createBucket(obsBucket);
            } else {
                LOGGER.error("桶已存在！");
                return null;
            }

        } catch (ObsException e) {
            LOGGER.error("创建桶失败！", e);
            return null;
        } finally {
            try {
                obsClient.close();
            } catch (IOException e) {
                LOGGER.error("桶链接关闭失败！", e);
            }
        }
        return bucketName;
    }


    /**
     * 查看桶列表，返回桶信息
     *
     * @return
     */
    public List<String> showBuckets() {
        // 创建ObsClient实例
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);
        ListBucketsRequest request = new ListBucketsRequest();
        request.setQueryLocation(true);
        List<ObsBucket> buckets = obsClient.listBuckets(request);
        List<String> objectList = null;
        if (buckets != null && buckets.size() > 0) {
            objectList = new ArrayList<String>();
            for (ObsBucket bucket : buckets) {
                objectList.add(bucket.getBucketName());
            }
        }
        try {
            obsClient.close();
        } catch (IOException e) {
            LOGGER.error("桶链接关闭失败！", e);
        }
        return objectList;
    }

    /**
     * 在某个桶中创建文件夹；返回文件夹信息
     *
     * @param bucketName
     * @param dirs
     * @return
     */
    public String createDirs(String bucketName, String dirs) {
        // 创建ObsClient实例
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);
        try {
            obsClient.putObject(bucketName, dirs, new ByteArrayInputStream(new byte[0]));
        } catch (Exception e) {
            LOGGER.error("无法创建目录！", e);
            return null;
        } finally {
            try {
                obsClient.close();
            } catch (IOException e) {
                LOGGER.error("桶链接关闭失败！", e);
            }
        }
        return dirs;
    }

    //在某一个桶的某个目录下，创建文件夹，返回文件夹信息(同上，暂时无需实现)
    //上传文件返回上传文件存储地址[注意：需通过设置对象元数据key，标示该对象文件类型]
    public static Map<String, Object> uploadFile(final String bucketName, final String dir, final String fileName, String fileType, final InputStream fileInputStream, final long fileSize) {
        Map<String, Object> map = null;
        // 创建ObsClient实例
        final ObsClient obsClient = new ObsClient(ak, sk, endPoint);
        try {
            // 初始化线程池
            ExecutorService executorService = Executors.newFixedThreadPool(5);
            // 初始化分段上传任务
            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, (dir != null && !dir.trim().equals("") ? dir + "/" : "") + fileName);
            InitiateMultipartUploadResult result = obsClient.initiateMultipartUpload(request);

            final String uploadId = result.getUploadId();
            System.out.println("\t" + uploadId + "\n");
            // 每段上传100MB
            long partSize = 100 * 1024 * 1024L;
            // 计算需要上传的段数
            long partCount = fileSize % partSize == 0 ? fileSize / partSize : fileSize / partSize + 1;
            final List<PartEtag> partEtags = Collections.synchronizedList(new ArrayList<PartEtag>());
            // 执行并发上传段
            for (int i = 0; i < partCount; i++) {
                // 分段在文件中的起始位置
                final long offset = i * partSize;
                // 分段大小
                final long currPartSize = (i + 1 == partCount) ? fileSize - offset : partSize;
                // 分段号
                final int partNumber = i + 1;
                executorService.execute(
                        new Runnable() {
                            public void run() {
                                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                                uploadPartRequest.setBucketName(bucketName);
                                uploadPartRequest.setObjectKey((dir != null && !dir.trim().equals("") ? dir + "/" : "") + fileName);
                                uploadPartRequest.setUploadId(uploadId);
                                uploadPartRequest.setInput(fileInputStream);
                                uploadPartRequest.setPartSize(currPartSize);
                                uploadPartRequest.setOffset(offset);
                                uploadPartRequest.setPartNumber(partNumber);


                                UploadPartResult uploadPartResult;
                                try {
                                    uploadPartResult = obsClient.uploadPart(uploadPartRequest);
                                    System.out.println("Part#" + partNumber + " done\n");
                                    partEtags.add(new PartEtag(uploadPartResult.getEtag(), uploadPartResult.getPartNumber()));
                                } catch (ObsException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }

            // 等待上传完成
            executorService.shutdown();
            while (!executorService.isTerminated()) {
                try {
                    executorService.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 合并段
            CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName, (dir != null && !dir.trim().equals("") ? dir + "/" : "") + fileName, uploadId, partEtags);

            obsClient.completeMultipartUpload(completeMultipartUploadRequest);
            map = new HashMap<String, Object>();
            //设置对象的元数据key和value
            Map<String, Object> metaDataMap = new HashMap<String, Object>();
            metaDataMap.put("filetype", fileType);//设置自定义的元数据key 标识文件类型
            setUserObjectMetaData(obsClient, metaDataMap, bucketName, dir, fileName);

        } catch (Exception e) {
            return null;
        } finally {
            try {
                obsClient.close();
            } catch (IOException e) {
                LOGGER.error("桶链接关闭失败！", e);
            }
        }
        map.put("fileName", fileName);
        map.put("dir", dir);
        map.put("fileSize", fileSize);
        map.put("fileType", fileType);
        return map;
    }


    //下载返回文件流
    public InputStream downloadFileInputStream(String bucketName, String dir, String fileName) {
        final ObsClient obsClient = new ObsClient(ak, sk, endPoint);
        ObsObject obsObject = obsClient.getObject(bucketName, (dir != null && !dir.trim().equals("") ? dir + "/" : "") + fileName);
        InputStream content = obsObject.getObjectContent();
        return content;
    }

    //返回文件地址（用于图片预览和视频文件播放）
    public String getFileTemporarySignature(String bucketName, String dir, String fileName) {

        // 创建ObsClient实例
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);
        String url = null;
        try {
            // URL有效期，3600秒
            long expireSeconds = 3600L;
            TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, expireSeconds);
            request.setBucketName(bucketName);
            request.setObjectKey((dir != null && !dir.trim().equals("") ? dir + "/" : "") + fileName);

            TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
            url = response.getSignedUrl();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                obsClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return url;
    }

    //获取桶或者桶目录下文件对象列表信息
    public List<Map<String, Object>> getObsBucketDirObjects(String bucketName, String dir) {
        final ObsClient obsClient = new ObsClient(ak, sk, endPoint);
        List<Map<String, Object>> list = null;
        ListObjectsRequest request = new ListObjectsRequest(bucketName);
        if (dir != null && !dir.trim().equals("")) {
            request.setPrefix(dir);
        }
        ObjectListing result;

        do {
            result = obsClient.listObjects(request);
            if (result != null) {
                list = new ArrayList<Map<String, Object>>();
                for (ObsObject obsObject : result.getObjects()) {
                    ObjectMetadata r = obsClient.getObjectMetadata(bucketName, obsObject.getObjectKey());//obsObject.getObjectKey() 包含了文件完整路径
                    System.out.println("Getting object metadata:");
                    System.out.println("\tContentType:" + r.getContentType());
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("fileName", obsObject.getObjectKey());
                    map.put("dir", dir);
                    map.put("filetype", r.getUserMetadata("filetype"));//注意：默认obs会将元数据key都转为小写字符存储；
                    map.put("ContentType", obsObject.getMetadata().getContentType());
                    Long fileSize = 0L;
                    String fileSizeStr = null;
                    if (obsObject.getMetadata().getContentLength() != null) {
                        fileSizeStr = getNetFileSizeDescription(obsObject.getMetadata().getContentLength());
                    }

                    map.put("fileSize", fileSizeStr);
                    map.put("modifedDate", new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(obsObject.getMetadata().getLastModified()));
                    list.add(map);
                }
            }

            request.setMarker(result.getNextMarker());
        } while (result.isTruncated());
        return list;
    }

    /***
     * @param metaDataMap
     * @param bucketName
     * @param dir
     * @param fileName
     * 设置对象元数据key，value
     */
    public static void setUserObjectMetaData(ObsClient obsClient, Map<String, Object> metaDataMap, String bucketName, String dir, String fileName) {

        try {
            SetObjectMetadataRequest request = new SetObjectMetadataRequest(bucketName, (dir != null && !dir.trim().equals("") ? dir + "/" : "") + fileName);

            // 设置自定义元数据
            if (metaDataMap != null) {
                Set<Map.Entry<String, Object>> set = metaDataMap.entrySet();
                Iterator<Map.Entry<String, Object>> ite = set.iterator();
                while (ite.hasNext()) {
                    Map.Entry<String, Object> entry = ite.next();
                    request.addUserMetadata(entry.getKey().toString(), entry.getValue().toString());
                }
            }

            ObjectMetadata metadata = obsClient.setObjectMetadata(request);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (obsClient != null) {
                try {
                    obsClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static String getNetFileSizeDescription(long size) {
        StringBuffer bytes = new StringBuffer();
        DecimalFormat format = new DecimalFormat("###.0");
        if (size >= 1024 * 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0 * 1024.0));
            bytes.append(format.format(i)).append("GB");
        } else if (size >= 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0));
            bytes.append(format.format(i)).append("MB");
        } else if (size >= 1024) {
            double i = (size / (1024.0));
            bytes.append(format.format(i)).append("KB");
        } else if (size < 1024) {
            if (size <= 0) {
                bytes.append("0B");
            } else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();
    }


    //控制层可调用该方法实现文件下载
    public void dowloadFile(HttpServletResponse response, InputStream inputStream, String fileName) {
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] items = new byte[1024 * 10];
        int i = 0;
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            OutputStream outputStream = response.getOutputStream();
            BufferedOutputStream outputStream1 = new BufferedOutputStream(outputStream);
            while ((i = bufferedInputStream.read(items)) != -1) {
                outputStream1.write(items, 0, i);
                outputStream1.flush();
            }
            outputStream1.close();
            outputStream.close();
            bufferedInputStream.close();
            inputStream.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    /*@param objectName,bucketName
    生成链接进行访问
     */
    public String getLink(String objectName) {
        // 1天后链接失效
        ObsClient obsClient = null;
        long expireSeconds = 3600L;
        TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, expireSeconds);
        request.setBucketName(BUCKET_NAME);
        request.setObjectKey(objectName);
        request.setRequestDate(new Date());
        obsClient = new ObsClient(ak, sk, endPoint);
        // 通过临时授权,直接访问链接下载
        boolean exist = obsClient.doesObjectExist(BUCKET_NAME, objectName);
        if (exist) {
            TemporarySignatureResponse signature = obsClient.createTemporarySignature(request);
            String link = signature.getSignedUrl();
            System.out.println("Obs获取链接成功!");
            return link;
        } else {
            System.out.println("对象不存在!");
            return null;
        }
    }

    /**
     * 下载文件
     *
     * @param objectKey
     * @return
     */
    public ObsObject getFile(String objectKey) {
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);
        boolean exist = obsClient.doesObjectExist(BUCKET_NAME, objectKey);
        if (exist) {
            ObsObject object = obsClient.getObject(BUCKET_NAME, objectKey);
            return object;
        }
        return null;
    }

    public void deleteFile(String objectKey) throws Exception {
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);
        boolean exist = obsClient.doesObjectExist(BUCKET_NAME, objectKey);
        if(exist) {
            obsClient.deleteObject(BUCKET_NAME, objectKey);
            System.out.println("Obs删除对象成功!");
        }
    }

    public void ObsUpload(String bucketName, String key, InputStream inputStream) throws IOException {
        // 创建ObsClient实例
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);
        obsClient.putObject(bucketName, key, inputStream);
        obsClient.close();
    }

    public static String shareFile(String bucketName,String objectKey){
        ObsClient obsClient = new ObsClient(ak,sk,endPoint);
        TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET,3600L);
        request.setBucketName(bucketName);
        request.setObjectKey(objectKey);
        TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
        return response.getSignedUrl();
    }
}