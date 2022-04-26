package com.blog.demo.strategy;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface UploadStrategy {

    String uploadFile(MultipartFile file, String path);
}
