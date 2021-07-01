package com.github.dytroInc.wintersurvival.system.animals

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Cow
import org.bukkit.entity.Fox
import org.bukkit.entity.Sheep
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

object AnimalSpawn {

    val entities = listOf(
        Sheep::class.java,
        Cow::class.java,
        Fox::class.java
    )

    const val max = 125

    fun run(plugin: JavaPlugin) {
        object : BukkitRunnable() {
            override fun run() {
                if(Bukkit.getWorld("winter")?.livingEntities!!.size > max) return
                if(nextInt(25) == 0) {
                    if(Bukkit.getOnlinePlayers().isEmpty()) return
                    val l = Bukkit.getOnlinePlayers().random().location
                        .add(Random.nextDouble(-100.0, 100.0), 0.0, Random.nextDouble(-100.0, 100.0))
                    if(l.toHighestLocation().block.type == Material.SNOW_BLOCK) {
                        l.set(l.x, l.toHighestLocation().y + 1, l.z)
                        l.world.spawn(l, entities.random()) {
                            if(it is Fox) {
                                it.foxType = Fox.Type.SNOW
                            }
                        }
                    }

                }
            }
        }.runTaskTimer(plugin, 0, 5)
    }
}