package com.foro.foro.Dtos.Output;

import com.foro.foro.Model.Topic;
import java.time.LocalDate;

public record TopicResponseDto(
        String title,
        String message,
        LocalDate creationDate,
        String author,
        Long courseId,
        boolean status
) {

    public TopicResponseDto(Topic topic) {
        this(
                topic.getTitle(),
                topic.getMessage(),
                topic.getCreationDate(),
                topic.getUser().getName(),
                topic.getCourse().getId(),
                topic.getStatus()
        );
    }
}
