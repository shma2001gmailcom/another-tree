package org.misha.tree;

import java.util.LinkedList;

/**
 * author: misha
 * date: 5/10/18
 * a non-recursive visitor
 */
public abstract class Walker<T> {
    private final MapNode<T> node;
    
    protected Walker(final MapNode<T> node) {
        this.node = node;
    }
    
    public void walk() {
        final LinkedList<MapNode<T>> stack = new LinkedList<>();
        stack.addFirst(node);
        while (!stack.isEmpty()) {
            final MapNode<T> removed = stack.remove();
            doSomethingWith(removed);
            stack.addAll(removed.children());
        }
    }
    
    protected abstract void doSomethingWith(final MapNode<T> remove);
}
