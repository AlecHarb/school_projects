/* Alec Harb
   February 19, 2019
   The purpose of this class is to implement a binary search tree as well as a balanced AVL tree


   ENHANCEMENTS:
   I was able to remove a node with either 0 or 1 children but I was unable to finish the method for a node with two chidlren.
*/
package avl;

public class AVL {

  public Node root;

  private int size;

  public int getSize() {
    return size;
  }

  /** find w in the tree. return the node containing w or
  * null if not found */
  public Node search(String w) {
    return search(root, w);
  }
  private Node search(Node n, String w) {
    if (n == null) {
      return null;
    }
    if (w.equals(n.word)) {
      return n;
    } else if (w.compareTo(n.word) < 0) {
      return search(n.left, w);
    } else {
      return search(n.right, w);
    }
  }

  /** insert w into the tree as a standard BST, ignoring balance */
  public void bstInsert(String w) {
    if (root == null) {
      root = new Node(w);
      size = 1;
      return;
    }
    bstInsert(root, w);
  }

  /* insert w into the tree rooted at n, ignoring balance
   * pre: n is not null */
  private void bstInsert(Node n, String w) {
    if (n.word.equals(w)) {
      return;
    }
    if (n.word.compareTo(w) > 0) {
      if (n.left == null) {
        n.left = new Node(w, n);
        size++;
      } else {
        bstInsert(n.left, w);
      }
    } else if (n.word.compareTo(w) < 0) {
      if (n.right == null) {
        n.right = new Node(w, n);
        size++;
      } else {
        bstInsert(n.right, w);
      }
    }
  }

  /** insert w into the tree, maintaining AVL balance
  *  precondition: the tree is AVL balanced */
  public void avlInsert(String w) {
    if (root == null) {
      root = new Node(w);
      size = 1;
      return;
    }
    avlInsert(root, w);
  }

  /* insert w into the tree, maintaining AVL balance
   *  precondition: the tree is AVL balanced and n is not null */
  private void avlInsert(Node n, String w) {
    if (n.word.equals(w)) {
      return;
    }
    if (n.word.compareTo(w) > 0) {
      if (n.left == null) {
        n.left = new Node(w, n);
        size++;
      } else {
        avlInsert(n.left, w);
      }
    } else if (n.word.compareTo(w) < 0) {
      if (n.right == null) {
        n.right = new Node(w, n);
        size++;
      } else {
        avlInsert(n.right, w);
      }
    }
    rebalance(n);
    n.height = 1 + Math.max(getHeight(n.left), getHeight(n.right));
  }

  /** do a left rotation: rotate on the edge from x to its right child.
  *  precondition: x has a non-null right child */
  public void leftRotate(Node x) {
    Node y = x.right;
    x.right = y.left;

    if (y.left != null) {
       y.left.parent = x;
    }
    y.parent = x.parent;

    if (x.parent == null) { //checking if x is root
       root = y;
       root.left = x;
       x.parent = y;
    } else if (x == x.parent.left) { //checking if x is a left child
       x.parent.left = y;
       y.left = x;
       x.parent = y;
    } else {              //checking if x is a right child
       x.parent.right = y;
       y.left = x;
       x.parent = y;
    }
    x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right)); //update height for x and y
    y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));
  }

  /** do a right rotation: rotate on the edge from x to its left child.
  *  precondition: y has a non-null left child */
  public void rightRotate(Node y) {
    Node x = y.left;
    y.left = x.right;

    if (x.right != null) {
      x.right.parent = y;
    }
    x.parent = y.parent;

    if (y.parent == null) { //checking if y is root
      root = x;
      root.right = y;
      y.parent = x;
    } else if (y == y.parent.right) { //checking if y is a left child
      y.parent.right = x;
      x.right = y;
      y.parent = x;
    } else {              //checking if y is a right child
      y.parent.left = x;
      x.right = y;
      y.parent = x;
    }
    y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right)); // update height for x and y
    x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));
  }

  /** rebalance a node N after a potentially AVL-violoting insertion.
  *  precondition: none of n's descendants violates the AVL property */
  public void rebalance(Node n) {
    if (balance(n) < -1) { // case 1
      if (balance(n.left) < 0) {
        rightRotate(n);
      } else {            // case 2
        leftRotate(n.left);
        rightRotate(n);
      }
    } else if (balance(n) > 1) { // case 3
      if (balance(n.right) < 0) {
        rightRotate(n.right);
        leftRotate(n);
      } else {                  // case 4
        leftRotate(n);
      }
    }
  }

  /* get balance value of tree rooted at n */
  private int balance (Node n) {
    if (n == null) {
      return 0;
    }
    return (getHeight(n.right) - getHeight(n.left));
  }

  /* return the height of the tree rooted at n */
  private int getHeight(Node n) {
    if (n == null) {
        return -1;
    }
    return n.height;
  }

  /** remove the word w from the tree */
  public void remove(String w) {
    remove(root, w);
  }

  /** remove v from the tree rooted at n */
  private void remove(Node n, String w) {
    Node x = search(w);
    if (x == null) {
      System.out.printf("Node '%s' was not found and is unable to be removed from the tree.\n", w);
      return;
    }
    if (x.left == null && x.right == null) {
      if (x == x.parent.left) { //checking if x is a left child;
          x.parent.left = null;
      } else {
          x.parent.right = null;
      }
    } else if (x.left != null ^ x.right != null) { // if x has a left child XOR a right child
        removeWithChild(x);
    } else {
        removeWithChildren(x);
      }
  }

  /** Removes node x if x has one child */
  private void removeWithChild(Node x) {
    if (x.left != null) {
      if (x == x.parent.left) { // if x has a left child and x is a left child
        x.parent.left = x.left;
        x.left.parent = x.parent;
      } else {                  // if x has a left child and x is a right child
        x.parent.right = x.left;
        x.left.parent = x.parent;
      }
    } else {
        if (x == x.parent.right) {   // if x has a right child and x is a right child
          x.parent.right = x.right;
          x.right.parent = x.parent;
        } else {                 // if x has a right child and x is a left child
          x.parent.left = x.right;
          x.right.parent = x.parent;
        }
      }
    }

  /** removes node x if x has two children */
  private void removeWithChildren(Node x) {
    Node k = minimum(x.right);
    System.out.println(k.word);
    k.parent = x.parent;
    k.left = x.left;
    k.right = x.right;

    remove(k.word);
  }

  /** Returns the node with min value in the BST rooted at n
     pre: n is not null*/
  private Node minimum (Node n) {
    if (n.left == null) {
      return n;
    }
    return minimum(n.left);
  }

  /** print a sideways representation of the tree - root at left,
  * right is up, left is down. */
  public void printTree() {
    printSubtree(root, 0);
  }

  private void printSubtree(Node n, int level) {
    if (n == null) {
      return;
    }
    printSubtree(n.right, level + 1);
    for (int i = 0; i < level; i++) {
      System.out.print("        ");
    }
    System.out.println(n);
    printSubtree(n.left, level + 1);
  }

  /** inner class representing a node in the tree. */
  public class Node {
    public String word;
    public Node parent;
    public Node left;
    public Node right;
    public int height;

    public String toString() {
      return word + "(" + height + ")";
    }

    /** constructor: gives default values to all fields */
    public Node() { }

    /** constructor: sets only word */
    public Node(String w) {
      word = w;
    }

    /** constructor: sets word and parent fields */
    public Node(String w, Node p) {
      word = w;
      parent = p;
    }

    /** constructor: sets all fields */
    public Node(String w, Node p, Node l, Node r) {
      word = w;
      parent = p;
      left = l;
      right = r;
    }
  }
}
