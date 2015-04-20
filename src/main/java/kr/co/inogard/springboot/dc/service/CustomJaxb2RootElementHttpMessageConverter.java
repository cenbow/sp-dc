package kr.co.inogard.springboot.dc.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class CustomJaxb2RootElementHttpMessageConverter extends Jaxb2RootElementHttpMessageConverter {
	
	private static final Logger log = LoggerFactory.getLogger(CustomJaxb2RootElementHttpMessageConverter.class);
	
	@Override
	protected Source processSource(Source source) {
		
		if (source instanceof StreamSource) {
			StreamSource streamSource = (StreamSource) source;
			//InputSource inputSource = new InputSource(streamSource.getInputStream());
			
			ByteArrayOutputStream _copy = new ByteArrayOutputStream();
			InputStream inputStream = null;
			try {
				IOUtils.copy(streamSource.getInputStream(), _copy);
			} catch (IOException e) {
				logger.warn("InputStream copy error", e);
				return source;
			}
			
			try {
				inputStream = (InputStream)new ByteArrayInputStream(_copy.toByteArray());
				File file = new File("c:/java/"+OpenAPIContext.get()+".xml");
				org.apache.commons.io.FileUtils.writeByteArrayToFile(file, _copy.toByteArray());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(null != _copy){
					try {
						_copy.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				_copy = null;
			}
			
			InputSource inputSource = new InputSource(inputStream);
			
			try {
				XMLReader xmlReader = XMLReaderFactory.createXMLReader();
				String featureName = "http://xml.org/sax/features/external-general-entities";
				xmlReader.setFeature(featureName, isProcessExternalEntities());
				if (!isProcessExternalEntities()) {
					xmlReader.setEntityResolver(NO_OP_ENTITY_RESOLVER);
				}
				return new SAXSource(xmlReader, inputSource);
			}
			catch (SAXException ex) {
				logger.warn("Processing of external entities could not be disabled", ex);
				return source;
			}
		}
		else {
			return source;
		}
	}
	
	private static final EntityResolver NO_OP_ENTITY_RESOLVER = new EntityResolver() {
		@Override
		public InputSource resolveEntity(String publicId, String systemId) {
			return new InputSource(new StringReader(""));
		}
	};
}
