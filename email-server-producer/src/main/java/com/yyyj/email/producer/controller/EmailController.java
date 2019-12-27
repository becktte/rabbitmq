package com.yyyj.email.producer.controller;

import com.alibaba.fastjson.JSONObject;
import com.yyyj.email.producer.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description TODO
 * @Author becktte
 * @Date 2019/4/23
 * @Version 1.0
 **/
@Controller
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @RequestMapping("/sendEmail")
    @ResponseBody
    public String sendEmail() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("to", "www.598038524@qq.com");
        jsonObject.put("subject", "email-server-producer");
        jsonObject.put("text", "<html><head></head><body><h1>邮件测试</h1><p>hello!this is mail test。</p></body></html>");
        emailService.sendEmail(jsonObject.toJSONString());
        return jsonObject.toJSONString();
    }
}
