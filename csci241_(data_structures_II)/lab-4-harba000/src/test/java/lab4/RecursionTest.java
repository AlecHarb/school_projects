package lab4;

import static org.junit.Assert.*;
import org.junit.FixMethodOrder;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Random;
import java.util.Collections;
import java.util.Arrays;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class RecursionTest {

    ////////////////////////////////////////////
    // JUnit test cases for recursion methods //
    ////////////////////////////////////////////

    @Test
    public void test00Len() {
        Recursion r = new Recursion();
        assertEquals(r.len("abc"), 3);
    }
    @Test
    public void test01Len() {
        Recursion r = new Recursion();
        assertEquals(r.len("       "), 7);
    }
    @Test
    public void test02Len() {
        Recursion r = new Recursion();
        assertEquals(r.len(""), 0);
    }
    @Test
    public void test03Len() {
        Recursion r = new Recursion();
        assertEquals(r.len("\\\\\\\\\\"), 5);
    }
    @Test
    public void test04Len() {
        Recursion r = new Recursion();
        assertEquals(r.len("This is a test!"), 15);
    }

    @Test
    public void test05CountE() {
        Recursion r = new Recursion();
        assertEquals(r.countE(""), 0);
    }
    @Test
    public void test06CountE() {
        Recursion r = new Recursion();
        assertEquals(r.countE("abc"), 0);
    }
    @Test
    public void test07CountE() {
        Recursion r = new Recursion();
        assertEquals(r.countE("abcde"), 1);
    }
    @Test
    public void test08CountE() {
        Recursion r = new Recursion();
        assertEquals(r.countE("eaebcde"), 3);
    }
    @Test
    public void test09CountE() {
        Recursion r = new Recursion();
        assertEquals(r.countE("eaebcdeeeee e"), 8);
    }

    @Test
    public void test10SumDigs() {
        Recursion r = new Recursion();
        assertEquals(r.sumDigs(100000), 1);
    }
    @Test
    public void test11SumDigs() {
        Recursion r = new Recursion();
        assertEquals(r.sumDigs(100001), 2);
    }
    @Test
    public void test12SumDigs() {
        Recursion r = new Recursion();
        assertEquals(r.sumDigs(99117812), 38);
    }
    @Test
    public void test13SumDigs() {
        Recursion r = new Recursion();
        assertEquals(r.sumDigs(0), 0);
    }
    @Test
    public void test14SumDigs() {
        Recursion r = new Recursion();
        assertEquals(r.sumDigs(55555), 25);
    }

    @Test
    public void test15Reverse() {
        Recursion r = new Recursion();
        assertEquals(r.reverse(""), "");
    }
    @Test
    public void test16Reverse() {
        Recursion r = new Recursion();
        assertEquals(r.reverse("racecar"), "racecar");
    }
    @Test
    public void test17Reverse() {
        Recursion r = new Recursion();
        assertEquals(r.reverse("maddam"), "maddam");
    }
    @Test
    public void test18Reverse() {
        Recursion r = new Recursion();
        assertEquals(r.reverse("10101010"), "01010101");
    }
    @Test
    public void test19Reverse() {
        Recursion r = new Recursion();
        assertEquals(r.reverse("<><"), "<><");
    }

    @Test
    public void test20BST(){
        BST t = new BST(0);
        assertEquals(t.isLeaf(t.root), false);
        assertEquals(t.size(), 0);
    }
    @Test
    public void test21BST(){
        BST t = new BST(0);
        assertEquals(t.size(), 0);
    }
    @Test
    public void test22BST(){
        BST t = new BST(0);
        t.preOrder();
        assertEquals(t.traversal, "");
        t.inOrder();
        assertEquals(t.traversal, "");
        t.postOrder();
        assertEquals(t.traversal, "");
    }
    @Test
    public void test23BST(){
        BST t = new BST(0);
        assertEquals(t.height(), -1);
    }

    @Test
    public void test24BST(){
        BST t = new BST(1);
        assertEquals(t.isLeaf(t.root), true);
        assertEquals(t.isLeaf(t.root.left), false);
    }
    @Test
    public void test25BST(){
        BST t = new BST(1);
        assertEquals(t.size(), 1);
    }
    @Test
    public void test26BST(){
        BST t = new BST(1);
        t.preOrder();
        assertEquals(t.traversal, "4");
        t.inOrder();
        assertEquals(t.traversal, "4");
        t.postOrder();
        assertEquals(t.traversal, "4");
    }
    @Test
    public void test27BST(){
        BST t = new BST(1);
        assertEquals(t.height(), 0);
    }

    @Test
    public void test28BST(){
        BST t = new BST(2);
        assertEquals(t.isLeaf(t.root), false);
        assertEquals(t.isLeaf(t.root.left), true);
        assertEquals(t.isLeaf(t.root.right), true);
    }
    @Test
    public void test29BST(){
        BST t = new BST(2);
        assertEquals(t.size(), 3);
    }
    @Test
    public void test30BST(){
        BST t = new BST(2);
        t.preOrder();
        assertEquals(t.traversal, "426");
        t.inOrder();
        assertEquals(t.traversal, "246");
        t.postOrder();
        assertEquals(t.traversal, "264");
    }
    @Test
    public void test31BST(){
        BST t = new BST(2);
        assertEquals(t.height(), 1);
    }

    @Test
    public void test32BST(){
        BST t = new BST(3);
        assertEquals(t.isLeaf(t.root), false);
        assertEquals(t.isLeaf(t.root.left), true);
        assertEquals(t.isLeaf(t.root.right), false);
        assertEquals(t.isLeaf(t.root.right.right.right.right), true);
    }
    @Test
    public void test33BST(){
        BST t = new BST(3);
        assertEquals(t.size(), 6);
    }
    @Test
    public void test34BST(){
        BST t = new BST(3);
        t.preOrder();
        assertEquals(t.traversal, "426789");
        t.inOrder();
        assertEquals(t.traversal, "246789");
        t.postOrder();
        assertEquals(t.traversal, "298764");
    }
    @Test
    public void test35BST(){
        BST t = new BST(3);
        assertEquals(t.height(), 4);
    }
}
