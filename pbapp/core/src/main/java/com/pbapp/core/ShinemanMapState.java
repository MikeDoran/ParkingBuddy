package com.pbapp.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.Gson;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import static java.time.temporal.ChronoUnit.HOURS;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author emmabrunell
 */
public class ShinemanMapState extends State {

    private ShapeRenderer sr;
    private Texture background;
    private Texture popUpBackground;
    private Texture timerBackground;
    private Button backButton;
    private Button emptySpaceButton;
    private Button fillSpaceButton;
    private Button obstructedButton;
    private Button sevenAMButton;
    private Button eightAMButton;
    private Button nineAMButton;
    private Button tenAMButton;
    private Button elevenAMButton;
    private Button twelvePMButton;
    private Button onePMButton;
    private Button twoPMButton;
    private Button threePMButton;
    private Button fourPMButton;
    private Button fivePMButton;
    private Button sixPMButton;
    private Button closeButton;
    private Button setTimerButton;
    private MapSprite map;
    private ParkingSpaceButton[] spaces;
    public String spaceID;
    public String host;
    private String port;
    public String lotName;
    private Boolean messageRecieved;

    private Socket s;
    private ObjectOutputStream o;
    private ObjectInputStream i;
    private InetAddress address;
    private OrthographicCamera camera;

    public boolean spacePressed;
    public boolean timerPressed;
    public boolean delay;
    public String spaceId;
    private int colorRefreshClock;

    public ShinemanMapState(GuiStateManager gsm) {
        super(gsm);
        colorRefreshClock = 0;
        background = new Texture("ShinemanMap.png");
        popUpBackground = new Texture("PopUp.png");
        timerBackground = new Texture("timerPopUp.png");
        backButton = new Button("BackButton.png", new Vector2(37, 60), new Vector2(230, 50));
        emptySpaceButton = new Button("emptySpace.png", new Vector2(75, 405), new Vector2(100, 100));
        fillSpaceButton = new Button("fillSpace.png", new Vector2(275, 405), new Vector2(100, 100));
        obstructedButton = new Button("obstructed.png", new Vector2(75, 475), new Vector2(100, 100));
        setTimerButton = new Button("setTimer.png", new Vector2(275, 475), new Vector2(100, 130));
        map = new MapSprite(0, PBApp.height, "ShinemanLotEditedMap.png", -200, -950, -190, -336);
        spacePressed = false;
        spacePressed = false;
        timerPressed = false;
        port = "2525";
        lotName = "Shineman";
        messageRecieved = false;
        delay = true;
        s = null;
        o = null;
        i = null;

        sevenAMButton = new Button("7AM.png", new Vector2(65, 375), new Vector2(100, 100));
        eightAMButton = new Button("8AM.png", new Vector2(160, 375), new Vector2(100, 100));
        nineAMButton = new Button("9AM.png", new Vector2(250, 375), new Vector2(100, 100));
        tenAMButton = new Button("10AM.png", new Vector2(340, 375), new Vector2(100, 100));
        elevenAMButton = new Button("11AM.png", new Vector2(65, 425), new Vector2(100, 100));
        twelvePMButton = new Button("12PM.png", new Vector2(160, 425), new Vector2(100, 100));
        onePMButton = new Button("1PM.png", new Vector2(250, 425), new Vector2(100, 100));
        twoPMButton = new Button("2PM.png", new Vector2(340, 425), new Vector2(100, 100));
        threePMButton = new Button("3PM.png", new Vector2(65, 475), new Vector2(100, 100));
        fourPMButton = new Button("4PM.png", new Vector2(160, 475), new Vector2(100, 100));
        fivePMButton = new Button("5PM.png", new Vector2(250, 475), new Vector2(100, 100));
        sixPMButton = new Button("6PM.png", new Vector2(340, 475), new Vector2(100, 100));
        closeButton = new Button("xButton.png", new Vector2(50, 342), new Vector2(100, 100));

        try {
            address = InetAddress.getLocalHost();
            host = address.getHostAddress();
            s = new Socket(host, Integer.parseInt(port));
            o = new ObjectOutputStream(s.getOutputStream());
            o.flush();
            i = new ObjectInputStream(s.getInputStream());

            String response = "";
            while (response.equals("")) {
                response = (String) i.readObject();
            }
            messageRecieved = true;

        } catch (EOFException eof) {
            System.out.println("Connection terminated.");
        } catch (UnknownHostException ex) {
            System.out.println("Unable to identify local host");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Failed to initialize socket/streams");
            ex.printStackTrace();
            System.exit(1);
        } catch (ClassNotFoundException ex) {
            System.out.println("Object recieved was not string");
            ex.printStackTrace();
            System.exit(1);
        }

        spaces = new ParkingSpaceButton[230];
        spaces[0] = new ParkingSpaceButton("1", new Vector2(260, 238), new Vector2(17, 50), 23f);
        spaces[1] = new ParkingSpaceButton("2", new Vector2(280, 230), new Vector2(17, 50), 23f);
        spaces[2] = new ParkingSpaceButton("3", new Vector2(298, 218), new Vector2(17, 53), 20f);
        spaces[3] = new ParkingSpaceButton("4", new Vector2(317, 205), new Vector2(17, 53), 18f);
        spaces[4] = new ParkingSpaceButton("5", new Vector2(340, 195), new Vector2(17, 53), 20f);
        spaces[5] = new ParkingSpaceButton("6", new Vector2(365, 178), new Vector2(17, 53), 20f);
        spaces[6] = new ParkingSpaceButton("7", new Vector2(395, 161), new Vector2(17, 53), 23f);
        spaces[7] = new ParkingSpaceButton("8", new Vector2(424, 144), new Vector2(17, 53), 23f);
        spaces[8] = new ParkingSpaceButton("9", new Vector2(449, 129), new Vector2(17, 53), 23f);
        spaces[9] = new ParkingSpaceButton("10", new Vector2(478, 112), new Vector2(17, 53), 24f);
        spaces[10] = new ParkingSpaceButton("11", new Vector2(523, 83), new Vector2(17, 53), 23f);
        spaces[11] = new ParkingSpaceButton("12", new Vector2(545, 70), new Vector2(17, 53), 23f);
        spaces[12] = new ParkingSpaceButton("13", new Vector2(585, 45), new Vector2(17, 53), 23f);
        spaces[13] = new ParkingSpaceButton("14", new Vector2(605, 35), new Vector2(17, 53), 23f);
        spaces[14] = new ParkingSpaceButton("15", new Vector2(644, 7), new Vector2(17, 53), 23f);
        spaces[15] = new ParkingSpaceButton("16", new Vector2(666, -7), new Vector2(17, 53), 23f);
        spaces[16] = new ParkingSpaceButton("17", new Vector2(708, -29), new Vector2(17, 53), 23f);
        spaces[17] = new ParkingSpaceButton("18", new Vector2(280, 390), new Vector2(17, 43), 90f);
        int x1 = 280;
        int y1 = 390;
        float t1 = 0f;
        for (int i = 18; i < 23; i++) {
            y1 = y1 + 28;
            x1 = x1 + 1;
            spaces[i] = new ParkingSpaceButton(Integer.toString(i + 1), new Vector2(x1, y1), new Vector2(17, 43), 90f);
        }

        spaces[23] = new ParkingSpaceButton("24", new Vector2(286, 609), new Vector2(17, 53), 0f);
        x1 = 286;
        y1 = 609;
        for (int i = 24; i < 45; i++) {
            x1 = x1 + 22;
            y1 = y1 - 1;
            if (i % 2 == 0) {
                x1 = x1 + 2;
            }
            if (i == 35 || i == 39) {
                x1 = x1 + 2;
            }
            spaces[i] = new ParkingSpaceButton(Integer.toString(i + 1), new Vector2(x1, y1), new Vector2(17, 53), 0f);
        }

        spaces[45] = new ParkingSpaceButton("46", new Vector2(835, 518), new Vector2(17, 45), 90f);
        x1 = 835;
        y1 = 518;
        for (int i = 46; i < 62; i++) {
            y1 = y1 - 28;
            if (i % 2 == 0) {
                x1 = x1 - 2;
            }
            if (i % 3 == 0) {
                y1 = y1 - 2;
            }
            spaces[i] = new ParkingSpaceButton(Integer.toString(i + 1), new Vector2(x1, y1), new Vector2(17, 45), 90f);
        }

        spaces[62] = new ParkingSpaceButton("63", new Vector2(380, 320), new Vector2(17, 43), 90f);
        x1 = 380;
        y1 = 320;
        for (int i = 64; i < 71; i = i + 2) {
            y1 = y1 + 30;
            spaces[i] = new ParkingSpaceButton(Integer.toString(i + 1), new Vector2(x1, y1), new Vector2(17, 43), 90f);
        }

        spaces[63] = new ParkingSpaceButton("64", new Vector2(428, 319), new Vector2(17, 43), 90f);
        x1 = 428;
        y1 = 319;
        for (int i = 65; i < 72; i = i + 2) {
            y1 = y1 + 28;
            if (i == 69) {
                y1 = y1 + 4;
            }
            spaces[i] = new ParkingSpaceButton(Integer.toString(i + 1), new Vector2(x1, y1), new Vector2(17, 43), 90f);
        }

        spaces[72] = new ParkingSpaceButton("73", new Vector2(524, 227), new Vector2(17, 43), 90f);
        x1 = 524;
        y1 = 227;
        for (int i = 74; i < 87; i = i + 2) {
            y1 = y1 + 28;
            x1 = x1 + 1;
            if (i == 84 || i == 86) {
                y1 = y1 + 2;
            }
            spaces[i] = new ParkingSpaceButton(Integer.toString(i + 1), new Vector2(x1, y1), new Vector2(17, 43), 90f);
        }

        spaces[73] = new ParkingSpaceButton("76", new Vector2(574, 225), new Vector2(17, 43), 90f);
        x1 = 574;
        y1 = 225;
        for (int i = 75; i < 88; i = i + 2) {
            y1 = y1 + 28;
            x1 = x1 + 1;
            if (i == 83 || i == 85) {
                y1 = y1 + 2;
            }
            spaces[i] = new ParkingSpaceButton(Integer.toString(i + 1), new Vector2(x1, y1), new Vector2(17, 43), 90f);
        }

        spaces[88] = new ParkingSpaceButton("89", new Vector2(673, 128), new Vector2(19, 43), 90f);
        x1 = 673;
        y1 = 128;
        for (int i = 90; i < 109; i = i + 2) {
            y1 = y1 + 29;
            x1 = x1 + 1;
            if (i == 97 || i == 99) {
                y1 = y1 + 2;
            }
            spaces[i] = new ParkingSpaceButton(Integer.toString(i + 1), new Vector2(x1, y1), new Vector2(19, 43), 90f);
        }

        spaces[89] = new ParkingSpaceButton("90", new Vector2(723, 128), new Vector2(19, 43), 90f);
        x1 = 723;
        y1 = 128;
        for (int i = 91; i < 110; i = i + 2) {
            y1 = y1 + 29;
            x1 = x1 + 1;
            spaces[i] = new ParkingSpaceButton(Integer.toString(i + 1), new Vector2(x1, y1), new Vector2(19, 43), 90f);
        }

        spaces[110] = new ParkingSpaceButton("111", new Vector2(968, -97), new Vector2(19, 46), 0f);
        x1 = 968;
        y1 = -97;
        for (int i = 111; i < 127; i++) {
            x1 = x1 + 25;
            y1 = y1 - 1;
            if (i == 112 || i == 114) {
                x1 = x1 + 7;
            }
            if (i >= 16) {
                x1 = x1 - 1;
                if (i % 4 == 0) {
                    x1 = x1 - 2;
                    y1 = y1 - 2;
                }
            }
            spaces[i] = new ParkingSpaceButton(Integer.toString(i + 1), new Vector2(x1, y1), new Vector2(19, 46), 0f);
        }

        spaces[127] = new ParkingSpaceButton("128", new Vector2(1424, -97), new Vector2(19, 43), 90f);
        x1 = 1424;
        y1 = -97;
        for (int i = 128; i < 144; i++) {
            x1 = x1 + 1;
            y1 = y1 + 29;
            if (i % 3 == 0) {
                y1 = y1 + 1;
            }
            if (i == 143) {
                x1 = x1 + 2;
            }
            spaces[i] = new ParkingSpaceButton(Integer.toString(i + 1), new Vector2(x1, y1), new Vector2(19, 43), 90f);
        }

        spaces[144] = new ParkingSpaceButton("145", new Vector2(1447, 425), new Vector2(19, 43), 90f);
        x1 = 1447;
        y1 = 425;
        for (int i = 145; i < 147; i++) {
            x1 = x1 + 1;
            y1 = y1 + 29;
            spaces[i] = new ParkingSpaceButton(Integer.toString(i + 1), new Vector2(x1, y1), new Vector2(19, 43), 90f);
        }

        spaces[147] = new ParkingSpaceButton("148", new Vector2(1132, 563), new Vector2(19, 47), 0f);
        x1 = 1132;
        y1 = 563;
        for (int i = 148; i < 159; i++) {
            x1 = x1 + 23;
            y1 = y1 - 1;
            if (i % 3 == 0) {
                y1 = y1 - 1;
                x1 = x1 + 1;

            }
            spaces[i] = new ParkingSpaceButton(Integer.toString(i + 1), new Vector2(x1, y1), new Vector2(19, 47), 0f);
        }

        spaces[159] = new ParkingSpaceButton("160", new Vector2(1019, 41), new Vector2(17, 44), 90f);
        x1 = 1019;
        y1 = 44;
        for (int i = 160; i < 178; i++) {
            y1 = y1 + 29;
            x1 = x1 + 1;
            spaces[i] = new ParkingSpaceButton(Integer.toString(i + 1), new Vector2(x1, y1), new Vector2(17, 44), 90f);
        }

        spaces[178] = new ParkingSpaceButton("179", new Vector2(1121, 39), new Vector2(17, 44), 90f);
        x1 = 1121;
        y1 = 39;
        for (int i = 179; i < 191; i++) {
            x1 = x1 + 1;
            y1 = y1 + 29;
            spaces[i] = new ParkingSpaceButton(Integer.toString(i + 1), new Vector2(x1, y1), new Vector2(17, 44), 90f);
        }

        spaces[191] = new ParkingSpaceButton("192", new Vector2(1170, 39), new Vector2(17, 44), 90f);
        x1 = 1170;
        y1 = 39;
        for (int i = 192; i < 204; i++) {
            x1 = x1 + 1;
            y1 = y1 + 29;
            spaces[i] = new ParkingSpaceButton(Integer.toString(i + 1), new Vector2(x1, y1), new Vector2(17, 44), 90f);
        }

        spaces[204] = new ParkingSpaceButton("205", new Vector2(1272, 29), new Vector2(17, 44), 90f);
        x1 = 1272;
        y1 = 29;
        for (int i = 205; i < 217; i++) {
            x1 = x1 + 1;
            y1 = y1 + 29;
            if (i % 3 == 0) {
                x1 = x1 + 1;
                y1 = y1 + 1;
            }
            spaces[i] = new ParkingSpaceButton(Integer.toString(i + 1), new Vector2(x1, y1), new Vector2(17, 44), 90f);
        }

        spaces[217] = new ParkingSpaceButton("216", new Vector2(1325, 27), new Vector2(17, 44), 90f);
        x1 = 1325;
        y1 = 27;
        for (int i = 218; i < 230; i++) {
            x1 = x1 + 1;
            y1 = y1 + 29;
            if (i % 3 == 0) {
                x1 = x1 + 1;
                y1 = y1 + 1;
            }
            spaces[i] = new ParkingSpaceButton(Integer.toString(i + 1), new Vector2(x1, y1), new Vector2(17, 44), 90f);
        }
        sr = new ShapeRenderer();
    }

    @Override
    protected void handleInput() {

        if (Gdx.input.isTouched()) {
            int deltaX = Gdx.input.getDeltaX();
            int deltaY = Gdx.input.getDeltaY();
            map.update(new Vector2(deltaX, deltaY));
        }

        if (Gdx.input.justTouched()) {

            if (backButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                try {
                    o.writeObject("exit");
                    o.flush();
                    String response = "";
                    while (response.equals("")) {
                        response = (String) i.readObject();
                    }
                    System.out.println(response);
                    i.close();
                    o.close();
                    s.close();
                } catch (IOException ex) {
                    System.out.println("Failed to intialize socket/streams");
                    ex.printStackTrace();
                    System.exit(1);
                } catch (ClassNotFoundException ex) {
                    System.out.println("Object recieved was not string");
                    ex.printStackTrace();
                    System.exit(1);
                }
                this.dispose();
                gsm.pop();
                MainMenuState m = new MainMenuState(gsm);
                gsm.push(m);

            }

            if (!spacePressed) {
                for (ParkingSpaceButton space : spaces) {
                    //if(space.wasTouched(Gdx.input.getX() - map.getXpos(), Gdx.input.getY() + map.getYpos() )){
                    if (space.wasTouchedTilted(Gdx.input.getX() - map.getXpos(), Gdx.input.getY() + map.getYpos(), space.getTilt())) {
                        spacePressed = true;
                        spaceId = space.getIdentifier();
                        System.out.println(spaceId + " was pressed.");
                        delay = true;
                        break;
                    }
                }

            }

            if (!delay) {
                if (spacePressed) {
                    if (fillSpaceButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                        if (messageRecieved) {
                            try {
                                //System.out.println("WRITING: " + spaceId);
                                o.writeObject(spaceId + " " + lotName + " fil " + 0);
                                o.flush();
                                i.readObject();
                            } catch (IOException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        spacePressed = false;
                    } else if (emptySpaceButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                        if (messageRecieved) {
                            try {
                                o.writeObject(spaceId + " " + lotName + " opn " + 0);
                                o.flush();
                                i.readObject();
                            } catch (IOException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        spacePressed = false;
                    } else if (obstructedButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                        if (messageRecieved) {
                            try {
                                //System.out.println("In ShinemanMapState writing object "+ spaceID + " " + lotName +" obs " + 0);
                                o.writeObject(spaceId + " " + lotName + " obs " + 0);
                                o.flush();
                                i.readObject();
                            } catch (IOException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        spacePressed = false;
                    } else if (setTimerButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                        System.out.println("timer");
                        timerPressed = true;
                    } else if (closeButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                        System.out.println("close button touched");
                        spacePressed = false;
                        timerPressed = false;
                    }
                }

                if (timerPressed) {
                    if (sevenAMButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                        LocalTime sevenAM = LocalTime.of(7, 0, 0);
                        int time = (int) HOURS.between(sevenAM, LocalDateTime.now());
                        if (messageRecieved) {
                            try {
                                o.writeObject(spaceId + " " + lotName + " fil " + time);
                                o.flush();
                                i.readObject();
                            } catch (IOException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    } else if (eightAMButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                        LocalTime eightAM = LocalTime.of(8, 0, 0);
                        int time = (int) HOURS.between(eightAM, LocalDateTime.now());
                        if (messageRecieved) {
                            try {
                                o.writeObject(spaceId + " " + lotName + " fil " + time);
                                o.flush();
                                i.readObject();
                            } catch (IOException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    } else if (nineAMButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                        LocalTime nineAM = LocalTime.of(9, 0, 0);
                        int time = (int) HOURS.between(nineAM, LocalDateTime.now());
                        if (messageRecieved) {
                            try {
                                o.writeObject(spaceId + " " + lotName + " fil " + time);
                                o.flush();
                                i.readObject();
                            } catch (IOException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    } else if (tenAMButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                        LocalTime tenAM = LocalTime.of(10, 0, 0);
                        int time = (int) HOURS.between(tenAM, LocalDateTime.now());
                        if (messageRecieved) {
                            try {
                                o.writeObject(spaceId + " " + lotName + " fil " + time);
                                o.flush();
                                i.readObject();
                            } catch (IOException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    } else if (elevenAMButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                        LocalTime elevenAM = LocalTime.of(11, 0, 0);
                        int time = (int) HOURS.between(elevenAM, LocalDateTime.now());
                        if (messageRecieved) {
                            try {
                                o.writeObject(spaceId + " " + lotName + " fil " + time);
                                o.flush();
                                i.readObject();
                            } catch (IOException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    } else if (twelvePMButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                        LocalTime twelvePM = LocalTime.of(12, 0, 0);
                        int time = (int) HOURS.between(twelvePM, LocalDateTime.now());
                        if (messageRecieved) {
                            try {
                                o.writeObject(spaceId + " " + lotName + " fil " + time);
                                o.flush();
                                i.readObject();
                            } catch (IOException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    } else if ((onePMButton.wasTouched(Gdx.input.getX(), Gdx.input.getY()))) {
                        LocalTime onePM = LocalTime.of(13, 0, 0);
                        int time = (int) HOURS.between(onePM, LocalDateTime.now());
                        if (messageRecieved) {
                            try {
                                o.writeObject(spaceId + " " + lotName + " fil " + time);
                                o.flush();
                                i.readObject();
                            } catch (IOException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    } else if (twoPMButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                        LocalTime twoPM = LocalTime.of(14, 0, 0);
                        int time = (int) HOURS.between(twoPM, LocalDateTime.now());
                        if (messageRecieved) {
                            try {
                                o.writeObject(spaceId + " " + lotName + " fil " + time);
                                o.flush();
                                i.readObject();
                            } catch (IOException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    } else if (threePMButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                        LocalTime threePM = LocalTime.of(15, 0, 0);
                        int time = (int) HOURS.between(threePM, LocalDateTime.now());
                        if (messageRecieved) {
                            try {
                                o.writeObject(spaceId + " " + lotName + " fil " + time);
                                o.flush();
                                i.readObject();
                            } catch (IOException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    } else if (fourPMButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                        LocalTime fourPM = LocalTime.of(16, 0, 0);
                        int time = (int) HOURS.between(fourPM, LocalDateTime.now());
                        if (messageRecieved) {
                            try {
                                o.writeObject(spaceId + " " + lotName + " fil " + time);
                                o.flush();
                                i.readObject();
                            } catch (IOException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    } else if (fivePMButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                        LocalTime fivePM = LocalTime.of(17, 0, 0);
                        int time = (int) HOURS.between(fivePM, LocalDateTime.now());
                        if (messageRecieved) {
                            try {
                                o.writeObject(spaceId + " " + lotName + " fil " + time);
                                o.flush();
                                i.readObject();
                            } catch (IOException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    } else if (sixPMButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                        LocalTime sixPM = LocalTime.of(18, 0, 0);
                        int time = (int) HOURS.between(sixPM, LocalDateTime.now());
                        if (messageRecieved) {
                            try {
                                o.writeObject(spaceId + " " + lotName + " fil " + time);
                                o.flush();
                                i.readObject();
                            } catch (IOException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(ShinemanMapState.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    }

                }
            }
            delay = false;

        }
    }

    @Override
    public void update(double dt) {
        handleInput();
        colorRefreshClock++;
        if (colorRefreshClock == 150) {
            colorRefreshClock = 0;
            try {
                Gson gson = new Gson();
                o.writeObject("chk Shineman");
                o.flush();
                String[] spacesJson = (String[]) i.readObject();
                Hashtable<String, ParkingSpace> serverData = new Hashtable();
                for (String jsonSpace : spacesJson) {
                    ParkingSpace space = gson.fromJson(jsonSpace, ParkingSpace.class);
                    serverData.put(space.getId(), space);
                }
                for (ParkingSpaceButton space : spaces) {
                    if (serverData.get(space.getIdentifier()) != null) {
                        space.setColor(serverData.get(space.getIdentifier()));
                    }
                }
                i.readObject();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(map.getTexture(), map.getXpos(), map.getYpos(), 1650, 1200);
        sb.end();

        sr.begin(ShapeRenderer.ShapeType.Filled);
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);

        // 0,0 because we already specify the origin when creating the spaces[i]
        // last parameter specifies the rotation of shape if necessary
        for (int i = 0; i < spaces.length; i++) {
            if (spaces[i] == null) {
                System.out.println("Space at index i: " + i + " is null");
            }
            if (spaces[i] != null) {
                sr.setColor(spaces[i].getColor());
                sr.rect(spaces[i].getXpos() + map.getXpos(), spaces[i].getYpos()
                        + map.getYpos(), spaces[i].getWidth(), spaces[i].getHeight(), 0, 0, spaces[i].getTilt());
            }
        }
        sr.end();
        if (spacePressed) {
            sb.begin();
            sb.draw(popUpBackground, 50, 300, 400, 170);
            sb.end();
            sb.begin();
            sb.draw(emptySpaceButton.getTexture(), emptySpaceButton.getXpos(), emptySpaceButton.getYpos());
            sb.draw(fillSpaceButton.getTexture(), fillSpaceButton.getXpos(), fillSpaceButton.getYpos());
            sb.draw(obstructedButton.getTexture(), obstructedButton.getXpos(), obstructedButton.getYpos());
            sb.draw(setTimerButton.getTexture(), setTimerButton.getXpos(), setTimerButton.getYpos());
            sb.end();

            sb.begin();
            sb.draw(closeButton.getTexture(), closeButton.getXpos(), closeButton.getYpos());
            sb.end();
        }

        if (timerPressed) {
            //System.out.println("Timer RENDER");
            sb.begin();
            sb.draw(timerBackground, 50, 300);
            sb.end();

            sb.begin();
            sb.draw(sevenAMButton.getTexture(), sevenAMButton.getXpos(), sevenAMButton.getYpos());
            sb.draw(eightAMButton.getTexture(), eightAMButton.getXpos(), eightAMButton.getYpos());
            sb.draw(nineAMButton.getTexture(), nineAMButton.getXpos(), nineAMButton.getYpos());
            sb.draw(tenAMButton.getTexture(), tenAMButton.getXpos(), tenAMButton.getYpos());
            sb.draw(elevenAMButton.getTexture(), elevenAMButton.getXpos(), elevenAMButton.getYpos());
            sb.draw(twelvePMButton.getTexture(), twelvePMButton.getXpos(), twelvePMButton.getYpos());
            sb.draw(onePMButton.getTexture(), onePMButton.getXpos(), onePMButton.getYpos());
            sb.draw(twoPMButton.getTexture(), twoPMButton.getXpos(), twoPMButton.getYpos());
            sb.draw(threePMButton.getTexture(), threePMButton.getXpos(), threePMButton.getYpos());
            sb.draw(fourPMButton.getTexture(), fourPMButton.getXpos(), fourPMButton.getYpos());
            sb.draw(fivePMButton.getTexture(), fivePMButton.getXpos(), fivePMButton.getYpos());
            sb.draw(sixPMButton.getTexture(), sixPMButton.getXpos(), sixPMButton.getYpos());
            sb.end();

            sb.begin();
            sb.draw(closeButton.getTexture(), closeButton.getXpos(), closeButton.getYpos());
            sb.end();
        }

        sb.begin();
        sb.draw(background, 0, 0, PBApp.width, PBApp.height);
        sb.draw(backButton.getTexture(), backButton.getXpos(), backButton.getYpos());
        sb.end();

    }

    public void dispose() {
        //remember to add all drawn objects to this method.
        background.dispose();
        map.getTexture().dispose();
        //popUpBackground.dispose();
    }

}
