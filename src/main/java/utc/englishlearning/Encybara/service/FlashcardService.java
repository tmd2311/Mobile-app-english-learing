package utc.englishlearning.Encybara.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import utc.englishlearning.Encybara.domain.Flashcard;
import utc.englishlearning.Encybara.domain.FlashcardGroup;
import utc.englishlearning.Encybara.domain.response.dictionary.ResWord;
import utc.englishlearning.Encybara.domain.response.flashcard.ResFlashcardDTO;
import utc.englishlearning.Encybara.exception.ResourceNotFoundException;
import utc.englishlearning.Encybara.repository.FlashcardRepository;
import utc.englishlearning.Encybara.repository.FlashcardGroupRepository;
import java.time.Instant;
import java.util.List;

@Service
public class FlashcardService {

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Autowired
    private FlashcardGroupRepository flashcardGroupRepository;

    @Autowired
    private GoogleTranslateService googleTranslateService;

    @Autowired
    private DictionaryService dictionaryService;

    public ResFlashcardDTO createFlashcard(String word) {
        Flashcard flashcard = new Flashcard();
        flashcard.setWord(word);
        flashcard.setLearnedStatus(false);
        flashcard.setAddedDate(Instant.now());

        // Sử dụng GoogleTranslateService để dịch
        String vietnameseMeaning = googleTranslateService.translate(word, "vi").block();
        flashcard.setVietNameseMeaning(vietnameseMeaning);

        // Sử dụng DictionaryService để lấy các thuộc tính khác
        List<ResWord> definitions = dictionaryService.getWordDefinition(word).block();
        flashcard.setDefinitions(definitions.toString());

        // Thêm vào nhóm "All flashcards"
        FlashcardGroup allFlashcardsGroup = flashcardGroupRepository.findByName("All flashcards");
        flashcard.setFlashcardGroup(allFlashcardsGroup);

        flashcardRepository.save(flashcard);

        ResFlashcardDTO res = new ResFlashcardDTO();
        res.setId(flashcard.getId());
        res.setWord(flashcard.getWord());
        res.setVietNameseMeaning(flashcard.getVietNameseMeaning());
        res.setDefinitions(flashcard.getDefinitions());
        res.setAddedDate(flashcard.getAddedDate());
        res.setLearnedStatus(flashcard.isLearnedStatus());

        return res;
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
        flashcard.setFlashcardGroup(null);
        flashcardRepository.save(flashcard);
    }

    public void deleteFlashcard(Long flashcardId) {
        flashcardRepository.deleteById(flashcardId);
    }

    public void markFlashcardAsLearned(Long flashcardId) {
        Flashcard flashcard = flashcardRepository.findById(flashcardId)
                .orElseThrow(() -> new ResourceNotFoundException("Flashcard not found"));
        flashcard.setLearnedStatus(true);
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

    public ResFlashcardDTO getFlashcard(Long flashcardId) {
        Flashcard flashcard = flashcardRepository.findById(flashcardId)
                .orElseThrow(() -> new ResourceNotFoundException("Flashcard not found"));
        flashcard.setLastReviewed(Instant.now());
        flashcardRepository.save(flashcard);

        ResFlashcardDTO res = new ResFlashcardDTO();
        res.setId(flashcard.getId());
        res.setWord(flashcard.getWord());
        res.setVietNameseMeaning(flashcard.getVietNameseMeaning());
        res.setDefinitions(flashcard.getDefinitions());
        res.setAddedDate(flashcard.getAddedDate());
        res.setLearnedStatus(flashcard.isLearnedStatus());
        res.setLastReviewed(flashcard.getLastReviewed());

        return res;
    }

    public Page<Flashcard> getFlashcardsInGroup(Long groupId, Pageable pageable, String word, Boolean learnedStatus,
            String vietnameseMeaning) {
        return flashcardRepository.findAllByGroupIdAndFilters(groupId, word, learnedStatus, vietnameseMeaning,
                pageable);
    }

    public List<Flashcard> getAllFlashcardsSortedByLatest() {
        return flashcardRepository.findAllByOrderByLastReviewedDesc();
    }

    public List<Flashcard> getAllFlashcardsSortedByOldest() {
        return flashcardRepository.findAllByOrderByLastReviewedAsc();
    }
}