import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
 
public class Fenetre extends JFrame {
	private JMenuBar menuBar = new JMenuBar();	
	private JMenu fichier = new JMenu("Fichier");	
	private JMenuItem ouvrir = new JMenuItem("Ouvrir");
	private JMenuItem quitter = new JMenuItem("Quitter");	
	private JMenu accederInformation= new JMenu("Acceder a une information");
	private JMenuItem utilitaire = new JMenuItem("Utilitaire");
	private JMenuItem maison = new JMenuItem("Maison");	
	private JMenuItem tout_afficher = new JMenuItem("Tout Afficher");
	private JMenu quitance= new JMenu ("Quittance de Loyer");
	private JMenuItem remplir_quitance = new JMenuItem("Remplir quitance de loyer");
	private JMenuItem envoyer_quitance = new JMenuItem("Envoyer quitance de loyer");
	private Connect con;
	private JLabel res_final=new JLabel();
	private JPanel container = new JPanel();
	private JScrollPane scroll=new JScrollPane();
	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	Dimension dim =new Dimension(1300,650);
	private JTable tableau;
	private int is_array_here;
	private int num_column_title;
	private int num_appartment=0;
	private String utilitaire_array[];
	private String maison_array[];
	
	public Fenetre(Connect _con) throws SQLException{        
		con=_con;
		this.setTitle("Gestion a distance");
		this.setSize(300, 450);
		this.setLocationRelativeTo(null);               
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		is_array_here=0;
		ResultSetMetaData resultMeta;
		Statement state = con.getConn().createStatement();
		ResultSet result = state.executeQuery("SELECT * FROM \"Appartement\"");
	   	resultMeta=result.getMetaData();
	   	Statement state1 = con.getConn().createStatement();
	   	ResultSet result1 = state1.executeQuery("SELECT \"Appartement\" FROM \"Appartement\"");
	   	
	  //get the number of columns
	   	num_column_title=resultMeta.getColumnCount();
	   	
	   	// Get all the title of the utilitaires and put it in the array utilitaire_array
	   	utilitaire_array=new String[num_column_title-1];
	    for(int k=2;k<=num_column_title;k++){
	    	utilitaire_array[k-2]=resultMeta.getColumnName(k);
	    }
	    
	  //get the number of appartments
	   	while(result.next()){ 
	   		num_appartment++;
	    }
	   	
	 // Put all the appartment in the array maison
	   	maison_array=new String [num_appartment];
	    int i=0;
	    while(result1.next()){ 
	    	maison_array[i]=result1.getObject(1).toString();
	    	i++;
	    }
	    
	    tableau=new JTable();
	    
		// set up the menus
		initMenu();
	    this.setContentPane(container);
		this.setVisible(true);
		state.close();
		state1.close();
		result.close();
		result1.close();
	}
	
	void initMenu()
	{
		// set up the menus
		fichier.add(ouvrir);
		fichier.add(quitter);
		accederInformation.add(utilitaire);
		accederInformation.add(maison);
		accederInformation.add(tout_afficher);
		quitance.add(remplir_quitance);
		quitance.add(envoyer_quitance);
		menuBar.add(fichier);
		menuBar.add(accederInformation);
		menuBar.add(quitance);
		
		//Function of the menu quitter
		quitter.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent event){
	          System.exit(0);
	        }
	      });
		
		//Function of the menu utilitaire
		utilitaire.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					//Create the statements and the results
					Statement state;
					ResultSet result;

				    // Create the panel to choose the service
					JOptionPane jop = new JOptionPane();
					String service = (String)jop.showInputDialog(null,"Quel type de service desirez-vous ?","Utilitaire",JOptionPane.QUESTION_MESSAGE,
					      null,
					      utilitaire_array,
					      utilitaire_array[0]);	
				   	
					// Create the pannel to chose the apparement
					JOptionPane jop2 = new JOptionPane();
					String appart = (String)jop2.showInputDialog(null, 
					      "Pour quel appartement ?",
					      "Appartement",
					      JOptionPane.QUESTION_MESSAGE,
					      null,
					      maison_array,
					      maison_array[0]);
				   	
					//Look for the service chosen for the appartment chosen
					state = con.getConn().createStatement();
					String search="SELECT \""+service+"\" FROM \"Appartement\" WHERE \"Appartement\"='"+appart+"'";
				   	result = state.executeQuery(search);
				   	result.next();
				   	Object o = result.getObject(1);
				   	if (result.wasNull()) {
				   	  return;
				   	}
				   	//Display the result and erase the previous one displayed
				   	container.remove(scroll);
				   	System.out.println(result.getObject(1).toString());
				   	res_final.setText(result.getObject(1).toString());
				   	container.add(res_final);
				   	container.revalidate();
				   	container.repaint();
				   	
				   	//reset and close attributes
				   	is_array_here=0;
				   	state.close();
				   	result.close();
				}
				catch (Exception e1) {
					e1.printStackTrace();
				} 					    
			}				
		});
		
		//Function of the menu maison
		maison.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					//Create the statements and the results
					Statement state;
					ResultSet result;
					ResultSetMetaData resultMeta;
				    
				    //Create the panel
					JOptionPane jop = new JOptionPane();
				    String appart = (String)jop.showInputDialog(null, 
				      "Pour quel appartement souhaitez-vous acceder aux informations ?",
				      "Appartement",
				      JOptionPane.QUESTION_MESSAGE,
				      null,
				      maison_array,
				      maison_array[0]);	
				   	if(appart==null){
				   		return;
				   	}
				   	
				    // Select the line of the appartment selected and put the titles of the column
				    // in the array title and the result in the array result_fin
					state = con.getConn().createStatement();
					String search="SELECT * FROM \"Appartement\" WHERE \"Appartement\"='"+appart+"'";
				   	result = state.executeQuery(search);
				   	resultMeta=result.getMetaData();
				   	String result_fin [][] =new String [1][resultMeta.getColumnCount()];
				   	String title [] =new String [resultMeta.getColumnCount()];
				   	result.next();
				   	for(int j = 1; j <= resultMeta.getColumnCount(); j++)
				   	{
				   		title[j-1]=resultMeta.getColumnName(j).toUpperCase();
				   		result.getObject(j);
				   		if(!result.wasNull()){
				   			result_fin[0][j-1]=result.getObject(j).toString();
				   		}
				   		else{
				   			result_fin[0][j-1]="";
				   		}
				   	}
				   	
				   	// If there is already an array, delete it
				   	if(is_array_here==1)
				   	{
				   		container.remove(scroll);
				   	}
				   	
				   	//Indicate that there is an array
				   	is_array_here=1;
				   	
				   	//Construct the array to display and add it to the container
				   	tableau=new JTable(result_fin,title);    					
				   	for(int i=0;i<resultMeta.getColumnCount();i++){
				   		tableau.getColumn(title[i]).setCellRenderer(new JLabelRenderer());
				   		tableau.getColumn(title[i]).setCellEditor(new JLabelRenderer());
				   	}
				   	tableau.setPreferredScrollableViewportSize(dim);
				    for(int l = 0; l < tableau.getRowCount(); l++){
				        //On affecte la taille de la ligne à l'indice spécifié !
				          tableau.setRowHeight(l,80);
				        
				    }
				   	scroll=new JScrollPane(tableau);
				   	container.add(scroll);	
				   	
				   	//Remove the label
				   	container.remove(res_final);
				   	container.revalidate();
				   	container.repaint();
				   	
				   	//close attributes
				   	state.close();
				   	result.close();
				}
				catch(Exception e1){
					e1.printStackTrace();
				}
			}	
		});
		
		//Function of the menu maison
		tout_afficher.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					//Create the statements and the results
					Statement state;
					ResultSet result;
					ResultSetMetaData resultMeta;
				    
				    //get all the lines of the table
					state = con.getConn().createStatement();
				   	result = state.executeQuery("SELECT * FROM \"Appartement\"");
				   	resultMeta=result.getMetaData();
				   	
				   	//put the titles of the columns in the array title and the lines in result_fin
				   	String result_fin_text [][] =new String [num_appartment][resultMeta.getColumnCount()];
				   	String title [] =new String [resultMeta.getColumnCount()];
				   	int k=0;
				   	while(result.next()){
					   	for(int j = 1; j <= resultMeta.getColumnCount(); j++)
					   	{
					   		title[j-1]=resultMeta.getColumnName(j).toUpperCase();
					   		result.getObject(j);
					   		if(!result.wasNull()){
					   			result_fin_text[k][j-1]=result.getObject(j).toString();
					   		}
					   		else{
					   			result_fin_text[k][j-1]="";
					   		}
					   	}
					   	k++;
				   	}
				   	
				   	// Remove an eventually existing table
				   	if(is_array_here==1)
				   	{
				   		container.remove(scroll);
				   	}
				   	
				   	//Indicate the creation of a table
				   	is_array_here=1;
				   	
				   	//Create the table
				   	Zmodel model = new Zmodel(result_fin_text,title);
				   	tableau=new JTable(model);	
				   	for(int i=0;i<resultMeta.getColumnCount();i++){
				   		tableau.getColumn(title[i]).setCellRenderer(new JLabelRenderer());
				   		tableau.getColumn(title[i]).setCellEditor(new JLabelRenderer());
				   	}
				   	
				    for(int l = 0; l < tableau.getRowCount(); l++){
				        //On affecte la taille de la ligne à l'indice spécifié !
				          tableau.setRowHeight(l,70);
				        
				    }
				   	scroll=new JScrollPane(tableau);
				   	tableau.setPreferredScrollableViewportSize(dim);
				   	container.add(scroll);	
				   	container.remove(res_final);
				   	container.revalidate();
				   	container.repaint();
				   	
				   	//close attributes
				   	state.close();
				   	result.close();
				}
				catch(Exception e1){
					e1.printStackTrace();
				}
			}	
		});
		
		//Function of the menu remplir quitance
		remplir_quitance.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
			    //Create the panel
				JOptionPane jop = new JOptionPane();
			    String appart = (String)jop.showInputDialog(null, 
			      "Pour quel appartement souhaitez-vous acceder aux informations ?",
			      "Appartement",
			      JOptionPane.QUESTION_MESSAGE,
			      null,
			      maison_array,
			      maison_array[0]);	
			   	if(appart==null){
			   		return;
			   	}
				Desktop desktop = Desktop.getDesktop();
				File file = new File("C:/Users/User/Desktop/QuitanceLoyer/"+appart+".docx");
		        if(file.exists()){ 
		        	try {
						desktop.open(file);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		        }
		        else{
		        	System.out.println("hahahaha");
		        }
			}
			
		});
		
		envoyer_quitance.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try{
					Statement state;
					ResultSet result;
				    //Create the panel
					JOptionPane jop = new JOptionPane();
				    String appart = (String)jop.showInputDialog(null, 
				      "Pour quel appartement souhaitez-vous acceder aux informations ?",
				      "Appartement",
				      JOptionPane.QUESTION_MESSAGE,
				      null,
				      maison_array,
				      maison_array[0]);	
				   	if(appart==null){
				   		return;
				   	}
				   	String email_array[]={"xxx@wanadoo.fr","xxx.fr"};
					JOptionPane jop2 = new JOptionPane();
				    String email = (String)jop2.showInputDialog(null, 
				      "Depuis quelle boite mail ?",
				      "Email",
				      JOptionPane.QUESTION_MESSAGE,
				      null,
				      email_array,
				      email_array[0]);	
				   	if(email==null){
				   		return;
				   	}
					state = con.getConn().createStatement();
				   	result = state.executeQuery("SELECT \"Email\" FROM \"Appartement\" Where \"Appartement\"='"+appart+"'");
				   	result.next();
				   	String mail_to=result.getObject(1).toString();
					String findStr = "<br>";
					int lastIndex = 0;
					int count = 1;
					while(lastIndex != -1){

					    lastIndex = mail_to.indexOf(findStr,lastIndex);

					    if(lastIndex != -1){
					        count ++;
					        lastIndex += findStr.length();
					    }
					}
					String bis=mail_to.substring(6);
					String email_dest[]=new String[count];
					for(int i=0;i<count;i++)
					{
						for(int j=0;j<bis.length()-5;j++){
							if(bis.substring(j, j+4).compareTo(findStr)==0)
							{
								email_dest[i]=bis.substring(0,j);
								bis=bis.substring(j+4);
								break;
							}
							else if(bis.substring(j, j+7).compareTo("</html>")==0)
							{
								email_dest[i]=bis.substring(0,j);
								bis=bis.substring(j+4);
								break;
							}
						}
					}
					
				   	String subject;
				   	String body;
				    
				    JPanel panSujet = new JPanel();
				    panSujet.setPreferredSize(new Dimension(300, 300));
				    JTextField sujet = new JTextField();
				    sujet.setPreferredSize(new Dimension(200, 25));
				    panSujet.setBorder(BorderFactory.createTitledBorder("Sujet"));
				    JLabel sujetLabel = new JLabel("Corps du sujet :");
				    JLabel emailLabel = new JLabel("Pour quel(s) locataire(s) de l'appartement:");
				    JCheckBox email_to []= new JCheckBox[email_dest.length];
				    panSujet.add(emailLabel);
				    for(int i=0;i<email_dest.length;i++){
				    	email_to[i]=new JCheckBox(email_dest[i]);
				    	panSujet.add(email_to[i]);
				    }
				    panSujet.add(sujetLabel);
				    panSujet.add(sujet);
				    
				    JPanel panMessage = new JPanel();
				    panSujet.setPreferredSize(new Dimension(300, 300));
				    JTextArea message = new JTextArea();
				    message.setPreferredSize(new Dimension(250, 250));
				    panMessage.setBorder(BorderFactory.createTitledBorder("Message"));
				    JLabel messageLabel = new JLabel("Corps du message :");
				    panMessage.add(messageLabel);
				    panMessage.add(message);
				    
				    JPanel content = new JPanel();
				    content.add(panSujet);
				    content.add(panMessage);
				    
				    int button = JOptionPane.showConfirmDialog(null, content, 
				               "Contenu du message", JOptionPane.OK_CANCEL_OPTION);
				    if (button == JOptionPane.OK_OPTION) {
				         subject=sujet.getText();
				         body=message.getText();
				    }
				    else
				    {
				    	return;
				    }
					File file = new File("C:/Users/User/Desktop/QuitanceLoyer/"+appart+".docx");
			        if(file.exists()){ 

						for(int i=0;i<email_dest.length;i++)
						{				
							if(email_to[i].isSelected()){
								System.out.println(email_dest[i]);
								SendMess.sendMess(subject,body,email,email_dest[i], file.getAbsolutePath());
							}
						}
			        }
			        else{
			        	System.out.println("File does not exist");
			        }
				}
				catch(Exception e1){
					e1.printStackTrace();
				}
			}
		});
		this.setJMenuBar(menuBar);
	}
} 