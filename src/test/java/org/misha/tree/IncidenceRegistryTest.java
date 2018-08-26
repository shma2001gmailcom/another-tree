package org.misha.tree;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import org.misha.another.Node;

import java.util.*;

import static org.junit.Assert.assertTrue;

/**
 * author: misha
 * date: 5/8/18
 */
public class IncidenceRegistryTest {
    private final MapNode<String> node0;
    private final MapNode<String> node00;
    private final MapNode<String> node01;
    private final MapNode<String> node000;
    private final MapNode<String> node001;
    private final MapNode<String> node02;
    
    public IncidenceRegistryTest() {
        final IncidenceTable<String> table = new IncidenceTable<>();
        node0 = new MapNode<>(table, "0");
        node00 = new MapNode<>(table, "00");
        node01 = new MapNode<>(table, "01");
        node000 = new MapNode<>(table, "000");
        node001 = new MapNode<>(table, "001");
        node02 = new MapNode<>(table, "02");
        node0.addChild(node00);
        node0.addChild(node01);
        node0.addChild(node02);
        node00.addChild(node000);
        node00.addChild(node001);
    }
    
    @Test
    public void iterator() {
        final Iterator<Node<String>> iterator = node0.iterator();
        final Collection<Node<String>> actual = new HashSet<>();
        while(iterator.hasNext()) {
            actual.add(iterator.next());
        }
        final ImmutableSet<MapNode<String>> expected = ImmutableSet.of(node00, node01, node02);
        assertTrue(expected.containsAll(actual) && actual.containsAll(expected));
    }
    
    @Test
    public void getParentFor() {
        assertTrue(
                node001.parent().equals(node00)
                        && node000.parent().equals(node00)
                        && node02.parent().equals(node0)
                        && node01.parent().equals(node0)
                        && node00.parent().equals(node0)
        );
    }
    
    @Test
    public void testWalker() {
        final Collection<String> found = new ArrayList<>();
        final Walker<String> walker = new Walker<String>(node0) {
            @Override
            protected void doSomethingWith(final Node<String> node) {
                if (node.data().endsWith("1")) {
                    found.add(node.data());
                }
            }
        };
        walker.walkWidth();
        assertTrue(found.containsAll(Arrays.asList("01", "001")));
        found.clear();
        walker.walkWidthUntil(n->n.data().endsWith("1"));
        assertTrue(found.containsAll(Collections.singletonList("01")));
        found.clear();
        walker.walkDepth();
        assertTrue(found.containsAll(Arrays.asList("01", "001")));
        found.clear();
        walker.walkDepthUntil(n->n.data().endsWith("1"));
        assertTrue(found.containsAll(Collections.singletonList("01")));
    }
}