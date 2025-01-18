package utc.englishlearning.Encybara.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utc.englishlearning.Encybara.domain.request.lesson.ReqAddQuestionsToLessonDTO;
import utc.englishlearning.Encybara.domain.request.lesson.ReqCreateLessonDTO;
import utc.englishlearning.Encybara.domain.request.lesson.ReqUpdateLessonDTO;
import utc.englishlearning.Encybara.domain.request.lesson.ReqRemoveQuestionFromLessonDTO;
import utc.englishlearning.Encybara.domain.response.lesson.ResLessonDTO;
import utc.englishlearning.Encybara.service.LessonService;

@RestController
@RequestMapping("/api/v1/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @PostMapping
    public ResponseEntity<ResLessonDTO> createLesson(@RequestBody ReqCreateLessonDTO reqCreateLessonDTO) {
        ResLessonDTO lessonDTO = lessonService.createLesson(reqCreateLessonDTO);
        return ResponseEntity.ok(lessonDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResLessonDTO> updateLesson(@PathVariable Long id,
            @RequestBody ReqUpdateLessonDTO reqUpdateLessonDTO) {
        ResLessonDTO lessonDTO = lessonService.updateLesson(id, reqUpdateLessonDTO);
        return ResponseEntity.ok(lessonDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResLessonDTO> getLessonById(@PathVariable Long id) {
        ResLessonDTO lessonDTO = lessonService.getLessonById(id);
        return ResponseEntity.ok(lessonDTO);
    }

    @GetMapping
    public ResponseEntity<Page<ResLessonDTO>> getAllLessons(Pageable pageable) {
        Page<ResLessonDTO> lessons = lessonService.getAllLessons(pageable);
        return ResponseEntity.ok(lessons);
    }

    @PostMapping("/{lessonId}/questions")
    public ResponseEntity<Void> addQuestionsToLesson(@PathVariable Long lessonId,
            @RequestBody ReqAddQuestionsToLessonDTO reqAddQuestionsToLessonDTO) {
        lessonService.addQuestionsToLesson(lessonId, reqAddQuestionsToLessonDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lessonId}/questions")
    public ResponseEntity<Void> removeQuestionFromLesson(@PathVariable Long lessonId,
            @RequestBody ReqRemoveQuestionFromLessonDTO reqRemoveQuestionFromLessonDTO) {
        lessonService.removeQuestionFromLesson(lessonId, reqRemoveQuestionFromLessonDTO.getQuestionId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.ok().build();
    }
}