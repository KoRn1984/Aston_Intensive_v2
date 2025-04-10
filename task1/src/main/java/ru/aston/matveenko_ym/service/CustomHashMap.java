package ru.aston.matveenko_ym.service;

import java.util.LinkedList;

/**
 * @param <K> ключ (key) в CustomHashMap
 * @param <V> значение (value) в CustomHashMap
 */
public class CustomHashMap<K, V> {

    // внутренний класс для представления пар ключ-значение в CustomHashMap
    private static class Node<K, V> {
        K key;
        V value;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final LinkedList<Node<K, V>>[] buckets; // массив цепочек в CustomHashMap
    private final int capacity; // вместимость CustomHashMap
    private int size; // количество элементов в CustomHashMap

    public CustomHashMap(int capacity) {
        this.capacity = capacity;
        this.buckets = new LinkedList[capacity];
        this.size = 0;
    }

    // хэш-функция
    private int hash(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    // метод для добавления или обновления элемента в CustomHashMap
    public void put(K key, V value) {
        int index = hash(key);
        if (buckets[index] == null) {
            buckets[index] = new LinkedList<>();
        }
        // проверяем, есть ли уже узел с таким ключом
        for (Node<K, V> node : buckets[index]) {
            if (node.key.equals(key)) {
                node.value = value;
                return;
            }
        }
        // если ключа нет, добавляем новый узел
        buckets[index].add(new Node<>(key, value));
        size++;
    }

    // метод для получения значения по ключу в CustomHashMap
    public V get(K key) {
        int index = hash(key);
        if (buckets[index] == null) {
            return null; // ключ не найден
        }
        // ищем узел с заданным ключом
        for (Node<K, V> node : buckets[index]) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    // метод для удаления элемента в CustomHashMap
    public boolean remove(K key) {
        int index = hash(key);
        if (buckets[index] == null) {
            return false; // ключ не найден
        }
        // ищем и удаляем узел с заданным ключом
        for (Node<K, V> node : buckets[index]) {
            if (node.key.equals(key)) {
                buckets[index].remove(node);
                size--;
                return true;
            }
        }
        return false;
    }

    // метод для получения текущего размера CustomHashMap
    public int size() {
        return size;
    }

    // переопределение метода toString (для удобного вывода)
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (LinkedList<Node<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Node<K, V> node : bucket) {
                    sb.append(node.key).append(": ").append(node.value).append(", ");
                }
            }
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // удаляем последнюю запятую
        }
        sb.append("}");
        return sb.toString();
    }
}