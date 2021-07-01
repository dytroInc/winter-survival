package com.github.dytroInc.wintersurvival.system.populators

import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.generator.BlockPopulator
import java.util.*

class RockPopulator : BlockPopulator() {



    override fun populate(world: World, r: Random, chunk: Chunk) {

        if(r.nextInt(5) < 3) {
            val rocks = ArrayList<Location>()


            val l = chunk.getBlock(r.nextInt(16), 0, r.nextInt(16)).location.toHighestLocation()
            if(l.block.type == Material.SNOW_BLOCK) {
                val material = when(r.nextInt(100)) {
                    0 -> Material.DIAMOND_ORE
                    in 1..15 -> Material.COBBLESTONE
                    in 16..25 -> Material.IRON_ORE
                    in 26..30 -> Material.GOLD_ORE
                    in 31..45 -> Material.COPPER_ORE
                    else -> Material.COAL_ORE
                }
                var dx = r.nextInt(2) + 1
                var sdx = (r.nextInt(2) + 1)
                var dz = r.nextInt(2) + 1
                var sdz = (r.nextInt(2) + 1)
                for(y in 1..3) {
                    for(x in -sdx..dx) {
                        for(z in -sdz..dz) {
                            if((x == -sdx && z == -sdz) || (x == -sdx && z == dz) || (x == dx && z == -sdz) || (x == dx && z == dz)) {
                                if(r.nextInt(4) < 3) {
                                    continue
                                }
                            }
                            rocks.add(
                                l.clone().add(x.toDouble(), y.toDouble(), z.toDouble())
                            )
                        }

                    }
                    dx = 0.coerceAtLeast(dx - 1)
                    sdx = 0.coerceAtLeast(sdx - 1)
                    dz = 0.coerceAtLeast(dx - 1)
                    sdz = 0.coerceAtLeast(sdx - 1)
                    if(sdx + dx + sdz + dz == 0) break
                }
                rocks.forEach {
                    create(it, material, r)
                }

            }

        }
    }

    private fun create(l: Location, m: Material, r: Random) {
        if(l.block.type == Material.AIR) {
            if(r.nextInt(10) < 2) {
                l.block.type = m
            } else {
                l.block.type = Material.STONE
            }
            create(l.clone().subtract(0.0, 1.0, 0.0), m, r)
        }
    }

}