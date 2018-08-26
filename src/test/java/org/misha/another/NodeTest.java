package org.misha.another;

import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import org.misha.another.impl.Tree;
import org.misha.tree.Walker;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NodeTest {
    private final Tree tree = new Tree();
    private final Node<String> node0 = tree.makeNode("0");
    private final Node<String> node00 = tree.makeNode("00");
    private final Node<String> node01 = tree.makeNode("01");
    private final Node<String> node000 = tree.makeNode("000");
    private final Node<String> node001 = tree.makeNode("001");
    private final Node<String> node010 = tree.makeNode("010");
    private final Node<String> node011 = tree.makeNode("011");

    @Before
    @SuppressWarnings("unchecked warargs")
    public void before() {
        node0.addChild(node00, node01);
        node00.addChild(node000, node001);
        node01.addChild(node010, node011);
    }

    @Test
    public void testNode() {
        assertEquals(node00.parent(), node0);
        assertEquals(node01.parent(), node0);
        final AtomicInteger count = new AtomicInteger(0);
        node01.forEachSiblings(s -> count.incrementAndGet());
        assertEquals(1, count.get());
        node00.forEachSiblings(s -> count.incrementAndGet());
        assertEquals(2, count.get());
    }

    @Test
    public void testWalker() {
        final List<Node<String>> found = new ArrayList<>();
        final Walker<String> walker = new Walker<String>(node0) {
            @Override
            protected void doSomethingWith(final Node<String> node) {
                if (node.data().endsWith("1")) {
                    found.add(node);
                }
            }
        };
        walker.walkDepth();
        Set<Node<String>> expected = ImmutableSet.of(node001, node01, node011);
        assertTrue(expected.containsAll(found) && found.containsAll(expected));
        found.clear();
        walker.walkWidth();
        assertTrue(found.containsAll(Arrays.asList(node01, node001)));
        found.clear();
        walker.walkWidthUntil(n -> n.data().endsWith("1"));
        assertTrue(found.containsAll(Collections.singletonList(node01)));
        found.clear();
        walker.walkDepth();
        assertTrue(found.containsAll(Arrays.asList(node01, node001)));
        found.clear();
        walker.walkDepthUntil(n -> n.data().endsWith("1"));
        assertTrue(found.containsAll(Collections.singletonList(node001)));
    }
}