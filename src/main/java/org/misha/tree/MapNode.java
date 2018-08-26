package org.misha.tree;

import org.misha.another.Node;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * author: misha
 * date: 5/8/18
 */
public class MapNode<T> implements Node<T> {
    private final IncidenceTable<T> table;
    private final T data;

    public MapNode(final IncidenceTable<T> table, final T data) {
        this.data = data;
        this.table = table;
    }

    private static String repeat(final String s, final int times) {
        return IntStream.range(0, times).mapToObj(i -> s).collect(Collectors.joining());
    }

    public String toRichString() {
        final StringBuilder sb = new StringBuilder(data.toString());
        forEach(c -> {
            final int parentDepth = c.parent().depth();
            sb.append("\n")
              .append(repeat("|" + repeat(" ", 3), parentDepth))
              .append("|\n")
              .append(repeat("|" + repeat(" ", 3), parentDepth))
              .append(repeat("+" + repeat("-", 3), c.depth() - parentDepth))
              .append(c.toRichString());
        });
        return sb.toString();
    }

    @SafeVarargs
    public final void addChild(final Node<T>... node) {
        Arrays.stream(node).forEach(n -> {
            checkArgument(!table.containsKey(n), "already exists" + n);
            table.put(n, this);
        });
    }

    @Override
    public long getId() {
        return 0;
    }

    public T data() {
        return data;
    }

    public Node<T> parent() {
        return table.parent(this);
    }

    @Override
    public void forEachSiblings(final Consumer<? super Node<T>> consumer) {
        //not implemented yet
    }

    @Nonnull
    @Override
    public Iterator<Node<T>> iterator() {
        return new Iterator<Node<T>>() {
            private final Iterator<Map.Entry<Node<T>, Node<T>>> iterator = table.iterator();
            private Node<T> node;

            @Override
            public boolean hasNext() {
                while (iterator.hasNext()) {
                    final Map.Entry<Node<T>, Node<T>> entry = iterator.next();
                    if (entry.getValue() == MapNode.this) {
                        node = entry.getKey();
                        return true;
                    }
                }
                return false;
            }

            @Override
            public Node<T> next() {
                if (node == null) throw new NoSuchElementException();
                return node;
            }
        };
    }

    @Override
    public void forEach(final Consumer<? super Node<T>> consumer) {
        table.forEach(entry -> {
            if (entry.getValue() == this) {
                consumer.accept(entry.getKey());
            }
        });
    }

    @Override
    public boolean equals(final Object o) {
        return this == o || null != o && getClass() == o.getClass() && data.equals(((MapNode) o).data);
    }

    /**
     * do not add children of MapNode to its equals or hashCode
     * because MapNode is used as key in Map
     */
    @Override
    public int hashCode() {
        return data.hashCode();
    }

    @Override
    public String toString() {
        return "MapNode{" +
                "data: " + data +
                '}';
    }

    @Override
    public int depth() {
        int result = 0;
        Node<T> parent = parent();
        while (null != parent) {
            parent = parent.parent();
            ++result;
        }
        return result;
    }
}
