package com.github.dytroInc.wintersurvival.system.crafting

import com.github.dytroInc.wintersurvival.plugin.WinterSurvival
import com.github.dytroInc.wintersurvival.utils.BasicUtils.hasAmount
import com.github.monun.invfx.InvScene
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

abstract class CustomCraftingInventory {
    abstract fun getInv(): InvScene
    abstract fun getItemList(): HashMap<ItemStack, ArrayList<ItemStack>>
    fun buy(p: Player, stack: ItemStack, needs: ArrayList<ItemStack>) {
        if(WinterSurvival.cooldown.contains(p.uniqueId)) return
        WinterSurvival.cooldown.add(p.uniqueId)

        object : BukkitRunnable() {
            override fun run() {
                WinterSurvival.cooldown.remove(p.uniqueId)
            }
        }.runTaskLater(WinterSurvival.instance, 2)

        needs.forEach {
            if(!p.inventory.hasAmount(it, it.amount)) {
                p.playSound(p.location, Sound.ENTITY_VILLAGER_NO, SoundCategory.MASTER, 1f, 1f)
                return
            }
        }
        if(p.inventory.firstEmpty() == -1) return p.playSound(p.location, Sound.BLOCK_CHAIN_STEP, SoundCategory.MASTER, 1f, 1f)

        needs.forEach {
            p.inventory.removeItem(it)
        }
        p.playSound(p.location, Sound.BLOCK_ANVIL_USE, SoundCategory.MASTER, 1f, 1f)
        p.inventory.addItem(stack)
    }


}