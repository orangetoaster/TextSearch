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
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
/**  Document object. 
 * It reads it's file on creation and creates a MultiSet representation of the terms in the document.
 */
public class Document implements Comparable<Document> {
	private double cosineSimilarity = -1;
	private MultiSet<Term> terms;
	private String fileName;
	/** Document Constructor.
	 * This opens the file and parses it for all terms not in the stopwords list.
	 *\param newFileName the file to open.
	 *\param stopwords The set of stopwords to ignore.
	 */
	public Document(String newFileName, MultiSet<Term> stopwords) throws IOException {
		terms = new MultiSet<Term>();
		fileName = newFileName;
		BufferedReader documentFile;
		documentFile = new BufferedReader(new FileReader(fileName));
		String fullLine;
		while((fullLine=documentFile.readLine()) != null) {
			String line[] = fullLine.split(" ");
			for(String word: line) {
				Term newTerm = new Term(word);
				/*If the string is only punctation, getvalue will return null as the value will not have been set.
				 * otherwise, if there is a stopwords set then ensure that our term isn't in it.
				 */
				if(newTerm.getValue() != null && (stopwords == null || !stopwords.test(newTerm))) {
					terms.add(newTerm);
				}
			}
		}
	}
	/** Document Constructor, no stopwords.
	 * This is the same as the other constructor but doesn't use stopwords, useful when you want to open a stopwords document.
	 *\param fileName the document to open.
	 */
	public Document(String fileName) throws IOException {
		this(fileName, null);
	}
	/** Comparable implementation.
	 * This compares two documents based on their cosine similarities to another document.
	 *\param comparee The document to compare to.
	 *\throws InvalidStateException If the cosine similarity hasn't been set on either of the documents being compared, this is thrown.
	 */
	public int compareTo(Document comparee) throws InvalidDocumentStateException {
		if(cosineSimilarity == -1)
			throw new InvalidDocumentStateException("Cosine Similarity Unset.");
		if( cosineSimilarity == comparee.getCosineSimilarity())
			return 0;
		else if( cosineSimilarity > comparee.getCosineSimilarity())
			return -1;
		else
			return 1;
	}
	/** Cosine Similarity accessor.
	 *Returns the value of the cosine similarity.
	 *\return cosine similarity
	 */
	public double getCosineSimilarity() {
		if(cosineSimilarity == -1)
			throw new InvalidDocumentStateException("Cosine Similarity Unset.");
		return cosineSimilarity;
	}
	/** Filename accessor.
	 * The document object remembers where it was read from.
	 *\return fileName
	 */
	public String getName() {
		return fileName;
	}
	/** Cosine similarity setter.
	 *This calculates the TF-IDF cosine similarity between this document and another, with consideration to a larger dataset
	 *The algorithm used is documented in greater detail in Database Systems Concepts, Fifth Edition ISBN 0-07-295886-3 pages 761-763
	 *\param MultiSet<Term> the multiset of the base document to compare to
	 *\param MultiSet<Term> the multiset that contains information about term frequency in the dataset
	 */
	public void setCosineSimilarity(MultiSet<Term> base, MultiSet<Term> documentFrequency) {
		double area=0;
		double baseMagnitude=0, magnitude=0;
		/*We need to consider both sets of words, the words in this document and the words in the multiset.
		 *This is so that the sine similarity is not 1 if our multiset is a superset of the base document
		 * multiset, ie so that a query of <cat> against <dog cat> doesn't return 1.
		 */
		MultiSet<Term> baseUnionTerms = new MultiSet<Term>();
		baseUnionTerms.add(base);
		baseUnionTerms.add(terms);
		/*We need to check the relevancy of each term in the document
		*/
		for(Term word : baseUnionTerms) {
			if(documentFrequency.count(word) != 0) {
				/*The inverse document frequency scales the relevence by the size of the document.
				 * This is so that a very large document with our search in it twice is not ranked
				 *  above a very small document with our search in it once.
				 */
				double inverseDocumentFrequency = 1 / (double) documentFrequency.count(word);
				/*The relevance of a term is based on the term frequency in the document, this is logrithmic
				 * as a document with the search term in it 10 times isn't 10 times more relevant than one
				 * with our search term once.
				 */
				double ourRelevance = inverseDocumentFrequency * Math.log(1 + (double) terms.count(word)/ (double) terms.count());
				double baseRelevance = inverseDocumentFrequency * ( Math.log(1 + (double) base.count(word)/(double)base.count()));
				/*We need to keep a running total so that we can normalize it at the end.
				 */
				area += ourRelevance * baseRelevance;
				magnitude += ourRelevance * ourRelevance;
				baseMagnitude += baseRelevance* baseRelevance;
			}
		}
		/*If the magnitude is 0, then our document is disjoint from the base document.
		 * We need to set the cosineSimilarity to 0 to avoid zero division.
		 */
		if(magnitude == 0)
			cosineSimilarity =0;
		else
			cosineSimilarity= area / (Math.sqrt(magnitude) * Math.sqrt(baseMagnitude));
	}
	/** Multi Set accessor.
	 *Returns a pointer to the multiset that represents this document.
	 *\return MultiSet pointer.
	 */
	public MultiSet<Term> getMultiSet() {
		return new MultiSet<Term>(terms);
	}
}
