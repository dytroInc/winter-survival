package com.github.dytroInc.wintersurvival.system.crafting

import com.github.dytroInc.wintersurvival.system.DefaultTranslation.getName
import com.github.monun.invfx.InvFX
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import com.github.dytroInc.wintersurvival.system.Items

class WorkbenchCraftingInventory : CustomCraftingInventory() {
    override fun getInv() = InvFX.scene(5, "작업대") {
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
        Items.CAMPFIRE to arrayListOf(
            Items.STICK.clone().apply {
                amount = 5
            }
        ),
        Items.WOODEN_PICK to arrayListOf(
            Items.STICK.clone().apply {
                amount = 15
            }
        ),
        Items.WOODEN_AXE to arrayListOf(
            Items.STICK.clone().apply {
                amount = 14
            }
        ),
        Items.STONE_PICK to arrayListOf(
            Items.STICK.clone().apply {
                amount = 12
            },
            ItemStack(Material.COBBLESTONE, 5)
        ),
        Items.STONE_AXE to arrayListOf(
            Items.STICK.clone().apply {
                amount = 12
            },
            ItemStack(Material.COBBLESTONE, 4)
        ),
        Items.FURNACE to arrayListOf(
            Items.STICK.clone().apply {
                amount = 25
            },
            ItemStack(Material.COBBLESTONE, 45)
        ),
        Items.CARPENTRY_TABLE to arrayListOf(
            Items.STICK.clone().apply {
                amount = 50
            },
            ItemStack(Material.COBBLESTONE, 15),
            Items.IRON.clone().apply {
                amount = 3
            }
        ),
        Items.TOOL_WORKBENCH to arrayListOf(
            Items.STICK.clone().apply {
                amount = 45
            },
            ItemStack(Material.COBBLESTONE, 10),
            Items.IRON.clone().apply {
                amount = 12
            }
        ),
        Items.CLOTHES_WORKBENCH to arrayListOf(
            Items.STICK.clone().apply {
                amount = 45
            },
            ItemStack(Material.WHITE_WOOL, 15),
            Items.IRON.clone().apply {
                amount = 3
            }
        ),
        Items.CUTTER to arrayListOf(
            Items.STICK.clone().apply {
                amount = 40
            },
            ItemStack(Material.COBBLESTONE, 64),
            Items.COPPER.clone().apply {
                amount = 5
            },
            Items.IRON.clone().apply {
                amount = 6
            }
        )
    )

    private fun getNumber(itemStack: ItemStack) = when(itemStack.type) {
        Material.STICK -> 1
        Material.CAMPFIRE -> 2
        Material.WOODEN_PICKAXE -> 3
        Material.WOODEN_AXE -> 4
        Material.STONE_PICKAXE -> 5
        Material.STONE_AXE -> 6
        Material.BLAST_FURNACE -> 7
        Material.BEEHIVE -> 8
        Material.STONECUTTER -> 9
        Material.SMITHING_TABLE -> 10
        Material.LOOM -> 11
        else -> 0
    }
}