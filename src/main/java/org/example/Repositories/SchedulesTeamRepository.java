package org.example.Repositories;

import org.example.Models.SchedulesTeamModel;
import org.springframework.data.repository.CrudRepository;

public interface SchedulesTeamRepository extends CrudRepository<SchedulesTeamModel, String> {
    SchedulesTeamModel findOneByUserName(String userName);

    SchedulesTeamModel findByUserNameContains(String userName);

    SchedulesTeamModel findOneByUserEmail(String userEmail);

    SchedulesTeamModel findByUserEmailContains(String userEmail);

    SchedulesTeamModel findOneByUserBirthDate(String userBirthDate);

    SchedulesTeamModel findByUserBirthDateContains(String userBirthDate);

    SchedulesTeamModel findOneByUserPhoneNumber(String userPhoneNumber);

    SchedulesTeamModel findByUserPhoneNumberContains(String userPhoneNumber);

}
