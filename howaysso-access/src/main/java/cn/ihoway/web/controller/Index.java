package cn.ihoway.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class Index {
	@RequestMapping(value = {"/howay"})
    public String upload() {
	    System.out.println("/");
		 return "/";
    }
}
