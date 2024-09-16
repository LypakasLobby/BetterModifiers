package com.lypaka.bettermodifiers.Modifiers;

import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.ItemStackBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class Modifier {

    private final String name;
    private final List<String> species;
    private final String itemID;
    private final String itemName;
    private final List<String> itemLore;
    private final List<String> functions;
    private final List<String> permissions;

    public Modifier (String name, List<String> species, String itemID, String itemName, List<String> itemLore, List<String> functions, List<String> permissions) {

        this.name = name;
        this.species = species;
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemLore = itemLore;
        this.functions = functions;
        this.permissions = permissions;

    }

    public String getName() {

        return this.name;

    }

    public List<String> getSpecies() {

        return this.species;

    }

    public boolean allowsAnySpecies() {

        return this.species.contains("*");

    }

    public String getItemID() {

        return this.itemID;

    }

    public String getItemName() {

        return this.itemName;

    }

    public List<String> getItemLore() {

        return this.itemLore;

    }

    public ItemStack getItem (int count) {

        ItemStack item = ItemStackBuilder.buildFromStringID(this.itemID);
        item.setHoverName(FancyText.getFormattedText(this.itemName));
        ListNBT lore = new ListNBT();
        for (String s : this.itemLore) {

            lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText(s))));

        }

        item.getOrCreateTagElement("display").put("Lore", lore);
        item.setCount(count);
        item.getOrCreateTag().putBoolean("ModifierItem", true);
        item.getOrCreateTag().putString("ModifierName", this.name);
        return item;

    }

    public List<String> getFunctions() {

        return this.functions;

    }

    public List<String> getPermissions() {

        return this.permissions;

    }

}
