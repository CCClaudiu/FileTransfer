package guiPackage;

import java.awt.BorderLayout;

import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import jade.core.AID;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import mainPackage.SenderAgent;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Dimension;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.SpringLayout;

public class SenderGUI extends JFrame{
	private JPanel contentPane;
	//DefaultListModel demoList;
	private JTextField textField;
	private SenderAgent myAgent;
	File selectedFile;
	String filePath;
	String fileName;
	//private List<AID> agentsAIDs;
	//AID [] receivers;
	//List<AID> receiversList;
	JList list_1;
	DefaultListModel dlmList;
	byte[] encoded;
	
	private Map<String,AID> receiversMap=new HashMap<String,AID>();
	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the frame.
	 */
	public SenderGUI(AID [] agents,SenderAgent sender) {
		this.setTitle("Sender:"+sender.getName());
		this.myAgent=sender;
		//this.receivers=agents;
		//receiversList=new ArrayList<AID>(Arrays.asList(agents));
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 524, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
//		demoList = new DefaultListModel();
//		for(int i=0;i<agents.length;i++)
//		{
//			//AID agentID = agents[i].getName();
//		    System.out.println(agents[i].getLocalName());
//		    demoList.addElement(agents[i].getLocalName());
//		}
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		JLabel lblNewLabel = new JLabel("Agenti disponibili");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblNewLabel, 5, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblNewLabel, 10, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblNewLabel, 38, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblNewLabel, 181, SpringLayout.WEST, contentPane);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		contentPane.add(lblNewLabel);
		
		textField = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, textField, 189, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, textField, 10, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, textField, -10, SpringLayout.EAST, contentPane);
		textField.setEditable(false);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Alege fisier");
		sl_contentPane.putConstraint(SpringLayout.SOUTH, textField, -6, SpringLayout.NORTH, btnNewButton);
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnNewButton, 220, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, btnNewButton, 10, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnNewButton, 107, SpringLayout.WEST, contentPane);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser=new JFileChooser();
				if(fileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
				{
					selectedFile=fileChooser.getSelectedFile();
					filePath=selectedFile.getAbsolutePath();
					textField.setText(filePath);
					fileName=selectedFile.getName();
					System.out.println(selectedFile.getName());
				}
			}
		});
		contentPane.add(btnNewButton);
		
		JButton sendFileBtn = new JButton("Trimite");
		sl_contentPane.putConstraint(SpringLayout.NORTH, sendFileBtn, 220, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, sendFileBtn, -10, SpringLayout.EAST, contentPane);
		sendFileBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(list_1.getSelectedIndex()==-1)
					{
						JOptionPane.showMessageDialog(null, "Selectati agentul destinatar!","Eroare",JOptionPane.ERROR_MESSAGE);
						return;
					}
					else if(textField.getText()==null || textField.getText().isEmpty())
					{
						JOptionPane.showMessageDialog(null, "Incarcati fisierul !","Eroare",JOptionPane.ERROR_MESSAGE);
						return;
					}
					encoded = Files.readAllBytes(Paths.get(textField.getText()));
					AID selectedAID=receiversMap.get(dlmList.getElementAt(list_1.getSelectedIndex()));
					sender.handleSendFile(encoded, selectedAID,fileName);
					JOptionPane.showMessageDialog(null, "Fisierul a fost trimis !","Informare",JOptionPane.INFORMATION_MESSAGE);
					//sender.handleSendFile(encoded, (AID)dlmList.getElementAt(list_1.getSelectedIndex()),fileName);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(sendFileBtn);
		
		JLabel lblFisier = new JLabel("Fisier");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblFisier, 160, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblFisier, 10, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblFisier, 187, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblFisier, -10, SpringLayout.EAST, contentPane);
		lblFisier.setFont(new Font("Tahoma", Font.BOLD, 14));
		contentPane.add(lblFisier);
		
		//agentsAIDs=new ArrayList<AID>();
		dlmList=new DefaultListModel();
		for(int i=0;i<agents.length;i++)
		{
			//AID agentID = agents[i].getName();
		    //System.out.println(agents[i].getLocalName());
			receiversMap.put(agents[i].getName(), agents[i]);
			dlmList.addElement(agents[i].getName());
		}
		list_1 = new JList(dlmList);
		sl_contentPane.putConstraint(SpringLayout.NORTH, list_1, 43, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, list_1, 10, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, list_1, 149, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, list_1, -10, SpringLayout.EAST, contentPane);
		contentPane.add(list_1);
		//JScrollPane scrollPane
		JScrollPane scrollPane = new JScrollPane(list_1);
		sl_contentPane.putConstraint(SpringLayout.NORTH, scrollPane, 30, SpringLayout.NORTH, lblNewLabel);
		sl_contentPane.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, scrollPane, -10, SpringLayout.EAST, contentPane);
		contentPane.add(scrollPane);
		//JScrollPane scrollPane = new JScrollPane(list_1);
		//scrollPane.setBounds(10, 43, 438, 106);
		//contentPane.add(scrollPane);
		
		
		setLocationRelativeTo(null);
		show();
	}
	public void refresh(AID ss)
	{
		//demoList.addElement(ss.getLocalName());
		receiversMap.put(ss.getName(), ss);
		dlmList.addElement(ss.getName());
	}
	public void removeReceiver(AID receiver)
	{
		AID agentToRemove=receiversMap.get(receiver.getLocalName());
		dlmList.removeElement(receiver.getName());
	}

	public void refreshReceiverList(AID [] receivers)
	{
		dlmList.clear();
		receiversMap.clear();
		for(AID receiver:receivers)
		{
			receiversMap.put(receiver.getName(),receiver);
			dlmList.addElement(receiver.getName());
		}
	}



}
