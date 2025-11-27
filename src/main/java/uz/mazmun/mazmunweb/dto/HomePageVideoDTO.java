package uz.mazmun.mazmunweb.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.mazmun.mazmunweb.entity.HomePageVideo;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomePageVideoDTO {
    private String url;
    private String embedId;
    private Instant updatedAt;

    public static HomePageVideoDTO from(HomePageVideo e) {
        if (e == null) return new HomePageVideoDTO(null, null, null);
        return new HomePageVideoDTO(e.getUrl(), e.getEmbedId(), e.getUpdatedAt());
    }
}
