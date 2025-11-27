// dto/ServiceOfferDTO.java
package uz.mazmun.mazmunweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceOfferDTO {
    private Integer id;
    private String slug;
    private String title;
    private String subtitle;
    private String overview;
    private List<String> bullets;      // UI ro‘yxat ko‘rinishida chiqaradi
    private String quoteText;
    private String quoteAuthor;
    private String quoteRole;
    private String heroPhotoUrl;
    private String quoteAuthorPhotoUrl;
    private String imageLeftUrl;
    private String imageRightUrl;
    private String iconUrl;
    private String status;
    private LocalDateTime publishedAt;
}
