/* Alec Harb
   February 19, 2019
   The purpose of this class is to implement the user interface for checking the total number of words and unique words in a
   text file or a user's input
*/
package avl;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
public class Vocab {

  public static void main(String[] args) {
    if (args.length > 0) {
      try {
        int i = 0;
        while (i < args.length) { // repeats for multiple text file inputs
        File f = new File(args[i]);
        Scanner sc = new Scanner(f);
        Count c = wordCount(sc);
        System.out.println(c);
        i++;
      }
    } catch (FileNotFoundException exc) {
      System.out.println("Could not find file " + args[0]);
    }
  } else {
    Scanner sc = new Scanner(System.in);
    Count c = wordCount(sc);
    System.out.println(c);

    }
  }

  /* count the total and unique words in a document being read
  * by the given Scanner. return the two values in a Count object.*/
  private static Count wordCount(Scanner sc) {
    AVL tree = new AVL();
    Count c = new Count();
    int totalWords = 0;

    while (sc.hasNext()) {
      // read and parse each word
      String word = sc.next();

      // remove non-letter characters, convert to lower case:
      word = word.replaceAll("[^a-zA-Z ]", "").toLowerCase();
      tree.avlInsert(word);
      totalWords++;
    }
    c.unique = tree.getSize();
    c.total = totalWords;

    return c;
  }


}
