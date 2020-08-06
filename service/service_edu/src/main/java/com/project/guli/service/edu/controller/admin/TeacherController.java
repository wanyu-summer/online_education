package com.project.guli.service.edu.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.guli.common.base.result.R;
import com.project.guli.service.edu.entity.Teacher;
import com.project.guli.service.edu.entity.vo.TeacherQueryVo;
import com.project.guli.service.edu.feign.OssFileService;
import com.project.guli.service.edu.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *     基于后台管理器的后台接口
 *  讲师 前端控制器
 * </p>
 * @author wan
 * @create 2020-06-21-22:58
 */
//@CrossOrigin//允许跨域
@Api(tags = {"讲师管理"})
@RestController
@RequestMapping("admin/edu/teacher")
@Slf4j
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private OssFileService ossFileService;

    @ApiOperation("所有讲师列表")
    @GetMapping("list")
    public R listAll(){ //由于R的方法均返回R 因此可以链式
        List<Teacher> list = teacherService.list();
        return R.ok().data("items",list);
    }

    @ApiOperation(value = "根据ID删除讲师",notes = "根据ID删除讲师，逻辑删除")
    @DeleteMapping("remove/{id}")
    public R removeById(@ApiParam(value="讲师ID",required = true) @PathVariable String id){
        //删除讲师头像
        teacherService.removeAvatarById(id);

        //删除讲师
        boolean result = teacherService.removeById(id);//表示是否删除成功
        if(result){
            return R.ok().message("删除成功");
        }else {
            return R.error().message("数据不存在，删除失败");
        }
    }
    //api参数中添加value，required=true后，必须填这两个数据，否则提交不了，当没有添加，没有填数据查询时后台报错
    @ApiOperation("讲师分页列表")
    @GetMapping("list/{page}/{limit}")
    public R listPage(@ApiParam(value="当前页码",required = true) @PathVariable Long page,
                      @ApiParam(value="每页记录数",required = true) @PathVariable Long limit,
                      @ApiParam("讲师列表查询对象") TeacherQueryVo teacherQueryVo){

        Page<Teacher> pageParam = new Page<>(page, limit);
        IPage<Teacher> pageModel = teacherService.selectPage(pageParam,teacherQueryVo);
        List<Teacher> records = pageModel.getRecords();
        long total = pageModel.getTotal();//总记录数
        return R.ok().data("total",total).data("rows",records);
    }

    @ApiOperation("新增讲师")
    @PostMapping("save")
    public R save(@ApiParam(value = "讲师对象",required = true) @RequestBody Teacher teacher){
//        teacher.setGmtModified(new Date());当该字段自动填充时，不需要加，否则就要加
        teacherService.save(teacher);
        return R.ok().message("保存成功");
    }

    @ApiOperation("更新讲师")
    @PutMapping("update")
    public R update(@ApiParam("讲师对象") @RequestBody Teacher teacher){
        boolean result = teacherService.updateById(teacher);
        if(result) return R.ok().message("更新成功");
        else return R.error().message("数据不存在");
    }

    @ApiOperation("根据ID获取讲师信息")
    @GetMapping("get/{id}")
    public R get(@ApiParam(value = "讲师对象",required = true) @PathVariable String id){
        Teacher teacher = teacherService.getById(id);
        //防止刚取出id数据被删除
        if(teacher!=null) return R.ok().data("item", teacher);
        else return R.error().message("数据不存在");
    }

    @ApiOperation(value = "根据ID列表删除讲师")
    @DeleteMapping("batch-remove")
    public R removeRows(
            @ApiParam(value = "讲师ID列表",required = true) @RequestBody List<String> idList){
        boolean result = teacherService.removeByIds(idList);//表示是否删除成功
        if(result){
            return R.ok().message("删除成功");
        }else {
            return R.error().message("数据不存在，删除失败");
        }
    }

    @ApiOperation("根据关键字查询讲师列表")
    @GetMapping("list/name/{key}")
    public R selectNameByKey(
            @ApiParam(value = "关键字",required = true)
            @PathVariable String key){ //由于R的方法均返回R 因此可以链式
        List<Map<String,Object>> nameList = teacherService.selectNameList(key);
        return R.ok().data("teacher_list",nameList);
    }

    //对远程方法进行调用
    @ApiOperation("测试服务调用")
    @GetMapping("test")
    public R test(){
        ossFileService.test();
        log.info("edu执行成功");
        return R.ok();
    }

    //Tomcat测试并发
    @ApiOperation("测试并发")
    @GetMapping("test_concurrent")
    public R testConcurrent(){
        log.info("test_concurrent");
        return R.ok();
    }

    @GetMapping("/message1")
    public String message1(){
        return "message1";
    }

    //
    @GetMapping("/message2")
    public String message2(){
        return "message2";
    }
}
