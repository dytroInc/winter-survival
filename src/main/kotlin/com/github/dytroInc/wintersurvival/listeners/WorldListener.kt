package com.github.dytroInc.wintersurvival.listeners

import com.github.dytroInc.wintersurvival.plugin.WinterSurvival
import com.github.dytroInc.wintersurvival.system.Items
import com.github.dytroInc.wintersurvival.system.Temperature
import com.github.dytroInc.wintersurvival.system.crafting.*
import com.github.dytroInc.wintersurvival.utils.BasicUtils.same
import com.github.monun.invfx.openWindow
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.ItemSpawnEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.scheduler.BukkitRunnable
import kotlin.random.Random.Default.nextDouble

class WorldListener : Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        if(e.player.world.name != "winter") e.player.teleport(getWild())
        e.player.loadDefault()
    }
    @EventHandler
    fun onDeath(e: PlayerRespawnEvent) {
        object : BukkitRunnable() {
            override fun run() {

                e.player.teleport(getWild())
                e.player.loadDefault()
                Temperature.temperature[e.player.uniqueId] = 100.0
            }
        }.runTaskLater(WinterSurvival.instance, 1)

    }
    @EventHandler
    fun creatureSpawn(e: CreatureSpawnEvent) {
        if(e.entity is Monster || e.entity is Bat || e.entity is Llama || e.entity is WanderingTrader) {
            e.isCancelled = true
            return
        }
    }

    @EventHandler
    fun onItemSpawn(e: ItemSpawnEvent) {
        val item = e.entity.itemStack

        if (item same (Items.BLANK)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun click(e: InventoryClickEvent) {
        e.currentItem?.let {
            if(it same Items.BLANK) e.isCancelled = true
            if(it.isSimilar(Items.CRAFT_MENU)) {
                e.isCancelled = true
                (e.whoClicked as Player).openWindow(HandCraftingInventory().getInv())
            }
        }
    }
    private fun getWild(): Location {
        while (true) {
            val l = Location(Bukkit.getWorld("winter"), nextDouble(-200.0, 200.0), 0.0, nextDouble(-200.0, 200.0))
            if(l.world.getHighestBlockAt(l).type == Material.SNOW_BLOCK) {
                return l.add(0.0, 1.0, 0.0)
            }

        }
    }
    private fun Player.loadDefault() {
        inventory.apply {
            for(slot in 9..34) {
                setItem(slot, Items.BLANK)
            }
            setItem(35, Items.CRAFT_MENU)

        }
    }

}
