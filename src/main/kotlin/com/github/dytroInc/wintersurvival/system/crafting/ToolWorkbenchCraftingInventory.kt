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

class ToolWorkbenchCraftingInventory : CustomCraftingInventory() {
    override fun getInv() = InvFX.scene(5, "도구 작업대") {
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
        Items.IRON_AXE to arrayListOf(
            Items.STICK.clone().apply {
                amount = 13
            },
            Items.IRON.clone().apply {
                amount = 7
            }
        ),
        Items.IRON_PICK to arrayListOf(
            Items.STICK.clone().apply {
                amount = 13
            },
            Items.IRON.clone().apply {
                amount = 7
            }
        ),
        Items.IRON_SWORD to arrayListOf(
            Items.STICK.clone().apply {
                amount = 13
            },
            Items.IRON.clone().apply {
                amount = 7
            }
        ),
    )

    private fun getNumber(itemStack: ItemStack) = run {

        if(itemStack.type.name.contains("iron", true)) {
            return@run (2 + tool(itemStack))
        }

        0
    }
    private fun tool(itemStack: ItemStack) = when(itemStack.type.name.split("_")[1].lowercase()) {
        "pickaxe" -> 0
        "axe" -> 1
        "sword" -> 2
        else -> 3
    }


}