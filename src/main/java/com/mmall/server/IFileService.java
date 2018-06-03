package com.mmall.server;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Administrator on 2018/5/15.
 */
public interface IFileService {

    String upload(MultipartFile file, String path);
}
