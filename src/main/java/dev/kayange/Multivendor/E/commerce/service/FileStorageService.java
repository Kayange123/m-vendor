package dev.kayange.Multivendor.E.commerce.service;


import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {
    String storeFile(MultipartFile file,  String subfolder, Long id);
    List<String> storeFiles(List<MultipartFile> files,  String subfolder, Long id);
}
