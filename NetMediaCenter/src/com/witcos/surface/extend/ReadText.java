package com.witcos.surface.extend;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.witcos.tool.DownloadFile;

public class ReadText {

	/**
	 * 获取垂直滚动字幕
	 * @param fileName
	 * @return
	 */
	public static String getVStringForTxt(String fileName) {
		StringBuffer sb = new StringBuffer();
		InputStream in = null;
		BufferedReader br = null;
		try {
			in = new BufferedInputStream(new FileInputStream(new File(DownloadFile.FILEPATH + fileName)));
			br = new BufferedReader(new InputStreamReader(in, "gb2312"));
			String temp = "";
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
				sb.append("\n");
			}
		} catch (Exception e) {
		} finally {
			try {
				br.close();
				in.close();
				br = null ;
				in = null ;
			} catch (IOException e) {
			}
		}
		return sb.toString();
	}
	
	/**
	 * 获取水平滚动字幕
	 * @param fileName
	 * @return
	 */
	public static String getHStringForTxt(String fileName) {
		StringBuffer sb = new StringBuffer();
		InputStream in = null;
		BufferedReader br = null;
		try {
			in = new BufferedInputStream(new FileInputStream(new File(DownloadFile.FILEPATH + fileName)));
			br = new BufferedReader(new InputStreamReader(in, "gb2312"));
			String temp = "";
			while ((temp = br.readLine()) != null) {
				sb.append(temp.trim());
			}
		} catch (Exception e) {
		} finally {
			try {
				br.close();
				in.close();
				br = null ;
				in = null ;
			} catch (IOException e) {
			}
		}
		return sb.toString();
	}
}
