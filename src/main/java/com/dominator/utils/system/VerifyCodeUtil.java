package com.dominator.utils.system;

import java.util.Random;

/**
 * 验证码
 * @author gsh
 *
 */
public class VerifyCodeUtil {

	/**
	 * 取得6位验证码
	 * @return
	 */
	public static int getCode6() {
		int[] array = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		Random rand = new Random();
		for (int i = 10; i > 1; i--) {
			int index = rand.nextInt(i);
			int tmp = array[index];
			array[index] = array[i - 1];
			array[i - 1] = tmp;
		}
		int result = 0;
		for (int i = 0; i < 6; i++)
			result = result * 10 + array[i];
		if(result<100000){
			result=result+100000;
		}
		return result;
	}
}
