package net.jahanco.jahandaru;

import net.jahanco.jahandaru.Models.ItemEntity;
import net.jahanco.jahandaru.Models.MainMenuItems;

import java.util.ArrayList;

/**
 * Created by Mr R00t on 4/24/2017.
 */

public class Fake {
    public static ArrayList<MainMenuItems> getMainFakeMenus(){
        ArrayList<MainMenuItems> mainMenuItemses=new ArrayList<>();
        for(int i=0;i<10;i++){
            MainMenuItems menuItems=new MainMenuItems();
            menuItems.setTitle("منو آیتم"+" "+i);
            menuItems.setImage("menu/i"+i);
            menuItems.setId(i);

            mainMenuItemses.add(menuItems);
        }
        return mainMenuItemses;

    }
    public static ArrayList<ItemEntity> getFakeItems(){
        ArrayList<ItemEntity> itemEntities=new ArrayList<>();
        for(int i=0;i<10;i++){
            ItemEntity itemEntity=new ItemEntity();
            itemEntity.setName("منو آیتم"+" "+i);
            itemEntities.add(itemEntity);
        }
        return itemEntities;
    }
}
