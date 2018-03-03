/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
// package org.apache.lucene.demo;
package lucene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cz.CzechAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
//import com.google.common.*;
import com.google.common.io.CharStreams;

/**
 * Index all text files under a directory.
 * <p>
 * This is a command-line application demonstrating simple Lucene indexing. Run
 * it with no command-line arguments for usage information.
 */
public class IndexFiles {
	private IndexFiles() {
	}

	/**
	 * Indexes the given file using the given writer, or if a directory is give
	 * recurses over files and directories found under the given directory.
	 *
	 * NOTE: This method indexes one document per input file. This is slow. For
	 * throughput, put multiple documents into your input file(s). An example in the
	 * benchmark module, which can create "line doc" files, one document per line
	 * using the <a href=
	 * "../../../../../contrib-benchmark/org/apache/lucene/benchmark/byTask/tasks/WriteLineDocTask.html"
	 * >WriteLineDocTask</a>.
	 *
	 * @param writer
	 *            Writer to the index where the given file/dir info will be
	 * @param path
	 *            The file to index, or the directory to recurse into to find
	 * @throws IOException
	 *             If there is a low-level I/O error
	 */
	static void indexDocs(final IndexWriter writer, Path path, int run) throws IOException {

		if (Files.isDirectory(path)) {
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					try {
						indexDoc(writer, file, attrs.lastModifiedTime().toMillis(), run);
					} catch (IOException ignore) {
						// don't index files that can't be read.
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} else {
			indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis(), run);
		}

		// try (BufferedReader stream = new BufferedReader(Files.newBufferedReader(path,
		// Charset.forName("UTF-8")))) {
		// String doc;
		// while ((doc = stream.readLine()) != null) {
		// doc = doc.trim();
		// Path file =
		// // List<String> tokens = parseDoc(doc);
		//
		// indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
		// }
		// } catch (IOException e) {
		// System.out.println("Doc does not exist");
		// }
	}

	// public static void indexDocs(Path path) {
	// String docsList = "/home/liefe/data/documents.list";
	// try (BufferedReader stream = new BufferedReader(new FileReader(docsList))) {
	// String doc;
	// while ((doc = stream.readLine()) != null) {
	// doc = doc.trim();
	// List<String> tokens = parseDoc(doc);
	//
	// }
	// } catch (IOException e) {
	// System.out.println("Doc does not exist");
	// }
	// }

	/** Indexes a single document */
	@SuppressWarnings("deprecation")
	static void indexDoc(IndexWriter writer, Path file, long lastModified, int run) throws IOException {

		try (BufferedReader stream = new BufferedReader(
				new InputStreamReader(new GZIPInputStream(Files.newInputStream(file))))) {
			// try (BufferedReader stream = new BufferedReader(
			// new InputStreamReader(new GZIPInputStream(new FileInputStream(doc))))) {

			// Make a new, empty document
			Document doc = new Document();
			// Add the path of the file as a field named "path". Use a
			// field that is indexed (i.e. searchable), but don't tokenize
			// the field into separate words and don't index term frequency
			// or positional information:
			Field pathField = new StringField("path", file.toString(), Field.Store.YES);
			doc.add(pathField);
			// Add the last modified date of the file a field named "modified".
			// Use a LongPoint that is indexed (i.e. efficiently filterable with
			// PointRangeQuery). This indexes to milli-second resolution, which
			// is often too fine. You could instead create a number based on
			// year/month/day/hour/minutes/seconds, down the resolution you require
			// For example the long value 2011021714 would mean
			// February 17, 2011, 2-3 PM.
			doc.add(new LongPoint("modified", lastModified));

			String text = CharStreams.toString(stream); // whole doc text

			// For base run, do not perform pre-processing
			if (run == 0) {
				String tokensOneField = parseText(text, run);
				if (tokensOneField != null) {
					// System.out.println(tokens);
					// Add the contents of the file to a field named "contents". Specify a
					// so that the text of the file is tokenized and indexed, but not store
					// Note that FileReader expects the file to be in UTF-8 encoding.
					// If that's not the case searching for special characters will fail.
					// doc.add(new TextField("contents", new BufferedReader(new
					// InputStreamReader(stream, StandardCharsets.UTF_8))));
					// This is a text field replacing the reader. Not sure if best use
					doc.add(new TextField("text", tokensOneField, Field.Store.NO));
				}

			}

			else {
				// System.out.println(text);
				// StringBuilder tokens;

				// Parse docid
				String regex = "<DOCID>(.*)</DOCID>";
				Pattern p = Pattern.compile(regex, Pattern.MULTILINE);
				Matcher m = p.matcher(text);
				String docId = null;
				while (m.find()) {
					// System.out.println(i + m.group(1));
					// tokens.add(m.group(1));
					// tokens.append(m.group(1) + " ");
					docId = m.group(1);
					// i++;
				}
				// System.out.println("docid: " + docId);
				// Add field to doc
				Field docIdField = new StringField("docId", docId, Field.Store.YES);
				doc.add(docIdField);
				// Get title
				regex = "<TITLE>(.*)</TITLE>";
				p = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
				m = p.matcher(text);
				// tokens = new StringBuilder();
				String title = null;
				while (m.find()) {
					// System.out.println(i + m.group(1));
					// tokens.add(m.group(1));
					// tokens.append(m.group(1) + " ");
					title = m.group(1);
					// i++;
				}

				// System.out.println("title: " + title);
				// System.out.println("parsing title");
				if (title != null) {
					String tokens = parseText(title, run);
					// System.out.println(tokens);
					// Add the contents of the file to a field named "contents". Specify a
					// so that the text of the file is tokenized and indexed, but not store
					// Note that FileReader expects the file to be in UTF-8 encoding.
					// If that's not the case searching for special characters will fail.
					// doc.add(new TextField("contents", new BufferedReader(new
					// InputStreamReader(stream, StandardCharsets.UTF_8))));
					// This is a text field replacing the reader. Not sure if best use
					Field tField = new TextField("title", tokens, Field.Store.NO);
					tField.setBoost(2f);
					doc.add(tField);
					doc.add(new NumericDocValuesField("boost", 2L));

					// tField.(1.8f);

				}

				// Parse <TEXT>

				regex = "<TEXT>(.*)</TEXT>";
				p = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
				m = p.matcher(text);
				// tokens = new StringBuilder();
				String body = null;
				while (m.find()) {
					// System.out.println(i + m.group(1));
					// tokens.add(m.group(1));
					// tokens.append(m.group(1) + " ");
					// i++;
					body = m.group(1);
				}

				// System.out.println("title: " + title);
				// System.out.println("parsing body");
				if (body != null) {
					String tokens = parseText(body, run);
					// System.out.println(tokens);
					// Add the contents of the file to a field named "contents". Specify a
					// so that the text of the file is tokenized and indexed, but not store
					// Note that FileReader expects the file to be in UTF-8 encoding.
					// If that's not the case searching for special characters will fail.
					// doc.add(new TextField("contents", new BufferedReader(new
					// InputStreamReader(stream, StandardCharsets.UTF_8))));
					// This is a text field replacing the reader. Not sure if best use
					doc.add(new TextField("text", tokens, Field.Store.NO));
					// doc.add(new NumericDocValuesField("boost", 2L));

				} else
					System.err.println("No body for this document.");
			}
			System.out.println("fields");
			System.out.println(doc.getFields());

			if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
				// New index, so we just add the document (no old document can be the
				System.out.println("adding " + file);
				writer.addDocument(doc);
			} else {
				// Existing index (an old copy of this document may have been indexed
				// we use updateDocument instead to replace the old one matching the
				// path, if present:
				System.out.println("updating " + file);
				writer.updateDocument(new Term("path", file.toString()), doc);
			}
		}

	}

	public static String parseText(String text, int run) {

		// String file = "/home/liefe/data/sample/LN-20020102001.vert.gz";
		// FileInputStream fis = new FileInputStream(file);
		// GZIPInputStream gz = new GZIPInputStream(fis);
		// BufferedReader stream = new BufferedReader(new InputStreamReader(gz));
		// try (BufferedReader stream = new BufferedReader(
		// new InputStreamReader(new GZIPInputStream(Files.newInputStream(doc))))) {
		// try (BufferedReader stream = new BufferedReader(
		// new InputStreamReader(new GZIPInputStream(new FileInputStream(doc))))) {

		// String text = CharStreams.toString(stream);
		//
		// String regex = "<DOCID>(.*)</DOCID>";
		// Pattern p = Pattern.compile(regex, Pattern.MULTILINE);
		// Matcher m = p.matcher(text);
		// docid = docidMatch.group(1)
		//
		// # Get title and return for tier 1 index
		// if title:
		// titleMatch = re.search('<TITLE>(.*)</TITLE>', text, re.DOTALL)
		// if titleMatch is not None:
		// text = titleMatch.group(1)
		// else:
		// text = None
		//
		// # Otherwise, parse and return <TEXT>
		// else:
		// textMatch = re.search('<TEXT>(.*)</TEXT>', text, re.DOTALL)
		// text = textMatch.group(1)
		//
		//
		//

		// String s = "This is a line.\nThis is another one.\n";
		// System.out.println(text);
		String regex;
		if (run == 0) {
			regex = "^[0-9]+\\s+" // line/token number
					+ "([a-zěščřžťďňńáéíýóůúA-ZĚŠČŘŽŤĎŇŃÁÉÍÝÓŮÚ]+)[0-9]*\\s+" // word
					+ "[a-zěščřžťďňńáéíýóůúA-ZĚŠČŘŽŤĎŇŃÁÉÍÝÓŮÚ]+[0-9]*[-_]?.*\\s+" // lemma
					+ "[A-ZĚŠČŘŽŤĎŇŃÁÉÍÝÓŮÚ0-9-=]+\\s+" // cat, etc
					+ "[a-zěščřžťďňńáéíýóůúA-ZĚŠČŘŽŤĎŇŃÁÉÍÝÓŮÚ]+$"; // not used
		}

		else {
			regex = "^[0-9]+\\s+" // line/token number
					+ "[a-zěščřžťďňńáéíýóůúA-ZĚŠČŘŽŤĎŇŃÁÉÍÝÓŮÚ]+[0-9]*\\s+" // word
					+ "([a-zěščřžťďňńáéíýóůúA-ZĚŠČŘŽŤĎŇŃÁÉÍÝÓŮÚ]+)[0-9]*[-_]?.*\\s+" // lemma
					+ "[A-ZĚŠČŘŽŤĎŇŃÁÉÍÝÓŮÚ0-9-=]+\\s+" // cat, etc
					+ "[a-zěščřžťďňńáéíýóůúA-ZĚŠČŘŽŤĎŇŃÁÉÍÝÓŮÚ]+$"; // not used
		}

		// String regex = "(^.+$)";
		// System.out.println(text.matches(regex));
		Pattern p = Pattern.compile(regex, Pattern.MULTILINE);
		Matcher m = p.matcher(text);

		// int i = 0;
		// List<String> tokens = new ArrayList<>();
		StringBuilder tokens = new StringBuilder();
		while (m.find()) {
			// System.out.println(i + m.group(1));
			// tokens.add(m.group(1));
			tokens.append(m.group(1) + " ");
			// i++;
		}

		// System.out.println(tokens.size());

		return tokens.toString();

		// } catch (IOException e) {
		// System.out.println("Doc does not exist");
		// return null;
		// }
	}

	/** Index all text files under a directory. */
	public static void main(String[] args) {
		String usage = "java org.apache.lucene.demo.IndexFiles" + " [-index INDEX_PATH] [-docs DOCS_PATH] [-update]\n\n"
				+ "This indexes the documents in DOCS_PATH, creating a Lucen"
				+ "in INDEX_PATH that can be searched with SearchFiles";
		String indexPath = "index";
		String docsPath = null;
		int run = 0; // default base run
		boolean create = true;
		for (int i = 0; i < args.length; i++) {
			if ("-index".equals(args[i])) {
				indexPath = args[i + 1];
				i++;
			} else if ("-docs".equals(args[i])) {
				docsPath = args[i + 1];
				i++;
			} else if ("-update".equals(args[i])) {
				create = false;
			} else if ("-run".equals(args[i])) {
				run = Integer.parseInt(args[i + 1]);
			}
		}
		if (docsPath == null) {
			System.err.println("Usage: " + usage);
			System.exit(1);
		}
		final Path docDir = Paths.get(docsPath);
		if (!Files.isReadable(docDir)) {
			System.out.println("Document directory '" + docDir.toAbsolutePath()
					+ "' does not exist or is not readable, please check the path");
			System.exit(1);
		}
		Date start = new Date();
		try {
			System.out.println("Indexing to directory '" + indexPath + "'...");
			Directory dir = FSDirectory.open(Paths.get(indexPath));
			// Analyzer analyzer = new StandardAnalyzer();
			Analyzer analyzer = new CzechAnalyzer();

			// TokenStream str = analyzer.tokenStream(null, reader);
			// str.reset();
			// while (str.incrementToken()) {
			// CharTermAttribute att = str.getAttribute(CharTermAttribute.class);
			// OffsetAttribute off = str.getAttribute(OffsetAttribute.class);
			// String token = text.substring(off.startOffset(), off.endOffset());
			// String lemma = att.toString();
			// System.out.println("token: " + token + " lemma: " + lemma);
			// }
			// // new StopFilter(, stopwords);
			// // anal.normalize(s, s);
			// str.close();
			// analyzer.close();

			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			if (create) {
				// Create a new index in the directory, removing any
				// previously indexed documents:
				iwc.setOpenMode(OpenMode.CREATE);
			} else {
				// Add new documents to an existing index:
				iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			}
			// Optional: for better indexing performance, if you
			// are indexing many documents, increase the RAM
			// buffer. But if you do this, increase the max heap
			// size to the JVM (eg add -Xmx512m or -Xmx1g):
			//
			// iwc.setRAMBufferSizeMB(256.0);
			IndexWriter writer = new IndexWriter(dir, iwc);
			indexDocs(writer, docDir, run);
			// NOTE: if you want to maximize search performance,
			// you can optionally call forceMerge here. This can be
			// a terribly costly operation, so generally it's only
			// worth it when your index is relatively static (ie
			// you're done adding documents to it):
			//
			// writer.forceMerge(1);
			writer.close();
			Date end = new Date();
			System.out.println(end.getTime() - start.getTime() + " total milliseconds");
		} catch (IOException e) {
			System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
		}
	}

}
