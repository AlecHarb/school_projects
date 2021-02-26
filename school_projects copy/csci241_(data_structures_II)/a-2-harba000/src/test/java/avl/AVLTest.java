package avl;

import static org.junit.Assert.*;
import org.junit.FixMethodOrder;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.rules.Timeout;
import org.junit.Test;
import org.junit.runners.MethodSorters;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AVLTest {
  @Rule
  public Timeout globalTimeout = Timeout.seconds(3); // 3sec timeout

  private static String preOrder(AVL.Node n) {
    if (n == null) {
      return "";
    }
    String result = n.word
        + " " + preOrder(n.left)
        + " " + preOrder(n.right);

    return result.trim().replaceAll(" +", " ");
  }

  private static String inOrder(AVL.Node n) {
    if (n == null) {
      return "";
    }
    String result = inOrder(n.left)
        + " " + n.word
        + " " + inOrder(n.right);

    return result.trim().replaceAll(" +", " ");
  }

  private static String postOrder(AVL.Node n) {
    if (n == null) {
      return "";
    }
    String result = postOrder(n.left)
        + " " + postOrder(n.right)
        + " " + n.word;
    return result.trim().replaceAll(" +", " ");
  }

  private static void check(String message, AVL a) {
    if (a.root == null) {
      return;
    }
    assertEquals(message, null, a.root.parent);
    check(message, a.root);

  }
  private static void check(String message, AVL.Node n) {
    if (n == null) {
      return;
    }
    if (n.left != null) {
      assertEquals(message, n.left.parent, n);
    }
    if (n.right != null) {
      assertEquals(message, n.right.parent, n);
    }
    check(message, n.left);
    check(message, n.right);
  }

  private static void checkWithHeight(String message, AVL a) {
    if (a.root == null) {
      return;
    }
    assertEquals(message, null, a.root.parent);
    checkWithHeight(message, a.root);

  }

  private static int getHeight(AVL.Node n) {
    if (n != null) {
      return n.height;
    }
    return -1;
  }
  private static void checkWithHeight(String message, AVL.Node n) {
    if (n == null) {
      return;
    }

    // check that h = 1 + max child height
    int h = 1 + Math.max(getHeight(n.left), getHeight(n.right));
    assertEquals(message, h, n.height);
    if (n.left != null) {
      assertEquals(message, n.left.parent, n);
    }
    if (n.right != null) {
      assertEquals(message, n.right.parent, n);
    }
    checkWithHeight(message, n.left);
    checkWithHeight(message, n.right);
  }


  private static void treeEquals(
      String message, AVL a, String preo, String ino, String posto) {

    assertTrue(message, preOrder(a.root).equals(preo));
    assertTrue(message, inOrder(a.root).equals(ino));
    assertTrue(message, postOrder(a.root).equals(posto));

  }

  @Test
  /** Test bst insertion into empty tree */
  public void test00bstInsertRoot() {
    AVL a = new AVL();
    assertEquals("Tree root should be null when empty.", null, a.root);
    assertEquals("Tree should be empty prior to imsertion.", 0, a.getSize());

    a.bstInsert("moo");
    assertEquals("Tree has incorrect number of nodes.", 1, a.getSize());
    assertEquals("Root node should not have a parent.", null, a.root.parent);
    assertEquals("With only one node in the tree, root should not have a left child.", null, a.root.left);
    assertEquals("With only one node in the tree, root should not have a right child.", null, a.root.right);

  }

  private static void printAssert(AVL a) {
    System.out.println(
      "treeEquals(a, \""
        + preOrder(a.root) + "\", \""
        + inOrder(a.root) + "\", \""
        + postOrder(a.root) + "\");");
  }

  @Test
  /** Test bst insertion with more nodes */
  public void test10bstInsert() {
    AVL a = new AVL();
    a.bstInsert("moo");
    assertEquals("Tree has incorrect number of nodes.", 1, a.getSize());
    treeEquals("Traversal of tree failed. Check your pointers, make sure they are properly updated.", a, "moo", "moo", "moo");

    a.bstInsert("quack");
    assertEquals("BSTinsert failed to insert node.", 2, a.getSize());
    treeEquals("Traversal of tree failed. Check your pointers and ensure node is placed on the correct side of its parent.", a, "moo quack", "moo quack", "quack moo");

    a.bstInsert("neigh");
    assertEquals("BSTinsert failed to insert node.", 3, a.getSize());
    treeEquals("Traversal of tree failed. Check your pointers and ensure node is placed on the correct side of its parent.", a, "moo quack neigh", "moo neigh quack", "neigh quack moo");

    a.bstInsert("neigh");
    assertEquals("", 3, a.getSize());
    treeEquals("Traversal of tree failed. Check your pointers and ensure node is placed on the correct side of its parent.", a, "moo quack neigh", "moo neigh quack", "neigh quack moo");

    a.bstInsert("oink");
    assertEquals("BSTinsert failed to insert node.", 4, a.getSize());
    treeEquals("Traversal of tree failed. Check your pointers and ensure node is placed on the correct side of its parent.", a, "moo quack neigh oink", "moo neigh oink quack", "oink neigh quack moo");

    a.bstInsert("meow");
    assertEquals("BSTinsert failed to insert node.", 5, a.getSize());
    treeEquals("Traversal of tree failed. Check your pointers and ensure node is placed on the correct side of its parent.", a, "moo meow quack neigh oink", "meow moo neigh oink quack", "meow oink neigh quack moo");

    a.bstInsert("baa");
    assertEquals("BSTinsert failed to insert node.", 6, a.getSize());
    treeEquals("Traversal of tree failed. Check your pointers and ensure node is placed on the correct side of its parent.", a, "moo meow baa quack neigh oink", "baa meow moo neigh oink quack", "baa meow oink neigh quack moo");
    //kale sound effect
    a.bstInsert("aaaaaagh");
    assertEquals("BSTinsert failed to insert node.", 7, a.getSize());
    treeEquals("Traversal of tree failed. Check your pointers and ensure node is placed on the correct side of its parent.", a, "moo meow baa aaaaaagh quack neigh oink", "aaaaaagh baa meow moo neigh oink quack", "aaaaaagh baa meow oink neigh quack moo");

  }

  @Test
  /** Test bst insertion and check parent pointers */
  public void test20parentPointers() {
    AVL a = new AVL();
    check("AVL tree was not properly initialized.", a);
    a.bstInsert("moo");
    a.bstInsert("quack");
    a.bstInsert("neigh");
    check("Parent pointers are not properly updated.", a);
    a.bstInsert("neigh");
    check("Second instance of node value should not be added to AVL tree.", a);
    a.bstInsert("oink");
    a.bstInsert("meow");
    a.bstInsert("baa");
    a.bstInsert("aaaaaagh");
    check("Parent pointers are not properly updated.", a);
  }

  @Test
  /** Test leftRotate on the root of a two-node tree */
  public void test30leftRotate() {
    AVL a = new AVL();
    a.root = a.new Node("x");
    a.root.right = a.new Node("y");
    a.root.right.parent = a.root;

    check("Root node and root's right child were not properly assigned.", a);
    a.leftRotate(a.root);
    check("Left rotation on root with right child failed.", a);
    treeEquals("Traversal on rotated root node was incorrect.", a, "y x", "x y", "x y");
  }

  @Test
  /** Test leftRotate on a node with a non-null parent */
  public void test31leftRotate() {
    AVL a = new AVL();
    a.bstInsert("r");
    a.bstInsert("x");
    a.bstInsert("y");
    a.bstInsert("v");

    check("Insertion into AVL tree failed.", a);
    a.leftRotate(a.search("x"));
    treeEquals("Traversal after left rotation on node with non-null parent was incorrect.", a, "r y x v", "r v x y", "v x y r");
  }

  @Test
  /** Test leftRotate on a node with a non-null parent and right.left */
  public void test32leftRotate() {
    AVL a = new AVL();
    a.bstInsert("a");
    a.bstInsert("x");
    a.bstInsert("v");
    a.bstInsert("z");
    a.bstInsert("y");

    check("Insertion into AVL tree failed.", a);
    a.leftRotate(a.search("x"));
    check("Left rotation failed when performed on node with non-null parent and right.left descendant.", a);
    treeEquals("Traversal failed after left rotation on node with non-null parent and right.left descendant.", a, "a z x v y", "a v x y z", "v y x z a");
  }

  @Test
  /** Test rightRotate on the root of a two-node tree */
  public void test40rightRotate() {
    AVL a = new AVL();
    a.root = a.new Node("y");
    a.root.left = a.new Node("x", a.root);

    check("Manually setting root node and left child failed.", a);
    a.rightRotate(a.root);
    check("Right rotation on root of tree failed.", a);
    treeEquals("Traversal after right rotation of root is incorrect.", a, "x y", "x y", "y x");
  }

  @Test
  /** Test rightRotate on the root of a two-node tree */
  public void test41rightRotate() {
    AVL a = new AVL();
    a.bstInsert("r");
    a.bstInsert("y");
    a.bstInsert("x");
    a.bstInsert("z");

    check("Insertion into AVL tree failed.", a);
    a.rightRotate(a.search("y"));
    treeEquals("Traversal after right rotataion on root node was incorrect.", a, "r x y z", "r x y z", "z y x r");
  }

  @Test
  /** Test rightRotate on a node with a non-null parent and left.right */
  public void test42rightRotate() {
    AVL a = new AVL();
    a.bstInsert("a");
    a.bstInsert("y");
    a.bstInsert("v");
    a.bstInsert("z");
    a.bstInsert("x");

    check("Insertion into AVL tree failed.", a);
    a.rightRotate(a.search("y"));
    treeEquals("Traversal was incorrect after rotating a node with a non-null parent and left.right descendant.", a, "a v y x z", "a v x y z", "x z y v a");
  }


  /* rebalance tests */

  @Test
  /** Test rebalance does nothing when the tree is balanced */
  public void test50rebalance() {
    AVL a = new AVL();

    a.bstInsert("m"); a.search("m").height = 2;
    a.bstInsert("p"); a.search("p").height = 1;
    a.bstInsert("r"); a.search("r").height = 0;
    a.bstInsert("b"); a.search("b").height = 1;
    a.bstInsert("c"); a.search("c").height = 0;
    a.bstInsert("a"); a.search("a").height = 0;

    a.rebalance(a.search("p"));
    treeEquals("Tree rotated when it should not have.", a, "m b a c p r", "a b c m p r", "a c b r p m");
    a.rebalance(a.search("r"));
    treeEquals("Tree rotated when it should not have.", a, "m b a c p r", "a b c m p r", "a c b r p m");
    a.rebalance(a.search("b"));
    treeEquals("Tree rotated when it should not have.", a, "m b a c p r", "a b c m p r", "a c b r p m");
    a.rebalance(a.search("m"));
    treeEquals("Tree rotated when it should not have.", a, "m b a c p r", "a b c m p r", "a c b r p m");
    a.rebalance(a.search("a"));
    treeEquals("Tree rotated when it should not have.", a, "m b a c p r", "a b c m p r", "a c b r p m");
  }

  /* test each of the 4 rebalance cases */
  @Test
  /** Test rebalance works in case 1 (LL) */
  public void test51rebalance() {
    AVL a = new AVL();
    a.bstInsert("x"); a.search("x").height = 3;
    a.bstInsert("z"); a.search("z").height = 1;
    a.bstInsert("w"); a.search("w").height = 2;
    a.bstInsert("v"); a.search("v").height = 1;
    a.bstInsert("u"); a.search("u").height = 0;
    a.rebalance(a.search("w"));
    treeEquals("Case 1 left-left rotations failed to rebalance tree.", a, "x v u w z", "u v w x z", "u w v z x");
  }

  @Test
  /** Test rebalance works in case 2 (LR) */
  public void test52rebalance() {
    AVL a = new AVL();
    a.bstInsert("x"); a.search("x").height = 3;
    a.bstInsert("y"); a.search("y").height = 1;
    a.bstInsert("z"); a.search("z").height = 0;
    a.bstInsert("t"); a.search("t").height = 2;
    a.bstInsert("g"); a.search("g").height = 1;
    a.bstInsert("h"); a.search("h").height = 0;

    a.rebalance(a.search("t"));
    treeEquals("Case 2 left-right rotations failed to rebalance tree.", a, "x h g t y z", "g h t x y z", "g t h z y x");
  }

  @Test
  /** Test rebalance works in case 3 (RL) */
  public void test53rebalance() {
    AVL a = new AVL();
    a.bstInsert("x"); a.search("x").height = 3;
    a.bstInsert("y"); a.search("y").height = 1;
    a.bstInsert("z"); a.search("z").height = 0;
    a.bstInsert("t"); a.search("t").height = 2;
    a.bstInsert("v"); a.search("v").height = 1;
    a.bstInsert("u"); a.search("u").height = 0;

    a.rebalance(a.search("t"));
    treeEquals("Case 3 right-left rotations failed to rebalance tree.", a, "x u t v y z", "t u v x y z", "t v u z y x");

  }

  @Test
  /** Test rebalance works in case 4 (RR) */
  public void test54rebalance() {
    AVL a = new AVL();
    a.bstInsert("x"); a.search("x").height = 3;
    a.bstInsert("y"); a.search("y").height = 1;
    a.bstInsert("z"); a.search("z").height = 0;
    a.bstInsert("t"); a.search("t").height = 2;
    a.bstInsert("u"); a.search("u").height = 1;
    a.bstInsert("v"); a.search("v").height = 0;

    a.rebalance(a.search("t"));
    treeEquals("Case 4 right-right rotations failed to rebalance tree.", a, "x u t v y z", "t u v x y z", "t v u z y x");
  }


  @Test
  /** Test rebalance works in case 1 (LL) on the root */
  public void test55rebalance() {
    AVL a = new AVL();
    a.bstInsert("z"); a.search("z").height = 2;
    a.bstInsert("y"); a.search("y").height = 1;
    a.bstInsert("x"); a.search("x").height = 0;

    a.rebalance(a.root);
    treeEquals("Case 1 left-left rotations failed to rebalance when performed on the root node.", a, "y x z", "x y z", "x z y");
  }

  @Test
  /** Test rebalance works in case 2 (LR) on the root */
  public void test56rebalance() {
    AVL a = new AVL();
    a.bstInsert("z"); a.search("z").height = 2;
    a.bstInsert("x"); a.search("x").height = 1;
    a.bstInsert("y"); a.search("y").height = 0;

    a.rebalance(a.root);
    treeEquals("Case 2 left-right rotations failed to rebalance when performed on the root node.", a, "y x z", "x y z", "x z y");
  }
  @Test
  /** Test rebalance works in case 3 (RL) on the root */
  public void test57rebalance() {
    AVL a = new AVL();
    a.bstInsert("x"); a.search("x").height = 2;
    a.bstInsert("z"); a.search("z").height = 1;
    a.bstInsert("y"); a.search("y").height = 0;

    a.rebalance(a.root);
    treeEquals("Case 3 right-left rotations failed to rebalance when performed on the root node.", a, "y x z", "x y z", "x z y");
  }
  @Test
  /** Test rebalance works in case 4 (RR) on the root */
  public void test58rebalance() {
    AVL a = new AVL();
    a.bstInsert("x"); a.search("x").height = 2;
    a.bstInsert("y"); a.search("y").height = 1;
    a.bstInsert("z"); a.search("z").height = 0;

    a.rebalance(a.root);
    treeEquals("Case 4 right-right rotations failed to rebalance when performed on the root node.", a, "y x z", "x y z", "x z y");
  }


  @Test
  /** Test avlInsert with no rotations needed */
  public void test60avlInsert() {
    AVL a = new AVL();
    a.avlInsert("moo");
    assertEquals("Tree is suddenly not the right size anymore!", 1, a.getSize());
    treeEquals("Pointers are incorrect once again. This AVL tree self identifies as chaotic evil.", a, "moo", "moo", "moo");

    a.avlInsert("quack");
    assertEquals("Size of tree is incorrect.", 2, a.getSize());
    treeEquals("Tree rotation when it should not have.", a, "moo quack", "moo quack", "quack moo");

    a.avlInsert("marmot");
    assertEquals("Size of tree is incorrect.", 3, a.getSize());
    treeEquals("Tree rotation when it should not have.", a, "moo marmot quack", "marmot moo quack", "marmot quack moo");

    a.avlInsert("oink");
    assertEquals("Size of tree is incorrect.", 4, a.getSize());
    treeEquals("Tree rotation when it should not have.", a, "moo marmot quack oink", "marmot moo oink quack", "marmot oink quack moo");


    a.avlInsert("baa");
    assertEquals("Size of tree is incorrect.", 5, a.getSize());
    treeEquals("Tree rotation when it should not have.", a, "moo marmot baa quack oink", "baa marmot moo oink quack", "baa marmot oink quack moo");

    a.avlInsert("meow");
    assertEquals("Size of tree is incorrect.", 6, a.getSize());
    treeEquals("Tree rotation when it should not have.", a, "moo marmot baa meow quack oink", "baa marmot meow moo oink quack", "baa meow marmot oink quack moo");
  }


  @Test
  /** Test avlInsert with rotations needed */
  public void test61avlInsert() {
    AVL a = new AVL();
    a.avlInsert("a");
    a.avlInsert("b");
    a.avlInsert("c");
    a.avlInsert("d");
    a.avlInsert("e");
    a.avlInsert("f");
    a.avlInsert("q");
    a.avlInsert("x");
    a.avlInsert("y");
    a.avlInsert("z");
    a.avlInsert("m");
    a.avlInsert("n");
    a.avlInsert("l");
    a.avlInsert("o");
    a.avlInsert("s");
    a.avlInsert("r");
    a.avlInsert("q");
    a.avlInsert("p");

    checkWithHeight("Rotations were not properly performed on AVL tree after an insertion caused an imbalance.", a);
    treeEquals("Tree traversal was incorrect. Rotations did not occur when needed.", a, "f d b a c e q n m l o p x s r y z", "a b c d e f l m n o p q r s x y z", "a c b e d l m p o n r s z y x q f");
  }

  @Test
  /** Test avlInsert with rotations needed on a larger tree*/
  public void test62avlInsert() {
    AVL a = new AVL();
    a.avlInsert("dmim");
    a.avlInsert("fefb");
    a.avlInsert("bvry");
    a.avlInsert("kivz");
    a.avlInsert("ohkq");
    a.avlInsert("jrth");
    a.avlInsert("gwrg");
    a.avlInsert("mqon");
    a.avlInsert("ranl");
    a.avlInsert("lwkw");
    a.avlInsert("vgen");
    a.avlInsert("eadj");
    a.avlInsert("taaf");
    a.avlInsert("ctvf");
    a.avlInsert("twpx");
    a.avlInsert("gcbp");
    a.avlInsert("pndi");
    a.avlInsert("lbvv");
    a.avlInsert("gzvg");
    a.avlInsert("ktfr");
    a.avlInsert("matu");
    a.avlInsert("rchs");
    a.avlInsert("ejhd");
    a.avlInsert("tinh");
    a.avlInsert("inac");
    a.avlInsert("svfl");
    a.avlInsert("cjwj");
    a.avlInsert("ixuy");
    a.avlInsert("lsxh");
    a.avlInsert("opvy");
    a.avlInsert("bjdo");
    a.avlInsert("vtud");
    a.avlInsert("fbhz");
    a.avlInsert("murr");
    a.avlInsert("opnu");
    a.avlInsert("usue");
    a.avlInsert("rztl");
    a.avlInsert("xqyh");
    a.avlInsert("rgbo");
    a.avlInsert("zxfd");
    a.avlInsert("bwmu");
    a.avlInsert("muzo");
    a.avlInsert("gzuh");
    a.avlInsert("xtze");
    a.avlInsert("bsey");
    a.avlInsert("ruqb");
    a.avlInsert("xlcp");
    a.avlInsert("xtcl");
    a.avlInsert("ppol");
    a.avlInsert("dwwi");
    a.avlInsert("xxfu");
    a.avlInsert("kaqq");
    a.avlInsert("wiae");
    a.avlInsert("fzhr");
    a.avlInsert("uzqn");
    a.avlInsert("lhvp");
    a.avlInsert("mqpy");
    a.avlInsert("sath");
    a.avlInsert("aigx");
    a.avlInsert("bkde");
    a.avlInsert("frsr");
    a.avlInsert("tsyj");
    a.avlInsert("kjrl");
    a.avlInsert("rkjw");
    a.avlInsert("kwvl");
    a.avlInsert("zocw");
    a.avlInsert("sbsx");
    a.avlInsert("tywp");
    a.avlInsert("zomf");
    a.avlInsert("leec");
    a.avlInsert("hbpf");
    a.avlInsert("fdiq");
    a.avlInsert("ijxs");
    a.avlInsert("kurn");
    a.avlInsert("wvum");
    a.avlInsert("pgrs");
    a.avlInsert("jxbq");
    a.avlInsert("yxzx");
    a.avlInsert("vpaq");
    a.avlInsert("jjeq");
    a.avlInsert("naso");
    a.avlInsert("obmk");
    a.avlInsert("hdig");
    a.avlInsert("tshu");
    a.avlInsert("xrym");
    a.avlInsert("wfki");
    a.avlInsert("kvgb");
    a.avlInsert("wpip");
    a.avlInsert("jqit");
    a.avlInsert("qbto");
    a.avlInsert("ersx");
    a.avlInsert("moph");
    a.avlInsert("hvjh");
    a.avlInsert("tdur");
    a.avlInsert("umoi");
    a.avlInsert("pxwa");
    a.avlInsert("nslc");
    a.avlInsert("kmrd");
    a.avlInsert("boms");
    a.avlInsert("ldsu");

    checkWithHeight("Height of AVL tree is incorrect.", a);
    treeEquals("Traversal of big AVL test failed. Make sure pointers and rotations are being correctly updated and performed.", a, "kivz fefb dmim bvry bjdo aigx boms bkde bsey cjwj bwmu ctvf ejhd eadj dwwi fbhz ersx fdiq gzvg gwrg fzhr frsr gcbp gzuh inac hdig hbpf ijxs hvjh jrth jjeq ixuy jqit kaqq jxbq ranl mqon lbvv ktfr kjrl kmrd kvgb kurn kwvl lsxh leec ldsu lhvp matu lwkw moph ohkq naso murr mqpy muzo obmk nslc pndi opvy opnu pgrs pxwa ppol qbto vgen taaf rztl rgbo rchs ruqb rkjw sbsx sath svfl twpx tshu tinh tdur tsyj usue tywp umoi uzqn xqyh wiae vtud vpaq wfki wvum wpip xlcp zocw xtze xtcl xrym xxfu yxzx zxfd zomf", "aigx bjdo bkde boms bsey bvry bwmu cjwj ctvf dmim dwwi eadj ejhd ersx fbhz fdiq fefb frsr fzhr gcbp gwrg gzuh gzvg hbpf hdig hvjh ijxs inac ixuy jjeq jqit jrth jxbq kaqq kivz kjrl kmrd ktfr kurn kvgb kwvl lbvv ldsu leec lhvp lsxh lwkw matu moph mqon mqpy murr muzo naso nslc obmk ohkq opnu opvy pgrs pndi ppol pxwa qbto ranl rchs rgbo rkjw ruqb rztl sath sbsx svfl taaf tdur tinh tshu tsyj twpx tywp umoi usue uzqn vgen vpaq vtud wfki wiae wpip wvum xlcp xqyh xrym xtcl xtze xxfu yxzx zocw zomf zxfd", "aigx bkde bsey boms bjdo bwmu ctvf cjwj bvry dwwi eadj ersx fdiq fbhz ejhd dmim frsr gcbp fzhr gzuh gwrg hbpf hvjh ijxs hdig ixuy jqit jjeq jxbq kaqq jrth inac gzvg fefb kmrd kjrl kurn kwvl kvgb ktfr ldsu lhvp leec lwkw moph matu lsxh lbvv mqpy muzo murr nslc obmk naso opnu pgrs opvy ppol qbto pxwa pndi ohkq mqon rchs rkjw ruqb rgbo sath svfl sbsx rztl tdur tinh tsyj tshu umoi tywp uzqn usue twpx taaf vpaq wfki vtud wpip xlcp wvum wiae xrym xtcl yxzx xxfu xtze zomf zxfd zocw xqyh vgen ranl kivz");

  }



}
