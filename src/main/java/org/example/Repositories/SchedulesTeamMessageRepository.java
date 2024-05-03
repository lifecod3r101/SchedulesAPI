package org.example.Repositories;

import org.example.Models.SchedulesTeamMessagesModel;
import org.example.Models.SchedulesTeamModel;
import org.springframework.data.repository.CrudRepository;

public interface SchedulesTeamMessageRepository extends CrudRepository<SchedulesTeamMessagesModel, String> {

}
