package org.example.Repositories;

import org.example.Models.SchedulesAdminsModel;
import org.springframework.data.repository.CrudRepository;

public interface SchedulesAdminsRepository extends CrudRepository<SchedulesAdminsModel, String> {
    SchedulesAdminsModel findOneByEmailAddress(String adminEmail);
}
