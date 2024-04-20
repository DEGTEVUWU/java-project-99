package hexlet.code.dto.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class TaskUpdateDTO {
    @JsonProperty("assignee_id")
    private JsonNullable<Long> assigneeId;

    @NotBlank
    @JsonProperty("title")
    private JsonNullable<String> name;

    @JsonProperty("content")
    private JsonNullable<String> description;

    @NotNull
    @JsonProperty("status")
    private JsonNullable<String> status;

    @JsonProperty("labels")
    private JsonNullable<Set<Long>> labelIds;

}
