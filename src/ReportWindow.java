import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ReportWindow{
	private static final int X_OFFSET = 10;
	private static final int Y_OFFSET = 10;

	private Stats stats;
	private World world;
	private Container contentPane;

	public ReportWindow(Stats stat, World world){
		this.stats = stats;
		this.world = world;

		JFrame frame = new JFrame("Evolution Org Map");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = frame.getContentPane();
		contentPane.add(new Map());
		frame.setSize(Constants.MAX_WORLD_X+(X_OFFSET*2)+10,Constants.MAX_WORLD_Y+(Y_OFFSET*2)+35);
		frame.setVisible(true);
	}

	public void refresh(){
		contentPane.repaint();
	}

	public class Map extends JComponent{
		public void paint(Graphics g){
			g.setColor(Color.BLACK);
			g.fillRect(X_OFFSET-2, X_OFFSET-2, Constants.MAX_WORLD_X+4, Constants.MAX_WORLD_Y+4);
			drawMap(g);
		}
		private void drawMap(Graphics g){
			g.setColor(Color.GREEN);

			for(int y=0; y<Constants.MAX_WORLD_Y; y++){
				for(int x=0; x<Constants.MAX_WORLD_X; x++){
					Organism o = world.getWorldMapAt(x, y);
					if(o!=null){
						g.setColor(o.getOrgColorObj());
						drawPoint(g,x+X_OFFSET,y+Y_OFFSET);
					}
				}
			}
		}
		private void drawPoint(Graphics g, int x, int y){
			g.drawLine(x,y,x,y);
		}
	}
}

//int height = 200;
//		  int width = 120;
//		  g.setColor(Color.red);
//		  g.drawRect(10,10,height,width);
//		  g.setColor(Color.gray);
//		  g.fillRect(11,11,height,width);
//		  g.setColor(Color.red);
//		  g.drawOval(250,20, height,width);
//		  g.setColor(Color.magenta);
//		  g.fillOval(249,19,height,width);