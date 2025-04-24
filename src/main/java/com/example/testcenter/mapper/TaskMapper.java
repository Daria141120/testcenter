package com.example.testcenter.mapper;

import com.example.testcenter.model.db.entity.Task;
import com.example.testcenter.model.dto.response.TaskInfoResp;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {
    TaskInfoResp toTaskInfoResp (Task task);

    List<TaskInfoResp> toTaskInfoRespList (List<Task> tasks);

}

