package com.github.dytroInc.wintersurvival.system.animals

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Cow
import org.bukkit.entity.Sheep
import org.bukkit.entity.Wolf
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

object AnimalSpawn {

    val entities = listOf(
        Sheep::class.java,
        Cow::class.java
    )

    const val max = 125

    fun run(plugin: JavaPlugin) {
        object : BukkitRunnable() {
            override fun run() {
                if(Bukkit.getWorld("winter")?.livingEntities!!.size > max) return
                if(nextInt(25) == 0) {
                    val l = Bukkit.getOnlinePlayers().random().location
                        .add(Random.nextDouble(-100.0, 100.0), 0.0, Random.nextDouble(-100.0, 100.0))
                    if(l.world.getHighestBlockAt(l).type == Material.SNOW_BLOCK) {
                        l.add(0.0, 1.0, 0.0)
                        l.world.spawn(l, entities.random())
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 5)
    }
}