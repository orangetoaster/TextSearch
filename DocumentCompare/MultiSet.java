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
package DocumentCompare;
import java.lang.Iterable;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashMap;
/** A multiset, true to the definition. 
 * Also known as a bag. This object has been omitted from the java standard library.
 */
public class MultiSet<T extends  Comparable<? super T>> implements Iterable<T> {
	private int elements=0;
	private HashMap<T, Node<T>> contents;
	/** Constructor, gives you an instance of a multiset of the declared type.
	 */
	public MultiSet() {
		contents = new HashMap<T, Node<T>>();
	}
	/** Clone by constructor.
	 */
	public MultiSet(MultiSet<T> other) {
		contents = new HashMap<T, Node<T>>(other.contents);
		elements = other.elements;
	}
	/** Adds an object to the set.
	 *\param object Object to add.
	 */
	public void add(T object) {
		add(object, 1);
	}
	/** Adds the elements of another multiset to the set.
	 *\param MultiSet Set to add.
	 */
	public void add(MultiSet<T> otherSet) {
		Collection<Node<T>> nodes = otherSet.contents.values();
		for(Node<T> n : nodes) {
			add(n.value, n.getCount());
		}
	}
	/** Removes an object from the set.
	 *\param object Object to remove.
	 *\return 0 when object is not present in set, 1 if removed.
	 */
	public int remove(T object) {
		Node<T> exists = getObject(object);
		if(exists != null) {
			elements--;
			if(exists.decrement() == 0) {
				deleteNode(exists);
			}
			return 1;
		}
		else
			return 0;
	}
	/** Tests membership of an object.
	 *\param object Object to test membership of.
	 *\return boolean
	 */
	public boolean test(T object) {
		Node<T> exists = getObject(object);
		return (exists != null);
	}
	/** Returns count of object in set.
	 *\param object
	 *\return int count.
	 */
	public int count(T object) {
		Node<T> exists = getObject(object);
		if(exists == null)
			return 0;
		else return exists.getCount();
	}
	/** Returns count of all objects in set.
	 *\return total number of objects.
	 */
	public int count() {
		return elements;
	}
	/** Returns a multiset with all values 1.
	 *\return MultiSet with unique entries.
	 */
	public MultiSet<T> getSet() {
		MultiSet<T> result = new MultiSet<T>();
		Collection<Node<T>> nodes = contents.values();
		for(Node<T> index : nodes) {
			result.add(index.value, 1);
		}
		return result;
	}
	/** Returns an instance of an iterator.
	 * So that operations can be performed on the unique
	 *elements of the set, complete with inline class override.
	 *\return Iterator of the multiset.
	 */
	public Iterator<T> iterator() {
		Iterator<T> result = new   Iterator<T>() {
						private Iterator<Node<T>> index = contents.values().iterator();
						public boolean hasNext() {
							return index.hasNext();
						}
						public T next() {
							return index.next().value;
						}
						public void remove() {}
					};
		return result;
	}
	private void add(T object, int count) {
		Node<T> exists = getObject(object);
		if(exists == null) {
			contents.put(object, new Node<T>(object, count));
			elements+=count;
		}
		else {
			exists.increment(count);
			elements+=count;
		}
	}
	private Node<T> getObject(T object) {
		Node<T> fetch = contents.get(object);
		if(fetch != null && fetch.compareTo(object) == 0) return fetch;
		else return null;
	}
	private void deleteNode(Node<T> node) {
		contents.remove(node.value);
	}
	private class Node<T extends Comparable<? super T> > implements Comparable<T> {
		private int count;
		T value;
		Node(T newValue, int newCount) {
			value = newValue;
			count = newCount;
		}
		public int compareTo(T comparee) {
			return value.compareTo(comparee);
		}
		public void increment(int amount) {
			count +=amount;
		}
		public int decrement() {
			count --;
			return count;
		}
		public int getCount() {
			return count;
		}
	}
}
