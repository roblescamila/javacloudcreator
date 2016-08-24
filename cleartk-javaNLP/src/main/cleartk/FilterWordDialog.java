package main.cleartk;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;

@SuppressWarnings("serial")
public class FilterWordDialog extends JFrame {

	private JPanel contentPane;
	private Vector<String> filteredWords;
	private Vector<String>  reservedWords;
	private boolean rjw =false;
	
	public Vector<String> getFilteredWords() throws IOException {
	
		Vector<String> aux = new Vector<String>();
		if (rjw)
		{
			for (String a: reservedWords )
		{
			aux.add(a);
		}
			for (String a: filteredWords )
			{
				aux.add(a);
			}
		return aux;
		}
		else
		{
		return filteredWords;
	}
	}
	
	
	public void initReservedWords() throws IOException
	{reservedWords = new Vector<String>();
		File fichero = new File("JavaReservedWords.txt");
		Scanner s = null;

		try {
			// Leemos el contenido del fichero
			System.out.println("... Leemos el contenido del fichero ...");
			s = new Scanner(fichero);

			// Leemos linea a linea el fichero
			while (s.hasNextLine()) {
				String linea = s.nextLine(); 	// Guardamos la linea en un String
				//fSystem.out.println(linea); // Imprimimos la linea
				reservedWords.add(linea);
			}

		} catch (Exception ex) {
			System.out.println("Mensaje: " + ex.getMessage());
		} finally {
			// Cerramos el fichero tanto si la lectura ha sido correcta o no
			try {
				if (s != null)
					s.close();
			} catch (Exception ex2) {
				System.out.println("Mensaje 2: " + ex2.getMessage());
			}
		}
	
	}
	
	private boolean removeWord(String w) {
		for (int i = 0; i < filteredWords.size(); i++) {
			if (filteredWords.elementAt(i).equals(w)) {
				filteredWords.removeElementAt(i);
				return true;
			}
		}
		return false;
	}

	private File getSaveLocation() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.TXT",
				"*.txt");
		chooser.setFileFilter(filter);
		int result = chooser.showSaveDialog(this);
		if (result == chooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		} else {
			return null;
		}
	}

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
					FilterWordDialog frame = new FilterWordDialog();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public FilterWordDialog() throws IOException {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 406, 517);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		filteredWords = new Vector<String>();
		JScrollPane scrollPane = new JScrollPane();
		initReservedWords();
		@SuppressWarnings("rawtypes")
		final
		DefaultListModel wordsModel = new DefaultListModel();

		final JTextField txtrEntry = new JTextField();
		txtrEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!filteredWords.contains(txtrEntry.getText())) {
					wordsModel.addElement(txtrEntry.getText() + "\n");
					filteredWords.addElement(txtrEntry.getText());
					txtrEntry.setText("");
				}
			}
		});
		txtrEntry.setFont(new Font("Tahoma", Font.PLAIN, 11));

		@SuppressWarnings({ "rawtypes", "unchecked" })
		final
		JList list = new JList(wordsModel);
		scrollPane.setViewportView(list);

		final JFrame openFileDialog = new JFrame("Select directory");

		JButton btnAddWord = new JButton("Add word");
		btnAddWord.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				if (!filteredWords.contains(txtrEntry.getText())) {
					wordsModel.addElement(txtrEntry.getText());
					filteredWords.addElement(txtrEntry.getText());
					txtrEntry.setText("");
				}
			}
		});

		
		
		JButton btnRemoveWord = new JButton("Remove");
		btnRemoveWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (list.getSelectedIndices().length > 0) {
					int[] tmp = list.getSelectedIndices();
					int[] selectedIndices = list.getSelectedIndices();
					for (int i = tmp.length - 1; i >= 0; i--) {
						selectedIndices = list.getSelectedIndices();
						if (removeWord(wordsModel.elementAt(i).toString())) {
							wordsModel.removeElementAt(selectedIndices[i]);
						}
					}
				}
			}
		});

		JButton btnSaveListTo = new JButton("Save list to file");
		btnSaveListTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileWriter fw;
				try {
					fw = new FileWriter(getSaveLocation() + ".txt");
					for (String word : filteredWords) {
						fw.write(word + "\n");
					}
					fw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		JButton btnLoadListFrom = new JButton("Load list from file");
		btnLoadListFrom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int seleccion = fc.showOpenDialog(openFileDialog);
				if (seleccion == fc.APPROVE_OPTION) {
					filteredWords.removeAllElements();
					wordsModel.removeAllElements();
					File file = fc.getSelectedFile();
					BufferedReader f;
					try {
						f = new BufferedReader(new FileReader(file));
						String s;
						while ((s = f.readLine()) != null) {
							filteredWords.add(s);
							wordsModel.addElement(s);
						}
					} catch (FileNotFoundException e2) {
						e2.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

			}
		});

		JLabel lblEditListOf = new JLabel("Edit list of filtered words");
		lblEditListOf.setFont(new Font("Tahoma", Font.PLAIN, 12));

		final JCheckBox chckbxNewCheckBox = new JCheckBox("Filter reserved words");
		chckbxNewCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton btnAccept = new JButton("Accept");
		btnAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		
		chckbxNewCheckBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
			rjw=chckbxNewCheckBox.isSelected();
			System.out.println(rjw);
			}
		});
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(txtrEntry, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnAddWord, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 223, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEditListOf))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(btnSaveListTo, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnLoadListFrom, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnRemoveWord, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
								.addComponent(chckbxNewCheckBox, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnAccept, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblEditListOf)
					.addGap(11)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnLoadListFrom, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSaveListTo, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(btnRemoveWord)
							.addGap(18)
							.addComponent(chckbxNewCheckBox, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 388, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtrEntry, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAddWord, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAccept, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
}
