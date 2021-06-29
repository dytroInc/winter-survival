package com.github.dytroInc.wintersurvival.system.populators

import org.bukkit.*
import org.bukkit.generator.BlockPopulator
import java.util.*
import kotlin.random.Random.Default.nextInt

class TreePopulator : BlockPopulator() {
    override fun populate(world: World, random: Random, chunk: Chunk) {
        if(nextInt(10) < 8) {
            val amount = nextInt(1, 3)

            for (i in 1..amount) {
                val x = random.nextInt(15)
                val z = random.nextInt(15)
                val l = chunk.getBlock(x, 0, z).location.toHighestLocation()
                if(l.block.type == Material.SNOW_BLOCK) {
                    l.block.type = Material.DIRT
                    l.clone().add(0.0, 1.0, 0.0).let {
                        world.generateTree(
                            it,
                            TreeType.REDWOOD
                        )
                    }
                    l.block.type = Material.SNOW_BLOCK
                }


            }
        }
    }
}