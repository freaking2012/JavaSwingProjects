import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.AttributedCharacterIterator;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.Random;
import java.util.jar.Attributes.Name;

import javax.swing.*;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
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
import javax.swing.ScrollPaneConstants;
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
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.plaf.ToolBarUI;

public class Compare_Files {

	JFrame frame;
	int screenWidth,screenHeight;
	JLabel lblFile1, lblFile2, lblMatch, lblUnmatch, lblMatchCount, lblUnmatchCount,lblCredits;
	JTextField txtFile1,txtFile2;
	JTextArea txtMatch, txtUnmatch;
	JScrollPane scrollPane1, scrollPane2;
	JPanel panel;
	JProgressBar progressBar;
	JCheckBox chkSpace,chkCase;
	JButton btnBrowse1, btnBrowse2, btnCompare, btnOutput,btnStop;
	Task task;	
	File fileMatch, fileUnmatch1, fileUnmatch2,file1,file2;	
	String str[],dir,file1Name,file2Name, errorMessage;
	int matchCount, unmatchCount;
	long startTime, endTime, totalTime;
	boolean flagError;

	class Task extends SwingWorker<Void,Void>
	{


		private void copyFiles(File source, File dest) throws IOException {
			InputStream is = null;
			OutputStream os = null;	        
			try {
				is = new FileInputStream(source);
				os = new FileOutputStream(dest);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = is.read(buffer)) > 0) {
					os.write(buffer, 0, length);
				}
			} finally {
				is.close();
				os.close();
			}
		}

		public  int countLines(String filename) throws IOException {
			System.out.println("inside count");
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			int lines = 0;
			while (reader.readLine() != null) lines++;
			reader.close();
			return lines;
		}

		protected Void doInBackground() throws Exception {

			progressBar.setValue(0);
			txtMatch.setText("");
			txtUnmatch.setText("");
			matchCount=0;
			unmatchCount=0;
			flagError=false;

			if(txtFile1.getText().equals(""))
			{
				flagError=true;
				errorMessage="Please Select File1";
				return null;
			}

			else if(txtFile2.getText().equals(""))
			{
				flagError=true;
				errorMessage="Please Select File2";
				return null;
			}			

			file1 = new File(txtFile1.getText().toString());
			file2 = new File(txtFile2.getText().toString());			
			dir = file1.getParent();
			file1Name = file1.getName();
			file2Name = file2.getName();
			fileMatch = new File(dir+"\\Compare\\LinesCommonToBothFiles.txt");
			fileUnmatch1 = new File(dir+"\\Compare\\LinesOnlyIn_"+file1Name+".txt");
			fileUnmatch2 = new File(dir+"\\Compare\\LinesOnlyIn_"+file2Name+".txt");

			System.out.println("Before files");
			copyFiles(file2,fileUnmatch2);
			System.out.println("Before files1");

			if (!(new File(dir+"\\Compare").exists())) {				
				new File(dir+"\\Compare").mkdirs();
				System.out.println("Created Compare Directory");
			}

			System.out.println("Before files2");

			BufferedReader br1 = new BufferedReader(new FileReader(file1));
			BufferedReader br2= new BufferedReader(new FileReader(file2));;
			BufferedWriter bw1 = new BufferedWriter(new FileWriter(fileMatch));			
			BufferedWriter bw2 = new BufferedWriter(new FileWriter(fileUnmatch1));
			//BufferedWriter bw3 = new BufferedWriter(new FileWriter(fileUnmatch2));
			System.out.println("After files");
			String line1;
			String line2;
			int line1Count,line2Count,currentCount;
			System.out.println("After files1");
			line1Count=0;
			line2Count=0;

			while ((line1 = br1.readLine()) != null) {line1Count++;}


			while ((line1 = br2.readLine()) != null) {line2Count++;}


			System.out.println("line1Count: "+line1Count+" line2count: "+line2Count);
			currentCount=0;
			boolean flagMatch;

			br1 = new BufferedReader(new FileReader(file1));
			while ((line1 = br1.readLine()) != null) {
				flagMatch=false;
				System.out.println(line1.toString());
				currentCount++;

				if(line1.trim().isEmpty())
					continue;
				br2 = new BufferedReader(new FileReader(file2));

				while((line2 = br2.readLine())!=null){
					if(flagError) return null;

					if(chkSpace.isSelected())
					{
						line1=line1.trim();
						line2=line2.trim();
					}

					if(line2.isEmpty())
						continue;

					System.out.println("line2: "+line2.toString());

					if(chkCase.isSelected()&&(line1.charAt(0)==line2.charAt(0)))						
					{



						if(line1.equals(line2))
						{
							bw1.append(line1);
							bw1.newLine();
							flagMatch=true;
							System.out.println("match: " + line1);
							txtMatch.append(line1+"\n");
							matchCount++;
							lblMatchCount.setText("Total Count: "+matchCount);
							break;

						}

					} 	
					else if((!chkCase.isSelected())&&(line1.charAt(0)==line2.charAt(0)))
					{
						if(line1.compareToIgnoreCase(line2)==0)
						{
							bw1.append(line1);
							bw1.newLine();
							flagMatch=true;
							matchCount++;
							lblMatchCount.setText("Total Count: "+matchCount);
							txtMatch.append(line1+"\n");
							break;
						}
					}

					progressBar.setValue((currentCount*100)/line1Count);
					txtMatch.setCaretPosition(txtMatch.getDocument().getLength());
					txtUnmatch.setCaretPosition(txtUnmatch.getDocument().getLength());

				}
				if(!flagMatch)
				{
					bw2.append(line1);
					bw2.newLine();
					txtUnmatch.append(line1+"\n");
					unmatchCount++;
					lblUnmatchCount.setText("Total Count: "+unmatchCount);
				}
				br2.close();				
			}
			br1.close();
			bw1.close();
			bw2.close();
			//bw3.close();

			return null;
		}

		public void done()
		{
			endTime=System.currentTimeMillis();
			totalTime=endTime-startTime;
			float executionTime=totalTime/1000.0f;
			System.out.println(executionTime);
			if(!flagError)
			{
				progressBar.setValue(100);

				JOptionPane.showMessageDialog(null, "File Comparison Completed.\nExecution Time: "+executionTime+" s", "Compare Files" , JOptionPane.INFORMATION_MESSAGE);
				btnOutput.setVisible(true);
			}
			else
			{

				if(errorMessage.equals("Comparison Process Stopped"))
					JOptionPane.showMessageDialog(null, errorMessage+"\nExecution Time: "+executionTime+" s", "Error" , JOptionPane.ERROR_MESSAGE);
				else
					JOptionPane.showMessageDialog(null, errorMessage, "Error" , JOptionPane.ERROR_MESSAGE);

			}
			btnStop.setVisible(false);
		}

	}

	public static void main(String args[]) throws IOException
	{
		final Compare_Files window = new Compare_Files();
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {

				window.frame.setVisible(true);
			}
		});

	}

	public Compare_Files() throws IOException
	{
		initialize();
	}

	private void initialize() throws IOException
	{

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			System.out.println("Exception : "+ e1);
		}
		frame = new JFrame();
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth=screensize.width;		
		screenHeight=screensize.height;
		frame.setBounds(screenWidth/2-450/2, 20, 450, 680);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(null);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setTitle("Compare Files");
		frame.getContentPane().setBackground(Color.getHSBColor(65, 86, 60));

		lblFile1 = new JLabel("File 1");
		lblFile1.setBounds(20, 20, 100, 25);
		frame.getContentPane().add(lblFile1);
		lblFile1.setFont(new Font(null, Font.PLAIN, 13));

		btnBrowse1 = new JButton("Browse");
		btnBrowse1.setBounds(340, 20, 79, 25);
		frame.getContentPane().add(btnBrowse1);

		txtFile1 = new JTextField();
		txtFile1.setBounds(20, 50, 400, 25);
		frame.getContentPane().add(txtFile1);

		lblFile2 = new JLabel("File 2");
		lblFile2.setBounds(20, 90, 100, 25);
		lblFile2.setFont(new Font(null, Font.PLAIN, 13));
		frame.getContentPane().add(lblFile2);

		btnBrowse2 = new JButton("Browse");
		btnBrowse2.setBounds(340, 90,79 , 25);
		frame.getContentPane().add(btnBrowse2);


		txtFile2 = new JTextField();
		txtFile2.setBounds(20, 120, 400, 25);		
		frame.getContentPane().add(txtFile2);

		chkSpace = new JCheckBox("Trim Header and Trailing Space");
		chkSpace.setBounds(16, 160, 400, 25);
		chkSpace.setFont(new Font(null,Font.PLAIN,13));
		frame.getContentPane().add(chkSpace);
		chkSpace.setBackground(Color.getHSBColor(65, 86, 60));
		chkSpace.setSelected(true);

		chkCase = new JCheckBox("Case Sensitive Comparision");
		chkCase.setBounds(16, 200, 400, 25);
		chkCase.setFont(new Font(null,Font.PLAIN,13));
		frame.getContentPane().add(chkCase);
		chkCase.setBackground(Color.getHSBColor(65, 86, 60));
		chkCase.setSelected(true);

		btnCompare = new JButton("Compare");
		btnCompare.setBounds(20, 240, 80, 25);
		frame.getContentPane().add(btnCompare);



		progressBar = new JProgressBar(0,100);
		progressBar.setBounds(120, 240, 300, 25);
		frame.getContentPane().add(progressBar);
		progressBar.setVisible(true);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		lblMatch = new JLabel("Matched Lines");
		lblMatch.setBounds(80, 290, 150, 25);
		lblMatch.setFont(new Font(null,Font.BOLD,13));
		frame.getContentPane().add(lblMatch);

		lblUnmatch = new JLabel("Unmatched Lines");
		lblUnmatch.setBounds(260, 290, 150, 25);
		lblUnmatch.setFont(new Font(null,Font.BOLD,13));
		frame.getContentPane().add(lblUnmatch);

		txtMatch = new JTextArea();
		txtMatch.setEditable(false);

		txtUnmatch = new JTextArea();
		txtUnmatch.setEditable(false);


		scrollPane1 = new JScrollPane(txtMatch);
		scrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane1.setBounds(20, 320, 200, 280);		
		frame.getContentPane().add(scrollPane1);
		scrollPane1.setVisible(true);

		scrollPane2 = new JScrollPane(txtUnmatch);
		scrollPane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane2.setBounds(220, 320, 200, 280);		
		frame.getContentPane().add(scrollPane2);
		scrollPane2.setVisible(true);

		lblMatchCount = new JLabel("Total Count: 0");
		lblMatchCount.setBounds(20, 610, 120, 25);
		lblMatchCount.setFont(new Font(null,Font.BOLD,13));
		frame.getContentPane().add(lblMatchCount);
		lblMatchCount.setForeground(Color.green.darker().darker());

		lblUnmatchCount = new JLabel("Total Count: 0");
		lblUnmatchCount.setBounds(320, 610, 120, 25);
		lblUnmatchCount.setFont(new Font(null,Font.BOLD,13));
		frame.getContentPane().add(lblUnmatchCount);
		lblUnmatchCount.setForeground(Color.RED);

		lblCredits = new JLabel("Designed and Developed by Manu Mishra");
		lblCredits.setBounds(115, 634, 250, 25);
		lblCredits.setFont(new Font(null,Font.PLAIN,10));
		frame.getContentPane().add(lblCredits);
		lblCredits.setForeground(Color.WHITE);
		 

		btnOutput= new JButton("Open Output Files");
		btnOutput.setBounds(155, 610, 130, 25);
		frame.getContentPane().add(btnOutput);
		btnOutput.setVisible(false);


		btnStop = new JButton("Stop");
		btnStop.setBounds(185, 610, 70, 25);
		frame.getContentPane().add(btnStop);
		btnStop.setVisible(false);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 640, 450, 15);
		panel.setBackground(Color.GRAY);
		frame.getContentPane().add(panel);
		

		btnBrowse1.addActionListener(new ActionListener() {


			@Override
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("./../.."));
				chooser.setDialogTitle("File 1");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);

				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					txtFile1.setText(chooser.getSelectedFile().toString());
					System.out.println("getSelectedFolder() : "+ chooser.getSelectedFile());
				} else {
					System.out.println("No Selection ");
				}

			}
		});

		btnBrowse2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("./../.."));
				chooser.setDialogTitle("File 2");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);



				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					txtFile2.setText(chooser.getSelectedFile().toString());
					System.out.println("getSelectedFolder() : "+ chooser.getSelectedFile());
				} else {
					System.out.println("No Selection ");
				}


			}
		});

		btnCompare.addActionListener(new ActionListener() {

			@Override

			public void actionPerformed(ActionEvent e) {

				txtMatch.setText("");
				txtUnmatch.setText("");
				lblMatchCount.setText("");
				lblUnmatchCount.setText("");
				btnOutput.setVisible(false);
				btnStop.setVisible(true);
				startTime=System.currentTimeMillis();

				task = new Task();
				task.execute();

			}
		});

		btnOutput.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				File outputDir = new File(dir+"\\Compare");
				try {
					Desktop.getDesktop().open(outputDir);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		btnStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				flagError=true;
				errorMessage="Comparison Process Stopped";				
			}
		});


	}

}
