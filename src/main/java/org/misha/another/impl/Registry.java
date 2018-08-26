package org.misha.another.impl;

import org.misha.another.Node;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public interface Registry<T> extends Iterable<Map.Entry<Long, Long>> {

    @Nonnull
    @Override
    Iterator<Map.Entry<Long, Long>> iterator();

    @Override
    void forEach(Consumer<? super Map.Entry<Long, Long>> action);

    AtomicLong counter();

    void addChild(long id, long parentId);

    void add(Node<T> node);

    Set<Long> getChildIds(final Long id);

    Node<T> getNode(long id);

    long getParent(long id);
}
