package com.github.dytroInc.wintersurvival.system

import com.github.dytroInc.wintersurvival.system.tribes.Tribe.Companion.hasAdvantage
import com.github.dytroInc.wintersurvival.system.tribes.Tribe.Companion.hasDisadvantage
import com.github.dytroInc.wintersurvival.system.tribes.TribeAdvantages
import com.github.dytroInc.wintersurvival.system.tribes.TribeDisadvantages
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.block.BlastFurnace
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.ceil

object GameSystem {
    val temperature = HashMap<UUID, Double>()
    val campfire = HashMap<Location, Double>()
    val furnace = HashMap<Location, Double>()
    val goods = ArrayList<EnvelopedMail>()

    fun run(plugin: JavaPlugin) {
        object : BukkitRunnable() {
            override fun run() {
                Bukkit.getOnlinePlayers().forEach {
                    if(!temperature.containsKey(it.uniqueId)) temperature[it.uniqueId] = 100.0
                    // COLD
                    var rate = if(it.location.clone().subtract(0.0, 1.0, 0.0).block.type == Material.SNOW_BLOCK) 0.05 else 0.025
                    if(it.location.block.type == Material.POWDER_SNOW) {
                        rate += 3
                    }
                    if(it.hasDisadvantage(TribeDisadvantages.COLDER_MEMBER)) rate *= 1.2

                    // COLD REDUCTION
                    if(it.hasAdvantage(TribeAdvantages.COLD_REDUCTION)) rate *= 0.9
                    val boots = it.inventory.boots
                    if(boots != null) {
                        if(boots.getClothesType() == ClothesType.LEATHER) {
                            rate *= (0.95)
                        }
                    }
                    val chestplate = it.inventory.chestplate
                    if(chestplate != null) {
                        if(chestplate.getClothesType() == ClothesType.LEATHER) {
                            rate *= (0.8)
                        }
                    }

                    // HEAT
                    if(campfire.any { c ->
                            c.key.getNearbyPlayers(3.0).contains(it)
                        }) {
                        rate -= 0.2
                    }
                    if(furnace.any { c ->
                            c.key.getNearbyPlayers(5.0).contains(it) && c.value > 0
                        }) {
                        rate -= 0.35
                    }

                    if(it.hasDisadvantage(TribeDisadvantages.HUNGER)) {
                        it.saturation = 0f
                    }

                    temperature[it.uniqueId] = 0.0.coerceAtLeast(100.0.coerceAtMost(temperature[it.uniqueId]!! - rate))
                    it.sendActionBar(
                        message(temperature[it.uniqueId]!!)
                    )
                    if(temperature[it.uniqueId] == 0.0) {
                        it.damage(1.0)
                    }


                }
                furnace.forEach { (t, u) ->
                    if(u > 0) {
                        furnace[t] = u - 0.01
                        val b = t.block.state
                        if(b is BlastFurnace) {
                            b.burnTime = ceil(u).toInt().toShort()
                            b.update()
                        }
                    }
                }
                campfire.forEach { (t, u) ->
                    campfire[t] = u - 0.01
                    val b = t.block
                    if(campfire[t]!! <= 0.0) {
                        b.type = Material.AIR
                        b.world.spawnParticle(
                            Particle.ASH,
                            b.location.clone().add(0.0, 1.0, 0.0),
                            75,
                            5.0,
                            5.0,
                            5.0,
                            5.0,
                            null
                        )
                    }
                }
                goods.forEach {
                    if(campfire.containsKey(it.sender)) {
                        if(campfire[it.sender]!! > 0.0) {
                            it.time -= 1
                            if(it.time == 0) {
                                it.sender.world.dropItemNaturally(it.sender, it.itemStack)
                            }
                        } else {
                            it.sender.world.dropItemNaturally(it.sender, it.original)
                        }
                    } else if(furnace.containsKey(it.sender)) {
                        if(furnace[it.sender]!! > 0.0) {
                            it.time -= 1
                            if(it.time == 0) {
                                it.sender.world.dropItemNaturally(it.sender.clone().add(0.0, 1.0, 0.0), it.itemStack)
                            }
                        }
                    }
                }
                campfire.entries.removeIf {
                    it.value <= 0
                }
                goods.removeIf {
                    !(campfire.containsKey(it.sender) || furnace.containsKey(it.sender)) || it.time <= 0
                }

            }
        }.runTaskTimer(plugin, 0, 1)
    }
    fun message(amount: Double) = run {
        val highlighted = ceil(amount / 5).toInt()
        val unhighlighted = 20 - highlighted
        val ht = run {
            var s = ""
            repeat(highlighted) {
                s += "▌"
            }
            s
        }
        val uht = run {
            var s = ""
            repeat(unhighlighted) {
                s += "▌"
            }
            s
        }

        Component.text("❄ ").color(TextColor.color(0x0FFFFF))
            .append(Component.text("«[ ").color(TextColor.color(0x222222)))
            .append(Component.text(ht).color(TextColor.color(0x0FFFFF)))
            .append(Component.text(uht).color(TextColor.color(0xCCCCCC)))
            .append(Component.text(" ${ceil(amount).toInt()}% ").color(TextColor.color(0x0CCCCC)))
            .append(Component.text("]»").color(TextColor.color(0x222222)))
            .append(Component.text(" ❄").color(TextColor.color(0x0FFFFF)))
    }

}