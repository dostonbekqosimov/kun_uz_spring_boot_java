package dasturlash.uz.service.post;

import dasturlash.uz.dtos.AttachDTO;
import dasturlash.uz.dtos.PostDTO;
import dasturlash.uz.entity.Post;
import dasturlash.uz.repository.AttachRepository;
import dasturlash.uz.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final AttachRepository attachRepository;
    private final PostRepository postRepository;
    private final PostAttachService postAttachService;


    public PostDTO create(PostDTO requestDto) {
        Post newPost = new Post();
        newPost.setTitle(requestDto.getTitle());
        newPost.setContent(requestDto.getContent());

        postRepository.save(newPost);

        // Create the attachments using the PostAttachService
        postAttachService.merge(newPost.getId(), requestDto.getAttachDTOList());

        requestDto.setId(newPost.getId());
        return requestDto;
    }//        postAttachService.update(post.getId(), postDTO.getAttachList());

    public boolean update(Integer postId, PostDTO postDTO) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        postRepository.save(post); // update

        // post attach save
        postAttachService.merge(post.getId(), postDTO.getAttachDTOList());
        return true;
    }


    public PostDTO getById(Integer id) {
        Post post = postRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException("Post not found");
        });

        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());


        // getting photo ids and urls by post id from PostAttachService
        List<AttachDTO> attachDTOList = postAttachService.getAttachList(id);

        // setting the details of photos in the post
        postDTO.setAttachDTOList(attachDTOList);



        // returning postDto with ids and urls of post as well as title and content
        return postDTO;
    }

}
