package com.dominator.utils.system;

import com.dominFramework.core.id.SystemIdUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 生成流水号
 * @author gsh
 *
 */
public class PrimaryGenerater {
	private static PrimaryGenerater primaryGenerater = null;
    static Random random = new Random();
    
	public static PrimaryGenerater getInstance() {
		if (primaryGenerater == null) {
			synchronized (PrimaryGenerater.class) {
				if (primaryGenerater == null) {
					primaryGenerater = new PrimaryGenerater();
				}
			}
		}
		return primaryGenerater;
	}

	/**
	 * 生成下一个编号
	 */
	public synchronized String next() {
    	Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        StringBuilder sb=new StringBuilder();
        sb.append(sf.format(date));
		for (int i=1;i<=8;i++){
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}

	public synchronized String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	public static void main(String[] args) {
		for(int i=0;i<100;i++){
		  String sn = PrimaryGenerater.getInstance().next();
		  System.out.println(String.format("%s--%s", i,sn));
//			Random random = new Random();
//			int r1 = random.nextInt(10);
//			int r2 = random.nextInt(10);
//			int r3 = random.nextInt(10);
//			System.out.println(String.format("%s,%s,%s", r1,r2,r3));
		}
	}
}