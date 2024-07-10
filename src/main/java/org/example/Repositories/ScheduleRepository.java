package org.example.Repositories;

import org.example.Models.ScheduleModel;
import org.example.Models.SchedulesMessageModel;
import org.springframework.data.repository.CrudRepository;

public interface ScheduleRepository extends CrudRepository<ScheduleModel, String> {
}
