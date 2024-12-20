package Utilities.TreeAnalyzers;

import java.util.ArrayList;

public class RootedTreeNode {

    private final int vertex;
    private final ArrayList<RootedTreeNode> sons;

    private int height;
    private int size;
    private int depth;

    RootedTreeNode(int vertex) {
        this.vertex = vertex;
        this.sons = new ArrayList<>();
        this.height = 0;
    }

    /**
     * Sets the height of the node by calculating the maximum height of its children plus one.
     */
    void setHeight() {
        int maxHeight = -1;
        for (RootedTreeNode son : getSons())
            maxHeight = Math.max(maxHeight, son.getHeight());
        setHeight(maxHeight + 1);
    }

    /**
     * Sets the size of the node, which is the total number of nodes in its subtree, including itself.
     */
    void setSize() {
        setSize(1);
        for (RootedTreeNode son : getSons()) setSize(getSize() + son.getSize());
    }

    /**
     * Sets the depth of each child node by incrementing the current node's depth by one.
     */
    void setSonsDepth() {
        for (RootedTreeNode son : getSons()) son.setDepth(getDepth() + 1);
    }

    /**
     * Returns the child node with the maximum size.
     *
     * @return The child node with the largest size.
     */
    RootedTreeNode maxSizeSon() {
        RootedTreeNode maxSon = null;
        for (RootedTreeNode son : getSons()) {
            if (son.getSize() > getSize(maxSon)) maxSon = son;
        }
        return maxSon;
    }

    /**
     * Returns the child node with the maximum height.
     *
     * @return The child node with the largest height.
     */
    RootedTreeNode maxHeightSon() {
        RootedTreeNode maxSon = null;
        for (RootedTreeNode son : getSons()) {
            if (son.getHeight() > getHeight(maxSon)) maxSon = son;
        }
        return maxSon;
    }

    /**
     * Returns the second-largest height among the child nodes.
     * If there is no second distinct maximum height, returns -1.
     *
     * @return The second-largest height among the children, or -1 if not found.
     */
    int secondMaxHeight() {
        int maxHeight = -1;
        int secondMaxHeight = -1;
        for (RootedTreeNode son : getSons()) {
            if (son.getHeight() > secondMaxHeight) {
                secondMaxHeight = Math.min(maxHeight, son.getHeight());
                maxHeight = Math.max(maxHeight, son.getHeight());
            }
        }
        return secondMaxHeight;
    }

    /**
     * Returns the height of the given node. Returns -1 if the node is null.
     *
     * @param n The node whose height is to be retrieved.
     * @return The height of the node.
     */
    private int getHeight(RootedTreeNode n) {
        return (n == null ? -1 : n.getHeight());
    }

    /**
     * Returns the size of the given node. Returns -1 if the node is null.
     *
     * @param n The node whose size is to be retrieved.
     * @return The size of the node.
     */
    private int getSize(RootedTreeNode n) {
        return (n == null ? -1 : n.getSize());
    }

    /**
     * Prints the details of the current node including its vertex, children, height, size,
     * second maximum height among its children, and depth.
     */
    void print() {
        System.out.print("RootedTreeNode " + getVertex() + ", sons: ");
        for (RootedTreeNode son : getSons()) {
            System.out.print(son.getVertex() + " ");
        }
        System.out.println("(height: " + getHeight()
                + ", size: " + getSize()
                + ", 2nd height: " + secondMaxHeight()
                + ", depth: " + getDepth()
                + ")");
    }

    ArrayList<RootedTreeNode> getSons() {
        return sons;
    }

    int getHeight() {
        return height;
    }

    int getDepth() {
        return depth;
    }

    int getSize() {
        return size;
    }

    int getVertex() {
        return vertex;
    }

    void setHeight(int value) {
        this.height = value;
    }

    private void setSize(int value) {
        this.size = value;
    }

    void setDepth(int value) {
        this.depth = value;
    }
}