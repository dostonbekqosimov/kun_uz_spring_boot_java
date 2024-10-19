package dasturlash.uz.enums;

public enum CategoryEnum {
    O_UZBEKISTON("O'zbekiston", "Узбекистан", "Uzbekistan"),
    JAHON("Жаҳон", "Мир", "World"),
    IQTISODIYOT("Иқтисодиёт", "Экономика", "Economy"),
    AUDIYO("Аудио", "Аудио", "Audio");

    private final String nameUz;
    private final String nameRu;
    private final String nameEn;

    CategoryEnum(String nameUz, String nameRu, String nameEn) {
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
