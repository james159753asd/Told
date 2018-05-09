package edu.fgu.dclab;

import javafx.scene.chart.PieChart;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.Date;
import java.text.SimpleDateFormat;





public class Servant implements Runnable
{

    private ObjectOutputStream out = null;
    private String source = null;
    private Socket socket = null;
    private ChatRoom room = null;

    Date date = new Date();




    public Servant(Socket socket, ChatRoom room)
    {
        this.room = room;
        this.socket = socket;

        try
        {
            this.out = new ObjectOutputStream
            (
                this.socket.getOutputStream()
            );
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        greet();
        // 初始化 Date 对象

        //write(new ChatMessage("MurMur", room.toString()));
        //write(new ChatMessage("MurMur", socket.toString()));

        // 使用 toString() 函数显示日期时间
        //System.out.println(date.toString());
    }


    public void process(Message message)
    {
        switch (message.getType())
        {
            case Message.ROOM_STATE:
                this.write(message);
                break;

            case Message.CHAT:
                String msg=((ChatMessage)message).MESSAGE;
                if (msg.equals("time")){

                //String ID= ((LoginMessage) message).ID;
                    if ( message.getSource().equals(source))
                    {

                        this.write(new ChatMessage("MurMur", date.toString()));

                    }}
                else
                this.write(message);
                break;

            case Message.LOGIN:
                if (this.source == null)
                {
                    this.source = ((LoginMessage) message).ID;
                    this.room.multicast(new ChatMessage
                    (
                        "MurMur",
                        MessageFormat.format("{0} 進入了聊天室。", this.source)
                    ));

                    this.room.multicast(new RoomMessage(
                        room.getRoomNumber(),
                        room.getNumberOfGuests()
                    ));
                }
                break;

            default:
        }
    }


    private void write(Message message)
    {
        try
        {
            this.out.writeObject(message);
            this.out.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void greet()
    {
        String[] greetings =
        {
            "歡迎來到 MurMur 聊天室",
            "請問你的【暱稱】?"
        };

        for (String msg : greetings)
        {
            write(new ChatMessage("MurMur", msg));
        }
    }



    @Override
    public void run()
    {
        Message message;
        Date date = new Date();
        try
        (ObjectInputStream in = new ObjectInputStream(this.socket.getInputStream()))
        {
            this.process((Message)in.readObject());

            while ((message = (Message) in.readObject()) != null)
            {
                /*
                if((message = (Message) in.readObject()) == null)
                {
                    System.out.println("測試");
                }
                */
                this.room.multicast(message);

            //    System.out.println("測試");
            //    System.out.println(date.toString());

            }



            this.out.close();

        }
        catch (IOException e)
        {
            System.out.println("Servant: I/O Exc eption");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}

// Servant.java