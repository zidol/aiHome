package kr.co.aihome.controller;

import kr.co.aihome.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final BookService bookservice;

    @PostMapping("/book/save")
    public String saveBook() throws Exception {
        bookservice.insertData();

        return "success";
    }
}
