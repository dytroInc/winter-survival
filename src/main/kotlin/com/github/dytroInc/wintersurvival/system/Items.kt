package com.github.dytroInc.wintersurvival.system

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object Items {
    fun ItemStack.fuel() = when(type) {
        Material.COAL -> 3.5
        Material.CHARCOAL -> 3.0
        Material.SPRUCE_LOG -> 1.8
        Material.STICK -> 0.8
        else -> 0.0
    }

    fun ItemStack.foodCookedResult() = when(type) {
        Material.MUTTON -> ItemStack(Material.COOKED_MUTTON)
        Material.BEEF -> ItemStack(Material.COOKED_BEEF)
        else -> null
    }
    fun ItemStack.goodsCookedResult() = when(type) {
        Material.RAW_IRON -> IRON
        else -> null
    }

    val BLANK = ItemStack(Material.GRAY_STAINED_GLASS_PANE).apply {
        itemMeta = itemMeta!!.apply {
            displayName(Component.text(""))
        }
    }
    val CRAFT_MENU = ItemStack(Material.CRAFTING_TABLE).apply {
        itemMeta = itemMeta!!.apply {
            displayName(Component.text("${ChatColor.GOLD}조합"))
        }
    }
    val STICK = ItemStack(Material.STICK).apply {
        itemMeta = itemMeta!!.apply {
            displayName(Component.text("${ChatColor.WHITE}막대기"))
        }
    }
    val IRON = ItemStack(Material.IRON_INGOT).apply {
        itemMeta = itemMeta!!.apply {
            displayName(Component.text("${ChatColor.AQUA}가공된 철"))
        }
    }
    val PLANKS = ItemStack(Material.SPRUCE_PLANKS).apply {
        itemMeta = itemMeta!!.apply {
            displayName(Component.text("${ChatColor.AQUA}가공된 나무"))
        }
    }
    val STAIRS = ItemStack(Material.SPRUCE_STAIRS).apply {
        itemMeta = itemMeta!!.apply {
            displayName(Component.text("${ChatColor.AQUA}가공된 나무 계단"))
        }
    }
    val SLABS = ItemStack(Material.SPRUCE_SLAB).apply {
        itemMeta = itemMeta!!.apply {
            displayName(Component.text("${ChatColor.AQUA}가공된 나무 반블록"))
        }
    }
    val DOOR = ItemStack(Material.SPRUCE_DOOR).apply {
        itemMeta = itemMeta!!.apply {
            displayName(Component.text("${ChatColor.AQUA}문"))
        }
    }



    val CAMPFIRE = ItemStack(Material.CAMPFIRE).apply {
        itemMeta = itemMeta!!.apply {
            displayName(Component.text("${ChatColor.WHITE}캠파이어"))
        }
    }
    val CRAFTING_TABLE = ItemStack(Material.CRAFTING_TABLE).apply {
        itemMeta = itemMeta!!.apply {
            displayName(Component.text("${ChatColor.WHITE}작업대"))
        }
    }
    val WOODEN_PICK = ItemStack(Material.WOODEN_PICKAXE).apply {
        itemMeta = itemMeta!!.apply {
            displayName(Component.text("${ChatColor.WHITE}나무로 된 곡괭이"))
            addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        }
    }
    val WOODEN_AXE = ItemStack(Material.WOODEN_AXE).apply {
        itemMeta = itemMeta!!.apply {
            displayName(Component.text("${ChatColor.WHITE}나무로 된 도끼"))
            addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        }
    }
    val STONE_PICK = ItemStack(Material.STONE_PICKAXE).apply {
        itemMeta = itemMeta!!.apply {
            displayName(Component.text("${ChatColor.AQUA}강화된 곡괭이"))
            addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        }
    }
    val STONE_AXE = ItemStack(Material.STONE_AXE).apply {
        itemMeta = itemMeta!!.apply {
            displayName(Component.text("${ChatColor.AQUA}강화된 도끼"))
            addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        }
    }
    val IRON_PICK = ItemStack(Material.IRON_PICKAXE).apply {
        itemMeta = itemMeta!!.apply {
            displayName(Component.text("${ChatColor.YELLOW}철 곡괭이"))
            addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        }
    }
    val IRON_AXE = ItemStack(Material.IRON_AXE).apply {
        itemMeta = itemMeta!!.apply {
            displayName(Component.text("${ChatColor.YELLOW}철 도끼"))
            addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        }
    }
    val IRON_SWORD = ItemStack(Material.IRON_SWORD).apply {
        itemMeta = itemMeta!!.apply {
            displayName(Component.text("${ChatColor.YELLOW}철 칼"))
            addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        }
    }

    val LEATHER_BOOTS = ItemStack(Material.LEATHER_BOOTS).apply {
        itemMeta = itemMeta!!.apply {
            displayName(Component.text("${ChatColor.YELLOW}가죽 장화"))
            addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        }
        lore(
            listOf(
                Component.text("${ChatColor.BLUE}+5% 추위 감소")
            )
        )
    }
    val LEATHER_CHESTPLATE = ItemStack(Material.LEATHER_BOOTS).apply {
        itemMeta = itemMeta!!.apply {
            displayName(Component.text("${ChatColor.YELLOW}가죽 잠바"))
            addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        }
        lore(
            listOf(
                Component.text("${ChatColor.BLUE}+20% 추위 감소")
            )
        )
    }

    val FURNACE = ItemStack(Material.BLAST_FURNACE).apply {
        itemMeta = itemMeta!!.apply {
            displayName(Component.text("${ChatColor.DARK_GRAY}강화된 화로"))
        }
    }
    val CARPENTRY_TABLE = ItemStack(Material.BEEHIVE).apply {
        itemMeta = itemMeta!!.apply {
            displayName(Component.text("${ChatColor.YELLOW}목공 테이블"))
        }
    }
    val TOOL_WORKBENCH = ItemStack(Material.SMITHING_TABLE).apply {
        itemMeta = itemMeta!!.apply {
            displayName(Component.text("${ChatColor.YELLOW}도구 작업대"))
        }
    }
    val CLOTHES_WORKBENCH = ItemStack(Material.LOOM).apply {
        itemMeta = itemMeta!!.apply {
            displayName(Component.text("${ChatColor.YELLOW}옷 작업대"))
        }
    }
}