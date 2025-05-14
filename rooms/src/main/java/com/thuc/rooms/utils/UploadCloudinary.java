package com.thuc.rooms.utils;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "resource_type", "auto",
                    "use_filename", true,
                    "unique_filename", false,
                    "overwrite", true
            );

            // Upload trực tiếp từ byte[]
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    imageFile.getBytes(), uploadParams
            );

            // Tạo URL với các tham số biến đổi
            String publicId = (String) uploadResult.get("public_id");

            String transformedImageUrl = cloudinary.url()
                    .transformation(new Transformation()
                            .crop("pad")
                            .width(300)
                            .height(400)
                            .background("auto:predominant"))
                    .version(uploadResult.get("version").toString()) // thêm version để khác URL
                    .generate(publicId);

            // Trả về URL của ảnh đã biến đổi
            return transformedImageUrl;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }


}
