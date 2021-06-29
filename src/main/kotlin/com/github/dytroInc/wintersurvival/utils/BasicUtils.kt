package com.github.dytroInc.wintersurvival.utils

import com.github.dytroInc.wintersurvival.system.DefaultTranslation.getName
import com.github.dytroInc.wintersurvival.system.DefaultTranslation.getTranslation
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

object BasicUtils {
    fun String.removeColor() = ChatColor.stripColor(this)
    infix fun String.endsWith(str: String): Boolean = endsWith(str)

    infix fun ItemStack.same(item: ItemStack?) =
        if(item?.type != null) this.type == item.type else false
    fun PlayerInventory.hasAmount(item: ItemStack, amount: Int): Boolean {
        if (amount <= 0) {
            return true
        } else {
            val var5: Int = storageContents.also { storageContents = it }.size
            for (var4 in 0 until var5) {
                val i = storageContents.getOrNull(var4)
                if(i != null) {
                    if(item same (i) && (amount - i.amount) <= 0) return true
                }

            }

            return false
        }
    }
}