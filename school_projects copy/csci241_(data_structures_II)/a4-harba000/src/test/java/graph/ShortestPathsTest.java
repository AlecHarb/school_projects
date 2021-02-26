/* Alec Harb
   CSCI 241
   3/12/2019
   Purpose: The purpose of this document is to implement JUnit tests to ensure that my ShortestPaths class is working correctly.
*/
package graph;

import static org.junit.Assert.*;
import org.junit.FixMethodOrder;

import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.net.URL;
import java.io.FileNotFoundException;

import java.util.LinkedList;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShortestPathsTest {

    /* Performs the necessary gradle-related incantation to get the
       filename of a graph text file in the src/test/resources directory at
       test time.*/
    private String getGraphResource(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        return resource.getPath();
    }

    /** Placeholer test case. Write your own tests here.  Be sure to include
     * the @Test annotation above each test method, or JUnit will ignore it and
     * not run it as a test case. */
    @Test
    public void test00Nothing() {
        String fn = getGraphResource("Simple1.txt");
        Graph simple1;
        try {
          simple1 = ShortestPaths.parseGraph("basic", fn);
        } catch (FileNotFoundException e) {
          fail("Could not find graph Simple1.txt");
          return;
        }
        assertTrue(true);
        assertEquals(2+2, 4);
    }

    @Test
    public void test01ShortestPathLength() {

      String fn = getGraphResource("Simple3.txt");
      Graph simple3;
      try {
        simple3 = ShortestPaths.parseGraph("basic", fn);
      } catch (FileNotFoundException e) {
        fail("Could not find graph Simple3.txt");
        return;
      }
      ShortestPaths newPath = new ShortestPaths();
      newPath.compute(simple3.getNode("A"));
      assertTrue("Shortest path to E is incorrect", 11.0 == newPath.shortestPathLength(simple3.getNode("E")));
    }

    @Test
    public void test02ShorTestPath() {
      String fn = getGraphResource("Simple4.txt");
      Graph simple4;
      try {
        simple4 = ShortestPaths.parseGraph("basic", fn);
      } catch (FileNotFoundException e) {
        fail("Could not find graph Simple4.txt");
        return;
      }
    ShortestPaths newPath = new ShortestPaths();
    newPath.compute(simple4.getNode("A"));

    LinkedList<Node> list = newPath.shortestPath(simple4.getNode("E"));
    String realPath = "ACE";
    String computedPath = "";

    while (!list.isEmpty()) {
      computedPath += list.remove();
    }
    assertTrue("Path should be ACE but was " + computedPath, realPath.equals(computedPath));
  }
}
