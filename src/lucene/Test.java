package lucene;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

public class Test {

	public static void main(String[] args) throws IOException {

		// FileInputStream fis = new
		// FileInputStream("/home/liefe/Lucene/port-text.txt.gz");
		// GZIPInputStream gzip = new GZIPInputStream(fis);
		// String file = "/home/liefe/Lucene/port-text.txt.gz";
		String file = "/home/liefe/data/sample/LN-20020102001.vert.gz";
		Path path = Paths.get(file);
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
		try (DataInputStream stream = new DataInputStream(new GZIPInputStream(new FileInputStream(file)))) {
			String line;
			while ((line = stream.readUTF()) != "\f") {
				System.out.println(line);
			}

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
		}
		// System.out.printf("test\ntest");

		// Scanner sc = new Scanner(new File(file));
		// String s = sc.useDelimiter("\\Z").next();
		// sc.close();
		// System.out.println(s);

	}
}

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
// // Czech
// String text = "Ten můj pocítač je rozbitý, a je to drahé pro mě ho vyzměnit.
// Ja, Erik, jsem velice blázen a ty jsou moje věci."
// ;
// StringReader reader = new StringReader(text);
// String[] sw = new String[] {
// "ten", "ta", "to", "ho", "jsem", "je", "jsou", "a", "i", "ale"
//
// CharArraySet stopwords = StopFilter.makeStopSet(sw);
//// Analyzer analyzer = new CzechAnalyzer(stopwords);
// Analyzer analyzer = new CzechAnalyzer();
// //
// analyzer.getStopwordSet();
// analyzer.normalize(null, text);
// TokenStream str = analyzer.tokenStream(null, reader);
// str.reset();
// while (str.incrementToken())
// { CharTermAttribute att = str.getAttribute(CharTermAttribute.class);
// OffsetAttribute off = str.getAttribute(OffsetAttribute.class);
// String token = text.substring(off.startOffset(), off.endOffset()); String
// lemma =
// att.toString();
// System.out.println("token: " + token + " lemma: " + lemma); }
// // new StopFilter(, stopwords);
// // anal.normalize(s, s);
// str.close();
// analyzer.close();
//
// } catch (IOException e) {
// System.err.println("file not found"); }
//
//
// }
//
// }
