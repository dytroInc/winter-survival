package com.github.dytroInc.wintersurvival.system.crafting

import com.github.dytroInc.wintersurvival.system.DefaultTranslation.getName
import com.github.monun.invfx.InvFX
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import com.github.dytroInc.wintersurvival.system.Items

class CutterCraftingInventory : CustomCraftingInventory() {
    override fun getInv() = InvFX.scene(5, "절단기") {
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
            amount = 8
        } to arrayListOf(
            ItemStack(Material.SPRUCE_LOG, 1)
        ),
        ItemStack(Material.IRON_BARS, 3) to arrayListOf(
            Items.IRON.clone().apply {
                amount = 6
            }
        ),
        ItemStack(Material.RAIL, 12) to arrayListOf(
            Items.IRON.clone().apply {
                amount = 10
            },
            Items.STICK.clone().apply {
                amount = 36
            },
            Items.COPPER.clone().apply {
                amount = 24
            }
        ),
        ItemStack(Material.MINECART) to arrayListOf(
            Items.IRON.clone().apply {
                amount = 12
            }
        ),
        ItemStack(Material.HOPPER_MINECART) to arrayListOf(
            Items.IRON.clone().apply {
                amount = 14
            },
            Items.COPPER.clone().apply {
                amount = 2
            },
            ItemStack(Material.COBBLESTONE).apply {
                amount = 30
            }
        )
    )

    private fun getNumber(itemStack: ItemStack) = when(itemStack.type) {
        Material.STICK -> 1
        Material.IRON_BARS -> 2
        Material.RAIL -> 2
        Material.MINECART -> 3
        Material.HOPPER_MINECART -> 4
        else -> 0
    }
}