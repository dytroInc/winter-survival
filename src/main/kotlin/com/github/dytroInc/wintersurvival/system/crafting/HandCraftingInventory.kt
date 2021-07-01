package com.github.dytroInc.wintersurvival.system.crafting

import com.github.dytroInc.wintersurvival.system.DefaultTranslation.getName
import com.github.monun.invfx.InvFX
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import com.github.dytroInc.wintersurvival.system.Items

class HandCraftingInventory : CustomCraftingInventory() {
    override fun getInv() = InvFX.scene(5, "손수 작업대") {
        panel(0, 0, 9, 5) {
            listView(0, 0, 9, 4, false, getItemList().toList().sortedBy {
                getNumber(it.first)
            }) {
                transform { item ->
                    item.first.clone().apply {
                        lore(
                            item.second.map {
                                Component.text(it.getName())
                                    .append(Component.text(" x${it.amount}")).color(TextColor.color(0xFFFF00))
                            }
                        )
                    }
                }
                onClickItem { _, _, _, item, e ->
                    getItemList().toList().first {
                        it.first.type == item.first.type
                    }.let {
                        buy(e.whoClicked as Player, it.first, it.second)
                    }

                }
            }
        }
    }

    override fun getItemList(): HashMap<ItemStack, ArrayList<ItemStack>> = hashMapOf(
        Items.STICK.clone().apply {
            amount = 3
        } to arrayListOf(
            ItemStack(Material.SPRUCE_LOG, 1)
        ),
        Items.CRAFTING_TABLE to arrayListOf(
            Items.STICK.clone().apply {
                amount = 3
            }
        ),
        Items.CAMPFIRE to arrayListOf(
            Items.STICK.clone().apply {
                amount = 5
            }
        )
    )

    private fun getNumber(itemStack: ItemStack) = when(itemStack.type) {
        Material.STICK -> 1
        Material.CAMPFIRE -> 2
        Material.CRAFTING_TABLE -> 3
        else -> 0
    }
}