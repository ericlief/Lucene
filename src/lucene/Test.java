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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import com.google.common.io.CharStreams;

public class Test {
	public static void main(String[] args) throws IOException {

		// String file = "/home/liefe/data/sample/LN-20020102001.vert.gz";
		// FileInputStream fis = new FileInputStream(file);
		// GZIPInputStream gz = new GZIPInputStream(fis);
		// BufferedReader stream = new BufferedReader(new InputStreamReader(gz));
		// String file = "/home/liefe/Lucene/port-text.txt.gz";
		// Path path = Paths.get(file);

		// String s = Files.toString(file, Charsets.UTF_8);
		// CharSource s = Files.asCharSource(stream, Charsets.UTF_8);

		String docsPath = "/home/liefe/data/sample";
		final Path docDir = Paths.get(docsPath);
		if (!Files.isReadable(docDir)) {
			System.out.println("Document directory '" + docDir.toAbsolutePath()
					+ "' does not exist or is not readable, please check the path");
			System.exit(1);
		}

		if (Files.isDirectory(docDir)) {
			Files.walkFileTree(docDir, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

					try (BufferedReader stream = new BufferedReader(
							new InputStreamReader(new GZIPInputStream(Files.newInputStream(file))))) {
						// try (BufferedReader stream = new BufferedReader(
						// new InputStreamReader(new GZIPInputStream(new
						// FileInputStream(docDir.toString()))))) {

						String text = CharStreams.toString(stream);
						// String s = "This is a line.\nThis is another one.\n";
						// System.out.println(text);
						// text = "<TITLE>what the fuck?</TITLE>";
						String regex = "^[0-9]+\\s+" // line/token number
								+ "[a-zěščřžťďňńáéíýóůúA-ZĚŠČŘŽŤĎŇŃÁÉÍÝÓŮÚ]+[0-9]*\\s+" // word
								+ "([a-zěščřžťďňńáéíýóůúA-ZĚŠČŘŽŤĎŇŃÁÉÍÝÓŮÚ]+)[0-9]*[-_]?.*\\s+" // lemma
								+ "[A-ZĚŠČŘŽŤĎŇŃÁÉÍÝÓŮÚ0-9-=]+\\s+" // cat, etc
								+ "[a-zěščřžťďňńáéíýóůúA-ZĚŠČŘŽŤĎŇŃÁÉÍÝÓŮÚ]+$"; // not used

						regex = "<TITLE>(.*)</TITLE>";

						// String regex = "(^.+$)";
						// System.out.println(text.matches(regex));
						Pattern p = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
						Matcher m = p.matcher(text);

						// int i = 0;
						// List<String> tokens = new ArrayList<>();
						StringBuilder tokens = new StringBuilder();
						String res = null;
						while (m.find()) {
							// System.out.println(i + m.group(1));
							// tokens.add(m.group(1));
							// tokens.append(m.group(1) + " ");
							res = m.group();
							// i++;
						}

						// System.out.println(tokens.toString());
						System.out.println(res);

					}
					return FileVisitResult.CONTINUE;
				}
			});

		}

	}
}

//
// try (BufferedReader stream = new BufferedReader(new InputStreamReader(
// new GZIPInputStream(Files.newInputStream(docsDir)))) {
//
//
//
//
// String text = CharStreams.toString(stream);
// // String s = "This is a line.\nThis is another one.\n";
// System.out.println(text);
//
// String regex = "^[0-9]+\\s+" // line/token number
// + "([a-zěščřžťďňńáéíýóůúA-ZĚŠČŘŽŤĎŇŃÁÉÍÝÓŮÚ]+)[0-9]*\\s+" // word
// + "[a-zěščřžťďňńáéíýóůúA-ZĚŠČŘŽŤĎŇŃÁÉÍÝÓŮÚ]+[0-9]*[-_]?.*\\s+" // lemma
// + "[A-ZĚŠČŘŽŤĎŇŃÁÉÍÝÓŮÚ0-9-=]+\\s+" // cat, etc
// + "[a-zěščřžťďňńáéíýóůúA-ZĚŠČŘŽŤĎŇŃÁÉÍÝÓŮÚ]+$"; // not used
//
// // String regex = "(^.+$)";
// // System.out.println(text.matches(regex));
// Pattern p = Pattern.compile(regex, Pattern.MULTILINE);
// Matcher m = p.matcher(text);
//
// int i = 0;
// List<String> tokens = new ArrayList<>();
// StringBuilder s = new StringBuilder();
// while (m.find()) {
// System.out.println(i + m.group(1));
// tokens.add(m.group(1));
// s.append(m.group(1) + " ");
// i++;
// }
//
// System.out.println(s.toString());

// text = s.toString();
// StringReader reader = new StringReader(text);
// String[] sw = new String[] { "ten", "ta", "to", "ho", "jsem", "je", "jsou",
// "a", "i", "ale" };
// CharArraySet stopwords = StopFilter.makeStopSet(sw);
// // Analyzer analyzer = new CzechAnalyzer(stopwords);
// Analyzer analyzer = new CzechAnalyzer();
// //
// // analyzer.getStopwordSet();
// analyzer.normalize(null, text);
//
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

// } catch (IOException e) {
// System.out.println(" caught a " + e.getClass() + "\n with message: " +
// e.getMessage());
//
//

// file = "file.txt";
// read file into stream, try-with-resources
// try (Stream<String> stream = Files.lines(Paths.get(file))) {
// String line;
// StringBuilder text = new StringBuilder();
// int no = 0;
// while ((line = stream.readLine()) != null) {
// // String newline = line.replace("\r", "\\r");
//
// System.out.printf(line);
// text.append(line);
// no++;// String line;
// StringBuilder text = new StringBuilder();
// int no = 0;
// while ((line = stream.readLine()) != null) {
// // String newline = line.replace("\r", "\\r");
//
// System.out.printf(line);
// text.append(line);
// no++;
// stream.forEach(System.out::println);
// stream.forEach(action);
//
// } catch (IOException e) {
// e.printStackTrace();
// }
// String file = "file.txt.gz";

// try (DataInputStream stream = new DataInputStream(new GZIPInputStream(new
// FileInputStream(file)))) {
// String line;
// while ((line = stream.readUTF()) != "\f") {
// System.out.println(line);
// }
//

// try (BufferedReader stream = new BufferedReader(new FileReader(file))) {
// char[] buffer = new char[4096];
// int i = stream.read(buffer);
// if (i <= 0)
// System.out.println("buffer full");
// for (i = 0; i < buffer.length; i++)
// System.out.print(buffer[i]);
// System.out.println();
// // docidMatch = buffer.matches("<DOCID>(.*)</DOCID>",re.DOTALL);
// try (BufferedReader stream = new BufferedReader(
// new InputStreamReader(new GZIPInputStream(new FileInputStream(file))))) {
// StringBuilder sb = new StringBuilder();
// sb.append("1\n");
// sb.append("2\n");
// sb.append("3\n");
// file = sb.toString();
// String[] sarr = file.split("\\r?\\n");
////
// for (String line : lines)
// System.out.println(line);
// for (String str : sarr)
// System.out.println(str);
//
// System.out.println(sarr.length);
// String[] lines = (String[]) Files.lines(path).toArray();
//
// for (String line : lines)
// System.out.println(line);
//
// Files.lines(new File(file).toPath()).map(s -> s.trim()).filter(s ->
// s.startsWith("abc"))
// .forEach(System.out::println);
// .orElseThrow(() -> new IOError("Enclosing method not found"));
// try (BufferedReader stream = new BufferedReader(new StringReader(file))) {
//
// String line;
// StringBuilder text = new StringBuilder();
// int no = 0;
// while ((line = stream.readLine()) != null) {
// // String newline = line.replace("\r", "\\r");
//
// System.out.printf(line);
// text.append(line);
// no++;
// }
//
// System.out.println(no);
//
// String s = text.toString();
// String[] sarr = s.split("\\r?\\n");
//
// for (String str : sarr)
// System.out.println(str);
//
// System.out.println(sarr.length);
// System.out.println("total " + no);
// System.out.println("Doc does not exist");

// System.out.printf("test\ntest");
// Scanner sc = new Scanner(new File(file));
// String s = sc.useDelimiter("\\Z").next();
// sc.close();
// System.out.println(s);

// CzechStemmer stem = new CzechStemmer();
// String s = "Jste moc simpatická. To je moje sestra.";
// char[] buf = s.toCharArray();
// System.out.println(buf);
// int i = stem.stem(buf, buf.length);
// System.out.println(s + ":" + buf.toString());
// for (char c : buf)
// System.out.println(c); System.out.println(i);
//
// // CzechStemFilterFactory
// filter = new CzechStemFilterFactory();
// // Analyzer anal = new
// CzechAnalyzer();
// // Analyzer anal = new PortugueseAnalyzer();
// // byte[] data = new byte[4096];
// // int read = s tream.read(data);
// // for (i = 0; i < data.length; i++)
// // System.out.print((char) data[i] + " ");
// //System.out.println();
// // System.out.println("bytes read: " + read);
// buf = new char[4096];
// //Tokenizer tok = new Tokenizer();
//
// // int read = stream.read(buf);
// // for (i = 0; i < buf.length; i++)
// // System.out.print(buf[i] + " "); //
// System.out.println();
// // System.out.println("chars read: " + read);
// // buf = s.toCharArray();
// // System.out.println(anal.getDefaultStopSet());
// //System.out.println(anal.getStopwordSet());
// // // Portuguese
// // String text = "Você é um cara muito legal. Acho que vou sair daqui assim
// que terminar o meu programa, velho."
// ; // StringReader reader = new StringReader(text); // String[] sw = new
//
// String[] { "o", "a", "os", "as", "um", "uma", "uns", "umas" };
// //CharArraySet stopwords = StopFilter.makeStopSet(sw);
// // Analyzer analyzer = new PortugueseAnalyzer(stopwords); //
// analyzer.normalize(null, text); //
// TokenStream str = analyzer.tokenStream(null, reader);
// // str.reset(); //
// while (str.incrementToken()) {
// // CharTermAttribute att =
// docsList
// str.getAttribute(CharTermAttribute.class);
// // OffsetAttribute off =
// str.getAttribute(OffsetAttribute.class);
// // String token = text.substring(off.startOffset(), off.endOffset()); //
// String lemma =
// att.toString();
// // System.out.println("token: " + token + " lemma: " + lemma);
// //
// // str.close();
// // analyzer.close();
//

// Czech
// text = "Ten můj pocítač je rozbitý, a je to drahé pro mě ho vyzměnit."
// + "Ja, Erik, jsem velice blázen a ty jsou moje věci.";
