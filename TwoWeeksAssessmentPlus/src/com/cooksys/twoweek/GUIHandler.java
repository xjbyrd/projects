/**
 * Developer   : Jason Byrd
 * Class       : GUIHandler.java
 * Description : This class is designed to set the visual layout for the file sharing app. The app will allow a user
 *				 to launch a server so others can retrieve files from a shared files folder as well as launch a client
 *				 so they can retrieve files from the client.
 * TO DO       : Change the color scheme
 * 				 Disable selection of list for items user can't use
 * 				 Fix the width of the JLists
 * 				 Fix button resizing
 */

package com.cooksys.twoweek;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class GUIHandler
{
	private static JFrame frmMain;
	private static JPanel pnlFirst;
	private static JPanel pnlSecondTop;
	private static JPanel pnlSecondBottom;
	private static JPanel pnlThirdLeft;
	private static JPanel pnlThirdCenter;
	private static JPanel pnlThirdRight;
	private static JButton btnServerControl;
	private static JButton btnClientControl;
	private static JButton btnDownloadFile;
	private static JButton btnServerDirectory;
	private static JButton btnDownloadDirectory;
	private static JLabel lblServerDirectory;
	private static JTextField txtClientIP;
	private static JLabel lblDownloadDirectory;
	private static JLabel lblConnectedDevelopersName;
	private static JList<String> lstServerFileList;
	private static JList<String> lstClientFileList;
	private static JList<String> lstDownloadFileList;
	private static DefaultListModel<String> serverListModel;
	private static DefaultListModel<String> clientListModel;
	private static DefaultListModel<String> downloadListModel;
	private static JFileChooser fcServerDirectory;
	private static JFileChooser fcDownloadDirectory;
	private static ExecutorService pool;
	private static Callable<String> callable;
	private static Future<String> future;
	private static Socket sock;
	private static InputStream is;
	private static DataInputStream dis;
	private static DataOutputStream dos;
	private static boolean clientConnected;
	private static JComboBox<String> cmbxServerIPs;
	private static final int PORT = 25251;

	/**
	 * Starts new thread for GUI to run on.
	 * 
	 * @param args Nothing
	 */
	public static void main(String[] args)
	{
//		SwingUtilities.invokeLater(new Runnable()
//		{
//			public void run()
//			{
//				GUIHandler();
//			}
//		});
		
		SwingUtilities.invokeLater(() ->
		{
			initializeGUI();
		});
			
	}
	
	/**
	 * Calls all the initializing methods and sets frame to visible for user.
	 */
	private static void initializeGUI() 
	{
		initializeComponents();
		loadHistoryFromXML();
		initializeActionListeners();
		setInitialListValues();
		frmMain.setVisible(true);
	}
	
	/**
	 * Initializes all the swing components used
	 */
	public static void initializeComponents()
	{
		frmMain = new JFrame("File Share Program");
		frmMain.setSize(900, 600);
		frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMain.setLocationRelativeTo(null);
		
		pnlFirst = new JPanel();
		pnlSecondTop = new JPanel();
		pnlSecondBottom = new JPanel();
		pnlThirdLeft = new JPanel();
		pnlThirdCenter = new JPanel();
		pnlThirdRight = new JPanel();
		btnServerControl = new JButton("Start Shared Server");
		btnClientControl = new JButton("Connect to Remote");
		btnDownloadFile = new JButton("Download File");
		btnServerDirectory = new JButton("Select Server Directory");
		btnDownloadDirectory = new JButton("Select Download Directory");
		lblServerDirectory = new JLabel();
		txtClientIP = new JTextField ();
		lblDownloadDirectory = new JLabel();
		lblConnectedDevelopersName = new JLabel("<No Connection to Server>");
		serverListModel = new DefaultListModel<String>();
		clientListModel = new DefaultListModel<String>();
		downloadListModel = new DefaultListModel<String>();
		lstServerFileList = new JList<String>(serverListModel);
		lstClientFileList = new JList<String>(clientListModel);
		lstDownloadFileList = new JList<String>(downloadListModel);
		fcServerDirectory = new JFileChooser();
		fcDownloadDirectory = new JFileChooser();
		clientConnected = false;
		cmbxServerIPs = new JComboBox<String>();
		
		// gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insets, ipadx, ipady
		GridBagConstraints constraints;
		
		// Sets layout of basic frames
		frmMain.add(pnlFirst);
		pnlFirst.setLayout(new BorderLayout());
		pnlFirst.add(pnlSecondTop, BorderLayout.NORTH);
		pnlFirst.add(pnlSecondBottom, BorderLayout.CENTER);
		
		// Sets layout of secondTop frame
		pnlSecondTop.setLayout(new GridBagLayout());
		constraints = new GridBagConstraints(0, 0, 1, 1, .6, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20,0,20,0), 0, 0);
		pnlSecondTop.add(btnServerControl, constraints);
		constraints.gridx = 2;
		pnlSecondTop.add(btnClientControl, constraints);
		constraints.gridx = 1;
		constraints.weightx = 1;
		pnlSecondTop.add(lblConnectedDevelopersName, constraints);
		
		// Sets layout of secondBottom frame
		pnlSecondBottom.setLayout(new GridBagLayout());
		constraints = new GridBagConstraints(0, 0, 1, 1, .6, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0);
		pnlSecondBottom.add(pnlThirdLeft, constraints);
		constraints.gridx = 2;
		pnlSecondBottom.add(pnlThirdRight, constraints);
		constraints.gridx = 1;
		constraints.weightx = 1;
		pnlSecondBottom.add(pnlThirdCenter, constraints);
		
		// Sets layout of third frames in secondBottom
		pnlThirdLeft.setLayout(new GridBagLayout());
		pnlThirdCenter.setLayout(new GridBagLayout());
		pnlThirdRight.setLayout(new GridBagLayout());
		constraints = new GridBagConstraints(0, 1, 1, 1, 0, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10,10,10,10), 0, 0);
		pnlThirdLeft.add(lstServerFileList, constraints);
		pnlThirdCenter.add(lstDownloadFileList, constraints);
		pnlThirdRight.add(lstClientFileList, constraints);
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridy = 0;
		pnlThirdLeft.add(lblServerDirectory, constraints);
		pnlThirdCenter.add(lblDownloadDirectory, constraints);
		pnlThirdRight.add(cmbxServerIPs, constraints);
		cmbxServerIPs.setPreferredSize(new Dimension(220, 18));

		constraints.gridy = 2;
		pnlThirdLeft.add(btnServerDirectory, constraints);
		pnlThirdCenter.add(btnDownloadDirectory, constraints);
		pnlThirdRight.add(btnDownloadFile, constraints);
		
		// Sets color scheme
		//pnlFirst.setBackground(Color.LIGHT_GRAY);
		pnlSecondBottom.setBackground(Color.LIGHT_GRAY);
		pnlSecondTop.setBackground(Color.DARK_GRAY);
		pnlThirdLeft.setOpaque(false);
		pnlThirdCenter.setOpaque(false);
		pnlThirdRight.setOpaque(false);
		lblConnectedDevelopersName.setForeground(Color.BLACK);
		txtClientIP.setForeground(Color.BLACK);
		lblServerDirectory.setForeground(Color.BLACK);
		lblDownloadDirectory.setForeground(Color.BLACK);
		lblConnectedDevelopersName.setFont(new Font("Times New Roman", Font.BOLD, 18));
		
		// centers text
		DefaultListCellRenderer renderer = (DefaultListCellRenderer)lstServerFileList.getCellRenderer();
		renderer.setHorizontalAlignment(JLabel.CENTER);
		renderer = (DefaultListCellRenderer)lstClientFileList.getCellRenderer();
		renderer.setHorizontalAlignment(JLabel.CENTER);
		renderer = (DefaultListCellRenderer)lstDownloadFileList.getCellRenderer();
		renderer.setHorizontalAlignment(JLabel.CENTER);
		
		// Lets user enter value into ComboBox
		cmbxServerIPs.setEditable(true);
		
		// Turns off download button until server is connected
		btnDownloadFile.setEnabled(false);
		
	}
	
	/**
	 * Sets initial list values from directories
	 */
	public static void setInitialListValues()
	{
		setFileList(lblDownloadDirectory, downloadListModel);
		setFileList(lblServerDirectory, serverListModel);
		getHistoryAndFillCmbx();
	}
	
	public static void setFileList(JLabel lbl, DefaultListModel<String> mdl)
	{
		File fileDir;
		File[] fileList;
		
		fileDir = new File(lbl.getText());
		fileList = fileDir.listFiles();
		
		for (File f : fileList)
		{
			if (!f.isDirectory())
			{
				mdl.addElement((String)f.getName());
			}
		}
	}

	/**
	 * Sets up ActionListeners to respond to user driven input.
	 */
	public static void initializeActionListeners()
	{
		btnServerControl.addActionListener(e ->
		{// Starts a single instance of the server running
			if (btnServerDirectory.isEnabled())
			{
				btnServerDirectory.setEnabled(false);
				btnServerControl.setText("Stop Shared Server");
				
				pool = Executors.newFixedThreadPool(1);
				
				callable = new Server();
				future = pool.submit(callable);
			}
			else
			{// Cancels the instance of the server that is running
				btnServerDirectory.setEnabled(true);
				btnServerControl.setText("Start Shared Server");
				
				if (!future.isDone())
				{
					future.cancel(true);
				}
			}
		});
		
		btnClientControl.addActionListener(e ->
		{
			if (!clientConnected)
			{
				connectAndGetFileNames();
				try
				{
					if (sock.isConnected())
					{
						btnDownloadFile.setEnabled(true);
						btnClientControl.setText("Disconnect from Remote");
					}
				}
				catch (NullPointerException f){ }
				clientConnected = true;
				
			}
			else
			{
				btnDownloadFile.setEnabled(false);
				btnClientControl.setText("Connect to Remote");
				lblConnectedDevelopersName.setText("<No Connection to Server>");
				clientListModel.clear();
				
				try
				{
					sock.close();
					System.out.println("Socket closed");
				}
				catch (IOException | NullPointerException f) { }
				
				clientConnected = false;
			}
		});
		
		btnDownloadFile.addActionListener(e ->
		{	
			
			saveFile();
			
			if (!clientConnected)
			{
				btnDownloadFile.setEnabled(false);
				btnClientControl.setText("Connect to Remote");
				lblConnectedDevelopersName.setText("<No Connection to Server>");
				clientListModel.clear();
				
				try
				{
					sock.close();
					System.out.println("Socket closed");
				}
				catch (IOException | NullPointerException f) { }
			}
		});
		
		btnServerDirectory.addActionListener(e ->
		{	
			fcServerDirectory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnValue = fcServerDirectory.showOpenDialog(frmMain);
			if (returnValue == JFileChooser.APPROVE_OPTION)
			{
				File file = fcServerDirectory.getSelectedFile();
				lblServerDirectory.setText(stringFixer(file.getAbsolutePath()));
				
				File fileDir = new File(lblServerDirectory.getText());
				File[] fileList = fileDir.listFiles();
				
				serverListModel.clear();
				
				for (File f : fileList)
				{
					if (!f.isDirectory())
					{
						serverListModel.addElement((String)f.getName());
					}
				}
			}
		});
		
		btnDownloadDirectory.addActionListener(e ->
		{	
			fcDownloadDirectory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnValue = fcDownloadDirectory.showOpenDialog(frmMain);
			if (returnValue == JFileChooser.APPROVE_OPTION)
			{
				File file = fcDownloadDirectory.getSelectedFile();
				lblDownloadDirectory.setText(stringFixer(file.getAbsolutePath()));
				
				File fileDir = new File(lblDownloadDirectory.getText());
				File[] fileList = fileDir.listFiles();
				
				downloadListModel.clear();
				
				for (File f : fileList)
				{
					if (!f.isDirectory())
					{
						downloadListModel.addElement((String)f.getName());
					}
				}
			}
		});
		
		frmMain.addWindowListener(new java.awt.event.WindowAdapter()
		{
			@Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent)
			{
		        saveHistoryToXML();
		    }

		});
		
	}
	
	/**
	 * 
	 * @return The text value for the directory of the shared server.
	 */
	public static String getServerDirectory()
	{
		return lblServerDirectory.getText();
	}
	
	/**
	 * Connects Client to the Server and gets/sets DevName and file names of the Server files.
	 */
	public static void connectAndGetFileNames()
	{
		String data = "";
		String[] dataArr;
		
		try
		{
			// (2) Client connects to Server
			sock = new Socket(cmbxServerIPs.getSelectedItem().toString(), 25251);
			System.out.println("Connected to socket");
			
			is = sock.getInputStream();
			dis = new DataInputStream(is);
			
			data = dis.readUTF();
			
			if (sock.isConnected())
			{
				dataArr = data.split(",");

				lblConnectedDevelopersName.setText("Connected to: " + dataArr[0]);

				if (dataArr.length > 1)
				{
					for (int i = 1; i < dataArr.length; i++)
					{
						clientListModel.addElement(dataArr[i]);
					}
				}
				
				saveHistory();
			}
		}
		catch (IOException | NullPointerException f) { }
	}

	/**
	 * Downloads and saves the selected file from the Server files to the
	 * Downloaded files directory.
	 */
	public static void saveFile()
	{
		String s;
		int testInt = 0;
		File file;
		FileOutputStream fos;
		
		
		if (!(lstClientFileList.getSelectedIndex() == -1))
		{
			try
			{
				// (5)(5-8 Repeated) Client sends request for file to server
				dos = new DataOutputStream(sock.getOutputStream());
				s = clientListModel.getElementAt(lstClientFileList.getSelectedIndex());
				System.out.println("Get file: " + s);
				dos.writeUTF(s);
				dos.flush();
				
				// (8)(5-8 Repeated) Receive requested file from server 
				file = new File(lblDownloadDirectory.getText() + s);

				fos = new FileOutputStream(file);
				
				testInt = dis.readInt();
				if (testInt != -1)
				{
					while (testInt != -1)
					{
						fos.write(testInt);
						testInt = dis.readInt();
					}
					fos.flush();
					//bos.flush();
					System.out.println("File Copied!");
				}
				
			}
			catch (IOException e)
			{ 
				clientConnected = false;
			}
		}
		downloadListModel.clear();
		setFileList(lblDownloadDirectory, downloadListModel);
	}
	
	/**
	 * Saves the Server and downloaded files directories and the IP of the last server Client connected to.
	 */
	public static void saveHistoryToXML()
	{
		History h = new History();
		h.setClientIP(cmbxServerIPs.getSelectedItem().toString());
		h.setDownloadDirectory(lblDownloadDirectory.getText());
		h.setServerDirectory(lblServerDirectory.getText());
		
		try
		{
			File file = new File("C:\\Users\\6GQ0QX1\\File_Sharing_Folder\\History.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(h.getClass());
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(h, file);
		}
		catch (JAXBException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * Loads the Server and downloaded files directories and the IP of the last server Client connected to.
	 */
	public static void loadHistoryFromXML()
	{
		File file = new File("C:\\Users\\6GQ0QX1\\File_Sharing_Folder\\History.xml");
		History h = new History();
		
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(History.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			
			h = (History) jaxbUnmarshaller.unmarshal(file);
					
			lblDownloadDirectory.setText(h.getDownloadDirectory());
			
			lblServerDirectory.setText(h.getServerDirectory());
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void saveHistory()
	{
		try
		{
			DBHandler.saveHistory(cmbxServerIPs.getSelectedItem().toString(), lblConnectedDevelopersName.getText(), PORT);
		}
		catch (Exception e) { }
	}
	
	public static void getHistoryAndFillCmbx()
	{
		ArrayList<String> strArr = new ArrayList<String>();
		cmbxServerIPs.removeAllItems();
		try
		{
			strArr = DBHandler.getHistory();
			
			for (String str : strArr)
			{
				cmbxServerIPs.addItem(str);
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Fixes a problem with directory names not having \ added at the end.
	 * 
	 * @param s String with a missing \ at the end
	 * @return Fixed String with a \ at the end
	 */
	public static String stringFixer(String s)
	{
		if (!s.endsWith("\\"))
		{
			return s + "\\";
		}
		
		return s;
	}
}
