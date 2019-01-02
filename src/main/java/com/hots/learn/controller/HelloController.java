package com.hots.learn.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hots.common.component.excel.Excel2007Export;
import com.hots.learn.model.User;

@Controller
public class HelloController {

    @Autowired
    private Excel2007Export<User> util;

    @RequestMapping("/downloadExcel")
    @ResponseBody
    public void downloadExcel(HttpServletResponse response) {
        Map<String, List<User>> paramData = new HashMap<String, List<User>>();

        List<User> userList = new ArrayList<User>();
        User user = null;
        for (int i = 0; i < 10; i++) {
            user = new User();
            user.setId(i + 1);
            user.setEmail("123@123.COM" + i);
            user.setPhoneNo("234234234" + i);
            userList.add(user);

        }
        paramData.put("测试", userList);

        util.initHadler(User.class);
//        util.exportExcelPrototype(response, null);
        util.exportExcel("学生", paramData, response);

    }

}
