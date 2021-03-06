package com.zwdbj.server.adminserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/html")
@ApiIgnore
public class HtmlController {
    @RequestMapping(value = "/shop",method = RequestMethod.GET)
    public void shop(HttpServletResponse response) throws IOException {
        response.sendRedirect("https://j.youzan.com/FB4P6Y");
    }
}
