import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.FontUIResource;

public class Calculator {
//TODO сделать всплывающие подсказки
    JFrame frame;
    JPanel mainPanel;
    JPanel optionalPanel;
    JButton btnCheck;
    JTextField tfPercent;
    JTextField tfNumOfMoney;
    JTextField tfResultMoney;
    JTextField tfResult;

    JLabel lblInfoWindow;

    JLabel lblCapitalization;
    JRadioButton rbCapMonth;
    JRadioButton rbCapWeek;
    ButtonGroup groupCap;

    JLabel lblMonthlyReplenishment;
    JCheckBox cbMonthlyReplenishment;
    JTextField tfSizeReplenishment;

    KeyAdapter onlyDigit;

    public Calculator() {
        frame = new JFrame();
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("unnamed.png")));
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("Рассчет сложного процента");
        frame.setResizable(false);
        frame.setLayout(null);

        mainPanel = new JPanel(new GridLayout(5, 1));
        mainPanel.setBounds(10, 10, 580, 300);
        frame.add(mainPanel);

        optionalPanel = new JPanel(null);
        //TODO подобрать картинку , и протестировать на верхней панели
//        Image backgroundImage = new ImageIcon(getClass().getClassLoader().getResource("imgon.png")).getImage();
//        optionalPanel = new JPanel(null){
//            @Override
//            protected void paintComponent(final Graphics g) {
//                g.drawImage(backgroundImage, 0, 0, null);
//            }
//        };
        optionalPanel.setBounds(10, 310, 565, 150);
        optionalPanel.setBorder(new BorderUIResource.TitledBorderUIResource("Дополнительные опции:"));

        frame.add(optionalPanel);
    }

    public void start() {

        btnCheck = new JButton("Рассчитать");
        btnCheck.setBackground(Color.DARK_GRAY);
        btnCheck.setForeground(Color.WHITE);
        btnCheck.setFont(new FontUIResource("", 9, 24));
        btnCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent actionEvent) {

                if (tfNumOfMoney.getText().equals("") || tfPercent.getText().equals("") || tfResultMoney.getText().equals("") ||
                        (cbMonthlyReplenishment.isSelected() && tfSizeReplenishment.getText().equals(""))) {
                    JOptionPane.showMessageDialog(frame, "Необходимо заполнить все поля! (3)", "БУДЬТЕ  ВНИМАТЕЛЬНЫ !!!", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double sum = Double.parseDouble(tfNumOfMoney.getText()); // начальная сумма
                double procent = Double.parseDouble(tfPercent.getText().replace(',', '.')); // процент в неделю
                double resultM = Double.parseDouble(tfResultMoney.getText());
                double replenishment;

                double daysCounter = 0;
                double weeks = 0;

                while (sum < resultM) { // сумма которую нужно достигнуть

                    daysCounter++;

                    if (daysCounter % 30 == 0 && rbCapMonth.isSelected()) {
                        sum += sum / 100 * procent; // ежемесечная капитализация
                        weeks += 4.3;
                    } else if (daysCounter % 7 == 0 && rbCapWeek.isSelected()){
                        sum += sum / 100 * procent; // еженедельная капитализация
                        weeks++;
                    }

                    if (cbMonthlyReplenishment.isSelected() && daysCounter % 30 == 0) {
                        replenishment = Double.parseDouble(tfSizeReplenishment.getText());
                        sum += replenishment;
                    }

                }
                double month = weeks / 4.3;

                tfResult.setText(String.format("%.2f", month) + " Месяцев");
                tfResult.setBackground(Color.DARK_GRAY);
                lblInfoWindow.setFont(new FontUIResource("", 2, 18));
                lblInfoWindow.setText("          ... IN GOD WE TRUST ...");
            }
        });

        onlyDigit = new KeyAdapter() {
            @Override
            public void keyTyped(final KeyEvent keyEvent) {

                char a = keyEvent.getKeyChar();
                if (!Character.isDigit(a) && a != ',' && a != '.') {
                    keyEvent.consume();
                }
            }
        };

        tfPercent = makeJTextField();

        tfNumOfMoney = makeJTextField();

        tfResultMoney = makeJTextField();

        tfResult = makeJTextField();
        tfResult.setEnabled(false);
        tfResult.setForeground(Color.red);

        JLabel lblPercent = makeJLabel("Введите % ставку =>", "вводит пользователь", Color.RED);
        JLabel lblNumOfMoney = makeJLabel("Стартовая сумма =>", "вводит пользователь", Color.RED);
        JLabel lblPlaningMoney = makeJLabel("Конечный капиталл =>", "вводит пользователь", Color.RED);
        JLabel lblResult = makeJLabel("Время ожидания =>", "ВЫВОД РЕЗУЛЬТАТА (округление до целого)", Color.BLUE);

        mainPanel.add(lblPercent);
        mainPanel.add(tfPercent);

        mainPanel.add(lblNumOfMoney);
        mainPanel.add(tfNumOfMoney);

        mainPanel.add(lblPlaningMoney);
        mainPanel.add(tfResultMoney);

        mainPanel.add(lblResult);
        mainPanel.add(tfResult);

        mainPanel.add(btnCheck);

        lblInfoWindow = new JLabel("     Да поможет Вам данная программа в расчетах :)");
        lblInfoWindow.setFont(new FontUIResource("", 2, 10));
        lblInfoWindow.setForeground(new Color(4, 65, 4));
        mainPanel.add(lblInfoWindow);

        lblCapitalization = new JLabel("Тип капитализации:");
        lblCapitalization.setSize(220, 50);
        lblCapitalization.setLocation(10, 15);
        lblCapitalization.setFont(new Font("", 3, 20));
        optionalPanel.add(lblCapitalization);

        rbCapMonth = new JRadioButton("month");
        JLabel lblMonth = new JLabel("Месяц");
        lblMonth.setBounds(390, 30, 40, 20);
        rbCapMonth.setSize(20, 20);
        rbCapMonth.setLocation(370, 30);

        rbCapWeek = new JRadioButton("week", true);
        JLabel lblWeek = new JLabel("Неделя");
        lblWeek.setBounds(310, 30, 60, 20);
        rbCapWeek.setSize(20, 20);
        rbCapWeek.setLocation(290, 30);

        groupCap = new ButtonGroup();
        groupCap.add(rbCapWeek);
        groupCap.add(rbCapMonth);

        optionalPanel.add(rbCapWeek);
        optionalPanel.add(lblWeek);
        optionalPanel.add(rbCapMonth);
        optionalPanel.add(lblMonth);

        lblMonthlyReplenishment = new JLabel("Ежемесячное пополнение:");
        lblMonthlyReplenishment.setSize(280, 50);
        lblMonthlyReplenishment.setLocation(10, 80);
        lblMonthlyReplenishment.setFont(new Font("", 3, 20));
        optionalPanel.add(lblMonthlyReplenishment);

        cbMonthlyReplenishment = new JCheckBox();
        cbMonthlyReplenishment.setLocation(290, 97);
        cbMonthlyReplenishment.setSize(20, 20);
        cbMonthlyReplenishment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent actionEvent) {
                if (cbMonthlyReplenishment.isSelected()) {
                    tfSizeReplenishment.setEnabled(true);
                } else {
                    tfSizeReplenishment.setText("");
                    tfSizeReplenishment.setEnabled(false);
                }
            }
        });
        optionalPanel.add(cbMonthlyReplenishment);

        JLabel sumReplenish = new JLabel("Сумма:");
        sumReplenish.setBounds(315, 97, 60, 20);
        optionalPanel.add(sumReplenish);

        tfSizeReplenishment = new JTextField(7);
        tfSizeReplenishment.addKeyListener(onlyDigit);
        tfSizeReplenishment.setLocation(380, 97);
        tfSizeReplenishment.setSize(50, 20);
        tfSizeReplenishment.setEnabled(false);
        optionalPanel.add(tfSizeReplenishment);

        URL url = getClass().getResource("dollar.jpg");
        ImageIcon imageicon = new ImageIcon( url );

        JLabel picture = new JLabel(imageicon);
        picture.setBounds(440, 20, 110, 110);
        optionalPanel.add(picture);

        frame.setVisible(true);
    }

    private JLabel makeJLabel(String title, String Border, Color color) {
        JLabel jLabel = new JLabel(title);
        jLabel.setFont(new FontUIResource("", 2, 18));
        jLabel.setForeground(color);
        jLabel.setBorder(new BorderUIResource.TitledBorderUIResource(Border));
        return jLabel;
    }

    private JTextField makeJTextField() {
        JTextField textField = new JTextField(20);
        textField.addKeyListener(onlyDigit);
        textField.setBackground(Color.LIGHT_GRAY);
        textField.setFont(new FontUIResource("", 2, 24));
        return textField;
    }
}
