package org.misha.tree;

import org.misha.another.Node;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

/**
 * author: misha
 * date: 5/8/18
 * shows who is node's parent
 */
public class IncidenceTable<T> implements Iterable<Map.Entry<Node<T>, Node<T>>> {
    private final Map<Node<T>, Node<T>> table;
    private final ReadWriteLock lock;
    
    public IncidenceTable() {
        table = new HashMap<>();
        lock = new ReentrantReadWriteLock();
    }
    
    void put(final Node<T> child, final Node<T> parent) {
        final Lock writeLock = lock.writeLock();
        try {
            writeLock.lock();
            table.put(child, parent);
        } finally {
            writeLock.unlock();
        }
    }
    
    boolean containsKey(final Node mapNode) {
        final Lock readLock = lock.readLock();
        try {
            readLock.lock();
            return table.containsKey(mapNode);
        } finally {
            readLock.unlock();
        }
    }
    
    @Nonnull
    @Override
    public Iterator<Map.Entry<Node<T>, Node<T>>> iterator() {
        return table.entrySet().iterator();
    }


    Node<T> parent(final Node<T> node) {
        final Lock readLock = lock.readLock();
        try {
            readLock.lock();
            return table.get(node);
        } finally {
            readLock.unlock();
        }
    }
    
    @Override
    public String toString() {
        return "IncidenceTable{" +
                "table=" + table +
                '}';
    }

    @Override
    public void forEach(final Consumer<? super Map.Entry<Node<T>, Node<T>>> action) {
        table.entrySet().forEach(action);
    }
}
