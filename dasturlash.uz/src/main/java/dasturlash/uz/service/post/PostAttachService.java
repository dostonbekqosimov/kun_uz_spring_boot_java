package dasturlash.uz.service.post;


import dasturlash.uz.dtos.AttachDTO;
import dasturlash.uz.entity.PostAttaches;
import dasturlash.uz.repository.PostAttachesRepository;
import dasturlash.uz.repository.PostRepository;
import dasturlash.uz.service.AttachService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostAttachService {

    private final PostRepository postRepository;
    private final PostAttachesRepository postAttachesRepository;
    private final AttachService attachService;


//    public void create(Integer postId, List<AttachDTO> imageIdList) {
//
//        for (AttachDTO attachId : imageIdList) {
//            PostAttaches entity = new PostAttaches();
//            entity.setPostId(postId);
//            entity.setAttachId(attachId.getId());
//            postAttachesRepository.save(entity);
//        }
//
//    }


    public List<AttachDTO> getAttachList(Integer postId) {

        // get photo ids of the post by postId
        List<String> attachIdList = postAttachesRepository.findAllByPostId(postId);

        // create new photo and its url holder
        List<AttachDTO> attachList = new ArrayList<>();


        for (String attachId : attachIdList) {
            // get photo id and url for this photo by the ids of post's images
            attachList.add(attachService.getDto(attachId));
        }

        // returning ids and urls of images of the post
        return attachList;
    }

    public void merge(Integer postId, List<AttachDTO> newAttachIdList) {
//         old [1,2,3,4]
//         new [1,7]
//         -----------
//         result [1,7]

        // make sure newAttachIdList isn't null, Now we are using this method(merge)
        // for both creating and updating of the post
        if (newAttachIdList == null) {
            newAttachIdList = new ArrayList<>();
        }
        // get photo ids of the post by postId
        List<String> oldAttachIdList = postAttachesRepository.findAllByPostId(postId);

        // Bu loop postni eski rasmlari bo'ylab aylanyabdi
        for (String attachId : oldAttachIdList) {

            // Bu yerda, berilgan yangi rasmlar bilan oldingi postdagi rasmlarni bir biriga teng yoki tengmasligi tekshirilinyabdi
            // agar yangi kelgan rasm idlari eski postdagi rasmlar bilan bir xil bo'lmasa eski rasmlarni o'chirib tashlaymiz, lekin
            // agar ular bir xil bo'lsa hech narsa qilmaymiz, keyingi loop da esa yangi kelgan rasm idlarini db ga qo'shib qo'yyabmiz
            // u loopga borguncha eski photo idlar o'chirib tashlangan bo'ladi.
            if (!exists(attachId, newAttachIdList)) {
                // delete operation {attachId}
                postAttachesRepository.deleteByPostIdAndAttachId(postId, attachId);
            }
        }

        for (AttachDTO newAttach : newAttachIdList) {

            // Agar yangi kelgan photo idlar eski photo idlar ichida bo'lmasa ularni saqlab ketyabmiz
            if (!oldAttachIdList.contains(newAttach.getId())) {
                // save
                PostAttaches entity = new PostAttaches();
                entity.setPostId(postId);
                entity.setAttachId(newAttach.getId());
                postAttachesRepository.save(entity);
            }
        }
    }

    private boolean exists(String attachId, List<AttachDTO> dtoList) {
        for (AttachDTO dto : dtoList) {
            if (dto.getId().equals(attachId)) {
                return true;
            }
        }
        return false;
    }


}
