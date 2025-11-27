package uz.mazmun.mazmunweb.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UserMessageDTO {
    private Integer id;
    private String fullName;
    private String contact;
    private String message;
    private boolean read;
    private Instant createdAt;
}
