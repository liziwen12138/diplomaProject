package com.blog.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/get_user/{username}")
    public String getUser(@PathVariable String username){
        return username;
    }
}
