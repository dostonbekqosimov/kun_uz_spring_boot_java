package dasturlash.uz.enums;

public enum RegionEnum {
    ANDIJON("Andijon", "Андижан", "Andijan"),
    BUXORO("Buxoro", "Бухара", "Bukhara"),
    FARG_ONA("Farg'ona", "Фергана", "Fergana"),
    JIZZAX("Jizzax", "Джизак", "Jizzakh"),
    QASHQADARYO("Qashqadaryo", "Кашкадарья", "Qashqadarya"),
    NAVOIY("Navoiy", "Навоий", "Navoi"),
    SAMARQAND("Samarqand", "Самарканд", "Samarkand"),
    SIRDARYO("Sirdaryo", "Сырдарья", "Sirdarya"),
    SURXONDARYO("Surxondaryo", "Сурхандарья", "Surkhandarya"),
    TOSHKENT("Toshkent", "Ташкент", "Tashkent"),
    XOREZM("Xorazm", "Хорезм", "Khorezm"),
    QORAQALPOG_ISTON("Qoraqalpog'iston", "Каракалпакстан", "Karakalpakstan");

    private final String nameUz;
    private final String nameRu;
    private final String nameEn;

    RegionEnum(String nameUz, String nameRu, String nameEn) {
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
