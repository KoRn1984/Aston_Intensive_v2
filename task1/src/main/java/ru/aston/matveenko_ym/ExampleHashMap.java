package ru.aston.matveenko_ym;

import ru.aston.matveenko_ym.service.CustomHashMap;

import java.util.HashMap;
import java.util.Map;

public class ExampleHashMap {

    public static void main(String[] args) {
        CustomHashMap<String, String> hashMap = new CustomHashMap<>(16);
        Map<String, String> hm = new HashMap<>();

        // добавляем элементы в CustomHashMap
        hashMap.put("key1", "value1");
        hashMap.put("key2", "value2");
        hashMap.put("key3", "value3");
        System.out.println("CustomHashMap после добавления элементов: " + hashMap);

        // получаем значение из CustomHashMap по ключу key2
        System.out.println("Значение по ключу key2: " + hashMap.get("key2"));

        // Проверяем наличие ключа в CustomHashMap
        System.out.println("Содержит ли ключ 'key2': " + hashMap.containsKey("key2"));
        System.out.println("Содержит ли ключ 'key4': " + hashMap.containsKey("key4"));

        // Проверяем наличие значения в CustomHashMap
        System.out.println("Содержит ли значение 'value3': " + hashMap.containsValue("value3"));
        System.out.println("Содержит ли значение 'value4': " + hashMap.containsValue("value4"));

        // Получаем все ключи из CustomHashMap
        System.out.println("Все ключи: " + hashMap.keys());

        // Получаем все значения из CustomHashMap
        System.out.println("Все значения: " + hashMap.values());

        // выводим размер CustomHashMap
        System.out.println("Размер CustomHashMap: " + hashMap.size());

        // удаляем элемент из CustomHashMap по ключу key2
        hashMap.remove("key2");
        System.out.println("CustomHashMap после удаления key2: " + hashMap);

        // выводим размер CustomHashMap после удаления key2
        System.out.println("Размер CustomHashMap после удаления key2: " + hashMap.size());

        // Проверяем, пуста ли CustomHashMap
        System.out.println("Пуста ли CustomHashMap: " + hashMap.isEmpty());

        // Очищаем CustomHashMap
        hashMap.clear();
        System.out.println("CustomHashMap после очистки: " + hashMap);

        // Проверяем, пуста ли CustomHashMap
        System.out.println("Пуста ли CustomHashMap: " + hashMap.isEmpty());

        // выводим размер CustomHashMap после очистки
        System.out.println("Размер CustomHashMap после очистки: " + hashMap.size());
    }
}