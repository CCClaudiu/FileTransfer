package guiPackage;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import jade.core.AID;
import jade.domain.FIPAException;
import mainPackage.ReceiverAgent;
import mainPackage.SenderAgent;

import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SpringLayout;

public class ReceiverGUI extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	
	private JFileChooser chooser;
	DefaultListModel dlmList;

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the frame.
	 */
	public ReceiverGUI(ReceiverAgent receiver) {
		this.setResizable(false);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setTitle("Receiver:"+receiver.getName());
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 562, 139);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(15, 38, 516, 24);
		textField.setEditable(false);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblAlegeLocatia = new JLabel("Alege locatia");
		lblAlegeLocatia.setBounds(15, 15, 516, 17);
		lblAlegeLocatia.setFont(new Font("Tahoma", Font.BOLD, 14));
		contentPane.add(lblAlegeLocatia);
		
		JButton btnNewButton = new JButton("Alege");
		btnNewButton.setBounds(459, 68, 72, 24);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser f = new JFileChooser();
		        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
		        f.showSaveDialog(null);
		        textField.setText(f.getSelectedFile().toString());
		        try {
					receiver.registerAgent();
				} catch (FIPAException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        
		        System.out.println(f.getCurrentDirectory());
		        System.out.println(f.getSelectedFile());
			}
		});
		contentPane.add(btnNewButton);
		
		dlmList=new DefaultListModel();
		
		show();
	}

	public void saveFile(byte[] fileContent,String fileName) throws IOException {
		//File dir = new File (textArea.getText());
		//File actualFile = new File (dir, fileName);
		//FileUtils.writeByteArrayToFile(actualFile,fileContent);
		try {
			FileOutputStream fos = new FileOutputStream(textField.getText()+"\\"+fileName);
			fos.write(fileContent);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void refreshSendersList(AID [] senders)
	{
		dlmList.clear();
		for(AID sender:senders)
		{
			dlmList.addElement(sender);
		}
	}
}
