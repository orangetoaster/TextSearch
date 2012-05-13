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
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
/** This is the main class for running this program.
 * This uses the other objects to perform the algorithm.
 */
public class DocCompare {
	MultiSet<Term> stopwords, query, documentFrequencyList;
	Document[] documents;
	/** This allows direct execution.
	 *\param commandline strings
	 */
	public static void main(String args[]) {
		String usage = "Usage: java -cp Build/ DocumentCompare.DocCompare <options>\n" +
		"\tOptions:\n"+
	                "\t-k <number of results to print greater than 0>  (10)\n" +
	                "\t-s <stopwords document>                         (stopwords.txt)\n" +
	                "\t-f <query document> or -q <query document>      (query.txt)\n" +
	                "\t-d <data directory>                             (set)\n";
		int k=10;
		String s = "stopwords.txt", q = "query.txt", d = "set";
		/*Input Parsing*/
		for(int x=0; x < args.length; x +=2) {
			if(args[x].equals("-k"))
				k = Integer.parseInt(args[x +1]);
			else if(args[x].equals("-s"))
				s = args[x +1];
			else if(args[x].equals("-f") || args[x].equals("-q"))
				q = args[x +1];
			else if(args[x].equals("-d"))
				d = args[x +1];
			else {
				System.out.println(usage);
				return;
			}
		}
		/*Input sanitizing*/
		if(k <= 0) {
			System.out.println("k must be greater than 0.\n" +usage);
		}
		try {
			new DocCompare(q, k, d, s);
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
			System.out.println(usage);
		}
	}
	/** Document Comparison Object.
	 * On instantiation it opens documents, calculates cosine similarities and sorts results.
	 *\param queryFile This is the file to compare all other documents to.
	 *\param resultCount This is the number of results to print
	 *\param documentDirectory This is the location of all the other documents.
	 *\param stopwordsFile This is the list of stopwords, these words will not have an impact on the search.
	 *\throws IOException if anything doesn't open.
	 */
	public DocCompare(String queryFile, int resultCount, String documentDirectory, String stopwordsFile) throws IOException {
		/*Method:
		 *create stopword document
		 *create query document
		 *for each document
		 *	create document - without stopwords
		 *	for each term in document
		 *		add term once to documentFrequencyList
		 *	for each document
		 *		set cosine simularity
		 *sort documents
		 *return resultCount documents
		 */
		stopwords = new Document(stopwordsFile).getMultiSet();
		query = new Document(queryFile).getMultiSet();
		File documentFolder = new File(documentDirectory);
		if(documentFolder.isDirectory()) {
			File[] documentPaths = documentFolder.listFiles();
			documents = new Document[documentPaths.length];
			int x=0;
			for( File docPath : documentPaths) {
				if(docPath.isFile())
					documents[x] = new Document(docPath.getPath(), stopwords);
				x++;
			}
		}
		else {
			throw new IOException("Specified directory is not a directory.");
		}
		/*We have the documents and we need to add the terms to documentFrequencyList*/
		documentFrequencyList = new MultiSet<Term>();
		for(Document doc : documents) {
			documentFrequencyList.add(doc.getMultiSet().getSet());
		}
		for(Document doc : documents) {
			doc.setCosineSimilarity(query, documentFrequencyList);
		}
		Arrays.sort(documents);
		for(int x=0; resultCount > 0 && x < documents.length; resultCount --, x ++) {
			System.out.println(documents[x].getName() + " : " + documents[x].getCosineSimilarity());
		}
	}
}
