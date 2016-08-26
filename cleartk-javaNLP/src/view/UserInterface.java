package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.Box;
import javax.swing.JRadioButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.ButtonGroup;
import javax.swing.JSlider;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JSpinner;

import java.awt.Dimension;

import javax.swing.JButton;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;
import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;
import org.xml.sax.SAXException;

import controller.ClearTKCloudController;
import controller.CloudController;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserInterface extends JFrame {

	private JPanel contentPane;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JSlider slider;
	private static Vector<String> filteredWords;
	private CloudController wcc;
	private Cloud cloud;
	private static Vector<String> files;
	private JTree tree;
	private MyFile projectFile;
	private static UserInterface frame;
	private FilterWordDialog fwd;
	private JSpinner spinner = new JSpinner();
	private JPanel panel = new JPanel();

	/**
	 * Launch the application.
	 * 
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserInterface frame = new UserInterface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setColor(int i, JLabel l) {
		if (i % 2 == 0) {
			l.setForeground(Color.GRAY);
			l.setBackground(Color.GRAY);
		} else {
			l.setForeground(Color.BLACK);
			l.setBackground(Color.BLACK);
		}
	}

	public JSpinner getSipner() {
		return spinner;
	}

	public void failmessage() {

		JLabel label = new JLabel("FAIL...");
		label.setOpaque(false);
		label.setFont(label.getFont().deriveFont(100));
		label.setForeground(Color.BLACK);
		label.setBackground(Color.BLACK);
		panel.removeAll();
		panel.repaint();
		panel.add(label);
		panel.revalidate();
		panel.repaint();
	}

	public void paintCloud() throws IOException {
		panel.removeAll();
		panel.repaint();
		int i = 0;
		filteredWords = fwd.getFilteredWords();
		Cloud aux = new Cloud();
		for (Tag a : cloud.tags()) {
			aux.addTag(a);
		}
		for (String remove : filteredWords) {
			aux.removeTag(remove);
		}
		for (Tag tag : aux.tags()) {
			if (tag.getScoreInt() > (int) (((SpinnerNumberModel) spinner
					.getModel()).getNumber())) {
				JLabel label = new JLabel(tag.getName());
				label.setOpaque(false);
				label.setFont(label.getFont().deriveFont(
						(float) tag.getWeight() * slider.getValue()));

				panel.add(label);
				i++;
			}
		}
		panel.revalidate();
		panel.repaint();

	}

	private DefaultMutableTreeNode addNodes(DefaultMutableTreeNode curTop,
			MyFile dir) {

		String curPath = dir.getPath();
		DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(curPath);
		if (curTop != null) {
			curTop.add(curDir);
		}
		Vector<String> ol = new Vector<String>();
		String[] tmp = dir.list();
		for (int i = 0; i < tmp.length; i++)
			ol.addElement(tmp[i]);
		Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
		MyFile f;
		Vector<String> files = new Vector<String>();
		for (int i = 0; i < ol.size(); i++) {
			String thisObject = (String) ol.elementAt(i);
			String newPath;
			if (curPath.equals("."))
				newPath = thisObject;
			else
				newPath = curPath + MyFile.separator + thisObject;
			if ((f = new MyFile(newPath)).isDirectory())
				addNodes(curDir, f);
			else
				files.addElement(thisObject);
		}
		for (int fnum = 0; fnum < files.size(); fnum++)
			curDir.add(new DefaultMutableTreeNode(files.elementAt(fnum)));
		return curDir;
	}

	public static String createFilePath(TreePath treePath) {
		StringBuilder sb = new StringBuilder();
		Object[] nodes = treePath.getPath();
		for (int i = nodes.length-2; i < nodes.length; i++) {
			sb.append(nodes[i].toString()).append(File.separatorChar);
		}
		return sb.toString().substring(0, sb.toString().length() - 1);
	}

	public BufferedImage createImage(JPanel panel) {
		int w = panel.getWidth();
		int h = panel.getHeight();
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		panel.paint(g);
		return bi;
	}

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 */
	public UserInterface() throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		final DefaultListModel wordsModel = new DefaultListModel();
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		final JList wordList = new JList();
		JMenu mnFile = new JMenu("Options");
		menuBar.add(mnFile);
		cloud = new Cloud();
		fwd = new FilterWordDialog(this);
		final JFileChooser fc = new JFileChooser();
		final JFileChooser fcs = new JFileChooser();
		final JFrame openFileDialog = new JFrame("Select directory");
		final JFrame saveFileDialog = new JFrame("Select directory");
		JMenuItem mntmOpen = new JMenuItem("Open");

		mnFile.add(mntmOpen);

		JMenuItem mntmSaveCloud = new JMenuItem("Save cloud");

		mnFile.add(mntmSaveCloud);

		JMenuItem mntmResetCloud = new JMenuItem("Reset cloud");

		mnFile.add(mntmResetCloud);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		panel.setBackground(Color.white);
		panel.setForeground(Color.DARK_GRAY);
		final JScrollPane scrollPane = new JScrollPane();

		JPanel panel_2 = new JPanel();
		JPanel panel_1 = new JPanel();
		JPanel panel_3 = new JPanel();

		final Vector<CloudController> wccs = new Vector<CloudController>();

		mntmSaveCloud.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileNameExtensionFilter filtro = new FileNameExtensionFilter(
						"*.PNG", "*.png");
				fcs.setFileFilter(filtro);
				// fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				BufferedImage image = createImage(panel);
				String ruta;
				// fc.showSaveDialog(frame3);
				int seleccion = fcs.showSaveDialog(saveFileDialog);

				if (seleccion == fcs.APPROVE_OPTION) {

					File file = new File(fcs.getSelectedFile() + ".png");
					try {
						ImageIO.write(image, "png", file);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileNameExtensionFilter filtro = new FileNameExtensionFilter(
						"*.JAVA", "*.java");
				fc.setFileFilter(filtro);
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int selection = fc.showOpenDialog(openFileDialog);
				fc.setMultiSelectionEnabled(true);
				if (selection == JFileChooser.APPROVE_OPTION) {
					projectFile = new MyFile(fc.getSelectedFile()
							.getAbsolutePath());
					tree = new JTree(addNodes(null, projectFile));
					scrollPane.setViewportView(tree);
					File a = fc.getSelectedFile();
				}
			}
		});

		JButton btnNewButton = new JButton("Filtered words");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fwd.setVisible(true);
			}
		});

		JButton btnNewButton_1 = new JButton("Create cloud");
		final JDialog dialog = new JDialog(frame, true);
		dialog.setUndecorated(true);
		dialog.setAlwaysOnTop(true);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height
				/ 2 - this.getSize().height / 2);
		JProgressBar bar = new JProgressBar();
		bar.setIndeterminate(true);
		bar.setStringPainted(true);
		bar.setString("Creating word cloud");
		dialog.add(bar);
		dialog.pack();

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane
				.setHorizontalGroup(gl_contentPane
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_contentPane
										.createSequentialGroup()
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.TRAILING)
														.addGroup(
																Alignment.LEADING,
																gl_contentPane
																		.createSequentialGroup()
																		.addGroup(
																				gl_contentPane
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(
																								scrollPane,
																								GroupLayout.DEFAULT_SIZE,
																								293,
																								Short.MAX_VALUE)
																						.addComponent(
																								panel_2,
																								GroupLayout.DEFAULT_SIZE,
																								285,
																								Short.MAX_VALUE)
																						.addGroup(
																								gl_contentPane
																										.createSequentialGroup()
																										.addGap(2)
																										.addGroup(
																												gl_contentPane
																														.createParallelGroup(
																																Alignment.LEADING)
																														.addComponent(
																																btnNewButton_1,
																																Alignment.TRAILING,
																																GroupLayout.DEFAULT_SIZE,
																																283,
																																Short.MAX_VALUE)
																														.addGroup(
																																gl_contentPane
																																		.createSequentialGroup()
																																		.addComponent(
																																				panel_3,
																																				GroupLayout.PREFERRED_SIZE,
																																				GroupLayout.DEFAULT_SIZE,
																																				GroupLayout.PREFERRED_SIZE)
																																		.addPreferredGap(
																																				ComponentPlacement.UNRELATED)
																																		.addComponent(
																																				btnNewButton,
																																				GroupLayout.PREFERRED_SIZE,
																																				118,
																																				Short.MAX_VALUE)))))
																		.addGap(10))
														.addGroup(
																Alignment.LEADING,
																gl_contentPane
																		.createSequentialGroup()
																		.addComponent(
																				panel_1,
																				GroupLayout.PREFERRED_SIZE,
																				232,
																				GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)))
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addComponent(panel,
												GroupLayout.PREFERRED_SIZE,
												1057,
												GroupLayout.PREFERRED_SIZE)));
		gl_contentPane
				.setVerticalGroup(gl_contentPane
						.createParallelGroup(Alignment.TRAILING)
						.addGroup(
								gl_contentPane
										.createSequentialGroup()
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addComponent(
																				scrollPane,
																				GroupLayout.PREFERRED_SIZE,
																				485,
																				GroupLayout.PREFERRED_SIZE)
																		.addGap(1)
																		.addComponent(
																				panel_1,
																				GroupLayout.PREFERRED_SIZE,
																				50,
																				GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				panel_2,
																				GroupLayout.PREFERRED_SIZE,
																				31,
																				GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addGroup(
																				gl_contentPane
																						.createParallelGroup(
																								Alignment.LEADING,
																								false)
																						.addComponent(
																								btnNewButton,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								panel_3,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE))
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				btnNewButton_1,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE))
														.addComponent(
																panel,
																GroupLayout.PREFERRED_SIZE,
																674,
																GroupLayout.PREFERRED_SIZE))
										.addContainerGap()));

		JLabel lblMinimunWordCount = new JLabel("Minimun word count");
		panel_3.add(lblMinimunWordCount);

		spinner.setPreferredSize(new Dimension(45, 20));
		panel_3.add(spinner);

		Box verticalBox = Box.createVerticalBox();
		panel_1.add(verticalBox);
		final JPanel pnlWordCloud = new JPanel();
		final JRadioButton rdbtnPackages = new JRadioButton("Packages");
		verticalBox.add(rdbtnPackages);
		rdbtnPackages.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnPackages.setVerticalAlignment(SwingConstants.TOP);

		final JRadioButton rdbtnImports = new JRadioButton("Imports");
		verticalBox.add(rdbtnImports);
		rdbtnImports.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnImports.setVerticalAlignment(SwingConstants.TOP);

		Box verticalBox_1 = Box.createVerticalBox();
		panel_1.add(verticalBox_1);

		final JRadioButton rdbtnVariables = new JRadioButton("Variables");
		verticalBox_1.add(rdbtnVariables);
		rdbtnVariables.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnVariables.setVerticalAlignment(SwingConstants.TOP);

		final JRadioButton rdbtnMethod = new JRadioButton("Methods");
		verticalBox_1.add(rdbtnMethod);
		rdbtnMethod.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnMethod.setVerticalAlignment(SwingConstants.TOP);

		Box verticalBox_2 = Box.createVerticalBox();
		panel_1.add(verticalBox_2);

		final JRadioButton rdbtnComments = new JRadioButton("Comments");
		verticalBox_2.add(rdbtnComments);
		rdbtnComments.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnComments.setVerticalAlignment(SwingConstants.TOP);

		final JRadioButton rdbtnClasses = new JRadioButton("Classes");
		rdbtnClasses.setVerticalAlignment(SwingConstants.TOP);
		rdbtnClasses.setHorizontalAlignment(SwingConstants.LEFT);
		verticalBox_2.add(rdbtnClasses);

		JLabel lblNewLabel = new JLabel("Word size");
		panel_2.add(lblNewLabel);

		slider = new JSlider();

		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				try {
					paintCloud();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				try {
					paintCloud();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		slider.setPaintLabels(true);
		slider.setName("Word size");
		slider.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panel_2.add(slider);
		contentPane.setLayout(gl_contentPane);

		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//
				// SwingWorker<Void, Void> worker = new SwingWorker<Void,
				// Void>() {
				// @Override
				// protected Void doInBackground() throws UIMAException,
				// SAXException {
				//
             
				Hashtable<String, Boolean> selected = new Hashtable<String, Boolean>();
				selected.put("Comments", rdbtnComments.isSelected());
				selected.put("Classes", rdbtnClasses.isSelected());
				selected.put("Variables", rdbtnVariables.isSelected());
				selected.put("Packages", rdbtnPackages.isSelected());
				selected.put("Imports", rdbtnImports.isSelected());
				selected.put("Methods", rdbtnMethod.isSelected());
				System.out.println(selected.containsValue(true));  
				if (selected.containsValue(true))
					
				{
					try {
						failmessage();
					CountDownLatch barrier = new CountDownLatch(tree
							.getSelectionPaths().length);
					runBackgroundNlp(selected, wccs, barrier);
					barrier.await();
					createCloud(wccs, selected);
					paintCloud();
				} catch (CASException | InterruptedException e) {
					e.printStackTrace();
				} catch (UIMAException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// return null;
				// }
				//
				// @Override
				// protected void done() {
				// dialog.dispose();
				// }
				// };
				// worker.execute();
				// dialog.setVisible(true); // will block but with a responsive
				// GUI
				}}
		});

		mntmResetCloud.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cloud.clear();
				panel.removeAll();
				panel.repaint();

			}
		});
	}

	private boolean allFalse(Hashtable<String, Boolean> selected) {
		boolean a = selected.containsValue(true);
		if (!a)
		{
			System.out.println("son todos falsos");
		}
		else
		{
			System.out.println("no son todos falsos");
		}
		return !a;

	}

	private void runBackgroundNlp(Hashtable<String, Boolean> selected,
			Vector<CloudController> wccs, CountDownLatch barrier)
			throws UIMAException, IOException {
		TreePath[] tpVector = tree.getSelectionPaths();
		

	
			Runnable thobjects[] = new Runnable[tpVector.length];
			Executor pool = Executors.newFixedThreadPool(tpVector.length);
			for (int i = 0; i < tpVector.length; i++) {
				String f = createFilePath(tpVector[i]);
				wcc = new ClearTKCloudController(f, barrier);
				wccs.add(wcc);
				thobjects[i] = wcc;
				pool.execute(thobjects[i]);
			
		}
	}

	private void createCloud(Vector<CloudController> wccs,
			Hashtable<String, Boolean> selected) throws CASException,
			InterruptedException {
		for (CloudController wc : wccs) {
			wc.updateCloud(selected, cloud);
		}
	}

	public boolean getSliderPaintLabels() {
		return slider.getPaintLabels();
	}

	public void setSliderPaintLabels(boolean paintLabels) {
		slider.setPaintLabels(paintLabels);
	}
}
