package com.project.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.guli.common.base.result.R;
import com.project.guli.service.edu.entity.Course;
import com.project.guli.service.edu.entity.Teacher;
import com.project.guli.service.edu.entity.vo.TeacherQueryVo;
import com.project.guli.service.edu.feign.OssFileService;
import com.project.guli.service.edu.mapper.CourseMapper;
import com.project.guli.service.edu.mapper.TeacherMapper;
import com.project.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author wwwy
 * @since 2020-06-21
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    //注入ossFileService
    @Autowired
    private OssFileService ossFileService;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public IPage<Teacher> selectPage(Page<Teacher> pageParam, TeacherQueryVo teacherQueryVo) {
        //显示分页查询列表
//        1.排序：按照sort字段排序
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");

//        2.分页查询
        if (teacherQueryVo == null) {
            return baseMapper.selectPage(pageParam, queryWrapper);
        }
//        3.条件查询
        String name = teacherQueryVo.getName();
        Integer level = teacherQueryVo.getLevel();
        String joinDateBegin = teacherQueryVo.getJoinDateBegin();
        String joinDateEnd = teacherQueryVo.getJoinDateEnd();

        if (!StringUtils.isEmpty(name)) {
            queryWrapper.likeRight("name", name);
        }

        if (level != null) {
            queryWrapper.eq("level", level);
        }

        if (!StringUtils.isEmpty(joinDateBegin)) {
            queryWrapper.ge("join_date", joinDateBegin);//大于等于开始时间
        }

        if (!StringUtils.isEmpty(joinDateEnd)) {
            queryWrapper.le("join_date", joinDateEnd);//小于等于结束时间
        }
        return baseMapper.selectPage(pageParam,queryWrapper);
    }

    @Override
    public List<Map<String, Object>> selectNameList(String key) {
        //即进行组装：select name from edu_teacher where name like '周%'
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("name");
        queryWrapper.likeRight("name", key);

        List<Map<String, Object>> list = baseMapper.selectMaps(queryWrapper);
        return list;
    }

    @Override
    public boolean removeAvatarById(String id) {
        //根据id获取讲师avatar的url
        Teacher teacher = baseMapper.selectById(id);
        //防止并发情况下该teacher已被删除情况
        if(teacher!=null){
            String avatar = teacher.getAvatar();
            //当该讲师没有上传头像时
            if(!StringUtils.isEmpty(avatar)){
                R r = ossFileService.removeFile(avatar);
                return r.getSuccess();//当远程调用失败时，返回false 成功时返回true
            }
        }
        //没有成功时失败
        return false;

    }

    @Override
    public Map<String, Object> selectTeacherInfoById(String id) {

        Teacher teacher = baseMapper.selectById(id);
        //获取该讲师的课程信息，注入courseMapper
        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.eq("teacher_id", id);

        List<Course> courseList = courseMapper.selectList(courseQueryWrapper);
        //再将teacher和courseList绑定到map返回前端
        Map<String, Object> map = new HashMap<>();
        map.put("teacher", teacher);
        map.put("courseList", courseList);
        return map;
    }

    @Cacheable(value = {"index"},key = "'selectHotTeacher'")
    @Override
    public List<Teacher> selectHotTeacher() {

        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        queryWrapper.last("limit 4");

        return baseMapper.selectList(queryWrapper);
    }
}
