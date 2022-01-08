package xyz.liangxin.utils.core.function;

import org.junit.Test;
import xyz.liangxin.utils.core.builder.ExecuteBuilder;

/**
 * <p> 描述
 *
 * @author liangxin
 * @version V1.0
 * @since 创建时间: 2022/1/8 21:29
 */
public class ExecuteBuilderTest {
    @Test
    public void test(){
        Node node1=new Node(1);
        Node node2=new Node(2);
        Node node3=new Node(3);
        node1.setNode(node2);
        node2.setNode(node3);
        Node result = ExecuteBuilder.builder(node1,true)
                .next(Node::getNode)
                .next(Node::getNode)
                .next(Node::getNode)
                .next(Node::getNode)
                .next(Node::getNode)
                .result();
        System.out.println(result);
    }

}

class Node {
   private int value;
   private  Node node;

   Node(int value){
       this.value=value;
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
