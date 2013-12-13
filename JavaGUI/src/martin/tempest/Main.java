package martin.tempest;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import martin.tempest.core.TSDRLibrary;
import martin.tempest.core.exceptions.TSDRException;
import martin.tempest.sources.TSDRSource;

public class Main implements TSDRLibrary.FrameReadyCallback {
	
	private static final int WIDTH = 1056;
	private static final int HEIGHT = 628;
	private static final double REFRESHRATE = 75.5622125;
	private static final String COMMAND = "D:\\Dokumenti\\Cambridge\\project\\mphilproj\\Toshiba-440CDX\\toshiba.iq";
	
	private final ImageVisualizer viz = new ImageVisualizer();
	private JFrame frame;

	public static void main(String[] args) {
		try {
			new Main();
		} catch(Exception e) {
			System.err.println("Cannot load! Reason: "+e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public Main() throws TSDRException {
		frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(viz, BorderLayout.CENTER);
		frame.setSize(WIDTH,HEIGHT);
		frame.setVisible(true);
		
		new Thread() {
			public void run() {
				try {
					TSDRLibrary sdrlib = new TSDRLibrary();
					sdrlib.registerFrameReadyCallback(Main.this);
					sdrlib.startAsync(TSDRSource.fromRawFile(COMMAND), WIDTH, HEIGHT, REFRESHRATE);
				} catch (Throwable e) {e.printStackTrace();};
				
			};
		}.start();
		
	}

	int fid = 0;
	@Override
	public void onFrameReady(TSDRLibrary lib, BufferedImage frame) {
		//viz.drawImage(frame);
		try {
			ImageIO.write(frame, "bmp", new java.io.File("D:\\temp\\"+(fid++)+".bmp"));
		} catch (IOException e) {}
	}

	@Override
	public void onException(TSDRLibrary lib, Exception e) {
		System.out.println("On Exception");
		e.printStackTrace();
	}

	@Override
	public void onClosed(TSDRLibrary lib) {
		System.out.println("Closed");
	}

}
