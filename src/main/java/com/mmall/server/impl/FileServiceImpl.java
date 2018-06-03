package com.mmall.server.impl;

import com.google.common.collect.Lists;
import com.mmall.server.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Administrator on 2018/5/15.
 */

@Service("iFileService")
public class FileServiceImpl implements IFileService{

    private Logger logger= LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file,String path){
        String fileName= file.getOriginalFilename();
        //获取扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        //上传文件的名字
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
        logger.info("开始上传文件，上传的文件名：{},上传的路径是：{},新文件名是：{}",fileName,path,uploadFileName);

        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setReadable(true);
            fileDir.mkdirs();
        }

        File targetFile= new File(path,uploadFileName);

        try {
            file.transferTo(targetFile);
            //上传成功
            //todo 上传到FTP
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //已上传到服务器
            //todo 上传完删除upload下的文件
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常",e);
            return null;
        }
        return targetFile.getName();
    }
}
