package cn.liujinhang.paper.BPMN;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class LetsDoThis {

	public static void main(String[] args) {

		System.out.println("xsl loading start");
		Source xslSource = new StreamSource(new File(
				System.getProperty("user.dir") + "/bpmn/xsd2owl.xsl"));
		System.out.println("xsl loading end");

		System.out.println("xsd loading start");
		InputStreamReader xsdFileReader = null;
		try {
			xsdFileReader = new InputStreamReader(
					new URL(
							"http://www.omg.org/spec/BPMN/20100501/Semantic.xsd")
							.openStream());
		} catch (Exception e) {
			try {
				xsdFileReader = new InputStreamReader(new FileInputStream(
						System.getProperty("user.dir") + "\\bpmn\\bpmn2_Semantic.xsd"));
			} catch (Exception ex) {
				e.printStackTrace();
			}
		}
		Source xsdSource = new StreamSource(xsdFileReader);
		System.out.println("xsd loading end");

		try {
			TransformerFactory factory = TransformerFactory.newInstance(
					"net.sf.saxon.TransformerFactoryImpl", null);
			Transformer transformer = factory.newTransformer(xslSource);
			StringWriter writer = new StringWriter();

			System.out.println("translation start");
			transformer.transform(xsdSource, new StreamResult(writer));
			System.out.println("translation end");

			System.out.println("file output start");
			String result = new String(writer.getBuffer());
			result = result.replaceAll("&amp;", "&");
			File file = new File(System.getProperty("user.dir")
					+ "/bpmn/bpmn2_OWL.owl");
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
					file));
			bufferedWriter.write(result);
			bufferedWriter.flush();
			bufferedWriter.close();
			System.out.println("file output end");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}