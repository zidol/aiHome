package kr.co.aihome.repository.user;

import kr.co.aihome.entity.Book;
import kr.co.aihome.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
}
