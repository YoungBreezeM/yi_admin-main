package com.fw.admin.utils;


import com.fw.admin.entity.FileProperties;
import com.fw.core.exception.FileException;
import com.fw.core.utils.DateUtil;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author yqf
 */
public class FileUtil  {
    

    private  Path fileStorageLocation;
    private String orgUrl;

    public FileUtil() {
    }

    public FileUtil(FileProperties fileProperties, String type) throws FileNotFoundException {

        File classPath = new File(ResourceUtils.getURL("classpath:").getPath());
        orgUrl = fileProperties.getUploadDir()+"/"+type;
        this.fileStorageLocation = Paths.get(
                classPath.getAbsolutePath()+fileProperties.getUploadDir()+"/"+type
        );

        try {
            //如果文件目录不存在就创建目录
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) throws IOException {

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String[] split = originalFileName.split("\\.");
        DateUtil dateUtil = new DateUtil();
        String fileName =split[0]+"_"+dateUtil.createTime("yyyy-MM-dd_HH-mm-ss")+"."+split[1];



        //解析本地文件路径
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        System.out.println(targetLocation);


        //将图片拷贝到指定文件夹下
        Files.copy(file.getInputStream(), targetLocation , StandardCopyOption.REPLACE_EXISTING);

        return orgUrl+"/"+fileName;

    }

    public Boolean deleteFile(String url){
        try{
            File classPath = new File(ResourceUtils.getURL("classpath:").getPath());
            File file = new File(classPath.getAbsolutePath()+url);

            if (!file.exists()) {
                return true;
            } else {
                if(file.delete()){
                    System.out.println(file.getName() + " 文件已被删除！");
                    return true;
                }else{
                    System.out.println("文件删除失败！");
                    return false;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

}