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
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import static java.time.temporal.ChronoUnit.HOURS;

/**
 * @author emmabrunell
 */
public class SheldonMapState extends State {

    private Texture background;
    private Texture popUpBackground;
    private Texture timerBackground;
    private Button backButton;
    private Button emptySpaceButton;
    private Button fillSpaceButton;
    private Button obstructedButton;
    private Button setTimerButton;
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
    private MapSprite map;
    private ParkingSpaceButton[] spaces;
    private ShapeRenderer sr;
    private Boolean messageRecieved;
    private String port;
    private Socket s;
    private ObjectOutputStream o;
    private ObjectInputStream i;
    private InetAddress address;
    private OrthographicCamera camera;
    String host;
    String lotName;
    public boolean spacePressed;
    public boolean timerPressed;
    public boolean delay;
    public String spaceId;
    private int colorRefreshClock;

    public SheldonMapState(GuiStateManager gsm) {
        super(gsm);
        colorRefreshClock = 0;
        background = new Texture("SheldonMap.png");
        popUpBackground = new Texture("PopUp.png");
        timerBackground = new Texture("timerPopUp.png");
        backButton = new Button("BackButton.png", new Vector2(37, 60), new Vector2(230, 50));
        emptySpaceButton = new Button("emptySpace.png", new Vector2(75, 405), new Vector2(100, 100));
        fillSpaceButton = new Button("fillSpace.png", new Vector2(275, 405), new Vector2(100, 100));
        obstructedButton = new Button("obstructed.png", new Vector2(75, 475), new Vector2(100, 100));
        setTimerButton = new Button("setTimer.png", new Vector2(275, 475), new Vector2(100, 130));

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

        map = new MapSprite(0, PBApp.height, "SheldonLotEditedMap.png", 0, -700, 0, 0);
        spaces = new ParkingSpaceButton[67];
        sr = new ShapeRenderer();
        spacePressed = false;
        timerPressed = false;
        delay = false;
        port = "2525";
        lotName = "Sheldon";
        messageRecieved = false;
        s = null;
        o = null;
        i = null;

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

        //generating first 22 parking space buttons (top row)
        spaces[0] = new ParkingSpaceButton("1", new Vector2(45, 270), new Vector2(45, 120));
        spaces[1] = new ParkingSpaceButton("2", new Vector2(100, 270), new Vector2(40, 120));
        int x1 = 155;
        int j1 = 3;
        for (int i = 2; i < 9; i++) {
            spaces[i] = new ParkingSpaceButton(Integer.toString(j1), new Vector2(x1, 270), new Vector2(40, 130));
            j1++;
            x1 = x1 + 50;
        }
        int x2 = 505;
        int j2 = 10;
        for (int i = 9; i < 16; i++) {
            spaces[i] = new ParkingSpaceButton(Integer.toString(j2), new Vector2(x2, 270), new Vector2(40, 136));
            j2++;
            x2 = x2 + 51;
        }
        int x3 = 913;
        int j3 = 17;
        for (int i = 16; i < 23; i++) {
            spaces[i] = new ParkingSpaceButton(Integer.toString(j3), new Vector2(x3, 270), new Vector2(38, 140));
            j3++;
            x3 = x3 + 50;
        }

        //generating second row of parking spaces #23-45
        int x4 = 52;
        int j4 = 24;
        int y1 = 580;
        for (int i = 23; i < 45; i++) {
            spaces[i] = new ParkingSpaceButton(Integer.toString(j4), new Vector2(x4, y1), new Vector2(40, 136));
            if (i > 38 && i < 46) {
                j4++;
                y1--;
                x4 = x4 + 49;
            } else {
                j4++;
                y1--;
                x4 = x4 + 51;
            }
        }

        //generating third row of parking spaces #46-66
        //spaces[45] = new ParkingSpaceButton("46",new Vector2(52,730), new Vector2(40,136));
        int x5 = 52;
        int j5 = 46;
        int y2 = 730;
        for (int i = 45; i < 67; i++) {
            spaces[i] = new ParkingSpaceButton(Integer.toString(j5), new Vector2(x5, y2), new Vector2(40, 136));
            j5++;
            y2--;
            x5 = x5 + 51;

        }

    }

    @Override
    protected void handleInput() {

        if (Gdx.input.isTouched()) {
            int deltaX = Gdx.input.getDeltaX();
            int deltaY = Gdx.input.getDeltaY();
            int x = Gdx.input.getX();
            int y = Gdx.input.getY();
            System.out.println("X: " + x);
            //System.out.println("Xmap: " + map.getXpos());
            //System.out.println("deltaX: " + deltaX); 

            //add condition somehow so that when map has reached left and right
            //boundaries, psb don't update anymore until not at boundaries
            map.update(new Vector2(deltaX, deltaY));
//            for (ParkingSpaceButton psb : spaces) {
//                if (map.getXpos() > -700 && map.getXpos() < 0) {
//                    psb.update(deltaX, 0);
//                } else {
//                    //psb.update(0, 0);
//                }
//            }
        }

        if (Gdx.input.justTouched()) {

            if (backButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                if (s != null) {
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
                }
                this.dispose();
                gsm.pop();
                MainMenuState m = new MainMenuState(gsm);
                gsm.push(m);

            }

            if (!spacePressed) {
                for (ParkingSpaceButton space : spaces) {
                    if (space.wasTouched(Gdx.input.getX() - map.getXpos(), Gdx.input.getY())) {
                        spacePressed = true;
                        spaceId = space.getIdentifier();
                        delay = true;
                        System.out.println("spaceId: " + spaceId + " was pressed");
                        break;
                        
                    }
                }
            }

            //id,lotname,cmd(fil,obs,opn,chk),data
            if (!delay) {
                if (spacePressed) {
                    if (fillSpaceButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                        if (messageRecieved) {
                            try {
                                o.writeObject(spaceId + " " + lotName + " fil " + 0);
                                o.flush();
                                i.readObject();
                            } catch (IOException ex) {
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
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
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        spacePressed = false;
                    } else if (obstructedButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                        if (messageRecieved) {
                            try {
                                o.writeObject(spaceId + " " + lotName + " obs " + 0);
                                o.flush();
                                i.readObject();
                            } catch (IOException ex) {
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        spacePressed = false;

                    } else if (setTimerButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                        System.out.println("timer");
                        //spacePressed = false;
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
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
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
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
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
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
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
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
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
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
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
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
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
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
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
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
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
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
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
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
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
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
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
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
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
                o.writeObject("chk Sheldon");
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
        sb.draw(background, 0, 0, PBApp.width, PBApp.height);
        sb.draw(backButton.getTexture(), backButton.getXpos(), backButton.getYpos());
        sb.draw(map.getTexture(), map.getXpos(), map.getYpos(), 1200, 717);
        sb.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND); //activate transparency
        //generating parking spaces
        for (int i = 0; i < spaces.length; i++) {
            if (spaces[i] != null) {
                sr.setColor(spaces[i].getColor());
                sr.rect(spaces[i].getXpos() + map.getXpos(), spaces[i].getYpos(), spaces[i].getWidth(), spaces[i].getHeight(), 0, 0, 0f);
            }
        }
        sr.end();
        if (spacePressed) {
            sb.begin();
            sb.draw(popUpBackground, 50, 300);
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
    }

    public void dispose() {
        //remember to add all drawn objects to this method.
        background.dispose();
        map.getTexture().dispose();
    }

}
