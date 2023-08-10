package com.github.laefye.minichat.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Примечание: данный класс был создан потому что <прекрасные разработчики> не смогли сделать всё использовать одним
//  типом как или хотя бы в GSON
//  поэтому здесь есть класс Хуита, которая переводить MemorySection в хотя бы Map
// TODO: Сделать нормальный конфиги, и старатся не использовать эту Хуиту
public class Huita {
    public static Map<String, Object> getMap(Map<String, Object> originalMap) {
        var map = new HashMap<String, Object>();
        for (var entry : originalMap.entrySet()) {
            if (entry.getValue() instanceof MemorySection section) {
                map.put(entry.getKey(), getMapFromSection(section));
            } else if (entry.getValue() instanceof ArrayList<?> list) {
                map.put(entry.getKey(), getList(list));
            } else {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return map;
    }

    private static ArrayList<?> getList(ArrayList<?> original) {
        var list = new ArrayList<>();
        for (var element : original) {
            if (element instanceof MemorySection section) {
                list.add(getMapFromSection(section));
            } else if (element instanceof ArrayList<?> anotherList) {
                list.add(getList(anotherList));
            } else {
                list.add(element);
            }
        }
        return list;
    }

    private static Map<String, Object> getMapFromSection(MemorySection section) {
        return getMap(section.getValues(false));
    }
}
