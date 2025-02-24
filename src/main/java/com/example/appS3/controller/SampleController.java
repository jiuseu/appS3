package com.example.appS3.controller;

import com.example.appS3.dto.SampleDTO;
import com.example.appS3.util.LocalUploader;
import com.example.appS3.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/sample")
public class SampleController {

    private final LocalUploader localUploader;
    private final S3Uploader s3Uploader;

    @PostMapping(value = "/upload")
    public List<String> upload(SampleDTO sampleDTO){


        MultipartFile[] files = sampleDTO.getFiles();
        log.info("upload post...........................");
        log.info("File : "+files);

        if(files == null || files.length <= 0){
            log.info("No file..................");
            return null;
        }

        List<String> uploadFilePaths = new ArrayList<>();

        for(MultipartFile file:files){
            uploadFilePaths.addAll(localUploader.uploadLocal(file));
        }

        log.info("--------------------------------------------");
        log.info(uploadFilePaths);

        List<String> s3Paths = uploadFilePaths.stream().map(fileName -> s3Uploader.upload(fileName))
                .collect(Collectors.toList());

        return s3Paths;
    }
}
