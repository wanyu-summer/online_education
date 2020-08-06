package com.project.guli.service_oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.project.guli.service_oss.service.FileService;
import com.project.guli.service_oss.util.OssProperties;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author wan
 * @create 2020-07-13-16:28
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private OssProperties ossProperties;

    @Override
    /**
     *
     * 阿里云oss文件上传
     * @param inputStream	输入流
     * @param module	文件夹名称
     * @param originalFilename	原始文件名
     * @return 文件在oss服务器上的url地址
     * @author wwwy
     * @date 2020/7/13 17:23
     */
    public String upload(InputStream inputStream, String module, String originalFilename) {

        //读取配置信息，由于加了data注解，可以直接进行获取
        String endPoint = ossProperties.getEndPoint();
        String keyId = ossProperties.getKeyId();
        String keySecret = ossProperties.getKeySecret();
        String bucketName = ossProperties.getBucketName();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endPoint,keyId,keySecret);
        //判断该实例是否存在
        if (!ossClient.doesBucketExist(bucketName)){
            //不存在时创建一个bucket，并设置权限
            ossClient.createBucket(bucketName);
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        }

        //构建objectName文件路径 avatar/20190807155734_87682.png 使用日期策略
        String folder = new DateTime().toString("yyyy/MM/dd");
        String fileName = UUID.randomUUID().toString();
        //取出原始文件名的扩展名
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String key = module + "/" + folder + "/" + fileName + fileExtension;
        // 上传文件流。
//        InputStream inputStream = new FileInputStream("<yourlocalFile>");
        ossClient.putObject(bucketName,key,inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
        //返回文件在oss服务器上的URL地址，如https://guli-file-wy.oss-cn-beijing.aliyuncs.com/avatar/20190807155734_87682.png

        return "https://" + bucketName + "." + endPoint + "/" + key;
    }

    @Override
    public void removeFile(String url) {
        //读取配置信息，由于加了data注解，可以直接进行获取
        String endPoint = ossProperties.getEndPoint();
        String keyId = ossProperties.getKeyId();
        String keySecret = ossProperties.getKeySecret();
        String bucketName = ossProperties.getBucketName();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endPoint,keyId,keySecret);

        //获取objectName
        //首先获取主机地址 https://guli-file-wy.oss-cn-beijing.aliyuncs.com/avatar/2020/07/13/07f19454-c201-4aca-a7f1-1fc2f35db61f.jpg
        String host = "https://" + bucketName + "." + endPoint + "/";
        String objectName = url.substring(host.length());//从主机名长度开始截取
        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucketName, objectName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
