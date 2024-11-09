package dasturlash.uz.service.article;

import dasturlash.uz.dtos.AttachDTO;
import dasturlash.uz.entity.article.ArticleAttach;
import dasturlash.uz.repository.ArticleAttachRepository;
import dasturlash.uz.service.AttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleAttacheService {

    @Autowired
    private ArticleAttachRepository articleAttachRepository;
    @Autowired
    private AttachService attachService;


    public List<AttachDTO> getAttachList(String articleId) {

        // get photo ids of the post by postId
        List<String> attachIdList = articleAttachRepository.findAllByArticleId(articleId);

        // create new photo and its url holder
        List<AttachDTO> attachList = new ArrayList<>();


        for (String attachId : attachIdList) {
            // get photo id and url for this photo by the ids of post's images
            attachList.add(attachService.getDto(attachId));
        }

        // returning ids and urls of images of the post
        return attachList;
    }

    public void merge(String articleId , List<AttachDTO> newAttachIdList) {


        // make sure newAttachIdList isn't null, Now we are using this method(merge)
        // for both creating and updating of the post
        if (newAttachIdList == null) {
            newAttachIdList = new ArrayList<>();
        }
        // get photo ids of the post by articleId 
        List<String> oldAttachIdList = articleAttachRepository.findAllByArticleId (articleId );

        // Bu loop postni eski rasmlari bo'ylab aylanyabdi
        for (String attachId : oldAttachIdList) {

            // Bu yerda, berilgan yangi rasmlar bilan oldingi postdagi rasmlarni bir biriga teng yoki tengmasligi tekshirilinyabdi
            // agar yangi kelgan rasm idlari eski postdagi rasmlar bilan bir xil bo'lmasa eski rasmlarni o'chirib tashlaymiz, lekin
            // agar ular bir xil bo'lsa hech narsa qilmaymiz, keyingi loop da esa yangi kelgan rasm idlarini db ga qo'shib qo'yyabmiz
            // u loopga borguncha eski photo idlar o'chirib tashlangan bo'ladi.
            if (!exists(attachId, newAttachIdList)) {
                // delete operation {attachId}
                articleAttachRepository.deleteByArticleIddAndAttachId(articleId, attachId);
            }
        }

        for (AttachDTO newAttach : newAttachIdList) {

            // Agar yangi kelgan photo idlar eski photo idlar ichida bo'lmasa ularni saqlab ketyabmiz
            if (!oldAttachIdList.contains(newAttach.getId())) {
                // save
                ArticleAttach entity = new ArticleAttach();
                entity.setArticleId (articleId );
                entity.setAttachId(newAttach.getId());
                articleAttachRepository.save(entity);
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
