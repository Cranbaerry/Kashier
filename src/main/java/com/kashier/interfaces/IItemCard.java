package com.kashier.interfaces;

import com.kashier.models.InventoryItem;

// This interface is used to pass the onClickEvent method back to the main controller
public interface IItemCard {
    public void onClickEvent(InventoryItem item);
}
