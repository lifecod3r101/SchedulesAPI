package org.example.Repositories;

import org.example.Models.SchedulesMessageModel;
import org.springframework.data.repository.CrudRepository;

public interface SchedulesMessageRepository extends CrudRepository<SchedulesMessageModel, String> {
    SchedulesMessageModel findOneByMessageTitle(String messageTitle);
    SchedulesMessageModel findByMessageTitleContains(String messageTitle);

    SchedulesMessageModel findOneByMessageContent(String messageContent);
    SchedulesMessageModel findByMessageContentContains(String messageContent);

}
