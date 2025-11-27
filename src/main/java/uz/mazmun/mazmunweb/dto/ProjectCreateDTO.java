package uz.mazmun.mazmunweb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import uz.mazmun.mazmunweb.enums.ProjectStatus;
import uz.mazmun.mazmunweb.enums.Status;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProjectCreateDTO {
    private String title;
    private String overview;

    private String clientName;
    private String location;
    private String service;     // admin’da “serviceName” deb turibdi – FE’da payloadni “service” deb yuboring
    private String duration;
    private String approach;
    private String startDate;   // "YYYY-MM-DD" ko‘rinishida keladi
    private String endDate;
    private String conclusion;
    private String howWeStarted;

    private ProjectStatus projectStatus;  // PENDING/APPROVED/REJECTED/FINISHED
    private String status;                // DRAFT yoki PUBLISHED (ixtiyoriy)

    private List<ProjectImageCreateDTO> images;
}



