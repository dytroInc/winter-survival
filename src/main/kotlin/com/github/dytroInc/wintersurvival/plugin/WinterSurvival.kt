package com.github.dytroInc.wintersurvival.plugin

import com.github.dytroInc.wintersurvival.listeners.BlockListener
import com.github.dytroInc.wintersurvival.listeners.WorldListener
import com.github.dytroInc.wintersurvival.system.Temperature
import com.github.dytroInc.wintersurvival.system.WinterWorldCreator
import com.github.dytroInc.wintersurvival.system.WinterWorldGen
import com.github.dytroInc.wintersurvival.system.animals.AnimalSpawn
import com.github.dytroInc.wintersurvival.system.populators.RockPopulator
import com.github.monun.invfx.plugin.InvListener
import com.github.monun.kommand.kommand
import com.sk89q.worldedit.WorldEdit
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.entity.Player
import org.bukkit.generator.ChunkGenerator
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.random.Random.Default.nextLong

/**
 * @author Dytro
 *
 * Forked from monun/tap-sample-plugin
 */
class WinterSurvival : JavaPlugin() {

    companion object {
        val worldedit = WorldEdit.getInstance()
        val cooldown = ArrayList<UUID>()
        lateinit var instance: WinterSurvival
    }

    override fun getDefaultWorldGenerator(worldName: String, id: String?): ChunkGenerator? {
        return WinterWorldGen()
    }

    override fun onEnable() {
        instance = this
        server.apply {
            pluginManager.apply {
                registerEvents(WorldListener(), this@WinterSurvival)
                registerEvents(BlockListener(), this@WinterSurvival)
                registerEvents(InvListener(), this@WinterSurvival)
            }


            createWorld(WinterWorldCreator())?.apply {
                setGameRule(GameRule.DO_WEATHER_CYCLE, false)
            }
            val iter = recipeIterator()
            while(iter.hasNext()) {
                iter.next()
                iter.remove()
            }
            Temperature.run(this@WinterSurvival)
            AnimalSpawn.run(this@WinterSurvival)
        }



    }


}
