package com.project.guli.service.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.guli.service.edu.entity.Subject;
import com.project.guli.service.edu.entity.excel.ExcelSubjectData;
import com.project.guli.service.edu.mapper.SubjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wan
 * @create 2020-07-16-16:16
 */
@Slf4j
@AllArgsConstructor//全参构造函数
@NoArgsConstructor//无参构造函数
public class ExcelSubjectDataListener extends AnalysisEventListener<ExcelSubjectData> {

    //ExcelSubjectDataListener不是spring的容器组件 不是自动生成的 不能自动注入 @autowired
    //是在SubjectServiceImpl 实现类中new的对象 可以将mapper作为参数传入ExcelSubjectDataListener的构造函数中，在SubjectServiceImpl中进行SubjectMapper注入
    private SubjectMapper subjectMapper;

    /*public ExcelSubjectDataListener(){}

    //创建一个有参的构造函数，传入SubjectMapper
    public ExcelSubjectDataListener(SubjectMapper subjectMapper) {
        this.subjectMapper=subjectMapper;
    }*/
    /**
     * 遍历每一行数据
     * @param data
     * @param context
     * @return void
     * @author wwwy
     * @date 2020/7/16 16:21
     */
    @Override
    public void invoke(ExcelSubjectData data, AnalysisContext context) {
        log.info("解析到一条记录：{}",data);
        //处理读取出来的数据
        String levelOneTitle = data.getLevelOneTitle();
        String levelTwoTitle = data.getLevelTwoTitle();
        log.info("levelOneTitle: {}", levelOneTitle);
        log.info("levelTwoTitle: {}", levelTwoTitle);

        //组装数据subject

        //组装前判断是否存在该条数据
        Subject subjectLevelOne = getByTitle(levelOneTitle);
        String parentId=null;
        if (subjectLevelOne == null) {
            //组装一级类别
            //这时有几个二级类别就会重复几次，由于在excel中按照二级类别数量的列数
            Subject subject = new Subject();
            subject.setParentId("0");
            subject.setTitle(levelOneTitle);
            //将数据存储到数据库，subjectMapper.insert()
            subjectMapper.insert(subject);
            parentId = subject.getId();
        }else{
            parentId = subjectLevelOne.getId();
        }

//        组装二级类别
        //同样先进行判断是否已经存在该数据
        Subject subjectLevelTwo = getSubByTitle(levelTwoTitle, parentId);
        if(subjectLevelTwo==null){
            Subject subject = new Subject();
            subject.setParentId(parentId);
            subject.setTitle(levelTwoTitle);
//            subject.setSort();
            subjectMapper.insert(subject);
        }


    }
    //数据收尾工作
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("全部数据解析完成");
    }

    //添加一个辅助方法根据标题查找是否已经存在该条数据,针对一级类别
    private Subject getByTitle(String title){
        QueryWrapper<Subject> subjectQueryWrapper = new QueryWrapper<>();
        subjectQueryWrapper.eq("title", title);
        subjectQueryWrapper.eq("parent_id", "0");

        return subjectMapper.selectOne(subjectQueryWrapper);//存在时返回一个对象，不存在时返回null值
    }

    /**
     *
     * 根据类别名称和父id查询数据是否存在
     * @param title
     * @param parentId
     * @return com.project.guli.service.edu.entity.Subject
     * @author wwwy
     * @date 2020/7/16 18:04
     */
    private Subject getSubByTitle(String title,String parentId){
        QueryWrapper<Subject> subjectQueryWrapper = new QueryWrapper<>();
        subjectQueryWrapper.eq("title", title);
        subjectQueryWrapper.eq("parent_id",parentId);

        return subjectMapper.selectOne(subjectQueryWrapper);//存在时返回一个对象，不存在时返回null值
    }
}
