package kr.co.inogard.springboot.dc.utils;

public class StringUtil {
	
	/**
	 * 널null 이 아니면 원문, 널인 경우 다른 문자열 반환
	 * 
	 * @param str
	 *            원문
	 * @param nl
	 *            null인경우 반환될 문자열
	 * @return 반환값
	 */
	public static String nvl(Object str, String nl) {
		String rtnVal = nl;
		if (str == null)
			return nl;
		if (str.toString().trim().length() == 0)
			return nl;
		rtnVal = replace(str.toString(), "null", "");
		return rtnVal;
	}

	/**
	 * 널null 이 아니면 원문, 널인 경우 공백문자열("") 반환
	 * 
	 * @param str
	 *            원문
	 * @return 반환값
	 */
	public static String nvl(Object str) {
		return nvl(str, "");
	}
	
	/**
	 * 문자열 전체 치환, 원문에서 어는 문자를 다른 문자열로 바꿈
	 * 
	 * @param str
	 *            원문
	 * @param ch
	 *            문자
	 * @param replacement
	 *            바꿀문자열
	 * @return 바뀐 문자열
	 */
	public static String replace(String str, int ch, String replacement) {
		StringBuffer ret = new StringBuffer("");
		int p, oldp = 0;
		while ((p = str.indexOf(ch, oldp)) >= 0) {
			ret.append(str.substring(oldp, p));
			ret.append(replacement);
			oldp = p + 1;
		}
		ret.append(str.substring(oldp));
		return ret.toString();
	}
	
	/**
	 * 문자열 전체 치환, 원문에서 어느 문자열1을 다른 문자열2로 바꿈
	 * 
	 * @param str
	 *            원문
	 * @param old
	 *            문자열1
	 * @param replacement
	 *            문자열2
	 * @return 바뀐 문자열
	 */
	public static String replace(String str, String old, String replacement) {
		StringBuffer ret = new StringBuffer();
		if (str == null)
			return "";
		int p, oldp = 0;
		int oldlen = old.length();
		while ((p = str.indexOf(old, oldp)) >= 0) {
			ret.append(str.substring(oldp, p));
			ret.append(replacement);
			oldp = p + oldlen;
		}
		ret.append(str.substring(oldp));
		return ret.toString();
	}
}
