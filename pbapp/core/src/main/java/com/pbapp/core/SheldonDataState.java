package com.pbapp.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
/**
 *
 * @author emmabrunell
 */
public class SheldonDataState extends State {
    
    private Texture background;
    private Button backButton;
    
    public SheldonDataState(GuiStateManager gsm) {
        super(gsm);
        background = new Texture("SheldonData.png");
        backButton = new Button("BackButton.png", new Vector2(37,60), new Vector2(230,50));
    }

    @Override
    public void handleInput() {
        //TODO: find x and y for back button and data button
        if (Gdx.input.justTouched()) {
            
            if (backButton.wasTouched(Gdx.input.getX(), Gdx.input.getY())) {
                this.dispose();
                gsm.pop();
                SheldonMapState sd = new SheldonMapState(gsm);
                gsm.push(sd);
                
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
        sb.end();
    }
    
     public void dispose() {
        //remember to add all drawn objects to this method.
        background.dispose();
    }
    
}
