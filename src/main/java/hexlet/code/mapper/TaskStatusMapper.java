package hexlet.code.mapper;


import hexlet.code.dto.taskStatuses.TaskStatusCreateDTO;
import hexlet.code.dto.taskStatuses.TaskStatusDTO;
import hexlet.code.dto.taskStatuses.TaskStatusUpdateDTO;
import hexlet.code.model.TaskStatus;
import org.mapstruct.*;

@Mapper(
        uses = { ReferenceMapper.class, JsonNullableMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskStatusMapper {
    public abstract TaskStatus map(TaskStatusCreateDTO dto);
    public abstract TaskStatusDTO map(TaskStatus model);
    public abstract void update(TaskStatusUpdateDTO dto, @MappingTarget TaskStatus model);
}
