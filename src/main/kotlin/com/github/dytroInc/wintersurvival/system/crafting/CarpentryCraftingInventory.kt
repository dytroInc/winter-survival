package com.github.dytroInc.wintersurvival.system.crafting

import com.github.dytroInc.wintersurvival.system.DefaultTranslation.getName
import com.github.monun.invfx.InvFX
import com.github.monun.invfx.InvScene
import com.github.monun.invfx.builder.InvSceneBuilder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import com.github.dytroInc.wintersurvival.system.DefaultTranslation.getTranslation
import com.github.dytroInc.wintersurvival.system.Items
import org.bukkit.ChatColor

class CarpentryCraftingInventory : CustomCraftingInventory() {
    override fun getInv() = InvFX.scene(5, "목공 테이블") {
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
            amount = 6
        } to arrayListOf(
            ItemStack(Material.SPRUCE_LOG, 1)
        ),
        Items.PLANKS.clone() to arrayListOf(
            Items.STICK.clone().apply {
                amount = 4
            }
        ),
        Items.STAIRS.clone() to arrayListOf(
            Items.STICK.clone().apply {
                amount = 3
            }
        ),
        Items.SLABS.clone() to arrayListOf(
            Items.STICK.clone().apply {
                amount = 2
            }
        ),
        Items.DOOR.clone() to arrayListOf(
            Items.STICK.clone().apply {
                amount = 6
            }
        ),
        ItemStack(Material.LADDER, 3) to arrayListOf(
            Items.STICK.clone().apply {
                amount = 6
            }
        ),
        ItemStack(Material.BARREL) to arrayListOf(
            Items.STICK.clone().apply {
                amount = 12
            }
        ),
        ItemStack(Material.SPRUCE_SIGN) to arrayListOf(
            Items.STICK.clone().apply {
                amount = 2
            }
        )
    )

    private fun getNumber(itemStack: ItemStack) = when(itemStack.type) {
        Material.STICK -> 1
        Material.SPRUCE_PLANKS -> 2
        Material.SPRUCE_SLAB -> 3
        Material.SPRUCE_STAIRS -> 4
        Material.SPRUCE_DOOR -> 5
        Material.LADDER -> 6
        Material.BARREL -> 7
        Material.SPRUCE_SIGN -> 8
        else -> 0
    }
}