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

    private LinkedList<Node<K, V>>[] buckets; // массив цепочек в CustomHashMap
    private int capacity; // вместимость CustomHashMap
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
            return false;
        }
        for (Node<K, V> node : buckets[index]) {
            if (node.key.equals(key)) {
                buckets[index].remove(node);
                size--;
                return true;
            }
        }
        return false;
    }

    // проверка наличия ключа в CustomHashMap
    public boolean containsKey(K key) {
        int index = hash(key);
        if (buckets[index] == null) {
            return false;
        }
        for (Node<K, V> node : buckets[index]) {
            if (node.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    // проверка наличия значения в CustomHashMap
    public boolean containsValue(V value) {
        for (LinkedList<Node<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Node<K, V> node : bucket) {
                    if (node.value.equals(value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // очистка CustomHashMap
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            buckets[i] = null;
        }
        size = 0;
    }

    // проверка, пустая ли CustomHashMap
    public boolean isEmpty() {
        return size == 0;
    }

    // получение всех ключей из CustomHashMap
    public LinkedList<K> keys() {
        LinkedList<K> keys = new LinkedList<>();
        for (LinkedList<Node<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Node<K, V> node : bucket) {
                    keys.add(node.key);
                }
            }
        }
        return keys;
    }

    // получение всех значений из CustomHashMap
    public LinkedList<V> values() {
        LinkedList<V> values = new LinkedList<>();
        for (LinkedList<Node<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Node<K, V> node : bucket) {
                    values.add(node.value);
                }
            }
        }
        return values;
    }

    // увеличение размера CustomHashMap и перераспределение элементов, если нагрузка становится слишком высокой
    private void resize() {
        int newCapacity = capacity * 2;
        LinkedList<Node<K, V>>[] newBuckets = new LinkedList[newCapacity];
        for (LinkedList<Node<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Node<K, V> node : bucket) {
                    int newIndex = Math.abs(node.key.hashCode()) % newCapacity;
                    if (newBuckets[newIndex] == null) {
                        newBuckets[newIndex] = new LinkedList<>();
                    }
                    newBuckets[newIndex].add(new Node<>(node.key, node.value));
                }
            }
        }
        buckets = newBuckets;
        capacity = newCapacity;
    }

    // вычисление коэффициента загрузки CustomHashMap
    public double loadFactor() {
        return (double) size / capacity;
    }

    // получение всех пар ключ-значение из CustomHashMap
    public LinkedList<Node<K, V>> entrySet() {
        LinkedList<Node<K, V>> entries = new LinkedList<>();
        for (LinkedList<Node<K, V>> bucket : buckets) {
            if (bucket != null) {
                entries.addAll(bucket);
            }
        }
        return entries;
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