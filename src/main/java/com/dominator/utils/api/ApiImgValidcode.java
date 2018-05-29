package com.dominator.utils.api;

import com.dominator.utils.encode.Des3Utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiImgValidcode extends HttpServlet  {
	 private static final long serialVersionUID = 3038623696184546092L;
	    
	    public static final int WIDTH = 120;//生成的图片的宽度
	    public static final int HEIGHT = 30;//生成的图片的高度

	    @Override
		public void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        this.doPost(request, response);
	    }

	    @Override
		public void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	       //获取需要加载验证码的密文
	    	String code=request.getParameter("code");
	     	code= Des3Utils.decode(code);
	        //1.在内存中创建一张图片
	        BufferedImage bi = new BufferedImage(WIDTH, HEIGHT,BufferedImage.TYPE_INT_RGB);
	        //2.得到图片
	        Graphics g = bi.getGraphics();
	        //3.设置图片的背影色
	        setBackGround(g);
	        //4.设置图片的边框
	        setBorder(g);
	        //5.在图片上画干扰线
	        drawRandomLine(g);
	        //6.写在图片上随机数
	        //String random = drawRandomNum((Graphics2D) g,"ch");//生成中文验证码图片
	        //String random = drawRandomNum((Graphics2D) g,"nl");//生成数字和字母组合的验证码图片
	        //String random = drawRandomNum((Graphics2D) g,"n");//生成纯数字的验证码图片
	        //String random = drawRandomNum((Graphics2D) g,"l");//生成纯字母的验证码图片
	        String random = drawRandomNum((Graphics2D) g,code);//根据客户端传递的createTypeFlag标识生成验证码图片
	        //7.将随机数存在session中
	        //request.getSession().setAttribute("checkcode", random);
	        //8.设置响应头通知浏览器以图片的形式打开
	        response.setContentType("image/jpeg");//等同于response.setHeader("Content-Type", "image/jpeg");
	        //9.设置响应头控制浏览器不要缓存
	        response.setDateHeader("expries", -1);
	        response.setHeader("Cache-Control", "no-cache");
	        response.setHeader("Pragma", "no-cache");
	        //10.将图片写给浏览器
	        ImageIO.write(bi, "jpg", response.getOutputStream());
	    }

	    /**
	     * 设置图片的背景色
	     * @param g
	     */
	    private void setBackGround(Graphics g) {
	        // 设置颜色
	        g.setColor(Color.WHITE);
	        // 填充区域
	        g.fillRect(0, 0, WIDTH, HEIGHT);
	    }

	    /**
	     * 设置图片的边框
	     * @param g
	     */
	    private void setBorder(Graphics g) {
	        // 设置边框颜色
	        //g.setColor(Color.RED);
	        // 边框区域
	        g.drawRect(1, 1, WIDTH - 2, HEIGHT - 2);
	    }

	    /**
	     * 在图片上画随机线条
	     * @param g
	     */
	    private void drawRandomLine(Graphics g) {
	        // 设置颜色
	        g.setColor(Color.GREEN);
	        // 设置线条个数并画线
	        for (int i = 0; i < 5; i++) {
	            int x1 = new Random().nextInt(WIDTH);
	            int y1 = new Random().nextInt(HEIGHT);
	            int x2 = new Random().nextInt(WIDTH);
	            int y2 = new Random().nextInt(HEIGHT);
	            g.drawLine(x1, y1, x2, y2);
	        }
	    }

	    /**
	     * 画随机字符
	     * @param g
	     * @param createTypeFlag
	     * @return
	     * String... createTypeFlag是可变参数，
	     * Java1.5增加了新特性：可变参数：适用于参数个数不确定，类型确定的情况，java把可变参数当做数组处理。注意：可变参数必须位于最后一项
	     */
	    private String drawRandomNum(Graphics2D g,String code) {
	        // 设置颜色
	        g.setColor(Color.RED);
	        // 设置字体
	        g.setFont(new Font("微软雅黑", Font.ITALIC, 28));
	        return createRandomChar(g, code);
	    }

	    /**
	     * 创建随机字符
	     * @param g
	     * @param baseChar
	     * @return 随机字符
	     */
	    private String createRandomChar(Graphics2D g,String code) {
	        StringBuffer sb = new StringBuffer();
	        int x = 5;
	        String ch ="";
	        // 控制字数
	        for (int i = 0; i < code.length(); i++) {
	            // 设置字体旋转角度
	            int degree = new Random().nextInt() % 30;
	            ch=code.substring(i,i+1);
	            sb.append(ch);
	            // 正向角度
	            g.rotate(degree * Math.PI / 180, x, 20);
	            g.drawString(ch, x, 20);
	            // 反向角度
	            g.rotate(-degree * Math.PI / 180, x, 20);
	            x += 30;
	        }
	        return sb.toString();
	    }
}
