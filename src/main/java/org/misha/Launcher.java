package org.misha;

import org.apache.log4j.Logger;
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
        node0.addChild(node00);
        node0.addChild(node01);
        node0.addChild(node02);
        node00.addChild(node000);
        node00.addChild(node001);
        node01.addChild(node010);
        node01.addChild(node011);
        node01.addChild(node012);
        log.info("\n" +node0.toRichString());
        final List<String> found = new ArrayList<>();
        new Walker<String>(node0) {
            
            @Override
            protected void doSomethingWith(final MapNode<String> node) {
                if (node.getData().endsWith("1")) {
                    found.add(node.getData());
                }
            }
        }.walk();
        log.info(found);
    }
}