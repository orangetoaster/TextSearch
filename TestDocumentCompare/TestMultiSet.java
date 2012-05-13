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
import DocumentCompare.MultiSet;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
/** Simple unit test for a MultiSet */
public class TestMultiSet {
	MultiSet<String> testSet;
	public TestMultiSet(){}
	@BeforeClass
	public static void setUpClass() throws Exception{}
	@AfterClass
	public static void tearDownClass() throws Exception{}
	@Before
	public void setUp() {
		testSet = new MultiSet<String>();
	}
	@After
	public void tearDown(){}
	@Test
	public void testEmptySet() {
		assertEquals(testSet.test("Cat"), false);
		assertEquals(testSet.count("Cat"), 0);
		assertEquals(testSet.count(), 0);
	}
	@Test
	public void testSingleMembership() {
		testSet.add("Cat");
		assertEquals(testSet.test("Cat"), true);
		assertEquals(testSet.count("Cat"), 1);
		assertEquals(testSet.count(), 1);
	}
	@Test
	public void testMultipleMembership() {
		testSet.add("Cat");
		testSet.add("Cat");
		assertEquals(testSet.test("Cat"), true);
		assertEquals(testSet.count("Cat"), 2);
		assertEquals(testSet.count(), 2);
	}
	@Test
	public void testVaryingMembership_single() {
		testSet.add("Cat");
		testSet.add("Dog");
		assertEquals(testSet.test("Cat"), true);
		assertEquals(testSet.test("Dog"), true);
		assertEquals(testSet.count("Cat"), 1);
		assertEquals(testSet.count("Dog"), 1);
		assertEquals(testSet.count(), 2);
	}
	@Test
	public void testVaryingMembership_multi() {
		testSet.add("Cat");
		testSet.add("Cat");
		testSet.add("Dog");
		assertEquals(testSet.test("Cat"), true);
		assertEquals(testSet.test("Dog"), true);
		assertEquals(testSet.count("Cat"), 2);
		assertEquals(testSet.count("Dog"), 1);
		assertEquals(testSet.count(), 3);
	}
	@Test
	public void testRemoval_single() {
		testSet.add("Cat");
		testSet.remove("Cat");
		assertEquals(testSet.test("Cat"), false);
		assertEquals(testSet.count("Cat"), 0);
		assertEquals(testSet.count(), 0);
	}
	@Test
	public void testRemoval_multi() {
		testSet.add("Cat");
		testSet.add("Cat");
		testSet.add("Dog");
		testSet.remove("Cat");
		assertEquals(testSet.test("Cat"), true);
		assertEquals(testSet.test("Dog"), true);
		assertEquals(testSet.count("Cat"), 1);
		assertEquals(testSet.count("Dog"), 1);
		assertEquals(testSet.count(), 2);
	}
	@Test
	public void testManyEntries() {
		testSet.add("Cat");
		testSet.add("Dog");
		testSet.add("Pig");
		testSet.add("Cow");
		testSet.add("Chicken");
		assertEquals(testSet.test("Cat"), true);
		assertEquals(testSet.test("Dog"), true);
		assertEquals(testSet.test("Pig"), true);
		assertEquals(testSet.test("Cow"), true);
		assertEquals(testSet.test("Chicken"), true);
		assertEquals(testSet.count(), 5);
	}
	@Test
	public void testGetSet() {
		testSet.add("Cat");
		testSet.add("Cat");
		testSet.add("Dog");
		testSet.add("Cow");
		testSet.add("Cow");
		testSet.add("Cow");
		MultiSet<String> singleSet = testSet.getSet();
		assertEquals(testSet.count("Cat"), 2);
		assertEquals(testSet.count("Dog"), 1);
		assertEquals(testSet.count("Cow"), 3);
		assertEquals(testSet.count(), 6);
		assertEquals(singleSet.count("Cat"), 1);
		assertEquals(singleSet.count("Dog"), 1);
		assertEquals(singleSet.count("Cow"), 1);
		assertEquals(singleSet.count(), 3);
	}
	@Test
	public void testAddMultiSet() {
		testSet.add("Cat");
		MultiSet<String> otherSet = new MultiSet<String>();
		otherSet.add("Chicken");
		otherSet.add("Cow");
		otherSet.add("Cow");
		testSet.add(otherSet);
		MultiSet<String> thirdSet = new MultiSet<String>();
		thirdSet.add("Dog");
		thirdSet.add("Cat");
		testSet.add(thirdSet);
		assertEquals(testSet.count("Cat"), 2);
		assertEquals(testSet.count("Chicken"), 1);
		assertEquals(testSet.count("Cow"), 2);
		assertEquals(testSet.count("Dog"), 1);
		assertEquals(testSet.count(), 6);
	}
	@Test
	public void testIterations() {
		Set<String> items = new HashSet<String>();
		items.add("Cat");
		items.add("Dog");
		items.add("Turkey");
		for(String item : items) {
			testSet.add(item);
		}
		for(String bagElement : items) {
			assertEquals(testSet.count(bagElement), 1);
		}
		Iterator<String> iter = testSet.iterator();
		assertEquals(items.contains(iter.next()), true);
		assertEquals(items.contains(iter.next()), true);
		assertEquals(items.contains(iter.next()), true);
	}
}
