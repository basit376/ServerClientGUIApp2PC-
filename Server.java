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
class Server extends JFrame
{
   
   
   ServerSocket server;
    Socket socket;
   BufferedReader br;
   PrintWriter out;

   //Decleare Components 
   private JLabel heading= new JLabel("Server Area");
   private JTextArea messageArea = new JTextArea();
   private JTextField messageInPut = new JTextField();
   private Font font= new Font("Roboto",Font.PLAIN,20);
    //Constractor:
    public Server(){

        try{

           createGUI();
            server = new ServerSocket(7080);
           System.out.println("Server Is Ready To Accept Connection:");
           System.out.println("Waiting...");
           socket = server.accept();
           
        


           br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           out = new PrintWriter(socket.getOutputStream());

           
           handleEvents();
           startReading();
           //startWriting();



        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void handleEvents() {

       messageInPut.addKeyListener(new KeyListener(){

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

            //System.out.println("Key Released"+e.getKeyCode());
            if(e.getKeyCode() == 10){

                //System.out.println("You Have press Enter Button");
               
                
                String contentToSend= messageInPut.getText();
                messageArea.append("You: "+contentToSend+"\n");
                out.println(contentToSend);
                out.flush();
                messageInPut.setText("");
                
            }
            
        }
           
       });
    }

    private void createGUI(){

        this.setTitle("Server Messanger[End]");
        this.setSize(600,650);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Coding For Components
        heading.setFont(font);
        messageArea.setFont(font);
        messageInPut.setFont(font);
        heading.setIcon(new ImageIcon("clogo.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        messageInPut.setHorizontalAlignment(SwingConstants.CENTER);



        //set Layout
        this.setLayout(new BorderLayout());

        //Adding Components To The Frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane= new JScrollPane(messageArea);

        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInPut,BorderLayout.SOUTH);
        



        this.setVisible(true);
    }


    // Start Reading [Method]
    public void startReading(){

        //thread-Read The Data

        Runnable r1=()->{

            System.out.println("reader started..");

            while(true){

                try{

                   
                    String messageAreatString =br.readLine();
                    if(messageAreatString.equals("Exit")){
    
                        System.out.println("Client Terminated Chat");
                        JOptionPane.showMessageDialog(this, "Client Terminated Chat");
                        messageInPut.setEnabled(false);
                        socket.close();
                        break;

    
                    }
                    //System.out.println("Client: "+ msg);
                    messageArea.append("Client: "+messageAreatString+"\n");
                } catch(Exception e){
                    e.printStackTrace();
                }



               
            }


        };

        new Thread(r1).start();
    }

    

    // Start wrting send[Method]
    public void startWriting(){

        //Thread Get Data From user And Send To Client

        Runnable r2=()->{

            System.out.println("Writer Started....");

            while(true){
                try {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

                    String content = br1.readLine();

                    out.println(content);
                    out.flush();
                    
                } catch (Exception e) {
                    
                    e.printStackTrace();
                }
            }

        };

        new Thread(r2).start();


    }
    



    public static void main(String[] args){
        System.out.println("This is server .. Going To Start");
       

        new Server();
    }
}