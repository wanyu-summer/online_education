package com.project.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.guli.service.edu.entity.Chapter;
import com.project.guli.service.edu.entity.Video;
import com.project.guli.service.edu.entity.vo.ChapterVo;
import com.project.guli.service.edu.entity.vo.VideoVo;
import com.project.guli.service.edu.mapper.ChapterMapper;
import com.project.guli.service.edu.mapper.VideoMapper;
import com.project.guli.service.edu.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author wwwy
 * @since 2020-06-21
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Autowired
    private VideoMapper videoMapper;

    @Transactional(rollbackFor = Exception.class)

    //注意video表中有章节id,所以应先删除video表中数据
    @Override
    public boolean removeChaperById(String id) {

        //删除video表中数据
        //先创建queryWrapper，进行where条件的查询
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        //匹配chapter_id对应项
        videoQueryWrapper.eq("chapter_id", id);
        //调用videoMapper的删除功能,传入videoWrapper，进行where条件的删除
        videoMapper.delete(videoQueryWrapper);

        //删除自身数据
        return this.removeById(id);
    }

    //根据课程ID返回一个嵌套列表，在业务层实现
    @Override
    public List<ChapterVo> nestedList(String courseId) {
        List<ChapterVo> chapterVoList = new ArrayList<>();

        //两种方案：第一种，先得到chapter列表，然后根据每一个chapter_id查询video列表，这种要执行1+n个sql语句
        //第二种，直接根据course_id获取到所有章节列表，以及视频频列表

        //根据课程id获取章节信息
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id", courseId);
        chapterQueryWrapper.orderByAsc("sort", "id");
        //获取章节列表
        List<Chapter> chapterList = baseMapper.selectList(chapterQueryWrapper);

        //根据课程ID获取课时信息
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", courseId);
        videoQueryWrapper.orderByAsc("sort", "id");
        //获取课时列表
        List<Video> videoList = videoMapper.selectList(videoQueryWrapper);

        //将数据添加至chapterVoList中，遍历chapter得到相应的chapterVo,添加至chapterVoList
        //对于每一个chapter,创建videoVoList,遍历video得到相应的videoVo，添加至videoVoList,再将该list设置为chapterVo的children

        //填充列表数据：Chapter列表
        for (int i = 0; i < chapterList.size(); i++) {
            //获取chapter元素
            Chapter chapter = chapterList.get(i);

            //创建chapterVo对象
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter,chapterVo);
            //将chapterVo对象添加至chapterVoList
            chapterVoList.add(chapterVo);

            //填充课时数据：Video列表
            //创建videoVoList容器
            List<VideoVo> videoVoList = new ArrayList<>();
            for (int i1 = 0; i1 < videoList.size(); i1++) {
                Video video = videoList.get(i1);

                //判断该课时的chapter.id是否与当前章节id相同
                //注意id为string类型，不能用==，应该用equals
                if(video.getChapterId().equals(chapter.getId())){
                    //创建videoVo对象
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(video, videoVo);
                    videoVoList.add(videoVo);
                }
            }
            chapterVo.setChildren(videoVoList);
        }
        return chapterVoList;
    }
}
