import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
class ScientificCalculator {
    JFrame frmCalculator;
    String result = "", expression = "";
    ArrayList<String> token = new ArrayList<String>();
    boolean num = false;
    boolean dot = false;

    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try{
                    ScientificCalculator window = new ScientificCalculator();
                    window.frmCalculator.setVisible(true);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    ScientificCalculator(){
        initialize();
    }

    int precedence (String x){
        return switch (x) {
            case "+" -> 1;
            case "-" -> 2;
            case "x" -> 3;
            case "/" -> 4;
            case "^" -> 6;
            case "!" -> 7;
            default -> 10;
        };
    }

    private boolean isOperator(String x){
        return x.equals("+") || x.equals("-") || x.equals("x") || x.equals("/") || x.equals("sqrt") || x.equals("^") || x.equals("!") || x.equals("sin") || x.equals("cos") || x.equals("tan") || x.equals("log") || x.equals("ln");
    }

    private String infixToPostfix() {
        Stack<String> s = new Stack<String>();
        String y;
        int flag;
        StringBuilder p = new StringBuilder();
        token.add(")");
        s.push("(");
        for(String i : token){
            if(i.equals("(")){
                s.push(i);
            } else if(i.equals(")")){
                y = s.pop();
                while(!y.equals("(")){
                    p.append(y).append(",");
                    y = s.pop();
                }
            } else if(isOperator(i)) {
                y = s.pop();
                flag = 0;
                if(isOperator(y) && precedence(y)> precedence(i)){
                    p.append(y).append(",");
                    flag = 1;
                }
                if(flag == 0){
                    s.push(y);
                }
            }else {
                p.append(i).append(",");
            }
        }
        while(!s.empty()){
            y = s.pop();
            if(!y.equals("(") && !y.equals(")")){
                p.append(y).append(",");
            }
        }
        return p.toString();
    }

    private double factorial(double y) {
        double fact = 1;
        if (y == 0 || y == 1) {
            return 1;
        } else {
            for(int i = 0 ; i<= y ; i++){
                fact *=i;
            }
        }
        return fact;
    }

    private double calculate(double x, double y, String c){
        return switch(c){
            case "-" -> x-y;
            case "+" -> x+y;
            case "x" -> x*y;
            case "/" -> x/y;
            case "^" -> Math.pow(x, y);
            default -> 0;
        };
    }

    private double calculate(double y, String c){
        return switch(c){
            case "log" ->Math.log10(y);
            case "ln" ->Math.log(y);
            case "sin" -> Math.sin(y);
            case "cos" -> Math.cos(y);
            case "tan" -> Math.tan(y);
            case "sqrt" -> Math.sqrt(y);
            case "!" -> factorial(y);
            default -> 0;
        };
    }

    private  double Eval(String p){
        String[] tokens = p.split(",");
        ArrayList<String> token2 = new ArrayList<String>();
        for (String s : tokens) {
            if (!s.isEmpty() && !s.equals(" ") && !s.equals("\n") && !s.equals("  ")) {
                token2.add(s);
            }
        }
        Stack<Double> s = new Stack<Double>();
        double x, y;
        for(String i : token2) {
            if (isOperator(i)){
                if (i.equals("sin") || i.equals("cos") || i.equals("tan") || i.equals("log") || i.equals("ln") || i.equals("sqrt") || i.equals("!")) {
                    y = s.pop();
                    s.push(calculate(y, i));
                } else {
                    y = s.pop();
                    x = s.pop();
                    s.push(calculate(x, y, i));
                }
            } else {
                if(i.equals("pi"))
                    s.push(Math.PI);
                else if(i.equals("e"))
                    s.push(Math.E);
                else
                    s.push(Double.valueOf(i));
            }
        }
        double res = 1;
        while(!s.empty()){
            res *= s.pop();
        }
        return res;
    }

    private void calculateMain(){
        String[] tokens =expression.split(",");
        for(String s : tokens){
            if(!s.isEmpty() && !s.equals(" ") && !s.equals("\n") && !s.equals("  ") ){
                token.add(s);
            }
        }
        try{
            double res = Eval(infixToPostfix());
            result = Double.toString(res);
        }catch(Exception _){}
    }

    private void initialize(){
        // make a new JFrame from swing and set its properties
        frmCalculator = new JFrame();
        frmCalculator.setResizable(false);
        frmCalculator.setTitle("Calculator");
        frmCalculator.getContentPane().setBackground(new Color(172, 170, 255));
        frmCalculator.getContentPane().setFont(new Font("Calibri" , Font.PLAIN,15));
        frmCalculator.getContentPane().setForeground(SystemColor.windowBorder);
        frmCalculator.getContentPane().setLayout(null);

        //make a new panel , define property and add it to frmCalculator
        JPanel textPanel = new JPanel();
        textPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null ,null , null , null));
        textPanel.setBounds(34, 25, 316, 80);
        frmCalculator.getContentPane().add(textPanel);
        textPanel.setLayout(null);

        //make JLabel, set properties and add it to textPanel
        JLabel exprLabel = new JLabel("");
        exprLabel.setBackground(SystemColor.control);
        exprLabel.setFont(new Font("Raleway", Font.PLAIN, 20));
        exprLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        exprLabel.setForeground(UIManager.getColor("Button.disabledForeground"));
        exprLabel.setBounds(2,2, 312, 27);
        textPanel.add(exprLabel);

        //make a text field, define its properties and add it to textPanel
        JTextField textField = new JTextField();
        exprLabel.setLabelFor(textField);
        textField.setHorizontalAlignment(SwingConstants.RIGHT);
        textField.setBackground(SystemColor.control);
        textField.setEditable(false);
        textField.setText("0");
        textField.setBorder(null);
        textField.setFont(new Font("Raleway", textField.getFont().getStyle(), 32));
        textField.setBounds(2,30,312,49);
        textPanel.add(textField);
        textField.setColumns(10);

        // make a button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null , null ,null ,null));
        buttonPanel.setBackground(SystemColor.inactiveCaptionBorder);
        buttonPanel.setBounds(34, 120,316,322);
        frmCalculator.getContentPane().add(buttonPanel);
        buttonPanel.setLayout(new GridLayout(0,5,0,0));

        //Clear button
        JButton button1 = new JButton("C");
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textField.setText("0");
                exprLabel.setText("");
                expression = "";
                token.clear();
                result = "";
                num = false;
                dot = false;
            }
        });
        button1.setFont(new Font("Calibri Light", Font.PLAIN, 17));
        buttonPanel.add(button1);

        JButton button2 = new JButton("DEL");
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String s = textField.getText();
                if(s.length() > 1){
                    String newString = s.substring(0, s.length()-1);
                    textField.setText(newString);
                    if(expression.charAt(expression.length()-1) == '.'){
                        dot = false;
                    }
                    if(expression.charAt(expression.length()-1) == ','){
                        expression = expression.substring(0, expression.length() -2);
                    } else {
                        expression = expression.substring(0, expression.length() -1);
                    }
                } else {
                    textField.setText("0");
                    expression = "";
                }
            }
        });

        //Button for constant PI
        JButton button3 = new JButton("<html><body><span>π</span></body></html>");
        button3.setFont(new Font("Calibri Light", Font.PLAIN, 17));
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(! "0".equals(expression)){
                    textField.setText(textField.getText() + Character.toString((char)960));
                } else {
                    textField.setText(Character.toString((char)960));
                }
                expression +=",pi";
                num = false;
                dot = false;
            }
        });
        buttonPanel.add(button3);

        //button for x^y
        JButton button4 = new JButton("<html><body><span>X<sup>y</sup></span></body></html>");
        button4.setFont(new Font("Calibri Light", Font.PLAIN, 17));
        button4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(! "0".equals(textField.getText())){
                    textField.setText(textField.getText() + "^");
                    expression+=",^";
                } else {
                    textField.setText("^");
                    expression += ",0,^";
                }
                num = false;
                dot = false;
            }
        });
        buttonPanel.add(button4);
        //factorial button
        JButton button5 = new JButton("x!");
        button5.setFont(new Font("Calibri Light", Font.PLAIN, 17));
        button5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(! "0".equals(textField.getText())){
                    textField.setText(textField.getText() + "!");
                    expression+=",!";
                } else  {
                    textField.setText(textField.getText() + "0!");
                    expression += ",0,!";
                }
            }
        });
        buttonPanel.add(button5);
        //sin button
        JButton button6 = new JButton("sin");
        button6.setFont(new Font("Calibri Light", Font.PLAIN, 17));
        button6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(! "0".equals(textField.getText())){
                    textField.setText(textField.getText() + "sin(");
                } else {
                    textField.setText("sin(");
                }
                expression=",sin(";
                num = false;
                dot = false;
            }
        });
        buttonPanel.add(button6);

        JButton button7 = new JButton("(");
        button7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(! "0".equals(textField.getText())) {
                    textField.setText(textField.getText()+"(");
                }else {
                    textField.setText("(");
                }
                expression+=",(";
                num=false;
                dot=false;
            }
        });
        button7.setFont(new Font("Calibri Light", Font.PLAIN, 17));
        buttonPanel.add(button7);

        JButton button8 = new JButton(")");
        button8.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(! "0".equals(textField.getText())) {
                    textField.setText(textField.getText()+")");
                }else {
                    textField.setText(")");
                }
                expression+=",)";
                num=false;
                dot=false;
            }
        });
        buttonPanel.add(button8);



        frmCalculator.setBounds(200,100,400,500);
        frmCalculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}