package com.example.bot.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FileUploadController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/upload")
    public String uploadFiles(@RequestParam("files") MultipartFile[] files, 
                              @RequestParam("uploadDir") String uploadDir, 
                              Model model) {
        List<String> filePaths = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                // Save the file locally
                Path filePath = saveFileLocally(file, uploadDir);
                filePaths.add(filePath.toString());
            } catch (IOException e) {
                e.printStackTrace();
                filePaths.add("Failed to upload " + file.getOriginalFilename());
            }
        }

        model.addAttribute("filePaths", filePaths);
        return "result";
    }

    private Path saveFileLocally(MultipartFile file, String uploadDir) throws IOException {
        Path copyLocation = Paths.get(uploadDir + "/" + file.getOriginalFilename());
        Files.createDirectories(copyLocation.getParent()); // Ensure the directory exists
        Files.copy(file.getInputStream(), copyLocation);
        return copyLocation;
    }
}
