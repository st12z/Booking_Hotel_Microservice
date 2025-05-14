package com.thuc.rooms.controller;

import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.utils.UploadCloudinary;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {
    private final UploadCloudinary uploadCloudinary;
    private final Logger log = LoggerFactory.getLogger(UploadController.class);
    @PostMapping(value = "",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponseDto<List<String>>> uploadFile(@RequestPart(name = "images", required = false) List<MultipartFile> images) {
        log.debug("uploadFile");
        List<String> result = new ArrayList<>();
        for(MultipartFile file : images) {
            result.add(uploadCloudinary.uploadCloudinary(file));
        }
        SuccessResponseDto<List<String>> response = SuccessResponseDto.<List<String>>builder()
                .code(200)
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }
}
