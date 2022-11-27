//Codé par Tobias Lepoutre & Maxence Puech

package components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.text.BadLocationException;
import java.io.IOException;
import java.lang.IllegalStateException;

// Classe principale du projet:
public class interfaceG {

	
	public static void main (String[] args) {
		
	// Creation de l'interface graphique:
		
	JFrame f = new JFrame("Tp");
	
	// Creation des bouttons: "Fichier", "Dictionnaire" et "Verifier".
	JPanel p = new JPanel();
	JButton fichier = new JButton("Fichier"); 
	p.add(fichier,FlowLayout.LEFT);
	JButton dictionnaire = new JButton("Dictionnaire");
	p.add(dictionnaire,FlowLayout.CENTER);
	JButton verifier = new JButton("Vérifier");
	p.add(verifier,FlowLayout.RIGHT);
	
	// Zone de texte associé au JFrame.
	JTextArea text = new JTextArea(25,50);
	
	// Zones de texte non-associé au JFrame, utilisées pour accéder à certaines valeurs(String, int...)
	// et les rendre accessible tout au long du code.
	JTextArea isClicked= new JTextArea();
	JTextArea corrList = new JTextArea();
	JTextArea dico = new JTextArea();
	JTextArea position = new JTextArea();

	// Retour à la ligne automatiquement pour JTextArea "text".
	text.setLineWrap(true);
	
	// Creation d'une scrollbar pour le JTextArea
	JScrollPane scroll = new JScrollPane(text);
    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    p.add(scroll);
    f.add(p);
    
    // Creation des sous-bouttons du boutton "Fichier".
	JButton ouvrir = new JButton("Ouvrir");
	JButton enregistrer = new JButton("Enregistrer");
	JButton retour = new JButton("Retour");
	
    // ActionListener de "Fichier".
	fichier.addActionListener(new ActionListener(){  
		public void actionPerformed(ActionEvent e5) {

			f.setVisible(false);
			// Change les boutton du JPanel initial par les sous-bouttons.
			p.removeAll();
			p.add(ouvrir,FlowLayout.LEFT);
			p.add(enregistrer,FlowLayout.CENTER);
			p.add(retour,FlowLayout.RIGHT);
		    p.add(scroll);
		    
		    f.setVisible(true);
		    

		}  
	});
	
	// ActionListener de "Dictionnaire".
	dictionnaire.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e6) {

			// Creation d'un String contenant le dictionnaire
			// en fonction du choix de l'utilisateur (FileChooser).
			String dicString = createString();
			
			// Enregistrement de ce string dans le JTextArea "dico".
			dico.setText(dicString);

			// Si le dictionnaire n'a pas été choisi:		
			if (dico.getText().length()==0) {
				JOptionPane.showMessageDialog(null, "Le dictionnaire n'a pas été chargé. Veuillez le choisir.");
			}
			else {
				JOptionPane.showMessageDialog(null, "Le dictionnaire a été chargé avec succès.");
			}
		}  
	});
	
	// ActionListener de "Verifier".
	verifier.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e7){
			
			// Si le dictionnaire n'a pas été choisi:
			if (dico.getText().length()==0) {
				JOptionPane.showMessageDialog(null, "Le dictionnaire n'a pas été chargé avec succès.");
			}
			else {
			
			// Initialisation de l'Highlighter.
			DefaultHighlighter.DefaultHighlightPainter myHighlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.red);
			Highlighter hilite = text.getHighlighter();
	        Highlighter.Highlight[] hilites = hilite.getHighlights();
	        // Remove l'ancien highlight
	        for (int i=0; i<hilites.length; i++) {
	    		   hilite.removeHighlight(hilites[i]);  
	            }
	        
	        // Récupération du dictionnaire et du TextArea sauvgardés, respectivement,
	        // dans les JTextArea "text" et "dico".
			String txtString = text.getText();
			String dicString = dico.getText();
			
			// Creation de 2 ArrayLists à partir du dico: 
			ArrayList<String> myList = new ArrayList<String>(Arrays.asList(txtString.split("\\s")));   // Sépare chaque mot
			ArrayList<String> myListPos = new ArrayList<String>(Arrays.asList(txtString.split("\n"))); // Sépar chaque ligne (pour le row)
			
			// Variable permettant de savoir si la liste de mot à corriger "corrList" a deja été créer par "Verifier".
			boolean isVerified=false;  
			if(corrList.getText().length()!=0) {isVerified=true;}

			// Variable que l'on utilisera pour retrouver l'index du mot à corriger.
			// (il est négatif pour ne pas interferer avec les autres valeur de position)
			int corrIndex= -1;
			// Variable qui calcul le nombre de lignes entre un mot "s" et le debut du TextArea.
			int nbLignes=0;
			// Variable qui donne le premier index (column) du mot "s" a corriger.
			int pos=0;
			// Variable qui calcule la somme des longueur des lignes jusqu'à la ligne contenant "s"
			int diff=0;
			// Si le dictionnaire n'a pas été choisi
			if (dicString != null){
				
				for(String s: myList){ // Pour chaque mot du TextArea:
					
					if (dicString.contains("\n"+s+"\n")==false){
						corrIndex-= 1;  
						if(isVerified==false) {corrList.append(s+" ");}  // voir si il est correct, si oui le sauvgarder dans corrList
						String corrIndexS=String.valueOf(corrIndex);  // Convertir corrIndex en String   
						
						for (String m: myListPos){   // Pour chaque ligne du textArea
							if (m.contains(s)==true) {      // si el contient le mot "s" incorrect:
								nbLignes=myListPos.indexOf(m);  // donner la valeur de nbLigne
								position.append(corrIndexS+" "+ String.valueOf(nbLignes)+" "); // sauvgarder l'index et le row dans "position"
								break; // sortir de la boucle 
							}
						}
						try {
							
				            Document doc = text.getDocument();
				            String textIndex = doc.getText(0, doc.getLength());

					            pos=textIndex.indexOf(s);  // trouver la position de "s" dans le TextArea

					            int var=0;
					            for (int i = 0; i < nbLignes; i++) {
					            	var+=myListPos.get(i).length();
					            	diff=var+nbLignes; // trouver la difference a soustraire à "pos" pour obtenir la column
					            }

				            	
					            position.append(String.valueOf(pos-diff)+" "); // sauvgarder le premier index(column) dans position
					            
					            hilite.addHighlight(pos, pos+s.length(), myHighlightPainter); //highlight la zone
					            position.append(String.valueOf(pos+s.length()-diff)+" ");  // sauvgarder le dernier index(column) dans position
					            


						} catch (BadLocationException e4) {
				        }	
						}

					}
				

				}
			

		
			}
		}
	});
	
	// fonction ouvrir qui permet de faire chargé un fichier dans 
	// le TextArea
	ouvrir.addActionListener(new ActionListener(){  
		public void actionPerformed(ActionEvent e8){

			JFileChooser fileChooser = new JFileChooser();
			int reponse  = fileChooser.showSaveDialog(null);
            if (reponse == JFileChooser.APPROVE_OPTION){
            	File file = new File (fileChooser.getSelectedFile().getAbsolutePath());                  
                Path path = file.toPath();
                try {
                	String res = Files.readString(path);
                	text.setText(res);
        			p.add(scroll);
        			f.setVisible(true);
        			JOptionPane.showMessageDialog(null, "Le fichier a été chargé avec succès.");
        			}
                    catch (IOException e2) {
                    	e2.printStackTrace();        
        		        }
                }		
			

		}
	});
	// button enregistre que permet d'enregistrer le contenu du textArea dans un fichier
	// nomé enrisgtre.txt.
	enregistrer.addActionListener(new ActionListener(){  
        public void actionPerformed(ActionEvent e9){

            File file = new File ("src/components/enregistre.txt");
            if(e9.getSource() == "enregistrer");
            {
                try {
                     FileWriter ecrire = new FileWriter(file);
                     BufferedWriter bw = new BufferedWriter(ecrire);
                     bw.write(text.getText());
                     bw.close();
                     ecrire.close();
                 }catch(IOException e1) {
                     e1.printStackTrace();
                 }
            }
       
         

        }
       });
	
	// fonction sur le boutton retour que permet de retourner a 
	// l'interface de base
	retour.addActionListener(new ActionListener(){  
		public void actionPerformed(ActionEvent e10) throws IllegalStateException{

			f.setVisible(false);
			p.removeAll();
			p.add(fichier,FlowLayout.LEFT);
			p.add(dictionnaire,FlowLayout.CENTER);
			p.add(verifier,FlowLayout.RIGHT);
		    p.add(text);
		    f.add(p);
		    f.setVisible(true);


		}  
	});
	
	// fonction capter la position du clique fait par l'utilisateur
	// et la comparer avec la postion du mot inconnu.
	// puis générer le menu déroulant de 5 mots proche du mot inconnu.
    text.addCaretListener( new CaretListener()
    {
        public void caretUpdate(CaretEvent e11) throws IllegalStateException{
            
            int caretPosition = text.getCaretPosition();
            Element root = text.getDocument().getDefaultRootElement();
            int row = root.getElementIndex( caretPosition );
            int column = caretPosition - root.getElement( row ).getStartOffset();

            ArrayList<Integer> tabPos = new ArrayList<Integer>();
            String txtPos = position.getText();
 
            ArrayList<String> divTab = new ArrayList<String>(Arrays.asList(txtPos.split(" ")));
            if (divTab.contains("")) {divTab.remove("");}
            for (String str: divTab ) {
            	tabPos.add(Integer.parseInt(str));
            }


            int tempRow=-1;
            int tempBeg=-1;
            int tempEnd=-1;
            int index=0;
            int count=0;
            String motFaux="";
            for (Integer i: tabPos) {
            	if (motFaux!="") {break;}

            	
            	if (i<=-2) {
            		index=(i*-1)-2;
            		count++;
            		continue;
            	}
            	if (count==1) {
            		tempRow=i;
            		count++;
            		continue;
            	}
            	
            	if (count==2) {
            		tempBeg=i;
            		count++;
            		continue;
            	}
            	
            	if (count==3) {
            		tempEnd=i;
            		count=0;
            	}
        
            		
            	if (row==tempRow && column>tempBeg && column<tempEnd) {
            		
            		String corr = corrList.getText();
            		ArrayList<String> motsCorr = new ArrayList<String>(Arrays.asList(corr.split("\\s")));
            		

            		motFaux = motsCorr.get(index);


            		ArrayList<String> optCorr= correction(motFaux, dico.getText());

            		String x=isClicked.getText();

            		if (x.equals(motFaux)==false) {
            		String[] optionsToChoose = {optCorr.get(0), optCorr.get(1), optCorr.get(2), optCorr.get(3), optCorr.get(4)};
            		String motCorrige = (String) JOptionPane.showInputDialog(
            		            null,
            		            "Choissisez une correction:",
            		            "Correcteur",
            		            JOptionPane.QUESTION_MESSAGE,
            		            null,
            		            optionsToChoose,
            		            optionsToChoose[0]);
            		
            		isClicked.setText(motFaux);

            		
            		String posText=text.getText();

            		int posBeg=posText.indexOf(motFaux);
            		int posEnd=posText.indexOf(motFaux)+motFaux.length();

            		text.replaceRange(motCorrige, posBeg,posEnd);
            		}
            		//text.replaceRange(motCorrige, posBeg,posEnd);



            		verifier.doClick();
            		
    				
    				break;
            		
    			} 

				

                		
            	} 		

        	} 

    	 
    });
	
	// paramètrage de l'interdface graphique
	f.setSize(800,500);
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setResizable(false);
	f.setVisible(true);
	
	
	}
	
	// fonction permettant de choisir son dictionnaire
	public static String createString() {
		
		String dicString = new String();				// dictionnaire sous forme de string
		JFileChooser fileChooser = new JFileChooser();	
        
        int reponse  = fileChooser.showSaveDialog(null);
        if (reponse == JFileChooser.APPROVE_OPTION) {
        	File file = new File (fileChooser.getSelectedFile().getAbsolutePath());   //choix du dictionnaire               
            Path path = file.toPath();
    	    try {
    			dicString = Files.readString(path);
    			 
    			}
    		    catch (IOException e3) {
    				e3.printStackTrace();
    				}
    	    }
        return dicString;
		
	}
	
	// L'algorithme de levenshtein importé du sujet 
	// Il permet de calculé la différence entre deux mots.
	// Dans notre cas la distance entre le mot inconnu et le dictionnaire
	public static int distance(String mot, String dico){
	     int edits[][]=new int[mot.length()+1][dico.length()+1];
	     for(int i=0;i<=mot.length();i++)
	         edits[i][0]=i;
	     for(int j=1;j<=dico.length();j++)
	         edits[0][j]=j;
	     for(int i=1;i<=mot.length();i++){
	         for(int j=1;j<=dico.length();j++){
	             int u=(mot.charAt(i-1)==dico.charAt(j-1)?0:1);
	             edits[i][j]=Math.min(
	                             edits[i-1][j]+1,
	                             Math.min(
	                                edits[i][j-1]+1,
	                                edits[i-1][j-1]+u
	                             )
	                         );
	         }
	     }
	     return edits[mot.length()][dico.length()];
	}
	
	// fonction permettant de regrouper 5 mots ressemblant au disctionnaire
	// par rapport au mot inconnu
	public static ArrayList<String> correction(String mot, String dico) {
		

		ArrayList<String> dicList = new ArrayList<String>(Arrays.asList(dico.split("\n"))); //Liste du dictionnaire
		ArrayList<String> motList = new ArrayList<String>();	// ArrayList stockant les 5 mots
		int var=1;
		
		// condition pour prendre uniquement 5 mots
		while (motList.size()!=5) {							
			
		
		for (String dicMot: dicList){
			
			if (distance(mot, dicMot)==var) { 

				motList.add(dicMot);
				if (motList.size()==5) {
					break;
				}
			}
			}
		var++;
		}
		return motList; // retourner la liste de mot ressemblant
	}
	
}