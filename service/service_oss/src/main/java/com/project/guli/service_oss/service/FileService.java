package com.project.guli.service_oss.service;

import java.io.InputStream;

/**
 * @author wan
 * @create 2020-07-13-16:07
 */
public interface FileService {

    /** 
     * 阿里云oss文件上传
     * @param inputStream	输入流
     * @param module	文件夹名称
     * @param originalFilename	原始文件名
     * @return 文件在oss服务器上的url地址
     * @date 2020/7/13 16:25
     */
    String upload(InputStream inputStream, String module, String originalFilename);

    /**
     * 阿里云oss 文件删除
     * @param url	文件的url地址
     * @author wwwy
     * @date 2020/7/14 18:21
     */
    void removeFile(String url);
}
