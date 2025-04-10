package ru.aston.matveenko_ym;

import ru.aston.matveenko_ym.service.CustomHashMap;

public class ExampleHashMap {

    public static void main(String[] args) {
        CustomHashMap<String, String> hashMap = new CustomHashMap<>(16);

        // добавляем элементы в CustomHashMap
        hashMap.put("key1", "value1");
        hashMap.put("key2", "value2");
        hashMap.put("key3", "value3");
        System.out.println("CustomHashMap после добавления элементов: " + hashMap);

        // получаем значение из CustomHashMap по ключу key2
        System.out.println("Значение по ключу key2: " + hashMap.get("key2"));

        // удаляем элемент из CustomHashMap по ключу key2
        hashMap.remove("key2");
        System.out.println("CustomHashMap после удаления key2: " + hashMap);

        // выводим размер CustomHashMap
        System.out.println("Размер CustomHashMap: " + hashMap.size());
    }
}