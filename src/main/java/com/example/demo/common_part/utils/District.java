package com.example.demo.common_part.utils;

import java.util.ArrayList;
import java.util.List;

public enum District {
    ALEKSEEVKA("Алексеевка", "1"), SALTOVKA("Салтовка", "2"), CENTER("Центр", "3"),
    SEV_SALTOVKA("Сев. Салтовка", "4"), ZASH_UKRAINE("Защ. Украины", "5"),
    HTZ_ROGAN("ХТЗ / Рогань", "6"), ODESSKAYA("Одесская", "7"),
    PAVLOVO_POLE("Павлово Поле", "8"), HOLODNAYA_GORA("Холодная Гора", "9"),
    KLOCHKOVSKOY("р-н Клочковской", "10"), GAGARINA("Пр. Гагарина", "11"),
    NOVYIE_DOMA("Новые дома", "12"), C_RYNOK_USHD("Ц. Рынок / ЮЖД", "13"),
    ZHUKOVSKOGO_HAI("Жуковского / ХАИ", "14"), ZAVOD_MALYSHEVA("Завод Малышева", "15");

    private final String name;
    private final String identifier;

    District(final String name, final String identifier) {
        this.name = name;
        this.identifier = "&!!" + identifier + "!!#";
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public static List<String> getAllNames() {
        List<String> s = new ArrayList<>();
        for (District temp: District.values()) {
            s.add(temp.getName());
        }
        return s;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
