package org.misha.another.impl;

import org.misha.another.Node;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toCollection;

final class NodeImpl implements Node<String>, Comparable<Node<String>> {
    private final long id;
    private final Registry<String> registry;
    private final String data;

    NodeImpl(final String dataObject, final Registry<String> registryImpl) {
        data = dataObject;
        registry = registryImpl;
        id = registry.counter().incrementAndGet();
        registry.add(this);
    }

    @SafeVarargs
    @Override
    public final void addChild(final Node<String>... nodes) {
        Arrays.stream(nodes).forEach(node -> registry.addChild(node.getId(), id));
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String data() {
        return data;
    }

    @Nonnull
    @Override
    public Iterator<Node<String>> iterator() {
        final Set<Long> childIds = registry.getChildIds(id);
        return childIds == null ?
               new TreeSet<Node<String>>().iterator() :
               childIds.stream()
                       .mapToLong(l -> l)
                       .mapToObj(registry::getNode)
                       .collect(toCollection(TreeSet::new))
                       .iterator();
    }

    @Override
    public void forEach(final Consumer<? super Node<String>> action) {
        registry.getChildIds(id).stream()
                .mapToLong(l -> l)
                .mapToObj(registry::getNode)
                .forEach(action::accept);
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder(data + "->[");
        for (Iterator<Node<String>> it = iterator(); it.hasNext(); ) {
            sb.append(it.next().toString()).append(it.hasNext() ? ", " : "");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int compareTo(@Nonnull final Node o) {
        return Long.signum(o.getId() - id);
    }

    @Override
    public Node<String> parent() {
        return registry.getNode(registry.getParent(id));
    }

    @Override
    public void forEachSiblings(final Consumer<? super Node<String>> consumer) {
        final long parent = registry.getParent(id);
        if (parent != -1) {
            registry.getChildIds(parent).stream()
                    .filter(i -> i != id)
                    .map(registry::getNode)
                    .forEach(consumer);
        }
    }

    @Override
    public int depth() {
        return 0;
    }

    @Override
    public String toRichString() {
        return toString();
    }
}
