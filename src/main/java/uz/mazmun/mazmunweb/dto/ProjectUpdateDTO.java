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
@AllArgsConstructor
@NoArgsConstructor
public class ProjectUpdateDTO {
    private String title;
    private String overview;
    private String clientName;
    private String location;
    private String service;
    private String duration;
    private String approach;
    private String startDate;
    private String endDate;
    private String howWeStarted;
    private String conclusion;
    private ProjectStatus projectStatus;
    private String status;
    private List<ProjectImageCreateDTO> images;
}
