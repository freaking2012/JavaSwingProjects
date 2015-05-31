import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Random;
import java.util.jar.Attributes.Name;

import javax.swing.*;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
public class Combine_Files extends JPanel {

	JFrame frame;
	JButton btnBrowse,btnCombine,btnOpen;
	JTextField txtFolderPath,txtFilesInfo,txtOutputFile,txtSeparator;
	ButtonGroup bg,bg1;
	JRadioButton r1,r2,r3,r4,r5;
	Task task;
	File[] fileList;
	File dir;
	int fileNumber;
	JTextArea taskOutput;
	JScrollPane scrollpane;
	JPanel panel;
	JProgressBar progressBar;
	JLabel label2,label3,label4,lblCredits;
	boolean flagError;
	String errorMessage;
	int noOfFiles;
	String[] files;
	FileInputStream fileIn;
	
	public static void main(String[] args) throws IOException {
		final Combine_Files window = new Combine_Files();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private Combine_Files() throws IOException {
		initialize();
	}	

	class Task extends SwingWorker<Void,Void>
	{

		@Override
		protected Void doInBackground() throws Exception {
			
			flagError=false;
			progressBar.setValue(0);

			if(txtFolderPath.getText().equals(""))	
			{
				flagError=true;
				errorMessage="Folder name not spcefied.";
				return null;
			}
			
			if(txtFilesInfo.getText().equals(""))	
			{
				flagError=true;
				errorMessage="File extension or input file names not specified";
				return null;
			}
			
			if(txtOutputFile.getText().equals(""))	
			{
				flagError=true;
				errorMessage="Output file name not specified";
				return null;
			}
			
				dir = new File(txtFolderPath.getText());
				System.out.println(dir.toString());
				if(r1.isSelected())
				{		
			
			 fileList = dir.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					// TODO Auto-generated method stub
					//return Name.EXTENSION_NAME(txtFilesInfo.getText());
					return name.endsWith("."+txtFilesInfo.getText());
				}
			});
			 System.out.println("Output File: "+txtOutputFile.getText().toString());
			for(int i=0;i<fileList.length;i++)
			{
				String file = fileList[i].toString();
				String fileName = file.substring(file.lastIndexOf("\\")+1);
				System.out.println("Merged File: "+fileName);				
				//System.out.println(fileList[i]);
				if(fileName.equals(txtOutputFile.getText().toString()))
				{
					
					flagError=true;
					errorMessage="File with specified output filename already exists.";
					break;
				}
				
				
			}
				if(fileList.length==0)
				{
					flagError=true;
					errorMessage="No file with this extension is present in the folder";
				
				}
				noOfFiles = fileList.length;
				}
				else if(r2.isSelected())
				{
				files = txtFilesInfo.getText().toString().split(",");
				noOfFiles = files.length;
				System.out.println(noOfFiles);
				
				for(int k=0;k<noOfFiles;k++)
				{
				File f = new File(dir+"\\"+files[k].toString());
				
					if(!(f.exists() && !f.isDirectory()))
					{
						
					
						flagError=true;
						errorMessage="File named "+files[k]+" doesn't exists.\nCheck if file extension is given.";
					}						
				}
			
				}
				
			
			if(!flagError)
			{
			taskOutput.setText("");
			FileOutputStream fileOut = null;
			try {
				
				fileOut = new FileOutputStream(dir+"\\"+txtOutputFile.getText());			
				
				int data = 0;

				if(r5.isSelected())
				{
					if(txtSeparator.getText().equalsIgnoreCase(""))
					{
						flagError=true;
						errorMessage="Custom error message cannot be left Blank.\nYou may choose 'None' option";
						return null;
					}
				}
				
				for(fileNumber=0;fileNumber<noOfFiles;fileNumber++)
				{
					
					if(r1.isSelected())
					{
						
						fileIn = new FileInputStream(fileList[fileNumber].toString());
						System.out.println(fileList[fileNumber].toString());
					}
					else if(r2.isSelected())
					{
						
						File f = new File(dir+"\\"+files[fileNumber].toString());
						
						if(f.exists() && !f.isDirectory())
						{
							fileIn = new FileInputStream(dir+"\\"+files[fileNumber].toString());
						}
						else
						{
							flagError=true;
							errorMessage="File named "+files[fileNumber]+" doesn't exists.";
						}						
						System.out.println(files[fileNumber].toString());
						
					}
						
					if(!flagError)
					{
						
						btnCombine.setEnabled(false);
						progressBar.setVisible(true);
						scrollpane.setVisible(true);
						
						setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
						double width2 = screenSize.getWidth();		
						
						
								
						lblCredits.setBounds(115, 545, 250, 25);
						panel.setBounds(0, 550, 450, 15);
						frame.setBounds((int)(width2/2)-450/2, 100, 450, 590);
						
						
						
					}

					while((data=fileIn.read())!=-1)
					{

						fileOut.write(data);
						
					}
					
					if(r4.isSelected())
					{
						
						fileOut.write("\n".getBytes());
					}
					if(r5.isSelected())
					{
						
						fileOut.write(txtSeparator.getText().getBytes());
						fileOut.write("\n".getBytes());
					}
					
					
					if(fileIn!=null)
						fileIn.close();	
					int j=fileNumber+1;
					String fileName="";
					if(r1.isSelected())
					{
					String file = fileList[fileNumber].toString();
					fileName = file.substring(file.lastIndexOf("\\")+1);
					}
					else if(r2.isSelected())
					fileName=files[fileNumber];					
					int progress=((fileNumber+1)*100)/noOfFiles;					
		            progressBar.setValue(progress);
		            taskOutput.append(j + ". " +fileName+" merged successfully\n");
		            taskOutput.setCaretPosition(taskOutput.getDocument().getLength());
				}
			}catch(Exception e)
			{
				System.out.println("Exception caught while merging files: "+e);
			}

				if(fileOut!=null)
					fileOut.close();
			}
			return null;
		}
		
		@SuppressWarnings("deprecation")
		public void done()
		{
			frame.setCursor(Cursor.DEFAULT_CURSOR);
			
			if(!flagError)
			{
			progressBar.setValue(100);
			taskOutput.append("---------------------------------------------------\n");
			taskOutput.append( fileNumber-- + " Files Merged Successfully");
			//System.out.println(fileNumber + " files merged successfully");
			Toolkit.getDefaultToolkit().beep();
			
			btnCombine.setEnabled(true);
			JOptionPane.showMessageDialog(null, "All files merged successfully.", "Merge Files" , JOptionPane.INFORMATION_MESSAGE);
			btnOpen.setVisible(true);
			}
			else
				JOptionPane.showMessageDialog(null, errorMessage, "Error" , JOptionPane.ERROR_MESSAGE);
				
			//label4.setText("Files Merged Successfully");
		}
		
	}
	
	private void initialize() throws IOException {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			System.out.println("Exception : "+ e1);
		}
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		frame = new JFrame();		
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds((int)(width/2)-450/2, 100, 450, 410);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("Merge Files (Beta Version)");		
		frame.setResizable(false);
		frame.setLayout(null);
		frame.getContentPane().setBackground(Color.getHSBColor(65, 86, 60));

		final JLabel label1 = new JLabel("Select Folder");
		label1.setBounds(20, 25, 200, 25);
		//label1.setFont(new Font(verdana, Font.PLAIN, 13));
		label1.setFont(new Font(null, Font.BOLD, 13));
		frame.getContentPane().add(label1);
		label1.setForeground(Color.BLACK);
		
		

		btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(330, 20, 89, 25);
		frame.getContentPane().add(btnBrowse);		

		txtFolderPath = new JTextField();
		txtFolderPath.setBounds(20, 55, 400, 25);
		frame.getContentPane().add(txtFolderPath);

		bg = new ButtonGroup();

		r1 = new JRadioButton("Enter File Extension");
		bg.add(r1);
		r1.setBackground(Color.getHSBColor(65, 86, 60));
		r1.setEnabled(true);
		r1.setBounds(20, 100, 170, 23);
		r1.setFont(new Font(null, Font.BOLD, 13));
		frame.getContentPane().add(r1);
		r1.setForeground(Color.BLACK);
		r1.setSelected(true);

		r2 = new JRadioButton("Enter Individual File Names");
		bg.add(r2);
		r2.setBackground(Color.getHSBColor(65, 86, 60));
		r2.setEnabled(true);
		r2.setFont(new Font(null, Font.BOLD, 13));
		r2.setBounds(220, 100, 200, 23);
		r2.setForeground(Color.BLACK);
		frame.getContentPane().add(r2);

		label2 = new JLabel("(Comma Separated)");
		label2.setBounds(305, 115, 120, 25);
		label2.setFont(new Font(null, Font.PLAIN, 12));
		frame.getContentPane().add(label2);

		txtFilesInfo = new JTextField();
		txtFilesInfo.setBounds(20, 140, 400, 25);
		frame.getContentPane().add(txtFilesInfo);
		txtFilesInfo.setColumns(10);

		label3 = new JLabel("Enter Output File Name");
		label3.setBounds(20, 180, 200, 25);
		label3.setFont(new Font(null, Font.BOLD, 13));
		frame.getContentPane().add(label3);
		label3.setForeground(Color.BLACK);
		
		txtOutputFile = new JTextField();
		txtOutputFile.setBounds(20, 210, 400, 25);
		frame.getContentPane().add(txtOutputFile);
		txtOutputFile.setColumns(10);

		
		label4 = new JLabel("Content-Separator");
		label4.setBounds(20, 250, 220, 25);
		label4.setFont(new Font(null, Font.BOLD, 13));
		frame.getContentPane().add(label4);
		label4.setForeground(Color.BLACK);
		
		txtSeparator = new JTextField();
		txtSeparator.setBounds(20, 280, 400, 25);
		frame.getContentPane().add(txtSeparator);
		txtSeparator.setColumns(10);
		txtSeparator.setEditable(false);
		
		bg1=new ButtonGroup();
		
		r3=new JRadioButton("None");
		bg1.add(r3);
		r3.setEnabled(true);
		r3.setBounds(160, 250, 60, 25);
		r3.setBackground(Color.getHSBColor(65, 86, 60));
		r3.setFont(new Font(null,Font.PLAIN,13));
		frame.getContentPane().add(r3);
		r3.setSelected(true);
		
		r4=new JRadioButton("Blank Line");
		bg1.add(r4);
		r4.setBounds(245, 250, 100, 25);
		r4.setBackground(Color.getHSBColor(65, 86, 60));
		r4.setFont(new Font(null,Font.PLAIN,13));
		frame.getContentPane().add(r4);
		r4.setEnabled(true);
		
		r5=new JRadioButton("Custom");
		bg1.add(r5);
		r5.setBounds(350, 250, 100, 25);
		r5.setBackground(Color.getHSBColor(65, 86, 60));
		r5.setFont(new Font(null,Font.PLAIN,13));
		frame.getContentPane().add(r5);
		r5.setEnabled(true);
		
		btnCombine = new JButton("Merge");
		btnCombine.setBounds(20, 330, 70, 25);
		frame.getContentPane().add(btnCombine);
		
		progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setBounds(100, 331, 220, 25);
        frame.getContentPane().add(progressBar);
        progressBar.setVisible(false);
        
        btnOpen = new JButton("Merged File");
        btnOpen.setBounds(330, 330, 90, 25);
		frame.getContentPane().add(btnOpen);
		btnOpen.setVisible(false);

		
		taskOutput = new JTextArea(5, 5);
        taskOutput.setEditable(false);
        
        lblCredits = new JLabel("Designed and Developed by Manu Mishra");
		lblCredits.setBounds(115, 365, 250, 25);
		lblCredits.setFont(new Font(null,Font.PLAIN,10));
		frame.getContentPane().add(lblCredits);
		lblCredits.setForeground(Color.WHITE);
		
        
        panel = new JPanel();
		panel.setBounds(0, 370, 450, 15);
		panel.setBackground(Color.GRAY);
		frame.getContentPane().add(panel);
        
		scrollpane = new JScrollPane(taskOutput);
		scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollpane.setBounds(20, 380, 400, 150);		
		frame.getContentPane().add(scrollpane);
		scrollpane.setVisible(false);
		
		
		r3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				txtSeparator.setEditable(false);
				
			}
		});

		
		
		r4.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				txtSeparator.setEditable(false);
				
			}
		});

		
		
		r5.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				txtSeparator.setEditable(true);
				
			}
		});
		
		btnBrowse.addActionListener(new ActionListener() {          
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("./../.."));
				chooser.setDialogTitle("Click On Open After Selecting The Folder");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);



				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					txtFolderPath.setText(chooser.getSelectedFile().toString());
					System.out.println("getSelectedFolder() : "+ chooser.getSelectedFile());
				} else {
					System.out.println("No Selection ");
				}
			}
		}); 
		
		
		

		btnCombine.addActionListener(new ActionListener() {          
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {

				frame.setCursor(Cursor.WAIT_CURSOR);
				btnOpen.setVisible(false);				
				task = new Task();
		        task.execute();
		       
				
					
				

			}
		}); 
		
		btnOpen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					File outputFile = new File(txtFolderPath.getText()+"//"+txtOutputFile.getText());
					Desktop.getDesktop().open(outputFile);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		 
		

	}
}
