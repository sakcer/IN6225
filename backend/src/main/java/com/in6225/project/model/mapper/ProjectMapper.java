package com.in6225.project.model.mapper;

import com.in6225.project.model.dto.ProjectDTO;
import com.in6225.project.model.entity.Project;
import com.in6225.project.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
//    @Mapping(source = "leader", target = "leader")
//    @Mapping(source = "members", target = "members")
    ProjectDTO toProjectDTO(Project project);

    List<ProjectDTO> toProjectDTOs(List<Project> projects);

    @Mapping(target = "leader", ignore = true)
    void toProject(@MappingTarget Project project, ProjectDTO projectDTO);
    default void updateLeader(Project project, ProjectDTO projectDTO) {
        if (projectDTO.getLeader() != null) {
            User newLeader = new User();
            newLeader.setId(projectDTO.getLeader().getId()); // 只通过 ID 更新 leader
            project.setLeader(newLeader);  // 更新 leader 引用
        }
    }
}
