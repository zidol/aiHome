package kr.co.aihome.service;

import kr.co.aihome.entity.Book;
import kr.co.aihome.entity.Note;
import kr.co.aihome.repository.BookRepository;
import kr.co.aihome.repository.user.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository notesRepository;

    private final VideoService videoservice;

//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveNote() throws Exception {

        Note note  = Note.builder()
                .name("나의 노트")
                .build();

        notesRepository.save(note);

        throw new Exception();


    }

}
