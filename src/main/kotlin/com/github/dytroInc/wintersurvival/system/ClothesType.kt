package com.github.dytroInc.wintersurvival.system

import org.bukkit.inventory.ItemStack

enum class ClothesType {
    NONE,
    LEATHER,
    POLAR_BEAR;

}

fun ItemStack.getClothesType() = if(type.name.contains("LEATHER", true)) run {
    if(translationKey.contains("북극곰")) return@run ClothesType.POLAR_BEAR
    ClothesType.LEATHER
} else ClothesType.NONE