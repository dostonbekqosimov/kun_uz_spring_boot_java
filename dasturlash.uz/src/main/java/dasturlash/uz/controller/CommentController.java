package dasturlash.uz.controller;

import dasturlash.uz.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")

public class CommentController {

    @Autowired
    private CommentService commentService;

}
