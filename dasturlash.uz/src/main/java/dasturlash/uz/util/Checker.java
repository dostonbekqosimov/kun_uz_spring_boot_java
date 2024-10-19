//package dasturlash.uz.util;
//
//import dasturlash.uz.exceptions.DataExistsException;
//import dasturlash.uz.repository.ArticleTypeRepository;
//import dasturlash.uz.repository.CategoryRepository;
//import dasturlash.uz.repository.RegionRepository;
//import lombok.RequiredArgsConstructor;
//
//@RequiredArgsConstructor
//public class Checker {
//
//    private final CategoryRepository categoryRepository;
//    private final ArticleTypeRepository articleTypeRepository;
//    private final RegionRepository repository;
//
//    public static void existsByAnyName(String nameUz, String nameRu, String nameEn) {
//        boolean isExist = articleTypeRepository.existsByNameUzOrNameRuOrNameEn(nameUz, nameRu, nameEn);
//        if (isExist) {
//            throw new DataExistsException("Article Type with name: " + nameUz + " exists");
//        }
//    }
//}
