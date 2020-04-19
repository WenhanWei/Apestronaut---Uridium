package managers;



import GameDatabase.XMLFileDetails;
import GameDatabase.WorkshopFileOperations;
import net.miginfocom.swing.MigLayout;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

import static managers.MenuController.jFrame;

/**
 * @author WenhanWei
 * @date 2020/4/12
 */

/**
 * The WorkshopView is similar to the MenuView but is responsible for uploading or downloading xml files.
 */
public class WorkshopView extends View {

    private JImage background;
    private JPanel workshopContainer;
    private CardLayout cardLayout;
    private JPanel uploadPanel;
    private JPanel downloadPanel;
    private JLabel pathDiskLabel;

    private JButton workshopBtn;
    private JButton upload;
    private JButton download;
    private JButton uploadBtn;
    private JButton searchBtn;

    private static JTextField authorField;
    private static JTextField filenameField;
    private static JTextField pathField;

    private static JTextField SearchField;

    public static List<XMLFileDetails> xmlFileDetails = new ArrayList<>();

    public WorkshopView() {
        File path = new File("src/resources/icons/stats.png");
        loadImage(path);
        workshopBtn = new JButton("WORKSHOP", new ImageIcon(image));
        workshopBtn.setHorizontalTextPosition(SwingConstants.LEFT);
        workshopBtn.setIconTextGap(20);

        download = new JButton("DOWNLOAD");
        upload = new JButton("UPLOAD");


        createAndShowGUI();
    }

    /**
     * Responsible for creating and displaying all components and visually structuring them.
     */
    private void createAndShowGUI() {
        // using a mainPanel (background) which adds both the title and other components
        background = loadResources();
        background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));
        JPanel titlePanel = super.createTitlePanel(super.getBackBtn(), workshopBtn);
        background.add(titlePanel);
        background.add(Box.createRigidArea(new Dimension(0, 100)));
        displayWorkshop();
    }

    /**
     * Populates the background panel with the information related to the leaderboards.
     */
    private void displayWorkshop() {
        JPanel workshopItems = new JPanel();
        workshopItems.setLayout(new BoxLayout(workshopItems, BoxLayout.X_AXIS));
        workshopItems.setOpaque(false);

        workshopContainer = new JPanel();
        workshopContainer.setLayout(new CardLayout());
        workshopContainer.setBackground(Color.decode("#7597E3"));
        workshopContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        workshopContainer.setMaximumSize(new Dimension(1000, 700));
        workshopContainer.setBorder(new CompoundBorder( // CompoundBorder allows us to add two borders to our panel
                BorderFactory.createMatteBorder(30, 30, 30, 30, Color.decode("#6668CC")),
                BorderFactory.createMatteBorder(30, 30, 30, 30, Color.decode("#AF8FE4"))));
        cardLayout = (CardLayout) workshopContainer.getLayout();

        setFont(getFont().deriveFont(30f));
        File path = new File("src/resources/images/button.png");
        loadImage(path);
        addButton(download, workshopItems, true);
        addButton(upload, workshopItems, true);
        background.add(workshopItems);
        background.add(Box.createRigidArea(new Dimension(0, 50)));

        download();
        upload();

    }

    /**
     * Displays the upload view.
     */
    protected void upload() {
        uploadPanel = new JPanel(new MigLayout("center", "[]10[]10[]"));
        uploadPanel.setOpaque(false); // our upload Panel that will contain all related components
        uploadPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        uploadBtn = new JButton("Upload"); // upload button
        uploadBtn.setPreferredSize(new Dimension(200, 100));
        uploadBtn.setFont(getFont().deriveFont(25f));
        uploadBtn.setForeground(Color.decode("#AF8EE4"));
        uploadBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel author = new JLabel("Author");
        author.setFont(getFont().deriveFont(40f));
        author.setForeground(Color.WHITE);
        authorField = new JTextField("Your Username", 20);
        authorField.setFont(getFont().deriveFont(25f));
        authorField.setBorder(null);

        JLabel fileName = new JLabel("FileName");
        fileName.setFont(getFont().deriveFont(40f));
        fileName.setForeground(Color.WHITE);
        filenameField = new JTextField("Set Unique File Name", 20);
        filenameField.setFont(getFont().deriveFont(25f));
        filenameField.setBorder(null);

        JLabel path = new JLabel("PATH");
        path.setFont(getFont().deriveFont(40f));
        path.setForeground(Color.WHITE);
        pathField = new JTextField(20);
        pathField.setFont(getFont().deriveFont(25f));
        pathField.setBorder(null);

        JLabel pathDisk = new JLabel("Find your file on the disk>>");
        pathDisk.setFont(getFont().deriveFont(20f));
        pathDisk.setForeground(Color.WHITE);
        pathDiskLabel = new JLabel("Path");
        pathDiskLabel.setFont(getFont().deriveFont(20f));
        pathDiskLabel.setForeground(Color.BLACK);
        pathDiskLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        uploadPanel.add(author, "gaptop 20, center, wrap");
        uploadPanel.add(authorField, "wrap");
        uploadPanel.add(fileName, "gaptop 20, center, wrap");
        uploadPanel.add(filenameField, "wrap");
        uploadPanel.add(path, "gaptop 20, center, wrap");
        uploadPanel.add(pathField, "wrap");
        uploadPanel.add(pathDisk, "gaptop 10, center, split 2");
        uploadPanel.add(pathDiskLabel);
        uploadPanel.add(uploadBtn,"gaptop 80, split 2, south");

        workshopContainer.add(uploadPanel, "Upload");
        background.add(workshopContainer);


        uploadBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                boolean result = WorkshopFileOperations.tryUploadXMLFILE(WorkshopView.getAuthorField().getText(),WorkshopView.getFilenameField().getText(),WorkshopView.getPathField().getText());
                if (!result) {
                    JOptionPane.showMessageDialog(jFrame, "File name has been used or File path is wrong, please try again.");
                } else {
                    JOptionPane.showMessageDialog(jFrame, "Upload succeed.");
                }
            }
        });

        pathDiskLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                showFileOpenDialog(uploadPanel);
            }
        });

    }


    /**
     * Displays the download view.
     */
    protected void download() {
        downloadPanel = new JPanel(new MigLayout("center", "[]20[]20[]"));
        downloadPanel.setOpaque(false); // our upload Panel that will contain all related components
        downloadPanel.setAlignmentX(Component.CENTER_ALIGNMENT);


        searchBtn = new JButton("Search"); // upload button
        searchBtn.setPreferredSize(new Dimension(100, 40));
        searchBtn.setFont(getFont().deriveFont(25f));
        searchBtn.setForeground(Color.decode("#AF8EE4"));
        searchBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        SearchField = new JTextField(30);
        SearchField.setFont(getFont().deriveFont(25f));
        SearchField.setBorder(null);

        Label result = new Label("Result", getFont());
        result.setForeground(Color.decode("#181A2D"));

        JLabel posText = new Label("Most Related" , getFont());
        JLabel authorText = new Label("AUTHOR", getFont());
        JLabel filenameText = new Label("FILENAME", getFont());


        downloadPanel.add(SearchField,"span 5,center");
        downloadPanel.add(searchBtn,"span 3,center,wrap");
        downloadPanel.add(result, "span 10, center, wrap");
        downloadPanel.add(posText,"span 3, center");
        downloadPanel.add(authorText,"span 3,center");
        downloadPanel.add(filenameText,"span 3, center,wrap");

        ArrayList<JLabel> position = new ArrayList<>(); // Create an ArrayList object
        ArrayList<JLabel> authors = new ArrayList<>();
        ArrayList<JLabel> filenames = new ArrayList<>();

        int pos = 1;
        for (XMLFileDetails xmlFileDetail : xmlFileDetails) {
            position.add(new Label(String.valueOf(pos), getFont()));
            authors.add(new Label(xmlFileDetail.getAuthor(), getFont()));
            filenames.add(new Label(xmlFileDetail.getFilename(), getFont()));
            pos++;
        }
        for (int i = 0; i < authors.size(); i++) {
            downloadPanel.add(position.get(i),"span 3,center");
            downloadPanel.add(authors.get(i),"span 3,center");
            downloadPanel.add(filenames.get(i), "span 3, center,wrap");

            int finalI = i;
            filenames.get(i).addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    boolean result = WorkshopFileOperations.tryDownloadXMLFILEbyFileName(filenames.get(finalI).getText());
                    if (!result) {
                        JOptionPane.showMessageDialog(jFrame, "Please try again.");
                    } else {
                        JOptionPane.showMessageDialog(jFrame, "Download succeed.");
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) { // changes the cursor and the background around the gamemode
                    super.mouseEntered(e);
                    filenames.get(finalI).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    filenames.get(finalI).setBorder(new LineBorder(Color.YELLOW, 3));
                }
                @Override
                public void mouseExited(MouseEvent e) { // reverts back to default settings
                    super.mouseExited(e);
                    filenames.get(finalI).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    filenames.get(finalI).setBorder(null);
                }
            });
        }

        workshopContainer.add(downloadPanel, "Download");
        background.add(workshopContainer);



        searchBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                xmlFileDetails= WorkshopFileOperations.trySearchXMLFILEbyAuthorANDFileName(WorkshopView.getSearchField().getText());
                updateDownloadPanel();

            }
        });


    }


    /**
     * If the user clicks to see the search result, then refresh the panels by retrieving the updated xml file details.
     */
    protected void updateDownloadPanel() {
        workshopContainer.remove(downloadPanel);
        download();
    }

    private static void showFileOpenDialog(Component parent) {

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory(new File("."));

        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        fileChooser.setMultiSelectionEnabled(false);

        fileChooser.setFileFilter(new FileNameExtensionFilter("xml(*.xml)", "xml"));

        int result = fileChooser.showOpenDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            pathField.setText(file.getAbsolutePath());
        }
    }


    public  JButton getWorkshopBtn() {
        return workshopBtn;
    }

    public JButton getUploadBtn() {
        return uploadBtn;
    }

    public JButton getSearchBtn() {
        return searchBtn;
    }

    public JLabel getPathDiskLabel() {
        return pathDiskLabel;
    }

    public static JTextField getSearchField() {
        return SearchField;
    }

    public static JTextField getAuthorField() {
        return authorField;
    }

    public static JTextField getFilenameField() {
        return filenameField;
    }

    public static JTextField getPathField() {
        return pathField;
    }

    public JButton getUpload() {
        return upload;
    }

    public JButton getDownload() {
        return download;
    }

    public JPanel getWorkshopContainer() {
        return workshopContainer;
    }

    public JPanel getUploadPanel() {
        return uploadPanel;
    }

    public JPanel getDownloadPanel() {
        return downloadPanel;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public JPanel getBackground() {
        return background;
    }

}
