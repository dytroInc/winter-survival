package com.github.dytroInc.wintersurvival.plugin

import com.github.dytroInc.wintersurvival.listeners.BlockListener
import com.github.dytroInc.wintersurvival.listeners.PlayerListener
import com.github.dytroInc.wintersurvival.listeners.WorldListener
import com.github.dytroInc.wintersurvival.system.GameSystem
import com.github.dytroInc.wintersurvival.system.WinterWorldCreator
import com.github.dytroInc.wintersurvival.system.WinterWorldGen
import com.github.dytroInc.wintersurvival.system.animals.AnimalSpawn
import com.github.dytroInc.wintersurvival.system.tribes.TribeCommand
import com.github.monun.invfx.plugin.InvListener
import com.github.monun.kommand.kommand
import com.sk89q.worldedit.WorldEdit
import org.bukkit.GameRule
import org.bukkit.generator.ChunkGenerator
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import kotlin.collections.ArrayList

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
                registerEvents(PlayerListener(), this@WinterSurvival)
                registerEvents(InvListener(), this@WinterSurvival)
            }


            createWorld(WinterWorldCreator())?.apply {
                setGameRule(GameRule.DO_WEATHER_CYCLE, false)
                setGameRule(GameRule.DO_INSOMNIA, true)
            }
            val iter = recipeIterator()
            while(iter.hasNext()) {
                iter.next()
                iter.remove()
            }
            GameSystem.run(this@WinterSurvival)
            AnimalSpawn.run(this@WinterSurvival)
            registerKommands()
        }
    }

    private fun registerKommands() = kommand {
        TribeCommand.buildCommands(this)
    }


}
