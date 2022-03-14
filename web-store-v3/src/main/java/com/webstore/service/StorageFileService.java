package com.webstore.service;

import com.webstore.util.ImageUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageFileService {
    private final String rootDir = "images";
    private final Path rootLocation = Paths.get(rootDir);

    public void init(){
        try {
            if(!Files.exists(rootLocation))
                Files.createDirectory(rootLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void store(String catalogName, MultipartFile file) throws IOException {
        String fullPath = new StringBuilder(rootDir).append("/").append(catalogName).toString();
        String fileName = file.getOriginalFilename().replaceFirst("[.][^.]+$", "");
        Path rootPath = Paths.get(fullPath);
        if(!Files.exists(rootPath))
            Files.createDirectory(rootPath);

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    ImageUtils.save(new StringBuilder(fullPath).append("/").append(fileName).append("512").toString(),
                            ImageUtils.resizeTo128(ImageIO.read(file.getInputStream())));
                    ImageUtils.save(new StringBuilder(fullPath).append("/").append(fileName).append("256").toString(),
                            ImageUtils.resizeTo64(ImageIO.read(file.getInputStream())));
                    ImageUtils.save(new StringBuilder(fullPath).append("/").append(fileName).append("128").toString(),
                            ImageUtils.resizeTo32(ImageIO.read(file.getInputStream())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public Resource load(String catalogName, String filename) throws MalformedURLException {
        Path rootPath = Paths.get(rootDir + "/" + catalogName);
        Path file = rootPath.resolve(filename);
        Resource resource = new UrlResource(file.toUri());
        if(!resource.exists() || !resource.isReadable())
            throw new RuntimeException("Файл не существует");

        return resource;
    }


}
