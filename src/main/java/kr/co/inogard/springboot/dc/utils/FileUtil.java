package kr.co.inogard.springboot.dc.utils;

import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtil {
	
	public static void generalizePath(String[] paths) {
		for (int i = 0; i < paths.length; i++)
			paths[i] = generalizePath(paths[i]);
	}
	
	/**
	 * 디렉터리 경로 구분자 정리(ex c:\wwwroot/xx.jsp -> c:\wwwroot\xx.jsp)
	 * 
	 * @param path
	 *            디렉터리
	 * @return 정리된 디렉터리
	 */
	public static String generalizePath(String path) {
		StringBuffer sb = new StringBuffer();
		int l = path.length();
		for (int i = 0; i < l; i++) {
			char ch = path.charAt(i);
			if (ch == '\\' || ch == '/') {
				sb.append('\\');
				while (i < l - 1
						&& (path.charAt(i + 1) == '\\' || path.charAt(i + 1) == '/'))
					i++;
			} else
				sb.append(ch);
		}
		return sb.toString();
	}
	
	public static String getHashSHA1FromFilepath(String fileName) {
		return getSHAFromFilepath(fileName, "SHA-1");
	}
	
	public static String getHashSHA256FromFilepath(String fileName) {
		return getSHAFromFilepath(fileName, "SHA-256");
	}
	
	public static String getSHAFromFilepath(String fileName, String algorithm) {
		
		String sha = "";
		FileInputStream fs = null;
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			fs = new FileInputStream(generalizePath(fileName));
			byte[] buff = new byte[65536];
			int r;
			while ((r = fs.read(buff)) >= 0) {
				md.update(buff, 0, r);
			}
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer(); 
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			sha = sb.toString();
		} catch (Exception e) {
			e.printStackTrace(); 
			sha = null; 
		}finally{
			try {
				if(null != fs){
					fs.close();
				}
			} catch (Exception e) {
			}
		}

		return sha;
	}
	
	public static String getSHA1FromString(String str) {
		return getSHAFromString(str, "SHA-1");
	}
	
	public static String getSHA256FromString(String str) {
		return getSHAFromString(str, "SHA-256");
	}
	
	public static String getSHAFromString(String str, String algorithm) {
		String sha = ""; 
		try{
			MessageDigest sh = MessageDigest.getInstance(algorithm); 
			sh.update(str.getBytes()); 
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer(); 
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			sha = sb.toString();
			
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace(); 
			sha = null; 
		}
		return sha;
	}
	
	public static String getSHA1FromByte(byte[] b) {
		return getSHAFromByte(b, "SHA-1");
	}
	
	public static String getSHA256FromByte(byte[] b) {
		return getSHAFromByte(b, "SHA-256");
	}
	
	public static String getSHAFromByte(byte[] b, String algorithm) {
		String sha = ""; 
		try{
			MessageDigest sh = MessageDigest.getInstance(algorithm); 
			sh.update(b); 
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer(); 
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			sha = sb.toString();
			
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace(); 
			sha = null; 
		}
		return sha;
	}
}
