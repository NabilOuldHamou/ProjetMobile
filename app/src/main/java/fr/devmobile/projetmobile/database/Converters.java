package fr.devmobile.projetmobile.database;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.List;

public class Converters {

    @TypeConverter
    public static String fromList(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s);
            sb.append(",");
        }
        return sb.toString();
    }

    @TypeConverter
    public static List<String> toList(String data) {
        return Arrays.asList(data.split(","));
    }

}
