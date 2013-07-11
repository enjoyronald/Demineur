
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;


public class Button extends JButton implements MouseListener, MouseMotionListener {
	private String nom;
	private int x;			//----------------------------coordonne sur les axes x et y de gamegrid  elles donne lors de l ajout a la grille 
	private int y;
	private int val=0;           //----------------------------si val<0, button est une bombe , si val=0, pas de menace autour, si val =idif 0, il y a i bombes autour
	private  boolean mark=false; // ----------------------------si isMarKed== 0, button non marké et 1 marké 
	private boolean open=false;  //                             s'il est ouvert 
	private Image img;
	private boolean repasse=true ;                              // pour savoir si la case doit être redessiné 
	private static ArrayList<Button> markedButtons= new ArrayList<Button>();


	public Button(String nom){
		super(nom);
		Font font=new Font("Arial", Font.BOLD, 5);
		this.setFont(font);
		this.setHorizontalAlignment(JLabel.CENTER);
		//this.setBackground(Color.blue);
		this.setPreferredSize(new Dimension(20,20));
		try {
			img=ImageIO.read(new File("default.png"));
		}
		catch(IOException e){
			e.printStackTrace();
		}

		this.addMouseListener(this);


	}


	//------------------------------------------------ les setteurs et les getteurs
	public int getnX(){
		return x;
	}
	public int getnY(){
		return y;
	}

	public int getVal(){
		return this.val;
	}
	public boolean getMark(){
		return this.mark;
	}
	public boolean getOpen(){
		return open;
	}
	public static ArrayList<Button> getMarkedButtons(){
		return markedButtons;
	}

	//------------------------------------------------ les setteurs
	public void setOpen(boolean b){
		open=b;
	}
	public void setnX(int x){
		this.x=x;
	}
	public void setImg(Image img){
		this.img=img;
	}
	public void setnY(int y){
		this.y=y;
	}
	public void setVal(int val){  //==================================================================A REMPLIRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
		this.val=val;
	}
	public void setRepasse(boolean b){
		repasse=b;
	}
	public void setMark(boolean b){
		mark=b;
	}
	//---------------------------------------------whoIsAt renvoi celui qui see trouve a x y

	public Button whoIsAt(int x, int y,ArrayList<Button> buttons){
		for(int i=0;i<buttons.size();i++){
			if((buttons.get(i).getnX()==x)&&(buttons.get(i).getnY()==y))return buttons.get(i);
		}
		Button res=new Button(" who a raté");
		res.setVal(30);

		return  res;
	}



	//================================================================la fonction VOISINS RENVOY UNE ARRAYLIST DE BUTTONS==================================================
	public ArrayList<Button> voisins(ArrayList<Button> buttons){
		//------------------------------------------------on doit tout distinguer les cas des bordures et les angles 
		//------------------------------------------------les angles 
		ArrayList<Button> voisins=new ArrayList<Button>();


		for(int i=0; i<buttons.size(); i++){

			if(this.equals(buttons.get(i))){

				voisins.add(whoIsAt(this.getnX()+1,this.getnY()+1,buttons));
				voisins.add(whoIsAt(this.getnX()+1,this.getnY()-1,buttons));
				voisins.add(whoIsAt(this.getnX()+1,this.getnY(), buttons));
				voisins.add(whoIsAt(this.getnX()-1,this.getnY(), buttons));
				voisins.add(whoIsAt(this.getnX()-1,this.getnY()-1, buttons));
				voisins.add(whoIsAt(this.getnX()-1,this.getnY()+1, buttons));
				voisins.add(whoIsAt(this.getnX(),this.getnY()+1, buttons));
				voisins.add(whoIsAt(this.getnX(),this.getnY()-1, buttons));
			}
		}
		return voisins;
	}

	@Override
	public void mouseClicked(MouseEvent ev) {
		if(!open){
			if(ev.getButton()==3){ //_______________===============bouton droit appuyé=======================-----------________
				if(!mark){
					this.setMark(true);
					try{
						img=ImageIO.read(new File("mark.png"));
						repasse=false;
						markedButtons.add(this);
					}
					catch(IOException e){
						e.printStackTrace();
						System.out.println("sds 545");
					}
				}
				else{
					this.setMark(false);
					try{
						img=ImageIO.read(new File("default.png"));
						repasse=true;
						markedButtons.remove(this);
					}
					catch(IOException e){
						e.printStackTrace();
						System.out.println("sds 545");
					}
				}
			}
		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {   //----------------------si on peut reppasser , alors on illumine la case
		// TODO Auto-generated method stub
		if(repasse){
			try{
				img=ImageIO.read(new File("survol.png"));
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(repasse){
			try{
				img=ImageIO.read(new File("default.png"));
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}

	}

	@Override
	public void mousePressed(MouseEvent arg0) {

	}	

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}

	//============================redefinission du paintConponent=========================================================================

	public void paintComponent(Graphics g){
		Graphics2D g2d=(Graphics2D)g;
		g2d.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	}


	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}