/**
 * todo: Сделать проверку на пустые строки.
 * */

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


class sw implements ActionListener, ItemListener {
    JLabel jLabelMessageCompareHash, jLabelMessageCheckSum, jLabelMessageChooseFile, jLabelTrueFalse;
    JTextField jTextFieldCompareHash, jTextFieldChooseFile;
    JButton jButtonGo, jButtonChooseFile;
    JFileChooser jFileOpen;
    JCheckBox md5, sha1, sha256;

    String fileadres;
    String algoritm;

    sw(){

        JFrame jfrm = new JFrame("Проверка CheckSum");

        // установить диспетчер компоновки GridBagLayout
        jfrm.setLayout(new GridBagLayout()); // определяет расположение компоновки

        jfrm.setSize(600, 200);
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jLabelMessageCompareHash = new JLabel("Сравниваемый хеш:");
        jTextFieldCompareHash = new JTextField(); // форма ввода 70
        jLabelMessageChooseFile = new JLabel("Выбрать файл:");
        jButtonChooseFile = new JButton("...");
        jTextFieldChooseFile = new JTextField(); // 70

        jButtonGo = new JButton("Go");
        jFileOpen = new JFileChooser();
        jLabelMessageCheckSum = new JLabel();

        jLabelTrueFalse = new JLabel();

        md5 = new JCheckBox("MD5");
        sha1 = new JCheckBox("SHA-1");
        sha256 = new JCheckBox("SHA-256");

        //**********************************
        // СООБЩЕНИЕ - Выбрать файл -
        jfrm.add(jLabelMessageChooseFile, new GridBagConstraints(0, 0, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER,
                new Insets(2, 2, 2, 2), 0, 0));

        // КНОПКА для выбора файла
        jfrm.add(jButtonChooseFile, new GridBagConstraints(1, 1, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 0, 0));

        // ПОЛЕ вывода выбора файла
        jfrm.add(jTextFieldChooseFile, new GridBagConstraints(0, 1, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));

        // Чек боксы
        // - md5 -
        jfrm.add(md5, new GridBagConstraints(0, 2, 1, 1, 1, 0,
                GridBagConstraints.LINE_START, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 0, 0));
        // - sha1 -
        jfrm.add(sha1, new GridBagConstraints(0, 2, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 0, 0));
        // - sha256 -
        jfrm.add(sha256, new GridBagConstraints(0, 2, 1, 1, 1, 0,
                GridBagConstraints.LINE_END, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 0, 0));

        /// СООБЩЕНИЕ вставки хеша-кода
        jfrm.add(jLabelMessageCompareHash, new GridBagConstraints(0, 3, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER,
                new Insets(2, 2, 2, 2), 0, 0));

        // КНОПКА Go
        jfrm.add(jButtonGo, new GridBagConstraints(1, 4, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 0, 0));

        // ПОЛЕ ввода хеш суммы
        jfrm.add(jTextFieldCompareHash, new GridBagConstraints(0, 4, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));

        // Вывод результата (true & false)
        jfrm.add(jLabelTrueFalse, new GridBagConstraints(1, 5, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0));

        // вывод результата
        jfrm.add(jLabelMessageCheckSum, new GridBagConstraints(0, 5, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0));

        //Работа чекбоксов
        md5.addItemListener(this);
        sha1.addItemListener(this);
        sha256.addItemListener(this);

        jButtonChooseFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jFileOpen.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

                int ret = jFileOpen.showDialog(null, "Выбрать файл");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    //file = jFileOpen.getSelectedFile().getName();
                    fileadres = jFileOpen.getSelectedFile().getAbsolutePath();
                    jTextFieldChooseFile.setText(fileadres);
                }
            }
        });
        jfrm.setVisible(true);

        jButtonGo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                try {
                    // Получаем контрольную сумму для файла в виде массива байт
                    final MessageDigest md = MessageDigest.getInstance(algoritm);
                    final FileInputStream fis = new FileInputStream(fileadres);
                    byte[] dataBytes = new byte[1024];
                    int bytesRead;
                    while((bytesRead = fis.read(dataBytes)) > 0) {
                        md.update(dataBytes, 0, bytesRead);
                    }
                    byte[] mdBytes = md.digest();

                    // Переводим контрольную сумму в виде массива байт в
                    // шестнадцатеричное представление
                    StringBuilder sb = new StringBuilder();
                    for(int i = 0; i < mdBytes.length; i++) {
                        sb.append(Integer.toString((mdBytes[i] & 0xff) + 0x100, 16)
                                .substring(1));
                    }
                    jLabelMessageCheckSum.setText("(" + algoritm + "): " + sb.toString());

                    // Сравнение хеша с вводимым пользователем,
                    // Сходит - true, не сходится - false
                    String str = jTextFieldCompareHash.getText().toString();
                         if (str.equals(sb.toString())) {
                            jLabelTrueFalse.setText("True");
                        } else {
                            jLabelTrueFalse.setText("False");
                        }

                } catch (FileNotFoundException | NoSuchAlgorithmException ex) {
                    Logger.getLogger(sw.class.getName())
                            .log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(sw.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    //Обработчик событий от чекбоксов
    public void itemStateChanged(ItemEvent ie) {
        if(md5.isSelected() == true) {
            algoritm = "MD5";
            sha1.setSelected(false);
            sha256.setSelected(false);
        }else if(sha1.isSelected() == true) {
            algoritm = "SHA-1";
            md5.setSelected(false);
            sha256.setSelected(false);
        }else if(sha256.isSelected() == true) {
            algoritm = "SHA-256";
            sha1.setSelected(false);
            md5.setSelected(false);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

public class main {
    public static void main(String[] args) {
        // создать рамку окна в потоке диспетчеризации событий
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new sw() {

                };
            }
        });
    }
}
/**
 * private static final String ALGORITHM = "SHA-1";
 *Например, доступными алгоритмами являются "SHA-256" и "MD5":
 *private static final String ALGORITHM = "SHA-256";
 *private static final String ALGORITHM = "MD5";
 * */