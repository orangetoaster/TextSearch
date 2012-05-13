/** This is a source file belonging to the DocumentCompare project.
 * Copyright (C) 2012 Lorne Schell <orange.toaster@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package TestDocumentCompare;
import DocumentCompare.Term;
import org.junit.*;
import static org.junit.Assert.*;
/**Simple unittest for Term object */
public class TestTerm {
	Term testTerm;
	public TestTerm(){}
	@BeforeClass
	public static void setUpClass() throws Exception{}
	@AfterClass
	public static void tearDownClass() throws Exception{}
	@Before
	public void setUp(){}
	@After
	public void tearDown(){}
	@Test
	public void testEmpty() {
		testTerm = new Term((String) null);
		assertEquals(testTerm.getValue(), null);
		assertEquals(testTerm.compareTo(new Term((String)null)), 0);
	}
	@Test
	public void testAlphabetical() {
		testTerm = new Term("Cat");
		Term dogTerm = new Term("Dog");
		assertEquals(dogTerm.compareTo(testTerm), 1);
		assertEquals(testTerm.compareTo(dogTerm), -1);
		assertEquals(testTerm.compareTo(testTerm), 0);
	}
	@Test
	public void testPunctuation() {
		testTerm = new Term("punc.t!");
		Term sameTerm = new Term("punc.t!");
		Term diffTerm = new Term("not-punc");
		assertEquals(testTerm.getValue(), "punct" );
		assertEquals(testTerm.compareTo(sameTerm), 0);
		assertEquals(testTerm.compareTo(diffTerm) == 0, false);
	}
}
