package edu.fgu.dclab;

import javafx.scene.chart.PieChart;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class Servant implements Runnable
{

    private ObjectOutputStream out = null;
    private String source = null;
    private Socket socket = null;
    private ChatRoom room = null;
    private JSONObject j;
    private String lo="走廊";


    Date date = new Date();


 /*   public class roomimformation
    {
        String test1="{\"801\":{\"箱子\":\"100$\"}, " +
                "\"802\":{\"箱子2\":\"200$\"}}," +
                "\"803\":[{\"北門\":\"普通的門\"},{\"桌子\":\"硬幣\"},{\"瓦斯爐\":\"可以使用\"}]"   ;
    }
    */

    public Servant(Socket socket, ChatRoom room)
    {
        this.room = room;
        this.socket = socket;

        try
        {
            System.out.print(lo);
        //  String tmp = "{\"801\":{\"箱子\":\"100$\"},\"802\":{\"箱子2\":\"200$\"}}";
            String tmp = "{" +
                    "\"走廊\":{\"房間描述\":\"明明剛過中午,天空卻是接近黃昏,不,黃昏也沒有這麼紅\"},"+
                    "\"801\":{\"房間描述\":\"觀望了四周,是一個普通的日式房間(箱子_桌子)\",\"箱子\":\"100$\",\"桌子\":\"紙條(_紙條)\"," +
                             "\"紙條\":\"我將罪與禁忌藏於我身，於地獄之火上請教                                 \"},"+

                    "\"802\":{\"房間描述\":\"屋內地板散落著破損且雜亂的家具,牆壁上看得出巨型的爪痕,燒焦痕跡,和褐色血跡;"+
                    "造就這一切的原因正在屋內走廊的盡頭看著你們,一隻巨型三頭犬,背上有無數的蛇(_三頭犬)\",\"鑰匙\":\"鑰匙\"," +
                    "\"三頭犬\":\"冷靜下來後仔細想想,是在那裡聽說過三頭犬的傳說呢?希臘神話嗎?\"}"+
                    "}";


/*
            String tmp = "[" +
                    "{\"801\":{\"房間描述\":\"觀望了四周,是一個普通的日式房間\"},{\"箱子\":\"100$\"}, " +
                    "\"802\":{\"箱子2\":\"200$\"}," +
                    "\"803\":{\"北門\":\"普通的門\"},{\"桌子\":\"硬幣\"},{\"瓦斯爐\":\"可以使用\"}}" +
                    "]";
*/
            j = new JSONObject(tmp);
         //   j = new JSONObject(room801);
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

    }


    public void process(Message message)
    {
        switch (message.getType())
        {
            case Message.ROOM_STATE:
                this.write(message);
                break;

            case Message.CHAT:
                String msg=((ChatMessage)message).MESSAGE;//輸入訊息,說是裝填比較合適
                //如果輸入走廊

                if (msg.equals("走廊"))
                {
                    if (message.getSource().equals(source))
                    {
                        lo = "走廊";
                        write(new ChatMessage("MurMur", ("前往"+lo)));
                        Object jsonOb = j.getJSONObject(lo).get("房間描述");
                        write(new ChatMessage("MurMur", MessageFormat.format("{0}", jsonOb)));
                    }
                }
                else if(lo.equals("走廊"))
                {
                    if (msg.equals("房間描述"))
                    {
                        if (message.getSource().equals(source))
                        {
                            Object jsonOb = j.getJSONObject(lo).get("房間描述");
                            write(new ChatMessage("MurMur", MessageFormat.format(" {0}", jsonOb)));
                        }
                    }
                    else
                    {this.write(message);}
                }
                /*else
                {this.write(message);}*/


                //如果輸入801
                if (msg.equals("801"))
                {
                    if (message.getSource().equals(source))
                    {
                    lo = "801";
                    write(new ChatMessage("MurMur", ("前往"+lo)));
                    Object jsonOb = j.getJSONObject(lo).get("房間描述");
                    write(new ChatMessage("MurMur", MessageFormat.format("{0}", jsonOb)));
                    }
                }
                else if(lo.equals("801"))
                {
                    if (msg.equals("箱子"))
                    {
                        if (message.getSource().equals(source))
                        {
                            Object jsonOb = j.getJSONObject(lo).get("箱子");
                            write(new ChatMessage("MurMur", MessageFormat.format("發現 {0}", jsonOb)));
                        }
                    }//在801中找桌子
                    else if (msg.equals("桌子"))
                    {
                        if (message.getSource().equals(source))
                        {
                            Object jsonOb = j.getJSONObject(lo).get("桌子");
                            write(new ChatMessage("MurMur", MessageFormat.format("發現 {0}", jsonOb)));
                        }
                    }
                    else if (msg.equals("紙條"))
                    {
                        if (message.getSource().equals(source))
                        {
                            Object jsonOb = j.getJSONObject(lo).get("紙條");
                            write(new ChatMessage("MurMur", MessageFormat.format(" {0}", jsonOb)));
                        }
                    }
                    else
                    {this.write(message);}
                }

               //在801中找箱子


              //移動到802
                if (msg.equals("802"))
                {

                    if (message.getSource().equals(source))
                    {
                        lo = "802";
                        write(new ChatMessage("MurMur", ("前往" + lo)));
                        Object jsonOb = j.getJSONObject(lo).get("房間描述");
                        write(new ChatMessage("MurMur", MessageFormat.format("{0}", jsonOb)));
                    }

                }
                else if(lo.equals("802"))
                {
                    if (msg.equals("鑰匙"))
                    {
                        if (message.getSource().equals(source))
                        {
                            Object jsonOb = j.getJSONObject(lo).get("鑰匙");
                            write(new ChatMessage("MurMur", MessageFormat.format("發現 {0}", jsonOb)));
                        }
                    }
                    else if (msg.equals("三頭犬"))
                    {
                        if (message.getSource().equals(source))
                        {
                            Object jsonOb = j.getJSONObject(lo).get("三頭犬");
                            write(new ChatMessage("MurMur", MessageFormat.format("{0}", jsonOb)));
                        }
                    }
                    else
                    { this.write(message); }
                }
                if (msg.equals("local"))
                {
                    if (message.getSource().equals(source))
                    {
                        write(new ChatMessage("MurMur", MessageFormat.format("{0}", lo)));
                    }
                }


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


        /*public static void main(String args[])
        {

            JSONObject j;
            try {
                String tmp = "{\"ManData\":{\"Name\":\"MichaelChan\",\"Email\":\"XXXX@XXX.com\",\"Phone\":[1234567,0911123456]}}";

                j = new JSONObject(tmp);

                Object jsonOb = j.getJSONObject("ManData");

                System.out.println(jsonOb);

            }catch(Exception e){
                System.err.println("Error: " + e.getMessage());
            }
        }*/















}

// Servant.java