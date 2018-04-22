package com.pbapp.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * @author emmabrunell
 */
public class ShinemanMapState extends State {

    private ShapeRenderer sr;
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
    public boolean spacePressed;
    public String spaceID;

    public ShinemanMapState(GuiStateManager gsm) {
        super(gsm);
        background = new Texture("ShinemanMap.png");
        popUpBackground = new Texture("popUp.png");
        backButton = new Button("BackButton.png", new Vector2(37, 60), new Vector2(230, 50));
        dataButton = new Button("DataButton.png", new Vector2(400, 60), new Vector2(230, 50));
        emptySpaceButton = new Button("emptySpace.png", new Vector2(75, 405), new Vector2(100, 100));
        fillSpaceButton = new Button("fillSpace.png", new Vector2(275, 405), new Vector2(100, 100));
        obstructedButton = new Button("obstructed.png", new Vector2(75, 475), new Vector2(100, 100));
        setTimerButton = new Button("setTimer.png", new Vector2(275, 475), new Vector2(100, 130));
        map = new MapSprite(0, PBApp.height, "ShinemanLotEditedMap.png", -200, -950, -190, -336);
        spacePressed = false;
        spaceID = "None";
        
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

        spaces[17] = new ParkingSpaceButton("18", new Vector2(280, 390), new Vector2(17, 43), 90f, "emptySpace.png");
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

        spaces[217] = new ParkingSpaceButton("218", new Vector2(1325, 27), new Vector2(17, 44), 90f);
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
            
            for (ParkingSpaceButton psb: spaces) {
                if (map.getXpos() > -700 && map.getXpos() < 0) {
                    psb.update(deltaX,0);
                } else {
                   //psb.update(0, 0);
                }
            }
            //spaces[17].update(map.getXpos(), map.getYpos());
        }

        if (Gdx.input.justTouched()) {

            System.out.println("X: " + Gdx.input.getX() + " Y: " + Gdx.input.getY());
            if (backButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                this.dispose();
                gsm.pop();
                MainMenuState m = new MainMenuState(gsm);
                gsm.push(m);

            }

            if (dataButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                this.dispose();
                gsm.pop();
                ShinemanDataState sm = new ShinemanDataState(gsm);
                gsm.push(sm);

            }
            
            for(int i = 0; i < spaces.length; i++){
                ParkingSpaceButton space = spaces[i];
            //}
            if(space == null){
                System.out.println("Space i: " + i + " is null");
            }
            }
            
            
            for(ParkingSpaceButton space : spaces){
                
                if (space != null){
                    if(space.wasTouched(Gdx.input.getX(), Gdx.input.getY())){
                        spacePressed = true;
                        spaceID = space.getIdentifier();
                        System.out.println(spaceID + " was pressed.");
                    }
                }
                
            }
            
            if (spacePressed) {
                if (fillSpaceButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                    System.out.println("Fill Space");
                    System.out.println(spaceID);
                    spacePressed = false;
                } else if (emptySpaceButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                    System.out.println("Empty Space");
                } else if (obstructedButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                    System.out.println("Obstructed");
                } else if (setTimerButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                    System.out.println("Set Timer");
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
        sb.draw(map.getTexture(), map.getXpos(), map.getYpos(), 1650, 1200);
        sb.end();

        sr.begin(ShapeRenderer.ShapeType.Filled);

        // 0,0 because we already specify the origin when creating the spaces[i]
        // last parameter specifies the rotation of shape if necessary
        for (int i = 0; i < spaces.length; i++) {
            if (spaces[i] != null) {
                Vector2 change = new Vector2 (spaces[i].getXpos() + map.getYpos(), spaces[i].getYpos() + map.getXpos());
                
                //spaces[i].setPos(change);
//                sr.rect(spaces[i].getXpos() + map.getXpos(), spaces[i].getYpos()
//                        + map.getYpos(), spaces[i].getWidth(), spaces[i].getHeight(), 0, 0, spaces[i].getTilt());
                sr.rect(spaces[i].getXpos() + map.getXpos(), spaces[i].getYpos() + map.getYpos(), spaces[i].getWidth(), spaces[i].getHeight(),0,0, spaces[i].getTilt());
            }

        }
        sr.end();
        if(spacePressed){
            sb.begin();
            sb.draw(popUpBackground, 50, 300, 400, 170);
            sb.end();
            sb.begin();
            sb.draw(emptySpaceButton.getTexture(), emptySpaceButton.getXpos(), emptySpaceButton.getYpos());
            sb.draw(fillSpaceButton.getTexture(), fillSpaceButton.getXpos(), fillSpaceButton.getYpos());
            sb.draw(obstructedButton.getTexture(), obstructedButton.getXpos(), obstructedButton.getYpos());
            sb.draw(setTimerButton.getTexture(), setTimerButton.getXpos(), setTimerButton.getYpos());
            sb.end();
        }
        sb.begin();
        sb.draw(background, 0, 0, PBApp.width, PBApp.height);
        sb.draw(backButton.getTexture(), backButton.getXpos(), backButton.getYpos());
        sb.draw(dataButton.getTexture(), dataButton.getXpos(), dataButton.getYpos());
        for(int i = 0; i < spaces.length; i++){
                if(spaces[i] != null){
                    Texture tText = spaces[i].getTexture();
                    if(tText != null){
                        //System.out.println("tText is not null");
                        sb.draw(tText, spaces[i].getXpos()  , spaces[i].getYpos());
                    }
                }
            }
        sb.end();
        
        
    }

    public void dispose() {
        //remember to add all drawn objects to this method.
        background.dispose();
        map.getTexture().dispose();
        popUpBackground.dispose();
        
    }

}
