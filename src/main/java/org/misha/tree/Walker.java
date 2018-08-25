package org.misha.tree;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

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

    public void walkWidth() {
        final LinkedList<MapNode<T>> queue = new LinkedList<>();
        queue.addLast(node);
        while (!queue.isEmpty()) {
            final MapNode<T> removed = queue.removeFirst();
            doSomethingWith(removed);
            queue.addAll(removed.children());
        }
    }

    public void walkDepth() {
        final LinkedList<MapNode<T>> stack = new LinkedList<>();
        stack.addFirst(node);
        while (!stack.isEmpty()) {
            final MapNode<T> removed = stack.removeFirst();
            doSomethingWith(removed);
            removed.children().forEach(stack::addFirst);
        }
    }

    public void walkWidthUntil(final Predicate<MapNode<T>> stopCondition) {
        final LinkedList<MapNode<T>> queue = new LinkedList<>();
        queue.addFirst(node);
        while (!queue.isEmpty()) {
            final MapNode<T> removed = queue.removeFirst();
            doSomethingWith(removed);
            if (stopCondition.test(removed)) return;
            queue.addAll(removed.children());
        }
    }

    public void walkDepthUntil(final Predicate<MapNode<T>> stopCondition) {
        final LinkedList<MapNode<T>> stack = new LinkedList<>();
        stack.addFirst(node);
        while (!stack.isEmpty()) {
            final MapNode<T> removed = stack.removeFirst();
            doSomethingWith(removed);
            if (stopCondition.test(removed)) return;
            removed.children().forEach(stack::addFirst);
        }
    }

    protected abstract void doSomethingWith(final MapNode<T> remove);

    private void printPocket(final List<MapNode<T>> list) {
        System.err.println("\n==============");
        list.forEach(node -> System.err.println(node.getData()));
    }
}
