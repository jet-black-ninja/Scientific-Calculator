import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
class ScientificCalculator {
    JFrame frmCalculator;
    String result = "";
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
        int p = switch (x) {
            case "+" -> 1;
            case "-" -> 2;
            case "x" -> 3;
            case "/" -> 4;
            case "^" -> 6;
            case "!" -> 7;
            default -> 10;
        };
        return p;
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
        String tokens[] = p.split(",");
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

    //TODO commented for now
//    private void calculateMain(){
//        String tokens[]=expression.split(",");
//        for(String s : tokens){
//            if(!s.isEmpty() && !s.equals(" ") && !s.equals("\n") && !s.equals("  ") ){
//                token.add(s);
//            }
//        }
//        try{
//            double res = Eval(infixToPostfix());
//            result = Double.toString(res);
//        }catch(Exception _){}
//    }

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
    }
}