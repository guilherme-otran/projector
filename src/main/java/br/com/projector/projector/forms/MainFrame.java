/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projector.projector.forms;

import br.com.projector.projector.dtos.ImportingMusicDTO;
import br.com.projector.projector.music_importing.ImportCallback;
import br.com.projector.projector.music_importing.ImporterFactory;
import br.com.projector.projector.music_importing.MusicUrlImporter;
import br.com.projector.projector.other.GeneralKeyboardDispatcher;
import br.com.projector.projector.other.ProgressDialog;
import br.com.projector.projector.other.ProgressDialog.Executor;
import br.com.projector.projector.other.WrappedTextCellRenderer;
import br.com.projector.projector.other.file_filters.ImageFileFilter;
import br.com.projector.projector.other.file_filters.TextFileFilter;
import br.com.projector.projector.projection.ProjectionManager;
import br.com.projector.projector.projection.TextWrapperFactoryChangeListener;
import br.com.projector.projector.projection.text.WrappedText;
import br.com.projector.projector.projection.text.WrapperFactory;
import br.com.projector.projector.repositories.MusicLoader;
import br.com.projector.projector.repositories.OpenMusicRepository;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import say.swing.JFontChooser;

/**
 *
 * @author 15096134
 */
public class MainFrame extends javax.swing.JFrame implements ListSelectionListener, GeneralKeyboardDispatcher.Listener {

    private final ProjectionManager projectionWindow;
    private File lastDirectory;
    private final OpenMusicRepository musicRepo;
    private boolean multiline;
    private final WrappedTextCellRenderer cellRenderer;

    /**
     * Creates new form MainFrame
     *
     * @param projectionWindow output window
     */
    public MainFrame(ProjectionManager projectionWindow) {
        this.projectionWindow = projectionWindow;
        this.musicRepo = new OpenMusicRepository();
        this.cellRenderer = new WrappedTextCellRenderer();
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jListMusics = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTablePhrases = new javax.swing.JTable();
        jButtonClearScreen = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemCreateMusic = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItemLoadFromFile = new javax.swing.JMenuItem();
        jMenuItemLoadFromLink = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemChangeFont = new javax.swing.JMenuItem();
        jMenuItemChangeBackground = new javax.swing.JMenuItem();
        jCheckBoxMenuItemCropBackground = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItemMultiline = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItemFullScreen = new javax.swing.JCheckBoxMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Projector");

        jListMusics.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListMusics.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListMusicsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jListMusics);

        jTablePhrases.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTablePhrases.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(jTablePhrases);

        jButtonClearScreen.setText("Limpar Tela (ESC)");
        jButtonClearScreen.setToolTipText("");
        jButtonClearScreen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearScreenActionPerformed(evt);
            }
        });

        jMenu1.setText("Arquivo");

        jMenuItemCreateMusic.setText("Criar Letra");
        jMenuItemCreateMusic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCreateMusicActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemCreateMusic);

        jMenu3.setText("Importar Letra");

        jMenuItemLoadFromFile.setText("Arquivo texto");
        jMenuItemLoadFromFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemLoadFromFileActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemLoadFromFile);

        jMenuItemLoadFromLink.setText("Inserir URL");
        jMenuItemLoadFromLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemLoadFromLinkActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemLoadFromLink);

        jMenu1.add(jMenu3);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Configurações");

        jMenuItemChangeFont.setText("Trocar fonte");
        jMenuItemChangeFont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemChangeFontActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemChangeFont);

        jMenuItemChangeBackground.setText("Alterar fundo");
        jMenuItemChangeBackground.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemChangeBackgroundActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemChangeBackground);

        jCheckBoxMenuItemCropBackground.setSelected(true);
        jCheckBoxMenuItemCropBackground.setText("Cortar Fundo");
        jCheckBoxMenuItemCropBackground.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemCropBackgroundActionPerformed(evt);
            }
        });
        jMenu2.add(jCheckBoxMenuItemCropBackground);

        jCheckBoxMenuItemMultiline.setText("Multiline");
        jCheckBoxMenuItemMultiline.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxMenuItemMultilineStateChanged(evt);
            }
        });
        jMenu2.add(jCheckBoxMenuItemMultiline);

        jCheckBoxMenuItemFullScreen.setText("Tela inteira");
        jCheckBoxMenuItemFullScreen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemFullScreenActionPerformed(evt);
            }
        });
        jMenu2.add(jCheckBoxMenuItemFullScreen);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonClearScreen, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonClearScreen, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemChangeFontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemChangeFontActionPerformed
        JFontChooser fontChooser = new JFontChooser();
        fontChooser.setSelectedFont(projectionWindow.getTextFont());

        int result = fontChooser.showDialog(this);
        if (result == JFontChooser.OK_OPTION) {
            projectionWindow.setTextFont(fontChooser.getSelectedFont());
        }
    }//GEN-LAST:event_jMenuItemChangeFontActionPerformed

    private void jMenuItemLoadFromFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLoadFromFileActionPerformed
        final JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new TextFileFilter());
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(true);

        if (lastDirectory != null) {
            fc.setCurrentDirectory(lastDirectory);
        }

        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            lastDirectory = fc.getCurrentDirectory();
            MusicLoader.loadFilesToRepository(fc.getSelectedFiles(), musicRepo);
        }

    }//GEN-LAST:event_jMenuItemLoadFromFileActionPerformed

    private void jListMusicsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListMusicsValueChanged
        int selectedMusic = jListMusics.getSelectedIndex();
        if (selectedMusic >= 0) {
            jTablePhrases.setModel(musicRepo.getPhrasesModel(selectedMusic));
        } else {
            jTablePhrases.setModel(new DefaultTableModel(0, 0));
        }
        updateRowHeights();
    }//GEN-LAST:event_jListMusicsValueChanged

    private void jCheckBoxMenuItemMultilineStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemMultilineStateChanged
        if (multiline == jCheckBoxMenuItemMultiline.isSelected()) {
            return;
        } else {
            multiline = jCheckBoxMenuItemMultiline.isSelected();
        }

        ListSelectionModel lsm = jTablePhrases.getSelectionModel();
        int position = lsm.getAnchorSelectionIndex();

        musicRepo.getGrouper().setWrapper(projectionWindow.getWrapperFactory().getTextWrapper(multiline));
        musicRepo.regroupPhrases();
        updateRowHeights();

        if (lsm.isSelectionEmpty()) {
            return;
        }

        if (position < 0) {
            return;
        }

        lsm.setSelectionInterval(position, position);
    }//GEN-LAST:event_jCheckBoxMenuItemMultilineStateChanged

    private void jMenuItemChangeBackgroundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemChangeBackgroundActionPerformed
        final JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new ImageFileFilter());
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(false);

        if (lastDirectory != null) {
            fc.setCurrentDirectory(lastDirectory);
        }

        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            lastDirectory = fc.getCurrentDirectory();
            projectionWindow.setBackgroundImageFile(fc.getSelectedFile());
        }
    }//GEN-LAST:event_jMenuItemChangeBackgroundActionPerformed

    private void jButtonClearScreenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearScreenActionPerformed
        clearScreen();
    }//GEN-LAST:event_jButtonClearScreenActionPerformed

    private void jMenuItemLoadFromLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLoadFromLinkActionPerformed
        final String url = JOptionPane.showInputDialog(this, "Digite a URL:");

        MusicUrlImporter importer = ImporterFactory.getFactory().getImporter(url);

        if (importer == null) {
            JOptionPane.showMessageDialog(null, "Falha: Site não suportado.");
            return;
        }

        Executor start = importer.getExecutor(new ImportCallback() {
            @Override
            public void onImportSuccess(ImportingMusicDTO music) {
                ManageMusicFrame frame = new ManageMusicFrame(music);
                frame.setOpenMusicRepository(musicRepo);
                frame.setVisible(true);
                frame.requestFocusInWindow();
            }

            @Override
            public void onImportError() {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao importar a música.");
            }
        });

        ProgressDialog pd = new ProgressDialog(this, start, "Carregando...");
        pd.getCancelButton().setText("Cancelar");
        pd.setVisible(true);
    }//GEN-LAST:event_jMenuItemLoadFromLinkActionPerformed

    private void jCheckBoxMenuItemFullScreenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemFullScreenActionPerformed
        projectionWindow.setFullScreen(jCheckBoxMenuItemFullScreen.isSelected());
    }//GEN-LAST:event_jCheckBoxMenuItemFullScreenActionPerformed

    private void jCheckBoxMenuItemCropBackgroundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemCropBackgroundActionPerformed
        projectionWindow.setCropBackground(jCheckBoxMenuItemCropBackground.isSelected());
    }//GEN-LAST:event_jCheckBoxMenuItemCropBackgroundActionPerformed

    private void jMenuItemCreateMusicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCreateMusicActionPerformed
        ManageMusicFrame frame = new ManageMusicFrame();
        frame.setOpenMusicRepository(musicRepo);
        frame.setVisible(true);
        frame.requestFocusInWindow();
    }//GEN-LAST:event_jMenuItemCreateMusicActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonClearScreen;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemCropBackground;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemFullScreen;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemMultiline;
    private javax.swing.JList<String> jListMusics;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItemChangeBackground;
    private javax.swing.JMenuItem jMenuItemChangeFont;
    private javax.swing.JMenuItem jMenuItemCreateMusic;
    private javax.swing.JMenuItem jMenuItemLoadFromFile;
    private javax.swing.JMenuItem jMenuItemLoadFromLink;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTablePhrases;
    // End of variables declaration//GEN-END:variables

    public void init() {
        multiline = jCheckBoxMenuItemMultiline.isSelected();

        projectionWindow.setTextWrapperChangeListener(new TextWrapperFactoryChangeListener() {
            @Override
            public void onWrapperFactoryChanged(WrapperFactory factory) {
                musicRepo.getGrouper().setWrapper(factory.getTextWrapper(multiline));
                musicRepo.regroupPhrases();
                updateRowHeights();
            }
        });

        jListMusics.setModel(musicRepo.getMusicsModel());

        jTablePhrases.setDefaultRenderer(WrappedText.class, cellRenderer);
        jTablePhrases.getSelectionModel().addListSelectionListener(this);
        setVisible(true);

        projectionWindow.setCropBackground(jCheckBoxMenuItemCropBackground.isSelected());

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new GeneralKeyboardDispatcher(this));
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        ListSelectionModel lsm = jTablePhrases.getSelectionModel();

        if (lsm.isSelectionEmpty()) {
            projectionWindow.setText(WrappedText.blankText());
            return;
        }

        cellRenderer.setMarker(-1);

        int music = jListMusics.getSelectedIndex();
        int selected = lsm.getAnchorSelectionIndex();

        WrappedText text = musicRepo.getTextFor(music, selected);
        projectionWindow.setText(text);

        if (music >= 0 && selected >= 0 && text.isEmpty()) {
            int newLine = selected;

            if (e.getFirstIndex() < selected) {
                newLine++;
            } else {
                newLine--;
            }

            if (newLine >= 0 && newLine < jTablePhrases.getRowCount()) {
                lsm.setSelectionInterval(selected, newLine);
            }
        }
    }

    private void updateRowHeights() {
        JTable table = jTablePhrases;
        for (int col = 0; col < table.getColumnCount(); col++) {
            for (int row = 0; row < table.getRowCount(); row++) {
                int rowHeight = table.getRowHeight();
                Component comp = table.prepareRenderer(table.getCellRenderer(row, col), row, col);
                Dimension d = comp.getPreferredSize();
                // first set the size to the new width
                comp.setSize(new Dimension(table.getWidth(), d.height));
                // then get the preferred size
                d = comp.getPreferredSize();
                rowHeight = Math.max(rowHeight, d.height);
                // finally set the height of the table
                table.setRowHeight(row, rowHeight);
            }
        }
    }

    @Override
    public void onKeyboardEscPressed() {
        clearScreen();
    }

    private void clearScreen() {
        int selected = jTablePhrases.getSelectionModel().getAnchorSelectionIndex();
        int rowCount = jTablePhrases.getRowCount();

        jTablePhrases.clearSelection();

        if (selected < 0 || selected >= rowCount) {
            return;
        }

        cellRenderer.setMarker(selected);
        jTablePhrases.repaint();
    }
}
