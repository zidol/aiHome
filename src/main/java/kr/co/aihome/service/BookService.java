package kr.co.aihome.service;

import kr.co.aihome.entity.Book;
import kr.co.aihome.entity.Video;
import kr.co.aihome.repository.BookRepository;
import kr.co.aihome.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookrepository;

    private final VideoService videoservice;

    private final NoteService noteservice;

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void insertData() throws Exception {

        Book book  = Book.builder()
                .name("토비의 스프링")
                .build();


        bookrepository.save(book);

        videoservice.saveVideo();

        noteservice.saveNote();

    }

}
