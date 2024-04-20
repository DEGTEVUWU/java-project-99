package hexlet.code.dto.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;
import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class TaskCreateDTO {
//    @NotNull
    private Long index;

    @NotNull
    @JsonProperty("assignee_id")
    private Long assigneeId;

    @NotBlank
    @JsonProperty("title")
    private String name;

//    @NotBlank
    @JsonProperty("content")
    private String description;

    @NotNull
    @JsonProperty("status")
    private String status;

    @JsonProperty("taskLabelIds")
    private Set<Long> taskLabelIds;
}
