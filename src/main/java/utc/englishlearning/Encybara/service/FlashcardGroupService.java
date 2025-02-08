package utc.englishlearning.Encybara.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import utc.englishlearning.Encybara.domain.Flashcard;
import utc.englishlearning.Encybara.domain.FlashcardGroup;
import utc.englishlearning.Encybara.domain.User;
import utc.englishlearning.Encybara.exception.ResourceNotFoundException;
import utc.englishlearning.Encybara.repository.FlashcardGroupRepository;
import utc.englishlearning.Encybara.repository.FlashcardRepository;
import utc.englishlearning.Encybara.repository.UserRepository;

@Service
public class FlashcardGroupService {

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Autowired
    private FlashcardGroupRepository flashcardGroupRepository;

    @Autowired
    private UserRepository userRepository;

    public FlashcardGroup createFlashcardGroup(String name, Long userId) {
        FlashcardGroup group = new FlashcardGroup();
        group.setName(name);

        // Tìm User từ userId
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        group.setUser(user); // Gán đối tượng User cho nhóm

        return flashcardGroupRepository.save(group);
    }

    public void addFlashcardToGroup(Long flashcardId, Long groupId) {
        Flashcard flashcard = flashcardRepository.findById(flashcardId)
                .orElseThrow(() -> new ResourceNotFoundException("Flashcard not found"));
        FlashcardGroup group = flashcardGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        flashcard.setFlashcardGroup(group);
        flashcardRepository.save(flashcard);
    }

    public void removeFlashcardFromGroup(Long flashcardId) {
        Flashcard flashcard = flashcardRepository.findById(flashcardId)
                .orElseThrow(() -> new ResourceNotFoundException("Flashcard not found"));

        // Tìm nhóm "All Flashcards"
        FlashcardGroup allFlashcardsGroup = flashcardGroupRepository.findByName("All Flashcards");
        if (allFlashcardsGroup == null) {
            // Nếu nhóm không tồn tại, có thể tạo mới hoặc xử lý theo cách khác
            allFlashcardsGroup = new FlashcardGroup();
            allFlashcardsGroup.setName("All Flashcards");
            allFlashcardsGroup = flashcardGroupRepository.save(allFlashcardsGroup);
        }

        // Thiết lập nhóm flashcard về nhóm "All Flashcards"
        flashcard.setFlashcardGroup(allFlashcardsGroup);
        flashcardRepository.save(flashcard);
    }

    public void deleteFlashcardGroup(Long groupId) {
        flashcardGroupRepository.deleteById(groupId);
    }

    public void updateFlashcardGroup(Long groupId, String newName) {
        FlashcardGroup group = flashcardGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        group.setName(newName);
        flashcardGroupRepository.save(group);
    }

    public Page<Flashcard> getFlashcardsInGroup(Long groupId, Pageable pageable, String word, Boolean learnedStatus,
            String vietnameseMeaning) {
        return flashcardRepository.findAllByGroupIdAndFilters(groupId, word, learnedStatus, vietnameseMeaning,
                pageable);
    }

}
