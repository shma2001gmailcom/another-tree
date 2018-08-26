package org.misha.another.impl;

import org.misha.another.Node;

public final class Tree {
    private final Registry<String> registry;

    public Tree() {
        registry = new RegistryImpl();
    }

    public Node<String> makeNode(final String data) {
        return new NodeImpl(data, registry);
    }
}
