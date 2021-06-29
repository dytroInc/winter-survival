package com.github.dytroInc.wintersurvival.system.crafting

import com.github.dytroInc.wintersurvival.system.DefaultTranslation.getName
import com.github.dytroInc.wintersurvival.system.Items
import com.github.monun.invfx.InvFX
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.apache.commons.lang.StringUtils.endsWith
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class LoomCraftingInventory : CustomCraftingInventory() {
    override fun getInv() = InvFX.scene(5, "옷 작업대") {
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
        Items.LEATHER_CHESTPLATE.clone() to arrayListOf(
            ItemStack(Material.LEATHER, 18),
            ItemStack(Material.WHITE_WOOL, 9)
        ),
        Items.LEATHER_BOOTS.clone() to arrayListOf(
            ItemStack(Material.LEATHER, 10),
            ItemStack(Material.WHITE_WOOL, 3)
        )
    )

    private fun getNumber(itemStack: ItemStack) = run {
        if(itemStack.type.name.contains("leather", true)) {
            return@run (2 + tool(itemStack))
        }
        0
    }
    private fun tool(itemStack: ItemStack) = when(itemStack.type.name.split("_")[1].lowercase()) {
        "helmet" -> 0
        "chestplate" -> 1
        "leggings" -> 2
        else -> 3
    }


}