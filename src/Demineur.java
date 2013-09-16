import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;



public class Demineur extends JFrame {

	//--les differents JPanel donc j'aurai besoin==============================================

	private JPanel containner=new JPanel();  //---------------------c'est lui qui reçoit tout
	private JPanel gameGrid= new JPanel(); //------------------------c'est lui qui recois les bouton du jeux
	private JPanel north   =new JPanel();
	private JPanel west    =new JPanel();
	private JPanel east    =new JPanel();
	private JPanel south    =new JPanel();


	//-----------------------------------------------------mes label pour les endroit ou je vais ecrire 

	private Label lNorth=new Label("je suis au north");
	private Label etimer=new Label("je suis au s");
	private Label timer=new Label("0s");
	private Label enbreBombes=new Label("il rest n bombes");
	private Label nbreBombes=new Label("50");
	private Label youWin    =new Label ("");

	//____________________________________________________________________image util pour fin de jeux
	private Image img;


	//__________________________________________________________buttons doit recevoir tous mes boutons 

	private ArrayList<Button> gameButtons=new ArrayList<Button>();
	private ArrayList<Button> gameBombes=new ArrayList<Button>();  // ce tableau prend les bombes   pour l'instant la diff entre bommbe et buttons c'ets val bommbe =-1
	private ArrayList<Button> voisinsBombes=new ArrayList<Button>();
	int[] tab= new int[50];   // ce tableau va recevoir les bombes aléatoire 

	//========================================================================bouton pour relancer le jeux
	private JButton restart=new JButton("Recommencer le jeux");



	///=========================================constructeur du demineur ==================================================================

	public Demineur(){
		this.setSize(480,600);
		this.setAlwaysOnTop(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("DEMINEUR");
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		initRestart();


	}

	//============================================initialisation de nos boutons

	public void initButtons(){
		gameButtons.clear();
		youWin.setText("Good Luck my friend");
		for (int i=0; i<400; i++) {

			gameButtons.add(new Button(""));
			gameButtons.get(i).addActionListener(new ButtonListener());

		}
		initBombes();

	}
	public void initRestart(){
		restart.addActionListener(new RestartListener());
		initButtons();
	}
	public void initBombes(){                           //__________________________________marche nickel
		tab=initTabAleatoire(50,400);
		for(int c:tab){
			gameButtons.get(c).setVal(-20);
			gameBombes.add(gameButtons.get(c));

		}

		initAff(); //----------------------initialise les valeurs pour touts les elements 
	}
	public void initVal(){
		ArrayList<Button> res=new ArrayList<Button>();
		int i; int c; int a;
		for(i=0; i<gameBombes.size();i++){
			voisinsBombes.addAll(gameBombes.get(i).voisins(gameButtons));
		}
		for(i=0; i<gameBombes.size();i++){
			if(voisinsBombes.contains(gameBombes.get(i))){voisinsBombes.remove(gameBombes.get(i));}
		}



		for(c=0; c<voisinsBombes.size(); c++){


			for(i=0;i<gameButtons.size();i++){
				//	
				if(gameButtons.get(i)==voisinsBombes.get(c)){
					gameButtons.get(i).setVal(gameButtons.get(i).getVal()+1);


				}
			}
			i=0;
		}


	}



	//=============================================initialisation de notre affichage

	public void initAff(){

		//-----------------------------------------------------------ajout de north
		north.setPreferredSize(new Dimension(480,40));
		north.add(lNorth);  ///               à retirer lorsque je vais y mettre une image
		containner.add(north, BorderLayout.NORTH);
		//===================================commencons par mettres nos bouttons dans le gameGrid
		gameGrid.removeAll();
		gameGrid.setLayout(new GridLayout(20,20)); //  en ajoutant les boutons , ont va les donner des coordonnees x y
		int x; int y; int c=0;

		for(y=0; y<20;y++){
			for(x=0; x<20; x++){
				gameGrid.add(gameButtons.get(c));
				gameButtons.get(c).setnX(x);
				gameButtons.get(c).setnY(y);


				c++;
			}
			x=0;
		}		
		gameGrid.setPreferredSize(new Dimension(400,400));
		containner.add(gameGrid, BorderLayout.CENTER);



		//------------------------------------------------------------ajout de south   A RE TOUCHER PLUS TARD POUR AJOUTER LE TEMPS ET AUTRES 
		south.setPreferredSize(new Dimension (480,100));
		south.add(etimer);
		south.add(timer);
		south.add(restart);
		south.add(enbreBombes);
		south.add(nbreBombes);
		south.add(youWin);
		containner.add(south, BorderLayout.SOUTH);


		///-------------------------------------------------------- affectation de contentpane a containner
		this.setContentPane(containner);
		initVal();
	}

	//============================================================fonction pour retrouver mes valeur aleatoire
	public int[] initTabAleatoire(int tailleTableau, int limite){
		boolean b=true; int[] tab=new int[tailleTableau];int j=0;
		for (int i=0;i<tailleTableau;i++){
			while(b){
				b=false;
				j=(int)(Math.random()*limite);
				for(int c=0;c<i;c++){
					if(j==tab[c]){
						b=true;
					}
				}
			}
			tab[i]=j;
			b=true;
		}
		return tab;
	}
	// ____________----------------------------------------------la classe ninterne RestartListener

	public class RestartListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			initButtons();


		}

	}
	//------------------------------------------------------------la classe interne ButtonListener
	public class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {  //-----------------il y a que 2 cas , celui ou c'est une bombe, jeux finit, ou alors celui ou c'est un vide , ouvrir les voisins
			// TODO Auto-generated method stub
			if(!((Button)(arg0.getSource())).getOpen()){	
				if(((Button)(arg0.getSource())).getVal()<0){
					for(int i=0; i<gameButtons.size();i++){
						repaintButton(gameButtons.get(i));
						gameButtons.get(i).setOpen(true);
					}
					youWin.setText("you loose, hihihihi ");
				}  // fin du cas de fin de jeux
				else if(((Button)(arg0.getSource())).getVal()==0){
					//	repaintButtonRec(((Button)(arg0.getSource())));
					repaintButtonBis(((Button)(arg0.getSource()))); //               zqdz razhl kuiyhi o
					((Button)(arg0.getSource())).setOpen(true);
					//	ArrayList<Button> voisins=new ArrayList<Button>();
					//	voisins.addAll(((Button)(arg0.getSource())).voisins(gameButtons));
					/*	for(int i=0;i<voisins.size();i++){
						if(!(voisins.get(i).getOpen())){
							repaintButton(voisins.get(i));
							voisins.get(i).setOpen(true);
							//voisins.remove(voisins.get(i));
						}
						repaintButtonBis(voisins.get(i));

					}*/

					/*	repaintButton(((Button)(arg0.getSource())));////int c=repaintRec(((Button)(arg0.getSource())),1); 
					((Button)(arg0.getSource())).setOpen(true);


					ArrayList<Button> voisins=new ArrayList<Button>();
					voisins.addAll(((Button)(arg0.getSource())).voisins(gameButtons));
					for(int i=0;i<voisins.size();i++){
						repaintButton(voisins.get(i));
						voisins.get(i).setOpen(true);

					}   */
					tryWin();
				}
				else{
					repaintButton(((Button)(arg0.getSource())));
					((Button)(arg0.getSource())).setOpen(true);
					tryWin();
				}

			}
		}
	}
	//=============================================================Determine si le jeux est gagné
	public void tryWin(){
		ArrayList<Button> set=new ArrayList<Button>(); boolean gameWin=true;
		set.addAll(gameButtons);
		for(int i=0; i<gameBombes.size(); i++){
		//	if(!gameBombes.get(i).getMarkedButtons().contains(gameBombes.get(i))){
		//		gameWin=false;
		//	}
		}
		if(gameWin){youWin.setText("you winnnnnnnnn");}

	}
	//=============================================================repaintButtonrec repaint les voisins 
	public void repaintButtonRec(Button b){
		ArrayList<Button> voisins=new ArrayList<Button>();
		voisins.addAll(b.voisins(gameButtons));

		System.out.println(b.getnX()+"  "+b.getnY());

		if(b.getOpen()){

		}
		else if((b.getnX()<0)||(b.getnX()>400)||(b.getnY()<0)||(b.getnY()>400)){
			System.out.println(" je suis klhilh la");
		}
		else if(b.getVal()!=0){
			repaintButton(b);
			b.setOpen(true);
		}
		else{

			//for(int i=0; i<voisins.size();i++){
			//if(!gameButtons.contains(voisins.get(i))){voisins.remove(voisins.get(i));
			//System.out.println(" je suis la");
			//}

			for(int i=0;i<voisins.size();i++){
				repaintButton(voisins.get(i));
				voisins.get(i).setOpen(true);
				//System.out.println(" nonnnn je suis la");
				repaintButtonRec(voisins.get(i));
			} 

		}


	}
	public int repaintRec(Button b, int n){
		if(b.getVal()!=0){
			repaintButton(b);

			return 0;
		}

		ArrayList<Button> voisins=new ArrayList<Button>();
		voisins.addAll(b.voisins(gameButtons));
		for(int i=0;i<voisins.size();i++)
		{
			repaintButton(voisins.get(i));
			return repaintRec(voisins.get(i), 1);
		}
		return 0;
	}

	//================================================fonction _____repainButton( Button b)
	public void repaintButton(Button b){

		if(b.getVal()<=-1){
			try{
				b.setImg(img=ImageIO.read(new File("bombe.png")));
				b.setRepasse(false);
				b.repaint();

			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		if(b.getVal()==0){
			try{
				b.setImg(img=ImageIO.read(new File("0.png")));
				b.setRepasse(false);
				b.repaint();

			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		if(b.getVal()==1){
			try{
				b.setImg(img=ImageIO.read(new File("1.png")));
				b.setRepasse(false);
				b.repaint();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		if(b.getVal()==2){
			try{
				b.setImg(img=ImageIO.read(new File("2.png")));
				b.setRepasse(false);
				b.repaint();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		if(b.getVal()==3){
			try{
				b.setImg(img=ImageIO.read(new File("3.png")));
				b.setRepasse(false);
				b.repaint();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		if(b.getVal()==4){
			try{
				b.setImg(img=ImageIO.read(new File("4.png")));
				b.setRepasse(false);
				b.repaint();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		if(b.getVal()==5){
			try{
				b.setImg(img=ImageIO.read(new File("5.png")));
				b.setRepasse(false);
				b.repaint();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		if(b.getVal()==6){
			try{
				b.setImg(img=ImageIO.read(new File("6.png")));
				b.setRepasse(false);
				b.repaint();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}



	}// ma fin doit être au dessus
	public void repaintButtonBis(Button b){
		if(!b.getOpen()){

			if(b.getVal()<=-1){
				try{
					b.setImg(img=ImageIO.read(new File("bombe.png")));
					b.setRepasse(false);
					b.repaint();

				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
			if(b.getVal()==0){
				try{
					b.setImg(img=ImageIO.read(new File("0.png")));
					b.setRepasse(false);
					b.repaint();

				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
			if(b.getVal()==1){
				try{
					b.setImg(img=ImageIO.read(new File("1.png")));
					b.setRepasse(false);
					b.repaint();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
			if(b.getVal()==2){
				try{
					b.setImg(img=ImageIO.read(new File("2.png")));
					b.setRepasse(false);
					b.repaint();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
			if(b.getVal()==3){
				try{
					b.setImg(img=ImageIO.read(new File("3.png")));
					b.setRepasse(false);
					b.repaint();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
			if(b.getVal()==4){
				try{
					b.setImg(img=ImageIO.read(new File("4.png")));
					b.setRepasse(false);
					b.repaint();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
			if(b.getVal()==5){
				try{
					b.setImg(img=ImageIO.read(new File("5.png")));
					b.setRepasse(false);
					b.repaint();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
			if(b.getVal()==6){
				try{
					b.setImg(img=ImageIO.read(new File("6.png")));
					b.setRepasse(false);
					b.repaint();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
			b.setOpen(true);

			if(b.getVal()==0){


				ArrayList<Button> voisins =new ArrayList<Button>();
				voisins.addAll(b.voisins(gameButtons));
				for(int i=0; i<voisins.size();i++){
					repaintButton(voisins.get(i));
					//voisins.get(i).setOpen(true);
					repaintButtonBis(voisins.get(i));
				}
			}

		}

	}

	public Button whoIsAt(int x, int y,ArrayList<Button> buttons){
		for(int i=0;i<buttons.size();i++){
			if((buttons.get(i).getnX()==x)&&(buttons.get(i).getnY()==y))return buttons.get(i);
		}
		Button res=new Button(" who a raté");
		res.setVal(30);

		return  res;
	}
}

