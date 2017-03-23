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
    static private int maxCount = 50;
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
                {
	            gui.text.setText(gui.text.getText().split("\n", (2))[1]);
                    gui.text.setCaretPosition( gui.text.getDocument().getLength());
                }else 
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
                          
	            }
	            else if(waitTime.get(0).equals(0) || fire){
	            	gui.text.setText(gui.text.getText() + inp);
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
           
            g.drawImage( image, 0, 0, getWidth(), getHeight(), this );
        }
    }
}

public class GUI extends JFrame{
    
    private Image[] background;
 
    final int anzBackgrounds = 2;
    Thread outThread = new Output(this, "", 0, 0);
    JPanel input = new JPanel();
    JPanel output = new JPanel();
    JPanel platzhalter = new JPanel();
    ImagePanel bg;
    JTextField jt = new JTextField();
    JTextArea text = new JTextArea();
    JScrollPane sp = new JScrollPane(text);
    
    public GUI()
    {
        //Laden der Hintergrundbilder
        background = new Image[anzBackgrounds];
        for(int i=0; i<anzBackgrounds; i++)
            background[i] = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("ressources/bg/bg"+i+".jpg"));
        
        
        ((Output) outThread).done = true;
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setTitle("Geschichtswelten");
        setSize(dim.width,dim.height);  
        setExtendedState(JFrame.MAXIMIZED_BOTH);
       setUndecorated(true);//make fullscreen without x and stuff
     
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
      
        bg = new ImagePanel(background[0]);
        bg.setLayout(new BorderLayout());
        setContentPane(bg);
        
        
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        
        text.setEditable(false);
        text.setLineWrap(true);
        text.setOpaque(false);
        text.setBorder(BorderFactory.createEmptyBorder());
        text.setFont(new Font("Arial", Font.PLAIN, 16));
        text.setForeground(Color.black);
        jt.setFont(new Font("Arial", Font.PLAIN, 16));
        jt.setPreferredSize(new Dimension(dim.width, 25));
        input.setOpaque(false);
        input.add(jt);
        input.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));//top, left, bottom, right
        add(input, BorderLayout.SOUTH);
        
        output.setOpaque(false);
        output.setLayout(new BorderLayout(0,0));
        output.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        output.add(sp);
        add(output, BorderLayout.CENTER);

        
        jt.addActionListener(new ActionListener()
        {
         public void actionPerformed(ActionEvent e)
            {
                
                String temp="";
                if(jt.isEditable())
                {
                    temp = jt.getText();
                    jt.setText("");
                    jt.update(jt.getGraphics());
                }
                temp = temp.toLowerCase();
                if(temp.equals("beende spiel"))
                {
                    dispose();
                    System.exit(0);
                }
                
                // call Game::OnInput
                Game.Get().OnInput(temp);
            }
        });
     
        setVisible(true);
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
        if(input=="")
            return;
        
    	write("\n> "+input, waitTime, delay);
    }
    
    public void setBg(int nmb)
    {
        bg.setImage(background[nmb]);
        bg.repaint();
    }
    
    public void setInputMessage(String msg)
    {
        jt.setHorizontalAlignment(JTextField.CENTER);
        jt.setText(msg);
        jt.setEditable(false);
    }
    public void enableWriting(boolean b){
        if(b)
            jt.setHorizontalAlignment(JTextField.LEFT);
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