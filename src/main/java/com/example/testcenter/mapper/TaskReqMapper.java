package com.example.testcenter.mapper;

import com.example.testcenter.model.db.entity.Task;
import com.example.testcenter.model.dto.request.TaskInfoReq;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskReqMapper {
    Task toTask (TaskInfoReq taskInfoReq);

    List<Task> toTaskList (List<TaskInfoReq> taskInfoReqs);
}


