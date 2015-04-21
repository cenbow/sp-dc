package kr.co.inogard.springboot.dc.service;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AnnStdDocAdapter extends XmlAdapter<String, String> {

	@Override
	public String marshal(String val) throws Exception {
		if(val.indexOf("&amp;") > -1){
			val.replaceAll("&amp;", "&");
		}
		return val;
	}

	@Override
	public String unmarshal(String val) throws Exception {
		if(val.indexOf("&") > -1){
			val.replaceAll("&", "&amp;");
		}
		return val;
	}

}
