package dasturlash.uz.controller;


import dasturlash.uz.dtos.PostDTO;
import dasturlash.uz.service.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<PostDTO> create(@RequestBody PostDTO requestDto) {

        return ResponseEntity.ok().body(postService.create(requestDto));
    }

    @GetMapping("/{postId}")
    public PostDTO getById(@PathVariable("postId") Integer postId) {

        return postService.getById(postId);
    }

    @PutMapping("/{postId}")
    public Boolean update(@PathVariable("postId") Integer id,
                          @RequestBody PostDTO postDTO) {
        Boolean result = postService.update(id, postDTO);
        return result;
    }


}
