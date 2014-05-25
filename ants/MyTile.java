package ants;

import ants.*;

public class MyTile implements Tile{
    private int ants;
    private int food;
    private boolean isTravelable;

    public MyTile(int _food, int _ants, boolean _isTravelable){
        this.food = _food;
        this.ants = _ants;
        this.isTravelable = _isTravelable;
    }
    public int getAmountOfFood(){
        return this.food;
    }
    public int getNumAnts(){
        return this.ants;
    }
    public boolean isTravelable(){
        return this.isTravelable;
    }
}
