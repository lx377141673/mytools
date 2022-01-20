package xyz.liangxin.utils.core.function;

import org.junit.Test;
import xyz.liangxin.utils.core.builder.ExecuteBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> 描述
 *
 * @author liangxin
 * @version V1.0
 * @since 创建时间: 2022/1/8 21:29
 */
public class ExecuteBuilderTest {

    @Test
    public void test() {
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        node1.setNode(node2);
        node2.setNode(node3);
        Node result = ExecuteBuilder.builder(node1, true)
                .next(Node::getNode)
                .next(Node::getNode)
                .next(Node::getNode)
                .next(Node::getNode)
                .next(Node::getNode)
                .result();
        System.out.println(result);
    }

    @Test
    public void test2() {
        Map<String, Object> map1 = new HashMap<>(10);
        Map<String, Object> map2 = new HashMap<>(10);
        Map<String, Object> map3 = new HashMap<>(10);
        map1.put("1", map2);
        map2.put("2", map3);
        map3.put("3", 123);
        final Object result = ExecuteBuilder.builder(map1, true)
                .next(x -> (Map<String, Object>) x.get("1"))
                .next(x -> (Map<String, Object>) x.get("2"))
                .next(x -> x.get("3"))
                .result();
        System.out.println(result);
    }

}

class Node {
    private int value;
    private Node node;

    Node(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return "Node{" +
                "value=" + value +
                ", node=" + node +
                '}';
    }
}
