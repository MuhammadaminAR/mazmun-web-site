package uz.mazmun.mazmunweb.dto;

import lombok.Data;

@Data
public class SocialDTO {
    private String network;
    private String iconClass; // agar bo‘sh kelsa, servis default qo‘yadi
    private String url;
}