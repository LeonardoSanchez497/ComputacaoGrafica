import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.RenderingHints;

import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ByteLookupTable;
import java.awt.image.ColorConvertOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;


import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Java2D extends JApplet implements ActionListener {
	ImagePanel imgSrc, imgDst;
	

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

			// Desenhar Eolicas

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

			g2.fill(new Rectangle2D.Double(-2.5, 0, 5, 2));
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

	public Java2D() {
		JMenuBar mb = new JMenuBar();
		setJMenuBar(mb);
		
		JMenu menu = new JMenu("File");
		JMenuItem mi = new JMenuItem("Trabalho");
		mi.addActionListener(this);
		menu.add(mi);
		
		mi = new JMenuItem("Exit");
		mi.addActionListener(this);
		menu.add(mi);
		mb.add(menu);
		
		// MENU PROCESS
				JMenu menuProcess = new JMenu("Process");

				mi = new JMenuItem("RGB to Gray I");
				mi.addActionListener(this);
				menuProcess.add(mi);

				mi = new JMenuItem("RGB to Gray II");
				mi.addActionListener(this);
				menuProcess.add(mi);

				mi = new JMenuItem("Sharpen");
				mi.addActionListener(this);
				menuProcess.add(mi);

				mi = new JMenuItem("Edge");
				mi.addActionListener(this);
				menuProcess.add(mi);

				mi = new JMenuItem("Brightness");
				mi.addActionListener(this);
				menuProcess.add(mi);
				
				mi = new JMenuItem("Rotate");
				mi.addActionListener(this);
				menuProcess.add(mi);

				mi = new JMenuItem("Mirror Effect");
				mi.addActionListener(this);
				menuProcess.add(mi);

				mi = new JMenuItem("Negative");
				mi.addActionListener(this);
				menuProcess.add(mi);

				mb.add(menuProcess);

				// ADD PANELS
				Container cp = this.getContentPane();
				cp.setLayout(new FlowLayout());
				imgSrc = new ImagePanel();
				imgDst = new ImagePanel();
				cp.add(imgSrc);
				cp.add(imgDst);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String cmd = e.getActionCommand();
		if ("Trabalho".equals(cmd)) {
			BufferedImage bi = null;
			java.net.URL url = getClass().getClassLoader().getResource("Imagens/final2d.png");
			try {
				bi = ImageIO.read(url);
				imgSrc.setImage(bi);
			} catch (Exception e1) {
				// TODO: handle exception
				e1.printStackTrace();
			}
		} else if ("Exit".equals(cmd)) {
			System.exit(0);
		} else {
			process(cmd);
		}
		
		
		
	}
	void process(String menuOp) {
		BufferedImageOp op = null;
		BufferedImage bi = null;
		if (menuOp.equals("RGB to Gray I")) {
			op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
			bi = op.filter(imgSrc.getImage(), null);
			imgDst.setImage(bi);
		} else if (menuOp.equals("RGB to Gray II")) {
			bi = RGBtoGray(imgSrc.getImage());
			imgDst.setImage(bi);
		} else if (menuOp.equals("Sharpen")) {
			float data[] = { -1.0f, -1.0f, -1.0f, -1.0f, 9.0f, -1.0f, -1.0f, -1.0f, -1.0f };
			Kernel kernel = new Kernel(3, 3, data);
			op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
			bi = op.filter(imgSrc.getImage(), null);
			imgDst.setImage(bi);
		} else if (menuOp.equals("Edge")) {
			float data[] = { 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, -1.0f };
			Kernel kernel = new Kernel(3, 3, data);
			op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
			bi = op.filter(imgSrc.getImage(), null);
			imgDst.setImage(bi);
		} else if (menuOp.equals("Brightness")) {
			float[] factors = new float[] { 1.4f, 1.4f, 1.4f };
			float[] offsets = new float[] { 0.0f, 0.0f, 30.0f };
			op = new RescaleOp(factors, offsets, null);
			bi = op.filter(imgSrc.getImage(), null);
			imgDst.setImage(bi);
		} else if (menuOp.equals("Rotate")) {
			AffineTransform at = new AffineTransform();
			at.rotate(Math.PI / 6);
			op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
			bi = op.filter(imgSrc.getImage(), null);
			imgDst.setImage(bi);
		} else if (menuOp.equals("Mirror Effect")) {
			BufferedImage bi1 = horizontalmirror(imgSrc.getImage());
			imgDst.setImage(bi1);
		} else if (menuOp.equals("Negative")) {
			byte reverse[] = new byte[256];
		    for (int i = 0; i < 256; i++) {
		      reverse[i] = (byte) (255 - i);
		    }
		    LookupTable lookupTable = new ByteLookupTable(0, reverse);
		    LookupOp lop = new LookupOp(lookupTable, null);
		    bi = lop.filter(imgSrc.getImage(), null);
		    imgDst.setImage(bi);
		}
	}
	private BufferedImage horizontalmirror(BufferedImage bi) {
		BufferedImage temp = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = temp.createGraphics();
		g2.drawImage(bi, 0, 0, bi.getWidth(), bi.getHeight(), bi.getWidth(), 0, 0, bi.getHeight(), null);
		g2.dispose();
		return temp;
	}

	BufferedImage RGBtoGray(BufferedImage imagIn) {
		BufferedImage imagOut = new BufferedImage(imagIn.getWidth(), imagIn.getHeight(), imagIn.getType());

		WritableRaster rImagIn = imagIn.getRaster();
		WritableRaster rImagOut = imagOut.getRaster();

		int rgb[] = new int[3];
		for (int x = 0; x < imagIn.getWidth(); x++) {
			for (int y = 0; y < imagIn.getHeight(); y++) {
				rImagIn.getPixel(x, y, rgb);
				int gray = (int) ((rgb[0] + rgb[1] + rgb[2]) / 3f);

				rgb[0] = rgb[1] = rgb[2] = gray;
				rImagOut.setPixel(x, y, rgb);
			}
		}

		return imagOut;
	}


	class ImagePanel extends JPanel {

		private BufferedImage image = null;

		public ImagePanel() {
			setPreferredSize(new Dimension(400, 400));
		}

		public BufferedImage getImage() {
			return image;
		}

		public void setImage(BufferedImage bi) {
			image = bi;
			setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
			invalidate();
			repaint();
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;

			if (image != null) {
				g2.drawImage(image, 0, 0, this);
			} else {
				g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
			}
		}

	}
	
}
