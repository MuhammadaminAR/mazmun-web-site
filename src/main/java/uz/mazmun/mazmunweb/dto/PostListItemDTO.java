package uz.mazmun.mazmunweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import uz.mazmun.mazmunweb.entity.PostCategory;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostListItemDTO {
        private String slug;
        private String title;
        private String summary;
        private String publishedAtIso;
        private String coverUrl;
        private String hashTag;
    }
