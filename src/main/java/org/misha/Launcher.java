package org.misha;

import org.apache.log4j.Logger;
import org.misha.another.Node;
import org.misha.tree.IncidenceTable;
import org.misha.tree.MapNode;
import org.misha.tree.Walker;

import java.util.ArrayList;
import java.util.List;

/**
 * author: misha
 * date: 5/8/18
 */
public class Launcher {
    private static final Logger log = Logger.getLogger(Launcher.class);
    
    public static void main(String... args) {
        final IncidenceTable<String> table = new IncidenceTable<>();
        final MapNode<String> node0 = new MapNode<>(table, "0");
        final MapNode<String> node00 = new MapNode<>(table, "00");
        final MapNode<String> node010 = new MapNode<>(table, "010");
        final MapNode<String> node011 = new MapNode<>(table, "011");
        final MapNode<String> node012 = new MapNode<>(table, "012");
        final MapNode<String> node01 = new MapNode<>(table, "01");
        final MapNode<String> node000 = new MapNode<>(table, "000");
        final MapNode<String> node001 = new MapNode<>(table, "001");
        final MapNode<String> node02 = new MapNode<>(table, "02");
        final MapNode<String> node0000 = new MapNode<>(table, "0000");
        final MapNode<String> node0001 = new MapNode<>(table, "0001");
        final MapNode<String> node0010 = new MapNode<>(table, "0010");
        final MapNode<String> node0011 = new MapNode<>(table, "0011");
        final MapNode<String> node0012 = new MapNode<>(table, "0012");
        final MapNode<String> node0013 = new MapNode<>(table, "0013");
        final MapNode<String> node0014 = new MapNode<>(table, "0014");
        final MapNode<String> node0002 = new MapNode<>(table, "0002");
        final MapNode<String> node00020 = new MapNode<>(table, "00020");
        final MapNode<String> node000200 = new MapNode<>(table, "000200");
        final MapNode<String> node0002000 = new MapNode<>(table, "0002000");
        node0.addChild(node00);
        node0.addChild(node01);
        node0.addChild(node02);
        node00.addChild(node000);
        node00.addChild(node001);
        node01.addChild(node010);
        node01.addChild(node011);
        node01.addChild(node012);
        node001.addChild(node0010, node0011, node0012, node0013, node0014);
        node000.addChild(node0000, node0001, node0002);
        node0002.addChild(node00020);
        node00020.addChild(node000200);
        node000200.addChild(node0002000);
        log.info("\n\n" + node0.toRichString());
        final List<String> found = new ArrayList<>();
        final Walker<String> walker = new Walker<String>(node0) {
            @Override
            protected void doSomethingWith(final Node<String> node) {
                if (node.data().contains("01")) {
                    found.add(node.data());
                }
            }
        };
        walker.walkWidth();
        log.info(found);
        found.clear();
        walker.walkWidthUntil(n->n.data().contains("01"));
        log.info(found);
        found.clear();
        walker.walkDepth();
        log.info(found);
        found.clear();
        walker.walkDepthUntil(n->n.data().contains("01"));
        log.info(found);
        found.clear();
    }
}
