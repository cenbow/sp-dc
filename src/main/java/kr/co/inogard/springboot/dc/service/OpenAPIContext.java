package kr.co.inogard.springboot.dc.service;

public class OpenAPIContext {

	private static ThreadLocal<String> local = new ThreadLocal<String>();
	
	public static void reset() {
		local.remove();
	}

	public static void set(String value) {
		local.set(value);
	}

	public static String get() {
		
		String s = local.get();
		
		if(null == s){
			s = new String("");
			set(s);
		}
		
		return s;
	}
}
