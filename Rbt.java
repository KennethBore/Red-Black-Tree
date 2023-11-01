/**
 * Written by: Kenneth Bores
 * Date: 10/26/2023
 * Description: A red-black tree implementation.
 * The tree is a self balancing binary search tree.
 * The tree is balanced by checking for violations in the red property and rotating when necessary.            
 */


class Rbt {
    // Root node (public for the vizualization)
    public RbtNode root;

    // Size of the tree
    private int size;

    // A string to hold the values of the tree in order
    private StringBuilder orderedString = new StringBuilder();

    public Rbt(){
        this.root = null;
        this.size = 0;
    }

    /**
     * Inserts a node into the red-black tree.
     * Checks for violations in the red property after insertion.
     * @param data The data to insert into the tree.
     */
    public void insert(int data){
        RbtNode cur = null;
        RbtNode prev = null;

        // Insert the node as normal
        // First check if the root is null
        if(root == null){
            root = new RbtNode(data);
            root.setColor(RbtNode.CL_BLACK);
            size++;
            return;
        } else {

            // Travel down the tree 
            cur = root;
            while(cur!=null){
                
                // If the data is smaller than the current data, go left
                if(data < cur.getData()){
                    prev = cur;
                    cur = cur.getLeft();
                } 
                // If the data is greater than the current data, go right
                else if(data > cur.getData()){
                    prev = cur;
                    cur = cur.getRight();
                }
            }
            // Create the new node at the correct leaf
            if(data < prev.getData()){
                // Insert the node to the left
                prev.setLeft(new RbtNode(data));
                size++;

                // Set the current node to the new node
                cur = prev.getLeft();
                cur.setParent(prev);
            }
            if(data > prev.getData()){
                // Insert the node to the right
                prev.setRight(new RbtNode(data));
                size++;

                // Set the current node to the new node
                cur = prev.getRight();
                cur.setParent(prev);

            }
        }

        // Check the branch for violations in the red property from the newly inserted node
        if(cur.getParent().getColor() != RbtNode.CL_BLACK && cur != root){
            checkRedViolation(cur);
        }

        // Check for a violaition in the root property
        if(root.getColor() == RbtNode.CL_RED){
            root.setColor(RbtNode.CL_BLACK);
        }   
    }

    /**
     * Deletes a node from the red-black tree using standard delete methods.
     * Checks for violations in the black property after deletion.
     * @param data The data to delete from the tree.
     */
    public void delete(int data){
        // If the root is null, return
        if(root == null){
            return;
        }

        // Start at the root
        RbtNode cur = root;

        // Travel down the tree until the node is found
        while(cur != null){
            // If the data at the current node is the data to delete, break
            if(data == cur.getData()){
                break;
            }
            // If the left side of the node is less than the data, go left
            if(data < cur.getData()){
                cur = cur.getLeft();
            }
            if(data > cur.getData()){
                cur=cur.getRight();
            }

        }

        // If the node is not found, return
        if(cur == null){
            return;
        }
        // Save the node to be deleted
        RbtNode deleted = cur;


        // If the node is a leaf
        if(cur.getLeft() == null && cur.getRight() == null){
            // If the node is the root
            if(cur == root){
                root = null;
            }
            // If the node is the left child
            else if(cur.getParent().getLeft() == cur){
                cur.getParent().setLeft(null);
            }
            // If the node is the right child
            else if(cur.getParent().getRight() == cur){
                cur.getParent().setRight(null);
            }

            size--;
        }

        // If the node has one child
        else if(cur.getLeft() == null ^ cur.getRight() == null){
            // If the current node is the root
            if(cur == root){
                // If the child is to the left
                if(cur.getLeft() != null){
                    cur.getLeft().setParent(null);
                    root = cur.getLeft();
                }
                else if(cur.getRight() != null){
                    cur.getRight().setParent(null);
                    root = cur.getRight();
                }
            }
            // If the current node has a left child
            else if(cur.getLeft() != null){
                // Set the current nodes child to the current nodes color
                cur.getLeft().setColor(cur.getColor());

                // If the current node is to the left of its parent
                if(cur.getParent().getLeft() == cur){
                    // Set the parent's left to the current nodes child
                    cur.getParent().setLeft(cur.getLeft());
                    cur.getLeft().setParent(cur.getParent());
                }
                // If the current node is to the right of its parent
                else if(cur.getParent().getRight() == cur){
                    // Set the parents left to the current nodes child
                    cur.getParent().setRight(cur.getLeft());
                    cur.getLeft().setParent(cur.getParent());
                }
            }
            // If the current node has a right child
            else if(cur.getRight() != null){
                // Set the current nodes child to the current nodes color
                cur.getRight().setColor(cur.getColor());

                // If the current node is to the left of its parent
                if(cur.getParent().getLeft() == cur){
                    // Set the parent's left to the current nodes child
                    cur.getParent().setLeft(cur.getRight());
                    cur.getRight().setParent(cur.getParent());
                }
                // If the current node is to the right of its parent
                else if(cur.getParent().getRight() == cur){
                    // Set the parents left to the current nodes child
                    cur.getParent().setRight(cur.getRight());
                    cur.getRight().setParent(cur.getParent());
                }
            }
            size--;
        }
        // If the current node has two children
        else if(cur.getLeft() != null && cur.getRight() != null){
            // Find the successor
            RbtNode successor = successor(cur);
            delete(successor.getData());
            cur.setData(successor.getData());
            cur.setColor(deleted.getColor());
        }


        // Check for violations in the black property
        if(deleted.getColor() == RbtNode.CL_BLACK){
            checkBlackViolation(deleted);
        }

    }

    /**
     * Finds the successor of the given node.
     * The successor is the left most node of the right child.
     * If the node has no right child, return null.
     * @param node The node to find the successor of.
     * @return The successor of the given node.
     */
    private RbtNode successor(RbtNode node){
        // If the node is null, return null
        if(node == null){
            return null;
        }
        // If the node has a right child, return the left most node of the right child
        if(node.getRight() != null){
            // Start to the right of the current node
            RbtNode successor = node.getRight();
            // Then keep going left until the left node is null
            while(successor.getLeft() != null){
                successor = successor.getLeft();
            }
            return successor;
        } else{
            return null;
        }
    }




    /**
     * Checks the branch for violations in the red property
     * @param cur The current node to check for violations. Starts at the new node.
     */
    private void checkRedViolation(RbtNode cur){
        // If the new node is null, it is out of the bounds of the tree
        if(cur==null ^ cur==root){
            return;
        }        

        // If there is a violation in the red property
        if(cur.getColor() == RbtNode.CL_RED && cur.getParentColor() == RbtNode.CL_RED){
            // Case 1: Red Uncle
            if(cur.getUncleColor() == RbtNode.CL_RED){
                changeUncle(cur);
            }

            // Case 2: Left Left case
            // The current node is red and the parent of the current node is red
            else if(cur.getParent().getLeft() == cur && cur.getGParent().getLeft() == cur.getParent() ){

                RbtNode p = cur.getParent();
                RbtNode g = p.getParent();
                RbtNode gg = g.getParent();

                // Set the grandparents left or right to the current node
                if(gg != null){
                    if(gg.getLeft() == g){
                        gg.setLeft(p);
                    } else{
                        gg.setRight(p);
                    }
                }

                // Rotate the nodes to the right
                rightRotate(p);

                // Set the current nodes parent to the Great grandparent
                p.setParent(gg);

                // Check if p is the new root node
                if(p.getParent() == null)
                    root = p;

                // Change the colors of p and g
                p.setColor(RbtNode.CL_BLACK);
                g.setColor(RbtNode.CL_RED);

            }

            // Case 3: Left Right case
            // The current node is red and the parent is red. The current node is to the right of its parent, which is to the left of its parent.
            else if(cur.getParent().getRight() == cur && cur.getGParent().getLeft() == cur.getParent()){
                RbtNode p = cur.getParent();
                RbtNode g = p.getParent();

                // Rotate to the left
                leftRotate(cur);

                cur.setParent(g);
                g.setLeft(cur);

                //Recusively call the method to check the rest of the tree
                checkRedViolation(cur.getLeft());
            }
            
            
            // Case 4: Right Right case
            // The current node is red and the parent is red. The current node is to the right of it's parent, which is to the right of its parent
            else if(cur.getParent().getRight() == cur && cur.getGParent().getRight() == cur.getParent()){

                // Assign pointers
                RbtNode p = cur.getParent();
                RbtNode g = p.getParent();
                RbtNode gg = g.getParent();

                // Set the grandparents left or right to the current node
                if(gg != null){
                    if(gg.getLeft() == g){
                        gg.setLeft(p);
                    } else{
                        gg.setRight(p);
                    }
                }

                // Rotate the nodes to the left
                leftRotate(p);

                // Set the current nodes parent to the Great grandparent
                p.setParent(gg);

                // Check if p is the new root node
                if(p.getParent() == null)
                    root = p;

                // Change the colors of p and g
                p.setColor(RbtNode.CL_BLACK);
                g.setColor(RbtNode.CL_RED);
            }
            
            // Case 5: Right Left case
            // The current node is red and the parent is red. The current node is to the left of it's parent, which is to the right of its parent
            else if(cur.getParent().getLeft() == cur && cur.getGParent().getRight() == cur.getParent()){
                RbtNode p = cur.getParent();
                RbtNode g = p.getParent();

                // Rotate to the right
                rightRotate(cur);

                cur.setParent(g);
                g.setRight(cur);

                //Recusively call the method to check the rest of the tree
                checkRedViolation(cur.getRight());
            }
        }
        
        // Get the gp to test the rest of the tree
        checkRedViolation(cur.getGParent());

        return;
    }

    /**
     * Checks the branch for violations in the depth property
     * by checking the replacement for the deleted node.
     * @param cur The replacement for the deleted node.
     */
    private void checkBlackViolation(RbtNode cur){
        // If the new node is null, it is out of the bounds of the tree
        if(cur==null ^ cur.getColor() == RbtNode.CL_BLACK){
            return;
        }

        // Get the sibiling and parent of the current node
        RbtNode sibiling = cur.getSibiling();
        RbtNode parent = cur.getParent();
        
        // If the current node is the left child of its parent
        if(cur.getParent().getLeft() == cur){
            // Case 1: If the sibiling is red
            if(sibiling.getColor() == RbtNode.CL_RED){
                // Set the color of the sibling to black
                sibiling.setColor(RbtNode.CL_BLACK);

                // Set the color of the parent to red
                parent.setColor(RbtNode.CL_RED);

                // Left rotate on the parent node
                leftRotate(parent);

                // Update the sibiling to the new sibiling
                sibiling = cur.getSibiling();
            }

            // Case 2: If bith the children on the sibiling are black
            if(sibiling.getLeft().getColor() == RbtNode.CL_BLACK && sibiling.getRight().getColor() == RbtNode.CL_BLACK){
                // Set the color of the sibiling to red
                sibiling.setColor(RbtNode.CL_RED);

                // Make the current node the parent of the current node
                cur = cur.getParent();
            // Case 3: Else if the color of the right child of the sibiling is black
            } else{
                if(sibiling.getRight().getColor() == RbtNode.CL_BLACK){
                    // Set the color of the left child of the sibiling to black
                    sibiling.getLeft().setColor(RbtNode.CL_BLACK);

                    // Set the color of the sibiling to red
                    sibiling.setColor(RbtNode.CL_RED); 

                    // Right rotate on the sibiling
                    rightRotate(sibiling);

                    // Update the sibiling to the new sibiling
                    sibiling = cur.getSibiling();
                }

                // Case 4
                // Set the color of the sibiling to the color of the parent
                sibiling.setColor(parent.getColor());

                // Set the color of the grand parent to black
                cur.setGParentColor(RbtNode.CL_BLACK);

                // Set the color of the right child of the sibiling to black
                sibiling.getRight().setColor(RbtNode.CL_BLACK);

                // Left rotate on the parent
                leftRotate(parent);

                // Make the current node the root
                cur = root;
            } 
        } else {

            // Case 1: If the sibiling is red
            if(sibiling.getColor() == RbtNode.CL_RED){
                // Set the color of the sibling to black
                sibiling.setColor(RbtNode.CL_BLACK);

                // Set the color of the parent to red
                parent.setColor(RbtNode.CL_RED);

                // Left rotate on the parent node
                rightRotate(parent);

                // Update the sibiling to the new sibiling
                sibiling = cur.getSibiling();
            }

            // Case 2: If bith the children on the sibiling are black
            if(sibiling.getRight().getColor() == RbtNode.CL_BLACK && sibiling.getLeft().getColor() == RbtNode.CL_BLACK){
                // Set the color of the sibiling to red
                sibiling.setColor(RbtNode.CL_RED);

                // Make the current node the parent of the current node
                cur = cur.getParent();

            // Case 3: Else if the color of the right child of the sibiling is black
            } else{
                if(sibiling.getLeft().getColor() == RbtNode.CL_BLACK){
                    // Set the color of the left child of the sibiling to black
                    sibiling.getRight().setColor(RbtNode.CL_BLACK);

                    // Set the color of the sibiling to red
                    sibiling.setColor(RbtNode.CL_RED); 

                    // Right rotate on the sibiling
                    leftRotate(sibiling);

                    // Update the sibiling to the new sibiling
                    sibiling = cur.getSibiling();
                }

                // Case 4
                // Set the color of the sibiling to the color of the parent
                sibiling.setColor(parent.getColor());

                // Set the color of the grand parent to black
                cur.setGParentColor(RbtNode.CL_BLACK);

                // Set the color of the right child of the sibiling to black
                sibiling.getLeft().setColor(RbtNode.CL_BLACK);

                // Left rotate on the parent
                rightRotate(parent);

                // Make the current node the root
                cur = root;
            }
        }
        cur.setColor(RbtNode.CL_BLACK);
        checkBlackViolation(cur);
    }




    /**
     * Changes the colors of the parent, uncle, and grandparent.
     * @param cur The current node to change the colors of.
     */
    private void changeUncle(RbtNode cur){

        // Change the colors of the parent, uncle, and grandparent
        cur.setParentColor(RbtNode.CL_BLACK);

        cur.setUncleColor(RbtNode.CL_BLACK);

        cur.setGParentColor(RbtNode.CL_RED);
    }


    

    /**
     * Rotates a subtree to the right.
     * It rotates around node p which is the parent of the current node that is being checked for violations.
     * This method will also change the colors of the nodes.
     * @param p The parent of the current node that is being checked for violations.
     */
    private void rightRotate(RbtNode p){
        // Get the nodes to rotate

        RbtNode x = p.getLeft();

        RbtNode t = p.getRight();

        RbtNode g = p.getParent();

        // Rotate the nodes

        // Set the old parents left to the current nodes right
        g.setLeft(t);

        // Set the currents right to the old parent
        p.setRight(g);

        // Set the old grandparent's parent to the current node
        g.setParent(p);


    }
    /**
     * Rotates a subtree to the left.
     * It rotates around node p which is the parent of the current node that is being checked for violations.
     * This method will also change the colors of the nodes.
     * @param p The parent of the current node that is being checked for violations.
     */
    private void leftRotate(RbtNode p){

        RbtNode x = p.getRight();

        RbtNode t = p.getLeft();

        RbtNode g = p.getParent();

        // Set the grandparents left or right to the current node
        g.setRight(t);

        
        // Set the current nodes left to the old parent
        p.setLeft(g);


        // Set the current nodes parent to the grandparent
        g.setParent(p);
        

    }




    /**
     * Searches for a node in the red black tree with the given data.
     * 
     * @param data the data to search for
     * @return true if the data is found, false otherwise
     */
    public boolean search(int data){
        if(root == null){
            return false;
        }
        RbtNode cur = root;
        while(cur != null){
            // If the data is found, return true
            if(cur.getData() == data){
                return true;
            
            // If the data is less than the current data, go left

            } else if(data < cur.getData()){ 
                cur = cur.getLeft();
            // If the data is more than the current data, go right
            } else if(data > cur.getData()){
                cur = cur.getRight();
            }
        }
        // If the data is never found return false.
        return false;
    }

    /**
     * Returns the smallest value in the tree.
     * @return The smallest value in the tree.
     */
    public int min(){
        // If the root is null, return -1.
        if(root == null){
            return -1;
        }
        // Start at the root
        RbtNode cur = root;
        // Travel down the left side of the tree until the left node is null
        while(cur != null){
            if(cur.getLeft() == null){
                break;
            }
            cur = cur.getLeft();
        }
        return cur.getData();
    }

    /**
     * Returns the largest value in the tree.
     * @return The lasrgest value in the tree.
     */
    public int max(){
        // If the root is null, return -1.
        if(root == null){
            return -1;
        }
        // Start at the root
        RbtNode cur = root;
        // Travel down the right side of the tree until the right node is null
        while(cur != null){
            if(cur.getRight() == null){
                break;
            }
            cur = cur.getRight();
        }
        return cur.getData();
    }

    /**
     * Returns the number of nodes in the tree.
     * @return The number of nodes in the tree.
     */
    public int size(){
        return this.size;
    }

    /**
     * Returns a string of the values in the tree in order.
     * @return A string of the values in the tree in order.
     */
    public String inorder(){
        if(root == null){
            return " ";
        }
        orderedString.setLength(0);
        inOrder(root);
        return orderedString.toString();
    }

    /**
     * A helper method to traverse the tree in order.
     * @param cur The current node to traverse from.
     */
    private void inOrder(RbtNode cur){
        if(cur == null){
            return;
        }
        inOrder(cur.getLeft());


        orderedString.append(cur.getData() + " ");
        //orderedString.append(cur.getData() + (cur.getColor() == RbtNode.CL_RED ? " Red" : " Black") + ", ");

        inOrder(cur.getRight());
    }
}




class RbtNode{

    // Possible colors for the node
    public static final byte CL_RED = 0;
    public static final byte CL_BLACK = 1;

    private int data; // Data
    private byte color; // The color of the node

    // Left and right nodes
    private RbtNode left;
    private RbtNode right;

    // The parent node
    private RbtNode parent;

    /**
     * Creates a node for a Red Black Tree.
     * Initializes the data, and makes the defualt color red.
     * The left, abd right node are set to null due to the properties\
     * of the structure. The parent is null by defualt.
     * @param data
     */
    public RbtNode(int data){
        this.data = data;
        this.color = CL_RED;

        this.left = null;
        this.right = null;

        this.parent = null;
    }


    // Accessors
    public int getData(){
        return this.data;
    }
    public byte getColor(){
        return this.color;
    }
    public RbtNode getLeft(){
        return this.left;
    }
    public RbtNode getRight(){
        return this.right;
    }
    public RbtNode getParent(){
        return this.parent;
    }
    public byte getParentColor(){
        return this.parent.getColor();
    }
    public RbtNode getSibiling(){
        // If there is no parent, there is no sibiling
        if(this.parent == null){
            return null;
        }
        // If the current node is to the left, the sibiling is to the right
        if(this.parent.getLeft() == this){
            return this.parent.getRight();
        // If the current node is to the right, the siblinig is to the left
        } else{
            return this.parent.getLeft();
        }
    }
    public byte getSibilingColor(){
        return this.getSibiling().getColor();
    }
    /**
     * Finds the Grand Parent of the current node
     * @return The grand parent of the node
     */
    public RbtNode getGParent(){
        if(this.parent == null){
            return null;
        }
        return this.parent.getParent();
    }

    public byte getGParentColor(){
        return getGParent().getColor();
    }
    /**
     * Finds the uncle of the current node
     * @return The uncle of the current node
     */
    public RbtNode getUncle(){
        if(getGParent() == null){
            return null;
        }
        // If the grandparents left is this nodes parent, then the right node is the uncle
        if(getGParent().getLeft() == this.getParent()){
            return getGParent().getRight();
        } else{ // The left node must be the uncle
            return getGParent().getLeft();
        }
    }

    public byte getUncleColor(){
        if(getUncle() == null){
            return CL_BLACK;
        }
        return getUncle().getColor();
    }


    // Mutators
    public void setData(int data){
        this.data = data;
    }
    public void setColor(byte color){
        this.color = color;
    }
    public void setLeft(RbtNode node){
        this.left = node;
    }
    public void setRight(RbtNode node){
        this.right = node;
    }
    public void setParent(RbtNode node){
        this.parent = node;
    }
    public void setParentColor(byte color){
        getParent().setColor(color);
    }
    public void setSibiling(RbtNode node){
        if(this.parent.getLeft() == this){
            this.parent.setRight(node);
        } else{
            this.parent.setLeft(node);
        }
    }
    public void setSibilingColor(byte color){
        getSibiling().setColor(color);
    }
    public void setGParentColor(byte color){
        getGParent().setColor(color);
    }
    public void setUncleColor(byte color){
        getUncle().setColor(color);
    }   
}