package com.github.dytroInc.wintersurvival.listeners

import com.github.dytroInc.wintersurvival.system.tribes.Tribe.Companion.hasAdvantage
import com.github.dytroInc.wintersurvival.system.tribes.Tribe.Companion.tribe
import com.github.dytroInc.wintersurvival.system.tribes.TribeAdvantages
import com.github.dytroInc.wintersurvival.system.tribes.TribeDisadvantages
import com.github.dytroInc.wintersurvival.system.tribes.TribeJoinEvent
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerAttemptPickupItemEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class PlayerListener : Listener {
    @EventHandler
    fun damage(e: EntityDamageEvent) {
        val vic = e.entity
        if(vic is Player) {
            if(vic.hasAdvantage(TribeAdvantages.RESISTANCE)) {
                e.damage *= 0.8
            }
        }
    }
    @EventHandler
    fun damageOther(e: EntityDamageByEntityEvent) {
        val vic = e.entity
        val off = e.damager
        if(off is Player) {
            if(off.hasAdvantage(TribeAdvantages.STRENGTH)) {
                e.damage *= 1.1
            }
            if(vic is Player) {
                if(vic.tribe() != null && vic.tribe() == off.tribe()) {
                    off.sendMessage("${ChatColor.RED}그 분은 님 부족 멤버에요!")
                    e.isCancelled = true
                }
            }
        }

    }
    @EventHandler
    fun joinTribe(e: TribeJoinEvent) {
        if(e.tribe.hasDisadvantage(TribeDisadvantages.SLOWNESS)) {
            e.player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, Int.MAX_VALUE, 1, false, false, false)) // 구속 2
        } else if(e.tribe.hasDisadvantage(TribeDisadvantages.HUNGER)) {
            e.player.addPotionEffect(PotionEffect(PotionEffectType.HUNGER, Int.MAX_VALUE, 1, false, false, false)) // 구속 2
        }
        if(e.tribe.hasAdvantage(TribeAdvantages.SPEED)) {
            e.player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 1, false, false, false)) // 구속 2
        }
    }

    private val rawItems = EnumSet.of(
        Material.BEEF,
        Material.MUTTON
    )

    @EventHandler
    fun eat(e: PlayerItemConsumeEvent) {
        if(e.item.type in rawItems) {
            e.player.addPotionEffect(
                PotionEffect(
                    PotionEffectType.POISON,
                    20 * 10,
                    0
                )
            )
        }
    }

    @EventHandler
    fun pick(e: PlayerAttemptPickupItemEvent) {
        e.player.updateInventory()
    }
    @EventHandler
    fun drop(e: PlayerDropItemEvent) {
        e.player.updateInventory()
    }
    @EventHandler
    fun swap(e: PlayerSwapHandItemsEvent) {
        e.player.updateInventory()
    }
}