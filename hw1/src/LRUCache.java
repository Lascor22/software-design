import java.util.HashMap;
import java.util.Map;

public class LRUCache<K, V> {
    private final Map<K, Node> VALUES = new HashMap<>();
    private final int CAPACITY;
    private Node head, tail;

    private class Node {
        private Node previous, next;
        public final V VALUE;
        public final K KEY;

        public Node(K key, V value) {
            this.VALUE = value;
            this.KEY = key;
        }
    }

    public LRUCache(int capacity) {
        assert capacity > 0 : "Capacity can't be non-positive";
        this.CAPACITY = capacity;
    }

    public void put(final K key, final V value) {
        assert key != null : "Key can't have null value";

        assert !VALUES.containsKey(key) : "Key already exists with value: " + VALUES.get(key).VALUE;

        if (VALUES.size() >= CAPACITY) {
            assert VALUES.remove(tail.KEY) != null;
            tail = tail.previous;

            assert VALUES.size() < CAPACITY;
        }

        Node newNode = new Node(key, value);
        assert VALUES.put(key, newNode) == null;

        moveToHead(newNode);
        assert head == newNode;
    }

    private void moveToHead(Node node) {
        Node prev = node.previous;
        Node next = node.next;
        if (prev != null && next != null) {
            node.previous.next = next;
            node.next.previous = prev;
        } else if (prev != null && next == null) {
            assert tail == node;
            tail = tail.previous;
        } else if (prev == null && next == null) {
            if (head == null && tail == null) {
                head = tail = node;
                return;
            }
        } else {
            // node is already head
            assert head == node;
            return;
        }

        assert head != null && tail != null;

        head.previous = node;
        node.next = head;
        head = node;
    }

    public V get(final K key) {
        Node node = VALUES.get(key);
        if (node == null) {
            return null;
        }

        moveToHead(node);
        assert head == node;

        return node.VALUE;
    }

    public int getCapacity() {
        return CAPACITY;
    }

    public int getSize() {
        assert VALUES.size() >= 0;
        return VALUES.size();
    }
}
