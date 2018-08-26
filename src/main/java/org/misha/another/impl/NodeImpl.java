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
        return null == childIds ?
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
        for (final Iterator<Node<String>> it = iterator(); it.hasNext(); ) {
            sb.append(it.next()).append(it.hasNext() ? ", " : "");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int compareTo(@Nonnull final Node o) {
        return Long.signum(o.getId() - id);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final NodeImpl nodes = (NodeImpl) o;
        return id == nodes.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public Node<String> parent() {
        return registry.getNode(registry.getParent(id));
    }

    @Override
    public void forEachSiblings(final Consumer<? super Node<String>> consumer) {
        final long parent = registry.getParent(id);
        if (-1 != parent) {
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
