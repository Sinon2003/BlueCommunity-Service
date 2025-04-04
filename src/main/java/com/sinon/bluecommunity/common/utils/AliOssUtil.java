package com.sinon.bluecommunity.common.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.sinon.bluecommunity.config.AliOssConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class AliOssUtil {    
    private static AliOssConfig ossConfig;
    
    @Autowired
    public void setOssConfig(AliOssConfig ossConfig) {
        AliOssUtil.ossConfig = ossConfig;
    }

    /**
     * 上传文件到指定的目录。
     *
     * @param objectName 上传到OSS中的文件名（带扩展名）
     * @param inputStream 文件输入流
     * @return 文件的URL地址
     * @throws Exception
     */
    public static String uploadFile(String objectName, InputStream inputStream) throws Exception {
        // 检查配置是否已加载
        if (ossConfig == null) {
            throw new IllegalStateException("OSS配置未加载，请确保AliOssConfig已正确注入");
        }
        
        String url = "";

        // 创建OSSClient实例，使用配置中的参数
        OSS ossClient = new OSSClientBuilder().build(
            ossConfig.getEndpoint(), 
            ossConfig.getAccessKeyId(), 
            ossConfig.getAccessKeySecret());

        try {
            // 拼接目录路径，构成完整的objectName
            String fullObjectName = ossConfig.getBaseDirectory() + objectName;

            // 创建PutObjectRequest对象，上传文件到指定目录
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossConfig.getBucketname(), fullObjectName, inputStream);

            // 上传文件
            PutObjectResult result = ossClient.putObject(putObjectRequest);

            // 拼接返回的URL
            String bucketDomain = ossConfig.getEndpoint().replace("https://", "https://" + ossConfig.getBucketname() + ".");
            if (bucketDomain.endsWith("/")) {
                bucketDomain = bucketDomain.substring(0, bucketDomain.length() - 1);
            }
            url = bucketDomain + "/" + fullObjectName;

        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught a ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        return url;
    }
}
