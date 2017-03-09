package game;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

//Counter eröhen sachen löschen - position setzen
class Output extends Thread{
    private GUI gui;
    ArrayList<String> input;
    public boolean done;
    static private int count=0;
    static private int maxCount = 20;
    private ArrayList<Integer> waitTime;
    private ArrayList<Integer> delay;
    private boolean fire = false;
    
    Output(GUI gui, String input, int waittime, int delay)
    {
        this.gui = gui;
        this.input = new ArrayList<>();
        this.input.add(input);
        this.waitTime = new ArrayList<>();
        this.waitTime.add(waittime);
        this.delay = new ArrayList<>();
        this.delay.add(delay);
        this.fire = false;
        done = false;
    }
    
    public void forceWrite(boolean all){
    	fire = all;
    	if(!all){
    		if(input.size() > 0){
    			waitTime.set(0, 0);
    			delay.set(0, 0);
    		}
    	}
    }
    
    public void appendInput(String input, int waittime, int delay){
    	this.input.add(input);
    	this.waitTime.add(waittime);
    	this.delay.add(delay);
    } 
    
    public void run() {
    	while(input.size() > 0){
    		if(!fire){
		        try {
		            Thread.sleep(delay.get(0));
		        } catch (InterruptedException ex) {
		        	
		        }
    		}
	    	
	        if (input.equals(""))
	        {
	            done = true;
	            return;
	        }
	        String inp = input.get(0);
	
	        if (count > maxCount) 
	            gui.text.setText(gui.text.getText().split("\n", (2))[1]);
	       
	        else 
	            count++;
	        
	
	        while (inp.length() != 0) {
	        	if(!fire){
		            try {
		                Thread.sleep(waitTime.get(0));
		            } catch (InterruptedException ex) {
		            	waitTime.set(0, 0);
		            }
	        	}
	            if(!waitTime.get(0).equals(0) && !fire){
		            gui.text.setText(gui.text.getText() + inp.charAt(0));
		            inp = inp.substring(1);
		            gui.text.update(gui.text.getGraphics());
	            }
	            else if(waitTime.get(0).equals(0) || fire){
	            	gui.text.setText(gui.text.getText() + inp);
	            	gui.text.update(gui.text.getGraphics());
	            	break;
	            }
	        }
                gui.text.setCaretPosition( gui.text.getDocument().getLength());
	        input.remove(0);
	        waitTime.remove(0);
	        delay.remove(0);
    	}
        done = true;
    }
}
class ImagePanel extends JPanel{
    private Image image;
    public ImagePanel()
    {
        this.image = null;
    }
    public ImagePanel(Image image)
    {
        this.image = image;
    }
    public void setImage(Image image)
    {
        this.image = image;
    }
    
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if(image != null)
        {
            int w = getWidth();
            int h = getHeight();
            int iw = image.getWidth( this );
            int ih = image.getHeight( this );
            int x = ( w - iw ) / 2;
            int y = ( h - ih ) / 2;
            g.drawImage( image, x, y, this );
        }
    }
}

public class GUI extends JFrame{
    
    private Image[] background= {
        //Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("ressources/bg0.jpg")),
        //Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("ressources/bg1.jpg"))
    };
 
    
    Thread outThread = new Output(this, "", 0, 0);
    JPanel input = new JPanel();
    JPanel output = new JPanel();
    ImagePanel bg;
    JTextField jt = new JTextField(53);
    JTextArea text = new JTextArea(19,53);
    JScrollPane sp = new JScrollPane(text);
    
    public GUI()
    {
        ((Output) outThread).done = true;
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setTitle("Geschichtswelten");
        setSize(600,400);
        setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //bg = new ImagePanel(background[0]);
        //bg.setLayout(new BorderLayout());
        //setContentPane(bg);
        
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        
        text.setEditable(false);
        text.setLineWrap(true);
        text.setOpaque(false);
        text.setBorder(BorderFactory.createEmptyBorder());
        text.setForeground(Color.black);
        
        input.setOpaque(false);
        input.setLayout(new FlowLayout(0));
        input.add(jt);
        add(input, BorderLayout.SOUTH);
        
        output.setOpaque(false);
        output.setLayout(new FlowLayout(0));
        output.add(sp);
        add(output);

        GUI tmp = this;
        jt.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String temp = jt.getText();
                jt.setText("");
                jt.update(jt.getGraphics());
                Game.Get().OnEnterPressed();
                Game.Get().OnInput(temp);
                //if(((Output) outThread).done) Parser.handle(temp, tmp);
            }
        });
     
        jt.requestFocusInWindow();
    }
    
    public void write(String input, int waitTime, int delay)
    {
    	
        if(!((Output) outThread).done)
        {
            //((Output) outThread).setWait(0);
            ((Output) outThread).appendInput(input, waitTime, delay);
        }
        else
        {
            outThread = new Output(this, input, waitTime, delay);
            outThread.start();
        }
    }
    
    public void writeln(String input, int waitTime, int delay){
    	write("> "+input+"\n", waitTime, delay);
    }
    
    public void switchBg(int nmb)
    {
        //bg.setImage(background[nmb]);
    }
    
    public void enableWriting(boolean b){
    	jt.setEditable(b);
    }
    
    public boolean stillWriting(){
    	if(!((Output) outThread).isAlive())
    		return false;
    	return !(((Output) outThread).done);
    }
    
    public void forceWrite(boolean all){
    	if(!((Output) outThread).done){
    		((Output) outThread).forceWrite(all);

    	}
    }
}
