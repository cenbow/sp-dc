package kr.co.inogard.springboot.dc.utils;

public class StringUtil {
	
	/**
	 * ��null �� �ƴϸ� ����, ���� ��� �ٸ� ���ڿ� ��ȯ
	 * 
	 * @param str
	 *            ����
	 * @param nl
	 *            null�ΰ�� ��ȯ�� ���ڿ�
	 * @return ��ȯ��
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
	 * ��null �� �ƴϸ� ����, ���� ��� ���鹮�ڿ�("") ��ȯ
	 * 
	 * @param str
	 *            ����
	 * @return ��ȯ��
	 */
	public static String nvl(Object str) {
		return nvl(str, "");
	}
	
	/**
	 * ���ڿ� ��ü ġȯ, �������� ��� ���ڸ� �ٸ� ���ڿ��� �ٲ�
	 * 
	 * @param str
	 *            ����
	 * @param ch
	 *            ����
	 * @param replacement
	 *            �ٲܹ��ڿ�
	 * @return �ٲ� ���ڿ�
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
	 * ���ڿ� ��ü ġȯ, �������� ��� ���ڿ�1�� �ٸ� ���ڿ�2�� �ٲ�
	 * 
	 * @param str
	 *            ����
	 * @param old
	 *            ���ڿ�1
	 * @param replacement
	 *            ���ڿ�2
	 * @return �ٲ� ���ڿ�
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
