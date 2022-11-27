
package components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.StringIndexOutOfBoundsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;



public class Brouillon {

	public static void main (String[] args) {
		
		
	JFrame f = new JFrame("Tp");

	JPanel p = new JPanel();
	JButton fichier = new JButton("Fichier"); 
	p.add(fichier,FlowLayout.LEFT);
	JButton dictionnaire = new JButton("Dictionnaire");
	p.add(dictionnaire,FlowLayout.CENTER);
	JButton verifier = new JButton("Vérifier");
	p.add(verifier,FlowLayout.RIGHT);
	
	JTextArea corrList = new JTextArea();
	JTextArea dico = new JTextArea();
	JTextArea position = new JTextArea();
	JTextArea text = new JTextArea(25,50);
	text.setLineWrap(true);
	JScrollPane scroll = new JScrollPane(text);
    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    p.add(scroll);
    f.add(p);
    
	JButton ouvrir = new JButton("Ouvrir");
	JButton enregistrer = new JButton("Enregistrer");
	JButton retour = new JButton("Retour");
	
    
	fichier.addActionListener(new ActionListener(){  
		public void actionPerformed(ActionEvent e){
			f.setVisible(false);
			p.removeAll();
			p.add(ouvrir,FlowLayout.LEFT);
			p.add(enregistrer,FlowLayout.CENTER);
			p.add(retour,FlowLayout.RIGHT);
		    p.add(scroll);
		    f.setVisible(true);
		    
		    
		}  
	});
	
	dictionnaire.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			String dicString = createString();
			JOptionPane.showMessageDialog(null, "Le dictionnaire a été chargé avec succès.");
			dico.setText(dicString);

		}  
	});
	 
	
	verifier.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			
			position.setText("");
			DefaultHighlighter.DefaultHighlightPainter myHighlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.red);

			Highlighter hilite = text.getHighlighter();
			
	        Highlighter.Highlight[] hilites = hilite.getHighlights();
	        for (int i=0; i<hilites.length; i++) {
	    		   hilite.removeHighlight(hilites[i]);
	                
	            }
			String txtString = text.getText();
			String dicString = dico.getText();
			ArrayList<String> myList = new ArrayList<String>(Arrays.asList(txtString.split("\\s")));
			ArrayList<String> myListPos = new ArrayList<String>(Arrays.asList(txtString.split("\n")));
			


			int corrIndex= -1;
			int nbLignes=0;
			int pos=0;
			int diff=0;
			if (dicString != null){
				for(String s: myList){
					
					if (dicString.contains(s)==false){
						corrIndex-= 1;
						corrList.append(s+" ");
						String corrIndexS=String.valueOf(corrIndex);
						for (String m: myListPos){
							
							if (m.contains(s)==true) {
								nbLignes=myListPos.indexOf(m);
								position.append(corrIndexS+" "+ String.valueOf(nbLignes)+" ");
								break;
							}
						}
						try {
				            Document doc = text.getDocument();
				            String textIndex = doc.getText(0, doc.getLength());

					            pos=textIndex.indexOf(s);

					            int var=0;
					            for (int i = 0; i < nbLignes; i++) {
					            	var+=myListPos.get(i).length();
					            	diff=var+nbLignes;
					            }

				            	System.out.println("pos:"+pos+"-var:"+var+"-nbLignes:"+nbLignes);
								System.out.println("res1="+(pos-diff));	
					            position.append(String.valueOf(pos-diff)+" ");
					            
					            hilite.addHighlight(pos, pos+s.length(), myHighlightPainter);
					            position.append(String.valueOf(pos+s.length()-diff)+" ");
					            

						} catch (BadLocationException e3) {
				        }	
						}
					}
				}
		}  
	});
	

	ouvrir.addActionListener(new ActionListener(){  
		public void actionPerformed(ActionEvent e){
			
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
	
	enregistrer.addActionListener(new ActionListener(){  
        public void actionPerformed(ActionEvent e){
            File file = new File ("src/components/enregistre.txt");
            if(e.getSource() == "enregistrer");
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
	
	retour.addActionListener(new ActionListener(){  
		public void actionPerformed(ActionEvent e){
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
	
	
    text.addCaretListener( new CaretListener()
    {
        public void caretUpdate(CaretEvent e) throws StringIndexOutOfBoundsException
        {
            int caretPosition = text.getCaretPosition();
            Element root = text.getDocument().getDefaultRootElement();
            int row = root.getElementIndex( caretPosition );
            int column = caretPosition - root.getElement( row ).getStartOffset();
            System.out.println( "Row   : " + ( row ) );
            System.out.println( "Column: " + ( column ) );
            
            ArrayList<Integer> tabPos = new ArrayList<Integer>();
            String txtPos = position.getText();
            ArrayList<String> divTab = new ArrayList<String>(Arrays.asList(txtPos.split(" ")));
            divTab.remove("");
            for (String str: divTab ) {

            	tabPos.add(Integer.parseInt(str));

            }
            System.out.println(tabPos);
            int tempRow=-1;
            int tempBeg=-1;
            int tempEnd=-1;
            int index=0;
            int count=0;

            for (Integer i: tabPos) {
            	
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
            		

            		String motFaux = motsCorr.get(index);

            		ArrayList<String> optCorr= correction(motFaux, dico.getText());
            		System.out.println("index:"+index+"-mot:"+motFaux);
            		System.out.println(correction(motFaux, dico.getText()));


            			
            		String[] optionsToChoose = {optCorr.get(0), optCorr.get(1), optCorr.get(2), optCorr.get(3), optCorr.get(4)};
            		String motCorrige = (String) JOptionPane.showInputDialog(
            		            null,
            		            "Choissisez une correction:",
            		            "Correcteur",
            		            JOptionPane.QUESTION_MESSAGE,
            		            null,
            		            optionsToChoose,
            		            optionsToChoose[0]);
            		f.setVisible(false);
            		
            		String posText=text.getText();
            		int posBeg=posText.indexOf(motFaux);
            		int posEnd=posText.indexOf(motFaux)+motFaux.length();
            		f.setVisible(false);
            		text.replaceRange(motCorrige, posBeg,posEnd);
            		f.setVisible(true);

            		
            		break;
            		
                		
            	} 		
                	
        	} 
    	} 
    });
	
	
	f.setSize(800,500);
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setResizable(false);
	f.setVisible(true);
	
	
	}
	
	public static String createString() {
		
		String dicString = new String();		
		JFileChooser fileChooser = new JFileChooser();
        
        int reponse  = fileChooser.showSaveDialog(null);
        if (reponse == JFileChooser.APPROVE_OPTION) {
        	File file = new File (fileChooser.getSelectedFile().getAbsolutePath());                  
            Path path = file.toPath();
    	    try {
    			dicString = Files.readString(path);
    			 
    			}
    		    catch (IOException e2) {
    				e2.printStackTrace();
    				}
    	    }
        return dicString;
		
	}
	

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
	
	public static ArrayList<String> correction(String mot, String dico) {
		

		ArrayList<String> dicList = new ArrayList<String>(Arrays.asList(dico.split("\n")));
		int var=1;
		ArrayList<String> motList = new ArrayList<String>();
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
		return motList;
	}
}


// il faut utiliser addCaretListener pour detecter le position de la souris lors d'un click 
//puis utiliser un if(condition) pour faire pop-up la fenetre d'option correspondant
// au mot se trouvant a la position selectionne
