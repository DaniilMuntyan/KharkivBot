package com.example.demo.common_part.utils;

import java.util.ArrayList;
import java.util.List;

public enum District {
    ALEKSEEVKA("Алексеевка"), SALTOVKA("Салтовка"), CENTER("Центр"),
    SEV_SALTOVKA("Сев. Салтовка"), ZASH_UKRAINE("Защ. Украины"), HTZ_ROGAN("ХТЗ / Рогань"),
    ODESSKAYA("Одесская"), PAVLOVO_POLE("Павлово Поле"), HOLODNAYA_GORA("Холодная Гора"),
    KLOCHKOVSKOY("р-н Клочковской"), GAGARINA("Пр. Гагарина"), NOVYIE_DOMA("Новые дома"),
    C_RYNOK_USHD("Ц. Рынок / ЮЖД"), ZHUKOVSKOGO_HAI("Жуковского / ХАИ"),
    ZAVOD_MALYSHEVA("Завод Малышева");

    private final String name;

    District(String name) {
        this.name = name;
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
