package kr.co.inogard.springboot.dc.service;

public class RestTemplateSaveFileHolder {
	
	private static ThreadLocal<String> local = new ThreadLocal<>();
	
	public static void reset() {
		local.remove();
	}

	public static void set(String val) {
		local.set(val);
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
