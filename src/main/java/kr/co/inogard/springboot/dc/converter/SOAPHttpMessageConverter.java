package kr.co.inogard.springboot.dc.converter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;

public class SOAPHttpMessageConverter extends AbstractHttpMessageConverter<SOAPMessage> {
	
	public static final String CONTENT_TYPE = "multipart/related";
	
	public SOAPHttpMessageConverter() {
		super(new MediaType("multipart", "related"), MediaType.TEXT_XML);
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return byte[].class.equals(clazz);
	}

	@Override
	protected SOAPMessage readInternal(Class<? extends SOAPMessage> clazz,
			HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {

		SOAPMessage msg = null;
	
		if(CONTENT_TYPE.equals(inputMessage.getHeaders().getContentType())){
			long contentLength = inputMessage.getHeaders().getContentLength();
			ByteArrayOutputStream bos = new ByteArrayOutputStream(contentLength >= 0 ? (int) contentLength : StreamUtils.BUFFER_SIZE);
			StreamUtils.copy(inputMessage.getBody(), bos);
			byte readBuffer[] = bos.toByteArray();
			try {
			    if(null != bos){
			    	bos.close();
			    }
			} catch(Exception ex) {
			} finally {
				bos = null;
			}
			
			ByteArrayInputStream bin = null;
			MimeHeaders header = new MimeHeaders();
			header.addHeader("Content-Type", CONTENT_TYPE);
			try {
//			    MessageFactory factory = MessageFactory.newInstance("SOAP 1.2 Protocol");
				MessageFactory factory = MessageFactory.newInstance();
			    bin = new ByteArrayInputStream(readBuffer);
			    msg = factory.createMessage(header, bin);
			} catch(Exception ex) {
				ex.printStackTrace();
				
//				try {
//			        MessageFactory factory = MessageFactory.newInstance("SOAP 1.1 Protocol");
//			        bin = new ByteArrayInputStream(readBuffer);
//			        msg = factory.createMessage(header, bin);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
			} finally {
			    try {
			        if(null != bin){
			        	bin.close();
			        }
			    } catch(Exception ex) {
			    } finally {
			    	bin = null;
			    }
			    readBuffer = null;
			}
		}
		
		return msg;
	}

	@Override
	protected void writeInternal(SOAPMessage msg, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			msg.writeTo(out);
			StreamUtils.copy(out.toByteArray(), outputMessage.getBody());
		    if(null != out){
		    	out.close();
		    }
		} catch (SOAPException e) {
			e.printStackTrace();
		} finally {
			out = null;
		}
	}

}
