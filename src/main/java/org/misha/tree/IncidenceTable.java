package org.misha.tree;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * author: misha
 * date: 5/8/18
 * shows who is node's parent
 */
public class IncidenceTable<T> implements Iterable<Map.Entry<MapNode<T>, MapNode<T>>> {
    private final Map<MapNode<T>, MapNode<T>> table;
    private final ReadWriteLock lock;
    
    public IncidenceTable() {
        table = new HashMap<>();
        lock = new ReentrantReadWriteLock();
    }
    
    void put(final MapNode<T> child, final MapNode<T> parent) {
        final Lock writeLock = lock.writeLock();
        try {
            writeLock.lock();
            table.put(child, parent);
        } finally {
            writeLock.unlock();
        }
    }
    
    boolean containsKey(final MapNode<T> mapNode) {
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
    public Iterator<Map.Entry<MapNode<T>, MapNode<T>>> iterator() {
        return table.entrySet().iterator();
    }
    
    public MapNode<T> getParentFor(final MapNode<T> node) {
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
    
    void lockForRead() {
        lock.readLock().lock();
    }
    
    void readUnlock() {
        lock.readLock().unlock();
    }
}
