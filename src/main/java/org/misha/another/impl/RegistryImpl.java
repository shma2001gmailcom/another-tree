package org.misha.another.impl;

import org.misha.another.Node;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

final class RegistryImpl implements Registry<String> {
    private final AtomicLong counter;
    private final Map<Long, Long> childToParent;
    private final Map<Long, Set<Long>> parentToChild;
    private final Map<Long, Node<String>> idToNode;

    RegistryImpl() {
        parentToChild = new TreeMap<>();
        childToParent = new TreeMap<>();
        idToNode = new TreeMap<>();
        counter = new AtomicLong(0);
    }

    @Nonnull
    @Override
    public Iterator<Map.Entry<Long, Long>> iterator() {
        return childToParent.entrySet().iterator();
    }

    @Override
    public void forEach(final Consumer<? super Map.Entry<Long, Long>> action) {
        childToParent.entrySet().forEach(action);
    }

    @Override
    public AtomicLong counter() {
        return counter;
    }

    @Override
    public void addChild(final long id, final long parentId) {
        childToParent.put(id, parentId);
        parentToChild.putIfAbsent(parentId, new TreeSet<>());
        parentToChild.get(parentId).add(id);
    }

    @Override
    public void add(final Node<String> node) {
        idToNode.put(node.getId(), node);
    }

    public Set<Long> getChildIds(final Long id) {
        return parentToChild.get(id);
    }

    @Override
    public Node<String> getNode(final long id) {
        return idToNode.get(id);
    }

    @Override
    public long getParent(final long id) {
        if (childToParent.containsKey(id))
            return childToParent.get(id);
        return -1;
    }
}