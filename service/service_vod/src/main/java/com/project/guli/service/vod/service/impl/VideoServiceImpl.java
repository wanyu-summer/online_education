package com.project.guli.service.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.project.guli.common.base.result.ResultCodeEnum;
import com.project.guli.service.base.exception.GuliException;
import com.project.guli.service.vod.service.VideoService;
import com.project.guli.service.vod.util.AliyunVodSDKUtils;
import com.project.guli.service.vod.util.VodProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.List;

/**
 * @author wan
 * @create 2020-07-22-17:40
 */
@Service
@Slf4j
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VodProperties vodProperties;

    @Override
    public String uploadVideo(InputStream inputStream, String originalFileName) {
        String title = originalFileName.substring(0, originalFileName.lastIndexOf("."));

        UploadStreamRequest request = new UploadStreamRequest(
                vodProperties.getKeyid(),
                vodProperties.getKeysecret(),
                title, originalFileName, inputStream);

        UploadVideoImpl upLoader = new UploadVideoImpl();
        UploadStreamResponse response = upLoader.uploadStream(request);

        String videoId = response.getRequestId();
        //判断videoid是否存在，存在则代表上传成功，否则失败
        if (StringUtils.isEmpty(videoId)) {
            log.error("阿里云视频上传失败", response.getCode() + "-" + response.getMessage());
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
        }
        return videoId;
    }

    @Override
    public void removeVideo(String videoId) throws ClientException {
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                vodProperties.getKeyid(),
                vodProperties.getKeysecret());

        DeleteVideoRequest request = new DeleteVideoRequest();
        request.setVideoIds(videoId);
        client.getAcsResponse(request);
    }

    @Override
    public void removeVideoByIdList(List<String> videoIdList) throws ClientException {
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                vodProperties.getKeyid(),
                vodProperties.getKeysecret());

        DeleteVideoRequest request = new DeleteVideoRequest();

        int size = videoIdList.size();//id列表的长度
        StringBuffer idListStr = new StringBuffer(); //组装好的字符串
        for (int i = 0; i < size; i++) {
            idListStr.append(videoIdList.get(i));
//            if(i == size - 1 ) //假设 size <= 20
            if(i == size - 1 || i % 20 == 19){//当size小于20时，第一个判断条件成立，否则第二个条件成立
                //删除
                //支持传入多个视频ID，多个用逗号分隔。id不能超过20个
//                log.info("idListStr = " + idListStr.toString());
                request.setVideoIds(idListStr.toString());
                client.getAcsResponse(request);
                //重置idListStr，重新进行拼接
                idListStr = new StringBuffer();
            }else if(i % 20 < 19){//每20个视频进行删除
                idListStr.append(",");
            }
        }
    }

    @Override
    public String getPlayAuth(String videoSourceId) throws ClientException {

        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                vodProperties.getKeyid(),
                vodProperties.getKeysecret());

        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();//创建请求对象
        request.setVideoId(videoSourceId);//设置请求参数

        GetVideoPlayAuthResponse response = client.getAcsResponse(request);//发送请求得到响应

        return response.getPlayAuth();
    }
}
