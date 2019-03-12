/**
 * 
 */
package com.htjf.common.lutil.codeutil;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author SHua.Qiu
 * 
 */
public class ImageCode extends HttpServlet {
	private static final int IMAGE_WIDTH = 80;
	private static final int IMAGE_HEIGHT = 24;


	/**
	 * 
	 */
	private static final long serialVersionUID = -2107932397232174870L;

	private static Font font = null;

	private void sendImage(OutputStream os, String code) {
		try {
			// prepare some output
			BufferedImage buffer = new BufferedImage(65, 21,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = buffer.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			// actually do the drawing
			Color c = Color.LIGHT_GRAY;
			c = new Color(c.getRed(), c.getGreen(), c.getBlue(), 255);
			g2.setColor(c);
			g2.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
			
			drawBackLine(g2);

			g2.setColor(Color.BLACK);
			g2.setFont(font);
			g2.drawRect(0, 0, 64, 20);
			g2.drawString(code, 6, 20);

			// output the image as png
			ImageIO.write(buffer, "png", os);
		} catch (Exception e) {
			System.err.println("Exception in Yazd Servlet: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void drawBackLine(Graphics2D g2) {
		g2.setColor(Color.WHITE);
		Random rand= new Random();
		int maxLineNum = 20;
		for(int i=0; i < rand.nextInt(maxLineNum);i++){
			int x1 = rand.nextInt(IMAGE_WIDTH);
			int x2 = rand.nextInt(IMAGE_WIDTH);
			int y1=rand.nextInt(IMAGE_HEIGHT);
			int y2=rand.nextInt(IMAGE_HEIGHT);
			
			g2.drawLine(x1, y1, x2, y2);
		}
		
	}

	@Override
	public void init() throws ServletException {
		System.setProperty("java.awt.headless", "true");
		try {
			InputStream resource = getClass().getResourceAsStream("Advert-Regular.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, resource); 
			font = font.deriveFont(24.0f);
			System.out.println("Yazd Initialized Font");

		} catch (Exception e) {
			System.err
					.println("Exception in Yazd Servlet 0: " + e.getMessage());
			e.printStackTrace();
		}
		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String code = RandomString.random(4);
		System.out.println("code: " + code);

		request.getSession().setAttribute("YazdCode", code);// �����֤��;

		// set the content type and get the output stream
		response.setContentType("image/png");
		ServletOutputStream os = response.getOutputStream();
		sendImage(os, code);
		os.close();
	}
}
