package utc.englishlearning.Encybara.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utc.englishlearning.Encybara.domain.Discussion;
import utc.englishlearning.Encybara.domain.request.discussion.ReqCreateDiscussionDTO;
import utc.englishlearning.Encybara.domain.response.discussion.ResDiscussionDTO;
import utc.englishlearning.Encybara.exception.ResourceNotFoundException;
import utc.englishlearning.Encybara.repository.DiscussionRepository;
import utc.englishlearning.Encybara.repository.LessonRepository;
import utc.englishlearning.Encybara.repository.UserRepository;

import java.util.List;

@Service
public class DiscussionService {

    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Transactional
    public ResDiscussionDTO createDiscussion(ReqCreateDiscussionDTO reqCreateDiscussionDTO) {
        Discussion discussion = new Discussion();
        discussion.setUser(userRepository.findById(reqCreateDiscussionDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));
        discussion.setLesson(lessonRepository.findById(reqCreateDiscussionDTO.getLessonId())
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found")));
        discussion.setContent(reqCreateDiscussionDTO.getContent());
        discussion.setNumLike(0);

        if (reqCreateDiscussionDTO.getParentId() != null) {
            Discussion parentDiscussion = discussionRepository.findById(reqCreateDiscussionDTO.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent discussion not found"));
            discussion.setParentDiscussion(parentDiscussion);
        }

        discussion = discussionRepository.save(discussion);
        return convertToDTO(discussion);
    }

    public List<ResDiscussionDTO> getAllDiscussionsByLessonId(Long lessonId) {
        List<Discussion> discussions = discussionRepository.findByLessonId(lessonId);
        return discussions.stream().map(this::convertToDTO).toList();
    }

    private ResDiscussionDTO convertToDTO(Discussion discussion) {
        ResDiscussionDTO dto = new ResDiscussionDTO();
        dto.setId(discussion.getId());
        dto.setUserId(discussion.getUser().getId());
        dto.setLessonId(discussion.getLesson().getId());
        dto.setContent(discussion.getContent());
        dto.setNumLike(discussion.getNumLike());

        // Lấy danh sách thảo luận con
        List<Discussion> replies = discussionRepository.findByParentDiscussionId(discussion.getId());
        List<ResDiscussionDTO> replyDTOs = replies.stream().map(this::convertToDTO).toList();
        dto.setReplies(replyDTOs); // Cập nhật danh sách replies

        return dto;
    }
}