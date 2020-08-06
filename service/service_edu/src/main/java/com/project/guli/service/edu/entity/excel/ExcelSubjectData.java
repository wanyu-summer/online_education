package com.project.guli.service.edu.entity.excel;

/**
 * @author wan
 * @create 2020-07-16-16:12
 */

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExcelSubjectData {

    @ExcelProperty("一级分类")
    private String levelOneTitle;

    @ExcelProperty("二级分类")
    private String levelTwoTitle;
}
