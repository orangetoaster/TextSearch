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
/** Immutable
 *This is a term, a case-insensitive wrapper for a string without puctuation.
 *The reason for this is to override the string's default compareTo.
 */
public class Term implements Comparable<Term> {
	private String value;
	/** Constructor, strips punctuation.
	 *\param String String to wrap.
	 */
	public Term(String newValue) {
		if(newValue != null)
			newValue = newValue.replaceAll("\\p{Punct}", "");
		if(newValue != null && !newValue.equals(""))
			value = newValue;
	}
	/** Clone by constructor.
	 *\param Term to clone.
	 */
	public Term(Term copy) {
		 value = copy.getValue();
	}
	/** Accessor.
	 *\return String value.
	 */
	public String getValue() {
		return value;
	}
	/** Compare To override.
	 *\param Term to compare to
	 *\return int value of compareToIgnoreCase, 0 when both strings are null.
	 */
	public int compareTo(Term comparee) {
		if(value == null && comparee.getValue() == null)
			return 0;
		else if(value == null)
			return -1;
		else if(comparee == null)
			return 1;
		else
			return value.compareToIgnoreCase( comparee.getValue());
	}
	/** toString override.
	 */
	public String toString() {
		return value;
	}
	/** hashCode override.
	 */
	public int hashCode() { 
		return value.toLowerCase().hashCode();
	}
	/** equals override.
	 */
	public boolean equals(Object other) {
		return ((other instanceof Term) && value.toLowerCase().equals(((Term)other).value.toLowerCase()));
	}
}
