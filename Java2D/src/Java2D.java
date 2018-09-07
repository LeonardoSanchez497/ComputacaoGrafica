import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Java2D extends JApplet {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		JFrame frame = new JFrame();
		frame.setTitle("Trabalho Final 2D");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JApplet applet = new Java2D();
		applet.init();
		frame.getContentPane().add(applet);
		frame.pack();
		frame.setVisible(true);
	}

	public void init() {
		JPanel panel = new Java2DPanel();
		getContentPane().add(panel);
	}

	class Java2DPanel extends JPanel {
		private int frameNumber;
		private GeneralPath terreno;
		private GeneralPath eolica;


		public Java2DPanel() {
			setPreferredSize(new Dimension(700, 500));
			setBackground(Color.LIGHT_GRAY);
			setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
			
			new Timer(30, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					frameNumber++;
					repaint();
				}
			}).start();
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();

			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			g2.scale(100, -100);
			g2.translate(0, -4);

			g2.setColor(new Color(150, 150, 255));
			g2.fillRect(0, 0, 7, 4);

			
			// desenhar montanhas
			
			terreno = new GeneralPath();
			terreno.moveTo(0, -1);
			terreno.lineTo(0, 1);
			terreno.curveTo(1, 2, 1, 2, 1.5F, 2);
			terreno.curveTo(2.5F, 2, 2.5F, 1.5F, 2, 1.5F);
			terreno.curveTo(2.5F, 1.5F, 2.5F, 2.5F, 3, 2.5F);
			terreno.curveTo(3.5F, 2.5F, 3.5F, 1.8F, 4, 1.8F);
			terreno.curveTo(5, 1.8F, 4, 2.2F, 5, 2.2F);
			terreno.curveTo(6, 2.2F, 6, 2, 7, 2);
			terreno.lineTo(7, -1);
			terreno.closePath();

			g2.setColor(new Color(0, 150, 30));
			g2.fill(terreno);

			// Desenhar Estrada	

			g2.setColor(new Color(100, 100, 150));
			g2.fill(new Rectangle2D.Double(0, -0.4, 7, 0.8));

			// Desenhar Linhas trassejadas

			g2.setStroke(new BasicStroke(0.1F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0,
					new float[] { 0.25F, 0.2F }, 1));
			g2.setColor(Color.WHITE);
			g2.drawLine(0, 0, 7, 0);
			
			AffineTransform saveTr = g2.getTransform();

			// Desenhar Sol

			g2.translate(5.7, 2.7);
			desenharSol(g2);
			g2.setTransform(saveTr);

			//Desenhar Eolicas

			g2.translate(0.75, 1);
			g2.scale(0.6, 0.6);
			desenharEolica(g2);
			g2.setTransform(saveTr);

			g2.translate(2.2, 1.6);
			g2.scale(0.4, 0.4);
			desenharEolica(g2);
			g2.setTransform(saveTr);

			g2.translate(3.7, 0.8);
			g2.scale(1.0, 1.0);
			desenharEolica(g2);
			g2.setTransform(saveTr);

			// Desenhar carro 

			g2.translate(8 - 13 * (frameNumber % 100) / 100.0, 0);
			g2.scale(0.3, 0.3);
			desenharCarro(g2);

		}

		private void desenharSol(Graphics2D g2) {
			GradientPaint redtoorange = new GradientPaint(-1, 0, Color.red, 1, 0, Color.orange);
			g2.setPaint(redtoorange);
			g2.rotate(frameNumber / 23.0);
			
			// Desenhar os raios de sol e roda los
			for (int i = 0; i < 13; i++) {
				g2.rotate(2 * Math.PI / 13);
				g2.setStroke(new BasicStroke(0.1F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0,
						new float[] { 0.25F, 0.2F }, 1));
				g2.draw(new Line2D.Double(0, 0, 0.50, 1));
			}
			g2.fill(new Ellipse2D.Double(-0.5, -0.5, 1, 1));
		}

		private void desenharEolica(Graphics2D g2) {


			eolica = new GeneralPath();
			eolica.moveTo(0, 0);
			eolica.lineTo(0.5F, 0.1F);
			eolica.lineTo(1.5F, 0);
			eolica.lineTo(0.5F, -0.1F);
			eolica.closePath();

			g2.setColor(new Color(200, 200, 225));
			g2.fill(new Rectangle2D.Double(-0.05, 0, 0.1, 3));

			// colocar as pas das eolicas no topo das eolicas
			g2.translate(0, 3);

			// Aplicar rotação nas eolicas

			g2.rotate(frameNumber / 23.0);
			g2.setColor(new Color(100, 100, 200));

			// Desenhar as pas das eolicas com o intervalor entre elas de 60º

			for (int i = 0; i < 6; i++) {
				g2.rotate(1 * Math.PI / 3);
				g2.fill(eolica);
			}
		}

		private void desenharCarro(Graphics2D g2) {
			AffineTransform tr = g2.getTransform(); 

			g2.translate(-1.5, -0.1);
			g2.scale(0.8, 0.8);
			desenharRodas(g2);

			// restaura a transformação

			g2.setTransform(tr);

			
			g2.translate(1.5, -0.1);
			g2.scale(0.8, 0.8);
			desenharRodas(g2);

			// restaura a transformação

			g2.setTransform(tr);
			g2.setColor(Color.RED);
			
			        
            g2.fill(new Rectangle2D.Double(-2.5,0,5,2) );
		}

		private void desenharRodas(Graphics2D g2) {

						g2.setColor(Color.BLACK);
			g2.fill(new Ellipse2D.Double(-1, -1, 2, 2));
			g2.setColor(Color.LIGHT_GRAY);
			g2.fill(new Ellipse2D.Double(-0.8, -0.8, 1.6, 1.6));
			g2.setColor(Color.BLACK);
			g2.fill(new Ellipse2D.Double(-0.2, -0.2, 0.4, 0.4));
			g2.rotate(-frameNumber / 30.0);

			for (int i = 0; i < 15; i++) {
				g2.rotate(2 * Math.PI / 15);
				g2.draw(new Rectangle2D.Double(0, -0.1, 1, 0.2));
			}
		}

	}
}
