package com.foro.foro.Service;

import com.foro.foro.Dtos.Output.TopicResponseDto;
import com.foro.foro.Model.Topic;
import com.foro.foro.Repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class TopicService {
    private final TopicRepository topicRepository;

    @Autowired
    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }


    public Topic saveTopic(Topic topic){
        return topicRepository.save(topic);
    }

    public boolean checkDuplicated(String title, String message) {
        return topicRepository.existsByTitleAndMessage(title, message);
    }


    public Page<TopicResponseDto> getTopics(Pageable pageable) {
        return topicRepository.findAll(pageable).map(TopicResponseDto::new);

    }

    public Optional<Topic> getTopicById(Long id) {
        return topicRepository.findById(id);
    }


    @Transactional
    public boolean deleteTopicById(Long id) {
        if(topicRepository.existsById(id)){
            topicRepository.deleteById(id);
            if(!topicRepository.existsById(id)){
                return true;
            }
        }
        return false;
    }
}
