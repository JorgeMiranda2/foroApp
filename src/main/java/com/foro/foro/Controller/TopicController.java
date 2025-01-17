package com.foro.foro.Controller;

import com.foro.foro.Dtos.Input.TopicDto;
import com.foro.foro.Dtos.Output.TopicResponseDto;
import com.foro.foro.Model.Course;
import com.foro.foro.Model.Topic;
import com.foro.foro.Model.User;
import com.foro.foro.Repository.UserRepository;
import com.foro.foro.Service.CourseService;
import com.foro.foro.Service.TopicService;
import com.foro.foro.Service.UserService;
import com.foro.foro.Utils.DuplicatedTopicException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class TopicController {

    private final UserService userService;
    private final CourseService courseService;
    private final TopicService topicService;

    @Autowired
    public TopicController(UserService userService, CourseService courseService, TopicService topicService) {
        this.userService = userService;
        this.courseService = courseService;
        this.topicService = topicService;
    }


    @PostMapping("/topic/{userId}")
        public ResponseEntity<TopicResponseDto> createTopic(@Valid @RequestBody TopicDto topicDto, @PathVariable Long userId, UriComponentsBuilder uriComponentsBuilder){

        User user = userService.getUserById(userId).orElseThrow(()->  new IllegalArgumentException("user not found"));
        Course course = courseService.getCourseById(topicDto.courseId()).orElseThrow(()-> new IllegalArgumentException(("Course not found")));

        if(topicService.checkDuplicated(topicDto.title(), topicDto.message())){
            throw new DuplicatedTopicException("Topico duplicado");

        }


        Topic topic = new Topic();
        topic.setTitle(topicDto.title());
        topic.setMessage(topicDto.message());
        topic.setCourse(course);
        topic.setUser(user);
        topic.setCreationDate(LocalDate.now());
        topic.setStatus(Boolean.TRUE);

        Topic topicResponse = topicService.saveTopic(topic);

        TopicResponseDto topicResponseDto = new TopicResponseDto(topicResponse);
        URI uri = uriComponentsBuilder.path("/api/topic/{id}").buildAndExpand(topicResponse.getId()).toUri();
        return ResponseEntity.created(uri).body(topicResponseDto);
    }

    @GetMapping("/topic")
    private ResponseEntity<Page <TopicResponseDto>> getTopics(Pageable pageable){
        return ResponseEntity.ok(topicService.getTopics(pageable));
    }

    @GetMapping("/topic/{id}")
    private ResponseEntity<TopicResponseDto> getTopics(@PathVariable Long id){
        TopicResponseDto topic = new TopicResponseDto(
                topicService.getTopicById(id).orElseThrow(()->  new IllegalArgumentException("Profile not found"))
        );
        return ResponseEntity.ok(topic);
    }


    @PutMapping("/topic/{id}")
    private ResponseEntity<TopicResponseDto> updateTopic(@Valid @RequestBody TopicDto topicDto, @PathVariable Long id) {
        Topic topic = topicService.getTopicById(id)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found"));

        topic.setTitle(topicDto.title());
        topic.setMessage(topicDto.message());
        topic.setCourse(courseService.getCourseById(topicDto.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found")));

        topicService.saveTopic(topic);

        TopicResponseDto topicResponseDto = new TopicResponseDto(topic);
        return ResponseEntity.ok(topicResponseDto);
    }

    @DeleteMapping("/topic/{id}")
    public ResponseEntity<String> deleteTopic(@PathVariable Long id) {
        boolean isDeleted = topicService.deleteTopicById(id);
        if (isDeleted) {
            return ResponseEntity.ok("Delete successful");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Topic not found, verify the id");
        }
    }


}
