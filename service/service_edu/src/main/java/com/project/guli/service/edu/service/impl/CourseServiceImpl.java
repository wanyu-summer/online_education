package com.project.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.guli.common.base.result.R;
import com.project.guli.service.base.dto.CourseDto;
import com.project.guli.service.edu.entity.*;
import com.project.guli.service.edu.entity.form.CourseInfoForm;
import com.project.guli.service.edu.entity.vo.*;
import com.project.guli.service.edu.feign.OssFileService;
import com.project.guli.service.edu.mapper.*;
import com.project.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private CourseDescriptionMapper courseDescriptionMapper;

    @Autowired
    private OssFileService ossFileService;
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private ChapterMapper chapterMapper;
    @Autowired
    private CourseCollectMapper courseCollectMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private TeacherMapper teacherMapper;

    @Transactional(rollbackFor = Exception.class)

    @Override
    public String saveCourseInfo(CourseInfoForm courseInfoForm) {
        //保存course,将传入的封装对象取出数据保存在course中
        Course course = new Course();

//        course.setTitle(courseInfoForm.getTitle()); 一条一条信息进行设置
        BeanUtils.copyProperties(courseInfoForm, course);//该方法将原类与目的类中的相同属性进行copy
        course.setStatus(Course.COURSE_DRAFT);//设置未发布状态
        baseMapper.insert(course);//将课程信息存入

        //保存CourseDescription
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(course.getId());
        courseDescriptionMapper.insert(courseDescription);

        return course.getId();
    }

    @Override
    public CourseInfoForm getCourseInfoById(String id) {

        //根据id获取course
        Course course = baseMapper.selectById(id);
        if (course == null) {
            return null;
        }

        //根据id获取courseDescription
        //次数不需要判断是否存在的问题，因为与course关联，course存在时自然存在
        CourseDescription courseDescription = courseDescriptionMapper.selectById(id);

        //组装成courseInform
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(course, courseInfoForm);
        courseInfoForm.setDescription(courseDescription.getDescription());
        return courseInfoForm;
    }

    @Override
    public void updateCourseInfoById(CourseInfoForm courseInfoForm) {
        //更新与保存类似，传入一个courseInfoForm，组装成course与courseDescription
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoForm, course);//源 目的
//        course.setStatus(Course.COURSE_DRAFT);
        baseMapper.updateById(course);

        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(course.getId());//course.getId()或者courseInfoFrom.getId()均可
        courseDescriptionMapper.updateById(courseDescription);

    }
/**
 * page,limit负责组装page,实现分页查询，courseQueryVo负责组装queryWrapper的查询，即where条件
 * @param page
 * @param limit
 * @param courseQueryVo
 * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.project.guli.service.edu.entity.vo.CourseVo>
 * @author wwwy
 * @date 2020/7/22 8:52
 */
    @Override
    public Page<CourseVo> selectPage(Long page, Long limit, CourseQueryVo courseQueryVo) {
        QueryWrapper<CourseVo> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.orderByDesc("c.gmt_create");//注意前缀不能丢

        String title = courseQueryVo.getTitle();
        String teacherId = courseQueryVo.getTeacherId();
        String subjectId = courseQueryVo.getSubjectId();
        String subjectParentId = courseQueryVo.getSubjectParentId();
        //将查询条件组装到queryWrapper中

        if (!StringUtils.isEmpty(title)) {
            courseQueryWrapper.like("c.title", title);
        }
        if (!StringUtils.isEmpty(teacherId)) {
            courseQueryWrapper.eq("c.teacher_id", teacherId);
        }
        if (!StringUtils.isEmpty(title)) {
            courseQueryWrapper.eq("c.subject_id", subjectId);
        }
        if (!StringUtils.isEmpty(title)) {
            courseQueryWrapper.eq("c.subject_parent_id", subjectParentId);
        }

        //组装分页
        Page<CourseVo> pageParam = new Page<>(page, limit);

        //执行分页查询，希望得到一个查询列表，一个CourseVo对象列表 ,先生成mapper的接口，在生成mapper的配置文件
        //只需要在mapper层传入封装好的分页组件即可，sql分页条件组装的过程由mp自动完成
        List<CourseVo> records = baseMapper.selectPageByCourseQueryVo(pageParam, courseQueryWrapper);

        //将records设置到pageParam中
        return pageParam.setRecords(records);
    }

    @Override
    public boolean removeCoverById(String id) {
        //根据id课程cover的url
        Course course = baseMapper.selectById(id);

        //防止并发情况下该课程已经被删除
        if (course != null) {
            String cover = course.getCover();
            if (!StringUtils.isEmpty(cover)) {
                R r = ossFileService.removeFile(cover);
                return r.getSuccess();
            }
        }
        //没有返回成功则失败
        return false;
    }

    /**
     * 数据库中外键约束的设置
     *  互联网分布式项目中不允许使用外键与级联更新，一切设计级联的操作不要依赖数据库层，要在业务层解决
     * 如果业务层解决级联删除功能
     *  先删除子表数据，再删除父表数据
     *
     * @param id
     * @return boolean
     * @author wwwy
     * @date 2020/7/21 15:51
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeCourseById(String id) {
        //课程数据表关联了其他表,注入其他表的mapper
        //当course关联了表A，B，A关联了表C，应先删除表C，至于A B两条路线任意
        //先删除video表，再删除chapter表
        //先获取queryWrapper，将id传进然后mapper删除querywrapper
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", id);
        videoMapper.delete(videoQueryWrapper);

        //删除章节
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id", id);
        chapterMapper.delete(chapterQueryWrapper);

        //删除课程简介，由于id也为简介的主键，因此也可以直接delete
//        QueryWrapper<CourseDescription> courseDescriptionQueryWrapper = new QueryWrapper<>();
//        courseDescriptionQueryWrapper.eq("course_id", id);
//        courseDescriptionMapper.delete(courseDescriptionQueryWrapper);
        courseDescriptionMapper.deleteById(id);

        //删除评论
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("course_id", id);
        commentMapper.delete(commentQueryWrapper);

        //删除课程收藏
        QueryWrapper<CourseCollect> courseCollectQueryWrapper = new QueryWrapper<>();
        courseCollectQueryWrapper.eq("course_id", id);
        courseCollectMapper.delete(courseCollectQueryWrapper);

        //删除课程自身
        return this.removeById(id);

//
    }

    @Override
    public CoursePublishVo getCoursePublishById(String id) {
        //根据id获取多表关联的数据,获取语句在mapper.xml中配置
        return baseMapper.selectCoursePublishVoById(id);
    }

    @Override
    public boolean publishCourseById(String id) {
        //根据id获取到course，然后修改course的状态
        Course course = new Course();
        course.setStatus(Course.COURSE_NORMAL);
        course.setId(id);
        //再调用更新方法
        return this.updateById(course);
    }

    @Override
    public List<Course> webSelectList(WebCourseQueryVo webCourseQueryVo) {
        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();

        //查询已发布课程
        courseQueryWrapper.eq("status", Course.COURSE_NORMAL);

        //将查询条件进行对应
        if (!StringUtils.isEmpty(webCourseQueryVo.getSubjectParentId())) {
            courseQueryWrapper.eq("subject_parent_id", webCourseQueryVo.getSubjectParentId());
        }
        if (!StringUtils.isEmpty(webCourseQueryVo.getSubjectId())) {
            courseQueryWrapper.eq("subject_id", webCourseQueryVo.getSubjectId());
        }
        if (!StringUtils.isEmpty(webCourseQueryVo.getBuyCountSort())) {
            courseQueryWrapper.orderByDesc("buy_count");//降序排
        }
        if (!StringUtils.isEmpty(webCourseQueryVo.getGmtCreateSort())) {
            courseQueryWrapper.orderByDesc("gmt_create");
        }
        if (!StringUtils.isEmpty(webCourseQueryVo.getPriceSort())) {
            if (webCourseQueryVo.getType() == null || webCourseQueryVo.getType() == 1) {
                courseQueryWrapper.orderByAsc("price");//type=1时升序
            }else {
                courseQueryWrapper.orderByDesc("price");//在前端，为1时升序，click时应该传入2，type=2时，click时应传入1
            }

        }

        //先将查询条件组装好，再通过basemapper查询
        return baseMapper.selectList(courseQueryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public WebCourseVo selectWebCourseVoById(String id) {
        Course course = baseMapper.selectById(id);
        //更新浏览数
        course.setViewCount(course.getViewCount() + 1);
        //再将course传入baseMapper进行更新
        baseMapper.updateById(course);
        //获取课程信息，即将id传入 在前端网站显示课程信息
        return baseMapper.selectWebCourseVoById(id);
    }

    @Cacheable(value = {"index"},key="'selectHotCourse'")
    @Override
    public List<Course> selectHotCourse() {
        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.orderByAsc("view_count");
        courseQueryWrapper.last("limit 8");

        return baseMapper.selectList(courseQueryWrapper);
    }

    @Override
    public CourseDto getCourseDtoById(String courseId) {
        /*
        并发量不大时的写法
        //根据courseId获取到course
        Course course = baseMapper.selectById(courseId);

        CourseDto courseDto = new CourseDto();
        //不能直接复制！！还涉及到关联查询，courseDto中为teacherName，course中只有teacherId
        //因此还要导入teacherMapper
        courseDto.setTeacherName(teacherMapper.selectById(course.getTeacherId()).getName());

        BeanUtils.copyProperties(course, courseDto);//剩下有的字段进行复制

        return courseDto;*/

        //对于并发量较大的情况，在mapper.xml文件中查询，只与数据库交互一次，效率更高
        return baseMapper.selectCourseDtoById(courseId);
    }


    @Override
    public void updateBuyCountById(String id) {
        Course course = baseMapper.selectById(id);
        course.setBuyCount(course.getBuyCount() + 1);
        baseMapper.updateById(course);
    }
}
