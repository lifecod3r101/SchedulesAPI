package org.example.Repositories;

import org.example.Models.SchedulesRolesModel;
import org.example.Models.SchedulesTeamModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SchedulesRolesRepository extends CrudRepository<SchedulesRolesModel, String> {
    List<SchedulesRolesModel> findByTeam(SchedulesTeamModel teamModel);
}
