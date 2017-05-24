/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.reniec.pki.cardcreator;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aalain
 */
public class Creator extends HttpServlet {

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		/*
		response.setContentType("text/html;charset=UTF-8");
		try (PrintWriter out = response.getWriter()) {
			
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet Creator</title>");			
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Servlet Creator at " + request.getContextPath() + "</h1>");
			
	
		
			String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

			for (int i = 0; i < fonts.length; i++) {
				out.println(fonts[i]);
			}
			
			
			out.println("</body>");
			out.println("</html>");
		}
		
	*/
		try{
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			//ClassLoader classLoader = getClass().getClassLoader();
			//File file = new File(classLoader.getResource("file/test.xml").getFile());
						
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, classLoader.getResourceAsStream("arialn.ttf")));
			//ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(getClass().getClassLoader().getResource("../resources/arialn.ttf").getFile())));
		}catch(Exception ex){
			
			//out.println(ex.getMessage());
			
			ex.printStackTrace();
		}		
		
		
		String nombres = request.getParameter("nombre");
		String organizacion = request.getParameter("organizacion");
		String printcode = request.getParameter("codigo");
		
		byte[] bytesNombres = nombres.getBytes(StandardCharsets.ISO_8859_1);
		nombres = new String(bytesNombres, StandardCharsets.UTF_8);
		
		byte[] bytesOrganizacion = organizacion.getBytes(StandardCharsets.ISO_8859_1);
		organizacion = new String(bytesOrganizacion, StandardCharsets.UTF_8);
		
		System.out.println("*********** nombres: " + nombres);
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream is = classLoader.getResourceAsStream("front.bmp");
		
		final BufferedImage image = ImageIO.read(is);

		Graphics g = image.getGraphics();
		//g.setFont(g.getFont().deriveFont(30f));
		
		g.setFont(new Font("Arial Narrow", Font.BOLD, 28));
		g.drawString(nombres, 44, 560);
		g.drawString(organizacion, 44, 600);
		
		
		Graphics2D g2 = (Graphics2D) g;
		Font font = new Font("Lucida Sans", Font.PLAIN, 20);
		AffineTransform affineTransform = new AffineTransform();
		affineTransform.rotate(Math.toRadians(90), 0, 0);
		Font rotatedFont = font.deriveFont(affineTransform);
		g2.setFont(rotatedFont);
		g2.setColor(Color.BLACK);
		g2.drawString(printcode,985,400);
		g2.dispose();
		
		
		//g.dispose();

		//ImageIO.write(image, "bmp", new File("test.bmp"));		
		
		ByteArrayOutputStream tmp = new ByteArrayOutputStream();
		ImageIO.write(image, "bmp", tmp);
		tmp.close();
		Integer contentLength = tmp.size();
		
		response.setContentType("image/bmp");
		response.setHeader("Content-Length", contentLength.toString());
		response.setHeader("Content-Disposition", "attachment; filename=\""+nombres.replaceAll("[^\\x00-\\x7F]", "")+".bmp\"");

		//String pathToWeb = getServletContext().getRealPath(File.separator);
		//File f = new File(pathToWeb + "front.bmp");
		//BufferedImage bi = ImageIO.read(f);
		try{
			OutputStream out = response.getOutputStream();
			//ImageIO.write(image, "bmp", out);
			out.write(tmp.toByteArray());
			out.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>

}
