package kr.co.aihome.service;

import kr.co.aihome.entity.Video;
import kr.co.aihome.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;


//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Video saveVideo() {
        Video video = Video.builder()
                .name("비디오 체험")
                .build();
        return videoRepository.save(video);
    }
}
