package org.misha.another;

import java.util.function.Consumer;

public interface Node<T> extends Iterable<Node<T>> {

    long getId();

    void addChild(Node<T>... nodes);

    T data();

    Node<T> parent();

    void forEachSiblings(Consumer<? super Node<T>> consumer);

    int depth();

    String toRichString();
}
