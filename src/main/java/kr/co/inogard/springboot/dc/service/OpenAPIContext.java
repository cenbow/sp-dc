package kr.co.inogard.springboot.dc.service;

import kr.co.inogard.springboot.dc.domain.OpenAPIRequest;

public class OpenAPIContext {

	private static ThreadLocal<OpenAPIRequest> local = new ThreadLocal<>();
	
	public static void reset() {
		local.remove();
	}

	public static void set(OpenAPIRequest openAPIRequest) {
		local.set(openAPIRequest);
	}

	public static OpenAPIRequest get() {
		
		OpenAPIRequest s = local.get();
		
		if(null == s){
			s = new OpenAPIRequest();
			set(s);
		}
		
		return s;
	}
}
