package com.mxch.imgreconsturct.util;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Aliyunoss {

    public static void main(String[] args) {
        getBuckets();
    }

    /**
     * 获取oss的容器
     * @param
     */
    public static void getBuckets() {
        // 从环境变量获取访问凭证
        String accessKeyId = System.getenv("OSS_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("OSS_ACCESS_KEY_SECRET");

        // 设置OSS地域和Endpoint
        String region = "cn-hangzhou";
        String endpoint = "oss-cn-hangzhou.aliyuncs.com";

        // 创建凭证提供者
        DefaultCredentialProvider provider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);

        // 配置客户端参数
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        // 显式声明使用V4签名算法
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);

        // 初始化OSS客户端
        OSS ossClient = OSSClientBuilder.create()
                .credentialsProvider(provider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .endpoint(endpoint)
                .build();

        // 列出当前用户的所有Bucket
        List<Bucket> buckets = ossClient.listBuckets();
        System.out.println("成功连接到OSS服务，当前账号下的Bucket列表：");

        if (buckets.isEmpty()) {
            System.out.println("当前账号下暂无Bucket");
        } else {
            for (Bucket bucket : buckets) {
                System.out.println("- " + bucket.getName());
            }
        }

        // 释放资源
        ossClient.shutdown();
        System.out.println("OSS客户端已关闭");
    }

    /**
     * 上传手绘图像到oss
     * @param inputStream 二进制文件流
     * @param imageName 图像名称
     * @param modelName 模型名称
     * @return
     * @throws Exception
     */
    public static Map<String, String> uploadAliyunOssByHandOrImage(boolean type, InputStream inputStream, String imageName, String modelName) throws Exception {
        // 路径结果
        Map<String, String> resultMap = new HashMap<>();
        // Endpoint华东1（杭州）
        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
        // 从环境变量中获取访问凭证
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "mxch-imagefiles";
        String modelBucketName = "mxch-modelfiles";
        // 填写Object完整路径，完整路径中不能包含Bucket名称
        String objectName = "handFile/" + imageName;
        String modelObjectName = "handFile/" + modelName;
        // 上传摄影图像
        if (type) {
            objectName = "imageFile/" + imageName;
            modelObjectName = "imageFile/" + modelName;
        }
        // 填写本地文件的完整路径
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
//        String filePath= "D:\\localpath\\examplefile.txt";
        // 填写Bucket所在地域。以华东1（杭州）为例，Region填写为cn-hangzhou。
        String region = "cn-hangzhou";

        // 创建OSSClient实例。
        // 当OSSClient实例不再使用时，调用shutdown方法以释放资源。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();

        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
            // 创建PutObject请求。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            String imagePath = "https://" + bucketName + ".oss-cn-hangzhou.aliyuncs.com/" + objectName;
            String modelPath = "https://" + modelBucketName + ".oss-cn-hangzhou.aliyuncs.com/" + modelObjectName;
            resultMap.put("imagePath", imagePath);
            resultMap.put("modelPath", modelPath);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } finally {
            ossClient.shutdown();
        }

        return resultMap;
    }

    public static String uploadAliyunOssByAvatar(InputStream inputStream, String avatarName) throws Exception {
        // 头像存储路径
        String avatarPath = "";
        // Endpoint华东1（杭州）
        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
        // 从环境变量中获取访问凭证
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "mxch-defaultsfiles";
        // 填写Object完整路径，完整路径中不能包含Bucket名称
        String objectName = "avatarfiles/" + avatarName;
        // 填写Bucket所在地域。以华东1（杭州）为例，Region填写为cn-hangzhou。
        String region = "cn-hangzhou";
        // 创建OSSClient实例。
        // 当OSSClient实例不再使用时，调用shutdown方法以释放资源。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();
        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
            // 创建PutObject请求。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            avatarPath = "https://" + bucketName + ".oss-cn-hangzhou.aliyuncs.com/" + objectName;
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } finally {
            ossClient.shutdown();
        }
        return avatarPath;
    }

}
