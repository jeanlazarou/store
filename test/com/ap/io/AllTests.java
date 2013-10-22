package com.ap.io;

import java.util.ArrayList;
import java.util.List;

import com.ap.junit.TestSuites;

import junit.framework.Test;
import junit.framework.TestSuite;

//TODO empty file, cr lf / cr / lf(1 line w/ and w/out ln, 2 lines w/ and w/out last line)
/*TODO only <cr> lines
 *TODO add a test that garentees buffer is not growing too much!
 *TODO start at BOF/EOF several times w/ the same buffer
 *TODO Forward/BackwardReader should not return the sentinel '\n'

 *TODO implement next tests
	lire une ligne tomber sur \r, buffer consommé et le \n est chargé à la lecture suivante!
	public void _testLineTooLong() throws Exception {
		fail("not implemented");
	}
	
	public void _testNotAllTheFileIsLoaded() throws Exception {
		fail("not implemented");
	}
	
smaller than line buffer
zig-zag walking
empty file
1 line only w/ and w/out newline
2 lines
3 lines

	> Lignes de taille différentes
	> dernière ligne courte (buffer petit) et ligne précédente plus grande
	> aucune ligne, une ligne, deux lignes
	> progression avant arrière sur 1 ligne, 2 lignes, 3 lignes
	
	public void testNoEvent() throws Exception {
		fail("not implemented");
	}

	public void testOneLineOnlyFile() throws Exception {
		fail("not implemented");
	}

	public void testSmallThanLinesBuffer() throws Exception {
		fail("not implemented");
	}

	public void testNoEventAtTheTop() throws Exception {
		fail("not implemented");
	}

	public void testLastEventWithoutEOL() throws Exception {
		fail("not implemented");
	}

	public void testLinesTooLong() throws Exception {
		fail("not implemented");
	}
*/

public class AllTests {
	
	public static Test suite() {

    	prepareData();

    	TestSuite suite = new TestSuite("All IO Tests");

//    	suite.addTestSuite(BufferAllocationTest.class);
    	
    	addReaderTests(suite);
//    	addTextFileIteratorTests(suite);

		return suite;
        
    }

	private static void prepareData() {

		if (sameLengthLines != null) return;
		
		sameLengthLines = new ArrayList();
		sameLengthLines.add("2004-06-29 18:46:51,001 ERROR - message");
		sameLengthLines.add("2004-06-29 18:46:51,002 ERROR - message");
		sameLengthLines.add("2004-06-29 18:46:51,003 ERROR - message");
		sameLengthLines.add("2004-06-29 18:46:51,004 ERROR - message");
		sameLengthLines.add("2004-06-29 18:46:51,005 ERROR - message");
		
		fiveLines = new ArrayList();
		fiveLines.add("line 001");
		fiveLines.add("002");
		fiveLines.add("line 3");
		fiveLines.add("line 004");
		fiveLines.add("005");

		oneLine = new ArrayList();
		oneLine.add("line 001");
		
		twoLines = new ArrayList();
		twoLines.add("line 001");
		twoLines.add("002");

		oneEmpyLine = new ArrayList();
		oneEmpyLine.add("");
		
		twoEmpyLines = new ArrayList();
		twoEmpyLines.add("");
		twoEmpyLines.add("");
		
		fiveEmpyLines = new ArrayList();
		fiveEmpyLines.add("");
		fiveEmpyLines.add("");
		fiveEmpyLines.add("");
		fiveEmpyLines.add("");

	}

	private static void addReaderTests(TestSuite parent) {

    	TestSuite suite = new TestSuite("Reader Tests");
    	
    	parent.addTest(suite);

    	suite.addTest(addReaderTests(new TestSuite("Big Buffer"), 100));
    	suite.addTest(addReaderTests(new TestSuite("Normal Buffer"), 9));
    	suite.addTest(addReaderTests(new TestSuite("Small Buffer"), 1));
		
	}
	
	private static void addTextFileIteratorTests(TestSuite parent) {

    	TestSuite suite = new TestSuite("TextFileIterator Tests");
    	
    	parent.addTest(suite);

//    	suite.addTest(addTextFileIteratorTests(new TestSuite("Big Buffer"), 100));
    	suite.addTest(addTextFileIteratorTests(new TestSuite("Normal Buffer"), 9));
//    	suite.addTest(addTextFileIteratorTests(new TestSuite("Small Buffer"), 1));
		
	}
	
	private static TestSuite addReaderTests(TestSuite parent, int size) {
		
		TestSuite suite;

		suite = new TestSuite("ForwardReader Tests");
    	parent.addTest(suite);

    	addForwardReaderTestMethods(suite, "Empty", new IteratorFixture(new ArrayList(), size));
    	addForwardReaderTestMethods(suite, "One Line", new IteratorFixture(oneLine, size));
    	addForwardReaderTestMethods(suite, "Two Lines", new IteratorFixture(twoLines, size));
    	addForwardReaderTestMethods(suite, "Five Lines", new IteratorFixture(fiveLines, size));

    	addForwardReaderTestMethods(suite, "One Empty Line", new IteratorFixture(oneEmpyLine, size));
    	addForwardReaderTestMethods(suite, "Two Empty Lines", new IteratorFixture(twoEmpyLines, size));
    	addForwardReaderTestMethods(suite, "Five Empty Lines", new IteratorFixture(fiveEmpyLines, size));

		suite = new TestSuite("BackwardReader Tests");
    	parent.addTest(suite);

    	addBackwardReaderTestMethods(suite, "Empty", new IteratorFixture(new ArrayList(), size));
    	addBackwardReaderTestMethods(suite, "One Line", new IteratorFixture(oneLine, size));
    	addBackwardReaderTestMethods(suite, "Two Lines", new IteratorFixture(twoLines, size));
    	addBackwardReaderTestMethods(suite, "Five Lines", new IteratorFixture(fiveLines, size));

    	addBackwardReaderTestMethods(suite, "One Empty Line", new IteratorFixture(oneEmpyLine, size));
    	addBackwardReaderTestMethods(suite, "Two Empty Lines", new IteratorFixture(twoEmpyLines, size));
    	addBackwardReaderTestMethods(suite, "Five Empty Lines", new IteratorFixture(fiveEmpyLines, size));

		return parent;
		
	}
	
	private static TestSuite addTextFileIteratorTests(TestSuite parent, int size) {
		
		TestSuite suite;

		suite = new TestSuite("Last Line With Line Break");
    	parent.addTest(suite);

//    	addTextFileIteratorTestMethods(suite, "Empty", new IteratorFixture(new ArrayList(), size));
    	addTextFileIteratorTestMethods(suite, "One Line", new IteratorFixture(oneLine, size));
//    	addTextFileIteratorTestMethods(suite, "Two Lines", new IteratorFixture(twoLines, size));
//		addTextFileIteratorTestMethods(suite, "Five Lines", new IteratorFixture(fiveLines, size));

//    	addTextFileIteratorTestMethods(suite, "One Empty Line", new IteratorFixture(oneEmpyLine, size));
//    	addTextFileIteratorTestMethods(suite, "Two Empty Lines", new IteratorFixture(twoEmpyLines, size));
//    	addTextFileIteratorTestMethods(suite, "Five Empty Lines", new IteratorFixture(fiveEmpyLines, size));

//    	suite = new TestSuite("Last Line Without Line Break");
//    	parent.addTest(suite);

//    	addTextFileIteratorTestMethods(suite, "Empty", new IteratorFixture(new ArrayList(), size, false));
//    	addTextFileIteratorTestMethods(suite, "One Line", new IteratorFixture(oneLine, size, false));
//    	addTextFileIteratorTestMethods(suite, "Two Lines", new IteratorFixture(twoLines, size, false));
//		addTextFileIteratorTestMethods(suite, "Five Lines", new IteratorFixture(fiveLines, size, false));

//    	addTextFileIteratorTestMethods(suite, "One Empty Line", new IteratorFixture(oneEmpyLine, size, false));
//    	addTextFileIteratorTestMethods(suite, "Two Empty Lines", new IteratorFixture(twoEmpyLines, size, false));
//    	addTextFileIteratorTestMethods(suite, "Five Empty Lines", new IteratorFixture(fiveEmpyLines, size, false));

		return parent;
	}

	private static void addTextFileIteratorTestMethods(TestSuite parent, String name, IteratorFixture fixture) {

		//TODO remove the test suite name from the utility methods so that double-click works in Eclipse,heu?
		TestSuites.addTestMethods(parent, TextFileIteratorTest.class, 
				                          IteratorFixture.class, fixture);
		
	}
	
	private static void addForwardReaderTestMethods(TestSuite parent, String name, IteratorFixture fixture) {

		TestSuites.addInheritedTestMethods(parent, ForwardReaderTest.class, 
				                                   IteratorFixture.class, fixture);
		
	}
	
	private static void addBackwardReaderTestMethods(TestSuite parent, String name, IteratorFixture fixture) {

		TestSuites.addInheritedTestMethods(parent, BackwardReaderTest.class, 
				                                   IteratorFixture.class, fixture);
		
	}
	
	static List oneLine;
	static List twoLines;
	static List fiveLines;
	static List sameLengthLines;

	static List oneEmpyLine;
	static List twoEmpyLines;
	static List fiveEmpyLines;

}
