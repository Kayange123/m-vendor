package dev.kayange.Multivendor.E.commerce.service.implementation;

import dev.kayange.Multivendor.E.commerce.service.FileStorageService;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${application.file.upload.photos-output-dir}")
    private String fileUploadPath;

    @Override
    public String storeFile(@Nonnull MultipartFile file, @Nonnull String subfolder, @Nonnull Long id) {
        final String fileUploadSubPath = subfolder+ File.separator+ id;
        return uploadFile(file, fileUploadSubPath);
    }

    @Override
    public List<String> storeFiles(List<MultipartFile> files,  String subfolder, Long id) {
        List<String> images = new ArrayList<>();

        for (MultipartFile file : files) {
            final String fileUploadSubPath = subfolder + File.separator + id;
            String uploadedFile = uploadFile(file, fileUploadSubPath);
            images.add(uploadedFile);
        }

        return images;
    }

    private String uploadFile(@Nonnull MultipartFile file,@Nonnull String fileUploadSubPath) {
        final String finalUploadPath = fileUploadPath + File.separator + fileUploadSubPath;
        final File uploadDir = new File(finalUploadPath);
        if (!uploadDir.exists()) {
            boolean mkdirs = uploadDir.mkdirs();
            if(!mkdirs){
                log.warn("Could not create directory {}", finalUploadPath);
                return null;
            }
        }

        final String fileName = file.getOriginalFilename();
        final String fileExtension = getFileExtension(fileName);
        final String targetFilePath = finalUploadPath + File.separator + System.currentTimeMillis() + fileExtension;
        Path path = Paths.get(targetFilePath);

        try {
            Files.write(path, file.getBytes());
            log.info("File uploaded successfully to {}", targetFilePath);
            //file.transferTo(path);
            return targetFilePath;
        } catch (Exception e) {
            log.error("Could not store file {}", targetFilePath, e);
        }

        return null;
    }

    private String getFileExtension(String fileName) {
        if(fileName != null && fileName.contains(".") && fileName.lastIndexOf(".") != -1){
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }
}
