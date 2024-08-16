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
        button1.addActionListener(e -> {
            textField.setText("0");
            exprLabel.setText("");
            expression = "";
            token.clear();
            result = "";
            num = false;
            dot = false;
        });
        button1.setFont(new Font("Calibri Light", Font.PLAIN, 17));
        buttonPanel.add(button1);

        JButton button2 = new JButton("DEL");
        button2.addActionListener(e -> {
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
        });

        //Button for constant PI
        JButton button3 = new JButton("<html><body><span>π</span></body></html>");
        button3.setFont(new Font("Calibri Light", Font.PLAIN, 17));
        button3.addActionListener(e -> {
            if(! "0".equals(expression)){
                textField.setText(textField.getText() + Character.toString((char)960));
            } else {
                textField.setText(Character.toString((char)960));
            }
            expression +=",pi";
            num = false;
            dot = false;
        });
        buttonPanel.add(button3);

        //button for x^y
        JButton button4 = new JButton("<html><body><span>X<sup>y</sup></span></body></html>");
        button4.setFont(new Font("Calibri Light", Font.PLAIN, 17));
        button4.addActionListener(e -> {
            if(! "0".equals(textField.getText())){
                textField.setText(textField.getText() + "^");
                expression+=",^";
            } else {
                textField.setText("^");
                expression += ",0,^";
            }
            num = false;
            dot = false;
        });
        buttonPanel.add(button4);
        //factorial button
        JButton button5 = new JButton("x!");
        button5.setFont(new Font("Calibri Light", Font.PLAIN, 17));
        button5.addActionListener(e -> {
            if(! "0".equals(textField.getText())){
                textField.setText(textField.getText() + "!");
                expression+=",!";
            } else  {
                textField.setText(textField.getText() + "0!");
                expression += ",0,!";
            }
        });
        buttonPanel.add(button5);
        //sin button
        JButton button6 = new JButton("sin");
        button6.setFont(new Font("Calibri Light", Font.PLAIN, 17));
        button6.addActionListener(e -> {
            if(! "0".equals(textField.getText())){
                textField.setText(textField.getText() + "sin(");
            } else {
                textField.setText("sin(");
            }
            expression=",sin(";
            num = false;
            dot = false;
        });
        buttonPanel.add(button6);

        JButton button7 = new JButton("(");
        button7.setFont(new Font("Calibri Light", Font.PLAIN, 17));
        button7.addActionListener(e -> {
            if(! "0".equals(textField.getText())) {
                textField.setText(textField.getText()+"(");
            }else {
                textField.setText("(");
            }
            expression+=",(";
            num = false;
            dot = false;
        });
        buttonPanel.add(button7);

        JButton button8 = new JButton(")");
        button8.setFont(new Font("Calibri Light", Font.PLAIN, 17));
        button8.addActionListener(e -> {
            if(! "0".equals(textField.getText())) {
                textField.setText(textField.getText()+")");
            }else {
                textField.setText(")");
            }
            expression+=",)";
            num = false;
            dot = false;
        });
        buttonPanel.add(button8);
        // exponential button
        JButton button9 = new JButton("e");
        button9.setFont(new Font("Calibri Light", Font.PLAIN, 17));
        button9.addActionListener(e -> {
            if(! "0".equals(textField.getText())) {
                textField.setText(textField.getText()+"e");
            }else{
                textField.setText("e");
            }
            expression+=",e";
            num = false;
            dot = false;
        });
        buttonPanel.add(button9);
        //sqrt button
        JButton button10 = new JButton("<html><body><span>√</span></body></html>");
        button10.setFont(new Font("Calibri Light", Font.PLAIN, 17));
        button10.addActionListener(e -> {
            if(! "0".equals(textField.getText())) {
                textField.setText(textField.getText()+Character.toString((char)8730));
            } else {
                textField.setText(Character.toString((char)8730));
            }
            expression +=",sqrt";
            num = false;
            dot = false;
        });
        buttonPanel.add(button10);
        //cos button
        JButton button11 = new JButton("cos");
        button11.setFont(new Font("Calibri Light", Font.PLAIN, 17));
        button11.addActionListener(e -> {
            if(! "0".equals(textField.getText())) {
                textField.setText(textField.getText()+"cos(");
            }else {
                textField.setText("cos(");
            }
            expression+=",cos,(";
            num=false;
            dot=false;
        });
        buttonPanel.add(button11);

        JButton button12 = new JButton("7");
        button12.setFont(new Font("Calibri Light", Font.PLAIN, 17));
        button12.setBackground(new Color(220,220,220));
        button12.addActionListener(e -> {
            if(! "0".equals(textField.getText())) {
                textField.setText(textField.getText()+"7");
            }else {
                textField.setText("7");
            }
            if(num){
                expression+="7";
            }else{
                expression+=",7";
            }
            num= true;
        });
        buttonPanel.add(button12);

        JButton button13 = new JButton("8");
        button13.setFont(new Font("Calibri Light", Font.PLAIN, 17));
        button13.setBackground(new Color(220,220,220));
        button13.addActionListener(e -> {
            if(! "0".equals(textField.getText())) {
                textField.setText(textField.getText()+"8");
            }else {
                textField.setText("8");
            }
            if(num) {
                expression+="8";
            }else {
                expression+=",8";
            }
            num=true;
        });
        buttonPanel.add(button13);

        JButton button14 = new JButton("9");
        button14.setFont(new Font("Calibri Light", Font.PLAIN, 17));
        button14.setBackground(new Color(220,220,220));
        button14.addActionListener(e -> {
            if(! "0".equals(textField.getText())) {
                textField.setText(textField.getText()+"9");
            }else {
                textField.setText("9");
            }
            if(num) {
                expression+="9";
            }else {
                expression+=",9";
            }
            num=true;
        });
        buttonPanel.add(button14);
        // division sign
        JButton button15 = new JButton("<html><body><span>÷</span></body></html>");
        button15.setFont(new Font("Calibri Light", Font.PLAIN, 17));
        button15.addActionListener(e -> {
            String s = textField.getText();
            if(s.equals("0")){
                expression+="0";
            }
            if(s.charAt(s.length()-1)=='-' || s.charAt(s.length()-1) == 'x' || s.charAt(s.length()-1) == '+'){
                String newString = s.substring(0, s.length()-1);
                textField.setText(newString + Character.toString((char)247));
                expression = expression.substring(0, expression.length()-1);
                expression+='/';
            }else if(s.charAt(s.length()-1 ) != (char)247){
                textField.setText(s+Character.toString((char)247));
                expression +='/';
            }else {
                textField.setText(s);
            }
            num = false;
            dot = false;
        });
        buttonPanel.add(button15);

        //tan button
        JButton button16 = new JButton("tan");
        button16.setFont(new Font("Calibri Light", Font.PLAIN, 17));
        button16.addActionListener(e -> {
            if(! "0".equals(textField.getText())) {
                textField.setText(textField.getText()+"tan");
            }else {
                textField.setText("tan");
            }
            expression+=",tan";
            num = false;
            dot = false;
        });
        buttonPanel.add(button16);

        JButton button17 = new JButton("4");
        button17.setFont(new Font("Calibri Light", Font.PLAIN, 17));
        button17.setBackground(new Color(220,220,220));
        button17.addActionListener(e -> {
            if(! "0".equals(textField.getText())) {
                textField.setText(textField.getText()+"4");
            } else {
                textField.setText("4");
            }
            if(num){
                expression+="4";
            }else{
                expression+=",4";
            }
            num = true;
            dot = false;
        });
        buttonPanel.add(button17);

        JButton button18 = new JButton("5");
        button18.setFont(new Font("Calibri Light", Font.PLAIN, 17));
        button18.setBackground(new Color(220,220,220));
        button18.addActionListener(e -> {
            if(! "0".equals(textField.getText())) {
                textField.setText(textField.getText()+"5");
            }else{
                textField.setText("5");
            }
            if(num){
                expression+="5";
            }else{
                expression+=",5";
            }
            num = true;
            dot = false;
        });
        buttonPanel.add(button18);

        JButton button19 = new JButton("6");
        button19.setFont(new Font("Calibri Light", Font.PLAIN, 17));
        button19.setBackground(new Color(220,220,220));
        button19.addActionListener(e -> {
            if(! "0".equals(textField.getText())) {
                textField.setText(textField.getText()+"6");
            }else {
                textField.setText("6");
            }
            if(num){
                expression+="6";
            }else{
                expression+=",6";
            }
            num = true;
            dot = false;
        });
        buttonPanel.add(button19);

        JButton button20 = new JButton("x");
        button20.setFont(new Font("Calibri Light", Font.PLAIN, 17));
        button20.addActionListener(e -> {
            String s = textField.getText();
            if(s.equals("0")){
                expression+="0";
            }
            if(s.charAt(s.length()-1)=='-' || s.charAt(s.length()-1) == '+' || s.charAt(s.length()-2) == (char)(247)){
                String newString = s.substring(0, s.length()-1);
                newString += "x";
                textField.setText(newString);
                expression = expression.substring(0, expression.length()-1);
                expression +="x";
            }else if(s.charAt(s.length()-1 ) != 'x'){
                s+="x";
                textField.setText(s);
                expression += ",x";
            }else {
                textField.setText(s);
            }
            num = false;
            dot = false;
        });
        buttonPanel.add(button20);


        frmCalculator.setBounds(200,100,400,500);
        frmCalculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}