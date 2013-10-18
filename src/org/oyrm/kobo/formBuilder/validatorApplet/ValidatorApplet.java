/**
 * This validator applet is based off of the ODK Validator found at 
 * http://code.google.com/p/opendatakit/source/checkout?repo=validate
 */
package org.oyrm.kobo.formBuilder.validatorApplet;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JApplet;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import netscape.javascript.JSObject;

import org.javarosa.core.model.FormDef;
import org.javarosa.xform.parse.XFormParseException;
import org.javarosa.xform.util.XFormUtils;

public class ValidatorApplet extends JApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -612157786926732933L;

	
	/**
	 * Text area that shows the output of the validation
	 */
	JTextArea validatorOutput = null;
	
	
	/**
	 * Scroll pane to put everything in
	 */
	JScrollPane scrollPane = null;
	
	/**
	 * Object that represents the java script in the browser, so we can pass the XML around
	 */
	JSObject jso = null;
	
	/**
	 * Classic default constructor
	 */
	public ValidatorApplet()
	{
		super();
	}

	/**
	 * You know, it initializes things
	 */
	public void init()
	{
		scrollPane = new JScrollPane();		
		validatorOutput = new JTextArea();
		
		// redirect out/errors to the GUI
        System.setOut(new PrintStream(new JTextAreaOutputStream(validatorOutput)));
        System.setErr(new PrintStream(new JTextAreaOutputStream(validatorOutput)));
		
		scrollPane.getViewport().add(validatorOutput);
		add(scrollPane);
		
		String xmlStr = ""; //the xml in question
		
		//get a handle on the javaScript object
		
		try
		{
			jso = JSObject.getWindow(this);
			xmlStr = jso.getMember("formXmlToSave").toString();
		}
		catch(Exception e)
		{
			System.err.println("Couldn't get a handle on the javascript object \r\n" + e.getMessage());
		}
		

	
		
		validate(xmlStr);
	}
	
    /**
     * An OutputStream that writes the output to a text area.
     * 
     * @author alerer@google.com (Adam Lerer)
     */
    class JTextAreaOutputStream extends OutputStream {
        private JTextArea textArea;


        public JTextAreaOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }


        @Override
        public void write(int b) {
            textArea.append(new String(new byte[] {
                (byte) (b % 256)
            }, 0, 1));
        }
    }
	
	
	/***
	 * Takes in some XML as a string and then writes out
	 * anny errors or warns to our text area
	 * @param xmlStr
	 */
	public void validate(String xmlStr) {
		
		
		if (xmlStr == null || xmlStr.length() == 0)
		{
			System.err.println("\n>> XML is zero length");
			return;
		}
		
		
		ByteArrayInputStream is = new ByteArrayInputStream(xmlStr.getBytes());

        // validate well formed xml
        System.out.println("Checking form...");
        //System.out.println(">>\r\n" + xmlStr);
        //System.out.println("\r\n\r\n");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            builder.parse(is);
        } catch (Exception e) {
            validatorOutput.setForeground(Color.RED);
            System.err.println("\n>> " + e.getMessage());
            System.err.println("\n>> XML is invalid. See above the errors.");
            return;
        }

        
        is = new ByteArrayInputStream(xmlStr.getBytes());
        
        // validate if the xform can be parsed.
        try {
            FormDef fd = XFormUtils.getFormFromInputStream(is);
            if (fd == null) {
                validatorOutput.setForeground(Color.RED);
                System.err.println(">> Something broke the parser. Try again.");
                return;
            }
            validatorOutput.setForeground(Color.BLUE);
            System.err.println("\n\n>> Xform is valid! See above for any warnings.");

        } catch (XFormParseException e) {
            validatorOutput.setForeground(Color.RED);

            if (e.getMessage() == null) {
                e.printStackTrace();
            } else {
                System.err.println(e.getMessage());
            }
            System.err.println(">> XForm is invalid. See above for the errors.");
            

        } catch (Exception e) {
            validatorOutput.setForeground(Color.RED);
            if (e.getMessage() != null) {
                System.err.println(e.getMessage());
            }
            e.printStackTrace();
            System.err.println("\n>> Something broke the parser. See above for a hint.");

        }
    }
}
