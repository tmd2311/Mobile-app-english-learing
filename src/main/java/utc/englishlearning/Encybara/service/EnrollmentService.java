package utc.englishlearning.Encybara.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utc.englishlearning.Encybara.domain.Enrollment;
import utc.englishlearning.Encybara.domain.Lesson_Result;
import utc.englishlearning.Encybara.domain.request.enrollment.ReqCreateEnrollmentDTO;
import utc.englishlearning.Encybara.domain.request.enrollment.ReqCalculateEnrollmentResultDTO;
import utc.englishlearning.Encybara.domain.response.enrollment.ResEnrollmentDTO;
import utc.englishlearning.Encybara.domain.response.enrollment.ResCalculateEnrollmentResultDTO;
import utc.englishlearning.Encybara.exception.ResourceNotFoundException;
import utc.englishlearning.Encybara.repository.EnrollmentRepository;
import utc.englishlearning.Encybara.repository.CourseRepository;
import utc.englishlearning.Encybara.repository.UserRepository;
import utc.englishlearning.Encybara.repository.LessonResultRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LessonResultRepository lessonResultRepository;

    @Transactional
    public ResEnrollmentDTO createEnrollment(ReqCreateEnrollmentDTO reqCreateEnrollmentDTO) {
        Enrollment enrollment = new Enrollment();
        enrollment.setUser(userRepository.findById(reqCreateEnrollmentDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));
        enrollment.setCourse(courseRepository.findById(reqCreateEnrollmentDTO.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found")));
        enrollment.setErrolDate(Instant.now());
        enrollment.setProStatus(false); // proStatus = 0

        enrollment = enrollmentRepository.save(enrollment);
        return convertToDTO(enrollment);
    }

    @Transactional
    public void joinCourse(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        enrollment.setProStatus(true); // proStatus = 1
        enrollment.setErrolDate(Instant.now());
        enrollmentRepository.save(enrollment);
    }

    @Transactional
    public void refuseCourse(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        enrollmentRepository.delete(enrollment);
    }

    public Page<ResEnrollmentDTO> getEnrollmentsByUserId(Long userId, Boolean proStatus, Pageable pageable) {
        Page<Enrollment> enrollments;
        if (proStatus != null) {
            enrollments = enrollmentRepository.findByUserIdAndProStatus(userId, proStatus, pageable);
        } else {
            enrollments = enrollmentRepository.findByUserId(userId, pageable);
        }
        return enrollments.map(this::convertToDTO);
    }

    @Transactional
    public ResCalculateEnrollmentResultDTO calculateEnrollmentResult(ReqCalculateEnrollmentResultDTO reqDto) {
        Enrollment enrollment = enrollmentRepository.findById(reqDto.getEnrollmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        List<Lesson_Result> lessonResults = lessonResultRepository.findByEnrollment(enrollment);

        int totalPoints = lessonResults.stream()
                .mapToInt(Lesson_Result::getTotalPoints)
                .max()
                .orElse(0); // Lấy điểm cao nhất

        double comLevel = lessonResults.stream()
                .mapToDouble(Lesson_Result::getComLevel)
                .average()
                .orElse(0); // Tính mức độ hoàn thành trung bình

        enrollment.setTotalPoints(totalPoints);
        enrollment.setComLevel(comLevel);
        enrollmentRepository.save(enrollment);

        ResCalculateEnrollmentResultDTO response = new ResCalculateEnrollmentResultDTO();
        response.setEnrollmentId(enrollment.getId());
        response.setTotalPoints(totalPoints);
        response.setComLevel(comLevel);

        return response;
    }

    private ResEnrollmentDTO convertToDTO(Enrollment enrollment) {
        ResEnrollmentDTO dto = new ResEnrollmentDTO();
        dto.setId(enrollment.getId());
        dto.setUserId(enrollment.getUser().getId());
        dto.setCourseId(enrollment.getCourse().getId());
        dto.setErrolDate(enrollment.getErrolDate());
        dto.setProStatus(enrollment.isProStatus());
        return dto;
    }
}