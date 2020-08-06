package com.project.guli.service.edu.controller;

import com.project.guli.common.base.result.R;
import org.springframework.web.bind.annotation.*;

/**
 * @author wan
 * @create 2020-07-09-19:10
 */
//@CrossOrigin
@RestController
@RequestMapping("user")
public class LoginController {

    @PostMapping("login")
    public R login(){
        return R.ok().data("token", "admin");
    }

    @GetMapping("info")
    public R info(){
        return R.ok()
                .data("name","admin")
                .data("roles","[admin]")
                .data("avatar","https://guli-file-wy.oss-cn-beijing.aliyuncs.com/avatar/2020/07/14/5f44915e-0926-465f-9974-bf8671b28e85.jpg");
    }

    @PostMapping("logout")
    public R logout(){
        return R.ok();
    }
}
