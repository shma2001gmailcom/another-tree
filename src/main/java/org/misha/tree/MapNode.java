package org.misha.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Iterables.isEmpty;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * author: misha
 * date: 5/8/18
 */
public class MapNode<T> {
    private final IncidenceTable<T> table;
    private final T data;
    
    public MapNode(final IncidenceTable<T> table, final T data) {
        this.data = data;
        this.table = table;
    }
    
    private static String repeat(String s, int times) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; ++i) {
            sb.append(s);
        }
        return sb.toString();
    }
    
    public String toRichString() {
        final StringBuilder sb = new StringBuilder(this.data.toString());
        final List<MapNode<T>> children = this.children();
        if (!children.isEmpty()) {
            for (final MapNode<T> c : children) {
                final int parentDepth = c.getParent().depth();
                sb.append("\n")
                        .append(repeat("|" + repeat(" ", 3), parentDepth))
                        .append("|\n")
                        .append(repeat("|" + repeat(" ", 3), parentDepth))
                        .append(repeat("+" + repeat("-", 3), c.depth() - parentDepth))
                        .append(c.toRichString());
            }
        }
        return sb.toString();
    }
    
    @SafeVarargs
    public final void addChild(final MapNode<T>... node) {
        for (final MapNode<T> n : node) {
            checkArgument(!table.containsKey(n), "already exists" + n);
            table.put(n, this);
        }
    }
    
    public T getData() {
        return data;
    }
    
    MapNode<T> getParent() {
        return table.getParentFor(this);
    }
    
    List<MapNode<T>> children() {
        final List<MapNode<T>> result = new ArrayList<>();
        try {
            table.lockForRead();
            for (final Map.Entry<MapNode<T>, MapNode<T>> e : table) {
                if (e.getValue().equals(this)) {
                    result.add(e.getKey());
                }
            }
        } finally {
            table.readUnlock();
        }
        return result;
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || o != null && getClass() == o.getClass() && data.equals(((MapNode) o).data);
    }
    
    @Override
    public int hashCode() {
        return data.hashCode();
    }
    
    @Override
    public String toString() {
        List<MapNode<T>> children = children();
        return "MapNode{" +
                "data: " + data +
                (isEmpty(children) ? EMPTY : "; children: " + children) +
                '}';
    }
    
    private int depth() {
        int result = 0;
        MapNode<T> parent = getParent();
        while (parent != null) {
            parent = parent.getParent();
            ++result;
        }
        return result;
    }
}
