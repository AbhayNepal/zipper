package com.example.zipper;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class Controller {
    @Autowired
    ZipperService zipperService;

    @GetMapping("/")
    HttpEntity home(){
        JSONObject obj = new JSONObject();
        obj.put("success",true);
        obj.put("module","zipper");
        return new ResponseEntity(obj,HttpStatus.OK);
    }
    @RequestMapping(value = "/first",method = RequestMethod.POST)
    HttpEntity getZip() throws IOException {
       return zipperService.downloadZip();
    }

}
