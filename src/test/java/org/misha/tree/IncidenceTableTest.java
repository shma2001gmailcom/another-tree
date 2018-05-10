package org.misha.tree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * author: misha
 * date: 5/8/18
 */
public class IncidenceTableTest {
    private static final String EXPECTED = "MapNode{data: 0; children: [MapNode{data: 00; children: [MapNode{data: 000}, MapNode{data: 001}]}, MapNode{data: 01}, MapNode{data: 02}]}";
    private final MapNode<String> node0;
    private final MapNode<String> node00;
    private final MapNode<String> node01;
    private final MapNode<String> node000;
    private final MapNode<String> node001;
    private final MapNode<String> node02;
    
    public IncidenceTableTest() {
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
        assertEquals(EXPECTED, node0.toString());
        assertTrue(node0.getData().equals("0"));
    }
    
    @Test
    public void getParentFor() {
        assertTrue(
                node001.getParent().equals(node00)
                        && node000.getParent().equals(node00)
                        && node02.getParent().equals(node0)
                        && node01.getParent().equals(node0)
                        && node00.getParent().equals(node0)
        );
    }
    
    @Test
    public void testWalker() {
        final List<String> found = new ArrayList<>();
        new Walker<String>(node0) {
            
            @Override
            protected void doSomethingWith(final MapNode<String> node) {
                if (node.getData().endsWith("1")) {
                    found.add(node.getData());
                }
            }
        }.walk();
        assertTrue(found.containsAll(Arrays.asList("01", "001")));
    }
}