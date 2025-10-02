package com.example.AlbumVerse.utils.constants.AppUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.imgscalr.Scalr;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class AppUtil {
    public static final String get_photo_upload_path(String fileName,String folder_name, Long album_id) throws IOException {
        String path="src\\main\\resources\\static\\uploads\\" + album_id+"\\"+folder_name;
        Files.createDirectories(Paths.get(path));
       
        return new File(path).getAbsolutePath()+"\\"+fileName;
    }

    // Generate thumbnail image
    //Decide the width of the thumbnail
    //Maintain aspect ratio
    //we will reduce the size of the image
    public static BufferedImage getThumbnail(MultipartFile originalFile,Integer width)throws IOException{
        BufferedImage thumbImg=null;
        BufferedImage img=ImageIO.read(originalFile.getInputStream());
        thumbImg=Scalr.resize(img, Scalr.Method.AUTOMATIC,Scalr.Mode.AUTOMATIC,width,Scalr.OP_ANTIALIAS );
        return thumbImg;
    }

    public static Resource getFileResource(long album_id,String folder_name,String file_name)throws IOException{
        String filePath ="src\\main\\resources\\static\\uploads\\" + album_id+"\\"+folder_name+"\\"+file_name;
         
        File file = new File(filePath);
       if (file.exists()) {
           Path path = Paths.get(file.getAbsolutePath());
           return new UrlResource(path.toUri());
       } else {
           return null;
       }
    }

    public static void deleteFileIfExists(Long albumId, String FolderName, String fileName) {
        String filePath = "src\\main\\resources\\static\\uploads\\" + albumId + "\\" + FolderName + "\\" + fileName;
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (Exception e) {
            
        }
        
    }
}
