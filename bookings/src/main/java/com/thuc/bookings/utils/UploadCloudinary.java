package com.thuc.bookings.utils;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class UploadCloudinary {
    @Value("${cloudinary.cloud_name}")
    private String cloudName;
    @Value("${cloudinary.api_key}")
    private String apiKey;
    @Value("${cloudinary.api_secret}")
    private String apiSecret;
    private Cloudinary cloudinary;

    @PostConstruct
    public void init() {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }
    public String uploadCloudinary(MultipartFile imageFile)  {
        try{
            String publicId = UUID.randomUUID().toString();
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "resource_type", "auto",
                    "public_id", publicId,
                    "overwrite", false
            );

            // Upload trực tiếp từ byte[]
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    imageFile.getBytes(), uploadParams
            );

            // Tạo URL với các tham số biến đổi

            String transformedImageUrl = cloudinary.url()
                    .transformation(new Transformation()
                            .gravity("center")
                            .quality("100")) // hoặc "100" nếu muốn chất lượng tối đa
                    .version(uploadResult.get("version").toString())
                    .generate(publicId);

            // Trả về URL của ảnh đã biến đổi
            return transformedImageUrl;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }


}
