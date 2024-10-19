package dasturlash.uz.dtos;

public enum ArticleTypeEnum {
    ASOSIY("Asosiy", "Основной", "Main"),
    MUHARRIR_TANLOVI("Muharrir tanlovi", "Выбор редакции", "Editor's choice"),
    DOLZARB("Dolzarb", "Актуальный", "Urgent"),
    MAQOLA("Maqola", "Статья", "Article"),
    FOTO_YANGILIK("Foto yangilik", "Фото новость", "Photo news"),
    INTERVIEW("Interview", "Интервью", "Interview"),
    BIZNES("Biznes", "Бизнес", "Business"),
    SURUSHTURUV("Surushturuv", "Расследование", "Investigation"),
    VIDEO_YANGILIK("Video yangilik", "Видео новость", "Video news");

    private final String nameUz;
    private final String nameRu;
    private final String nameEn;

    ArticleTypeEnum(String nameUz, String nameRu, String nameEn) {
        this.nameUz = nameUz;
        this.nameRu = nameRu;
        this.nameEn = nameEn;
    }

    public String getNameByLanguage(String lang) {
        switch (lang.toLowerCase()) {
            case "uz":
                return nameUz;
            case "ru":
                return nameRu;
            case "en":
            default:
                return nameEn;
        }
    }
}
