package com.pbapp.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author emmabrunell
 */
public class SheldonMapState extends State {

    private Texture background;
    private Texture popUpBackground;
    private Button backButton;
    private Button dataButton;
    private Button emptySpaceButton;
    private Button fillSpaceButton;
    private Button obstructedButton;
    private Button setTimerButton;
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
    public String spaceId;

    public SheldonMapState(GuiStateManager gsm) {
        super(gsm);
        background = new Texture("SheldonMap.png");
        popUpBackground = new Texture("popUp.png");
        backButton = new Button("BackButton.png", new Vector2(37, 60), new Vector2(230, 50));
        dataButton = new Button("DataButton.png", new Vector2(400, 60), new Vector2(230, 50));
        emptySpaceButton = new Button("emptySpace.png", new Vector2(75, 405), new Vector2(100, 100));
        fillSpaceButton = new Button("fillSpace.png", new Vector2(275, 405), new Vector2(100, 100));
        obstructedButton = new Button("obstructed.png", new Vector2(75, 475), new Vector2(100, 100));
        setTimerButton = new Button("setTimer.png", new Vector2(275, 475), new Vector2(100, 130));
        map = new MapSprite(0, PBApp.height, "SheldonLotEditedMap.png", 0, -700, 0, 0);
        spaces = new ParkingSpaceButton[67];
        sr = new ShapeRenderer();
        spacePressed = false;
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
            for (ParkingSpaceButton psb: spaces) {
                if (map.getXpos() > -700 && map.getXpos() < 0) {
                    psb.update(deltaX,0);
                } else {
                   //psb.update(0, 0);
                }
            }
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

            } else if (dataButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                //System.out.println("Data");
                this.dispose();
                gsm.pop();
                SheldonDataState sd = new SheldonDataState(gsm);
                gsm.push(sd);
            }

            for (ParkingSpaceButton space : spaces) {
                if (space.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                    spacePressed = true;
                    spaceId = space.getIdentifier();
                    //System.out.println(spaceId);
                }
            }

            //id,lotname,cmd(fil,obs,opn,chk),data
            if (spacePressed) {
                if (fillSpaceButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                    if (messageRecieved) {
                        try {
                            o.writeObject(spaceId + " " + lotName + " fil " + 0);
                            o.flush();
                        } catch (IOException ex) {
                            Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    spacePressed = false;
                } else if (emptySpaceButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                    if (messageRecieved) {
                        try {
                            o.writeObject(spaceId + " " + lotName + " opn " + 0);
                            o.flush();
                        } catch (IOException ex) {
                            Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    spacePressed = false;
                } else if (obstructedButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                    if (messageRecieved) {
                        try {
                            o.writeObject(spaceId + " " + lotName + " obs " + 0);
                            o.flush();
                        } catch (IOException ex) {
                            Logger.getLogger(SheldonMapState.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    spacePressed = false;
                } else if (setTimerButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                    if (messageRecieved) {
                        System.out.println("Set Timer");
                    }
                    spacePressed = false;
                }

            }
        }
    }

    @Override
    public void update(double dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0, 0, PBApp.width, PBApp.height);
        sb.draw(backButton.getTexture(), backButton.getXpos(), backButton.getYpos());
        sb.draw(dataButton.getTexture(), dataButton.getXpos(), dataButton.getYpos());
        sb.draw(map.getTexture(), map.getXpos(), map.getYpos(), 1200, 717);
        sb.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND); //activate transparency
        sr.setColor(new Color(0, 255, 0, 0.4f));
        //generating parking spaces
        for (int i = 0; i < spaces.length; i++) {
            if (spaces[i] != null) {
                sr.rect(spaces[i].getXpos(), spaces[i].getYpos(), spaces[i].getWidth(), spaces[i].getHeight(), 0, 0, 0f);
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

        }
    }

    public void dispose() {
        //remember to add all drawn objects to this method.
        background.dispose();
        map.getTexture().dispose();
    }

}
