package org.misha.tree;

import org.misha.another.Node;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * author: misha
 * date: 5/10/18
 * a non-recursive visitor
 */
public abstract class Walker<T> {
    private final Node<T> node;

    protected Walker(final Node<T> node) {
        this.node = node;
    }

    public void walkWidth() {
        final LinkedList<Node<T>> queue = new LinkedList<>();
        queue.addLast(node);
        while (!queue.isEmpty()) {
            final Node<T> removed = queue.removeFirst();
            doSomethingWith(removed);
            for (final Node<T> n : removed) {
                queue.add(n);
            }
        }
    }

    public void walkDepth() {
        final LinkedList<Node<T>> stack = new LinkedList<>();
        stack.addFirst(node);
        while (!stack.isEmpty()) {
            final Node<T> removed = stack.removeFirst();
            doSomethingWith(removed);
            for (final Node<T> n : removed) {
                stack.addFirst(n);
            }
        }
    }

    public void walkWidthUntil(final Predicate<Node<T>> stopCondition) {
        final LinkedList<Node<T>> queue = new LinkedList<>();
        queue.addFirst(node);
        walk(stopCondition, queue, queue::add);
    }

    private void walk(final Predicate<Node<T>> stopCondition,
                      final Deque<Node<T>> queue,
                      final Consumer<? super Node<T>> action
    ) {
        while (!queue.isEmpty()) {
            final Node<T> removed = queue.removeFirst();
            doSomethingWith(removed);
            if (stopCondition.test(removed)) return;
            for (final Node<T> n : removed) {
                action.accept(n);
            }
        }
    }

    public void walkDepthUntil(final Predicate<Node<T>> stopCondition) {
        final LinkedList<Node<T>> stack = new LinkedList<>();
        stack.addFirst(node);
        walk(stopCondition, stack, stack::addFirst);
    }

    protected abstract void doSomethingWith(final Node<T> remove);
}
