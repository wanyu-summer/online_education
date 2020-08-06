package com.project.guli.service.vod.service;

import com.aliyuncs.exceptions.ClientException;

import java.io.InputStream;
import java.util.List;

/**
 * @author wan
 * @create 2020-07-22-17:37
 */
public interface VideoService {
    //上传完视频返回视频Id
    String uploadVideo(InputStream file, String originalFileName);

    void removeVideo(String vodId) throws ClientException;

    void removeVideoByIdList(List<String> videoIdList) throws ClientException;

    String getPlayAuth(String videoSourceId) throws ClientException;
}
