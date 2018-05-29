package com.dominator.utils.system;

import java.io.StringWriter;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.velocity.VelocityHelper;






/**
 * formula工具
 * @author gsh
 *
 */
public class FormulaTools {
	
	
	/**
	 * 计算
	 * @param formulaStr  公式
	 * @param data		    参数
	 * @param clazz      返回类型 
	 * @return
	 * demo:
	 * BigDecimal big = execute("$a * $b + $c", dto, BigDecimal.class);
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Number> T execute(String formulaStr, Dto data, Class<T> clazz) {
		T res;
		try {
			String formula = String.format("#set($val = %s)  ${val}", formulaStr);
			StringWriter writer = VelocityHelper.mergeStringTemplate(formula, data);
			String val = writer.toString().trim();

			Method m = null;
			if (clazz.getName().equals(BigDecimal.class.getName())) {
				return (T) new BigDecimal(val);
			} else {
				m = clazz.getMethod("valueOf", String.class);
			}
			T nb = (T) m.invoke(clazz, val);
			res = (T) nb;
		} catch (Exception ex) {
			return null;
		}
		return res;
	}

	
	
	public static BigDecimal getFormulaResultByMSC(Map<String,Object> pDto){
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//示例
	public static void main(String[] args) {
		Map<String,Object> dto = new HashMap<String,Object>();
		dto.put("a", 0);
		dto.put("b", 0);
		dto.put("c", 1000);
		dto.put("d", -10);


		
	}
}
