package com.purezhihudaily.utils.uuid;

/**
 * 一个单例的UUID服务
 * @author fabianofranz@users.sourceforge.net
 * 
 */
public class UUIDService {

	private static final UUIDService instance = new UUIDService();

	static public UUIDService getInstance() {
		return instance;
	}

	public String simpleHex(String separator) {
		return (String) new UUIDHexGenerator(separator).generate();
	}

	public String simpleHex() {
		return (String) new UUIDHexGenerator().generate();
	}
}
