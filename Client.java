import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class Client extends JFrame {

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    //Decleare Components 
    private JLabel heading= new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messagInput = new JTextField();
    private Font  font = new Font("Roboto",Font.PLAIN,20);

    public Client() {

        try {

            System.out.println("Sending Request To Server");
            socket = new Socket("127.0.0.1",7080);
            System.out.println("Connection Done..");

            br= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out= new PrintWriter(socket.getOutputStream());


            createGUI();
            handleEvents();
            startReading();
            //startWriting();

            
        } catch (Exception e) {
            
        }
    }

    private void handleEvents() {

        messagInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub

                //System.out.println("key released"+e.getKeyCode());
                if(e.getKeyCode()==10){

                    //System.out.println("You Have Press Enter Button");
                    String contentToSend= messagInput.getText();
                    messageArea.append("ME :"+contentToSend+"\n");
                    out.println(contentToSend); 
                    out.flush();
                    messagInput.setText("");
                    messagInput.requestFocus();
                }
                
            }

        });
    }

    private void createGUI(){

        // Gui Code... 

        this.setTitle("Client Messager[End]");
        this.setSize(600,650);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //coding For Components
        heading.setFont(font);
        messageArea.setFont(font);
        messagInput.setFont(font);
        heading.setIcon(new ImageIcon("clogo.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messagInput.setHorizontalAlignment(SwingConstants.CENTER);
        messageArea.setEditable(false);

        //Set Frame Layout 
        this.setLayout(new BorderLayout());

        //Adding the Components To The Frame 
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=  new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messagInput,BorderLayout.SOUTH);



        this.setVisible(true);

    }


    public void startReading(){

        //thread-Read The Data

        Runnable r1=()->{

            System.out.println("reader started..");

           try {
           
            while(true){

               

                    String msg =br.readLine();
                    if(msg.equals("Exit")){
    
                        System.out.println("Server Terminated The Chat");
                        JOptionPane.showMessageDialog(this, "Server Terminated The Chat");
                        messagInput.setEnabled(false);
                        break;

    
                    }
                    //System.out.println("Server: "+ msg);
                    messageArea.append("Server:"+ msg+"\n");
            }



               
            } catch (Exception e){
                e.printStackTrace();
            }


        };

        new Thread(r1).start();
    }

    

    public void startWriting(){

        //Thread Get Data From user And Send To Client

        Runnable r2=()->{

            System.out.println("Writer Started....");

            try{

            while(true){
                

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

                    String content = br1.readLine();

                    out.println(content);
                    out.flush();
                    
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        };

        new Thread(r2).start();


    }


    public static void main(String[] args){
        System.out.println("This Is Client");
        new Client();
    }
    
}
