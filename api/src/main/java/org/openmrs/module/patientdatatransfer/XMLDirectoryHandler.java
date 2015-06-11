package org.openmrs.module.patientdatatransfer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.util.ArrayList;
import java.util.List;

public class XMLDirectoryHandler implements ContentHandler
{
	protected Log log = LogFactory.getLog(getClass());
	private List<PDTClinic> dirClinics = new ArrayList<PDTClinic>();
	private X509CRL x509crl;
	private String x509crlx64;
	
	public List<PDTClinic> getClinics() {
		return dirClinics;
	}
	
	public X509CRL getCRL() {
		return x509crl;
	}
	
	public String getCRLBase64() {
		return x509crlx64;
	}
	
    public void startElement (String uri, String localName, String qName, Attributes atts) throws SAXException {
    	if (localName.equalsIgnoreCase("clinic")) {
    		try {
    			PDTClinic newclinic = new PDTClinic(atts.getValue("fqdn"), atts.getValue("ip_addr"), atts.getValue("name"), Integer.parseInt(atts.getValue("port")), atts.getValue("instance"), Float.parseFloat(atts.getValue("latitude")), Float.parseFloat(atts.getValue("longitude")));
    			dirClinics.add(newclinic);
    		} catch (Exception e) {
    			log.error("XML parsing failure for one or more clinics");
    		}
    	}
    	
    	if (localName.equalsIgnoreCase("crl")) {
    		CertificateFactory certfac;
    		try {
    			certfac = CertificateFactory.getInstance("X.509");
    			x509crlx64 = atts.getValue("value");
    			InputStream crldata = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(atts.getValue("value")));
				this.x509crl = (X509CRL)certfac.generateCRL(crldata);
				log.info("CRL Issuer = " + this.x509crl.getIssuerDN().getName() + ", Algorithm = " + this.x509crl.getSigAlgName());
			} catch (Exception e) {
				log.error("Error parsing and/or setting up CRL!");
				this.x509crl = null;
			}
    	}
	}

    public void setDocumentLocator (Locator locator) { }

    public void startDocument () { }

    public void endDocument() { }

    public void startPrefixMapping (String prefix, String uri) { }

    public void endPrefixMapping (String prefix) { }
    
    public void endElement (String uri, String localName, String qName) { }

    public void characters (char ch[], int start, int length) { }

    public void ignorableWhitespace (char ch[], int start, int length) { }

    public void processingInstruction (String target, String data) { }

    public void skippedEntity (String name) { }
}