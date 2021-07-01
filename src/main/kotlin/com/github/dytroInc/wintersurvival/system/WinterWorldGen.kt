package com.github.dytroInc.wintersurvival.system

import com.github.dytroInc.wintersurvival.system.populators.RockPopulator
import com.github.dytroInc.wintersurvival.system.populators.TreePopulator
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Biome
import org.bukkit.generator.BlockPopulator
import org.bukkit.generator.ChunkGenerator
import org.bukkit.util.noise.PerlinNoiseGenerator
import java.util.*
import kotlin.random.Random.Default.nextInt

class WinterWorldGen : ChunkGenerator() {
    companion object {
        val offset = 50.0
    }
    private val generator = PerlinNoiseGenerator(1200120)
    override fun generateChunkData(world: World, random: Random, cx: Int, cz: Int, biome: BiomeGrid): ChunkData {

        val chunk = createChunkData(world)

        for (x in 0..15) for (z in 0..15) {
            val currentHeight = (generator.noise((cx * 16.0 + x) / offset, (cz * 16.0 + z) / offset, 1, 1.0, 0.75) * 10.0 + 60.0).toInt()
            for(y in 0..(currentHeight - 2)) {
                chunk.setBlock(x, y, z, Material.DEEPSLATE)
            }
            val m = if(currentHeight <= 54 || nextInt(500) == 0) Material.POWDER_SNOW else Material.SNOW_BLOCK
            chunk.setBlock(x, currentHeight, z, m)
            chunk.setBlock(x, currentHeight - 1, z, m)
            chunk.setBlock(x, 0, z, Material.BEDROCK)
            /*
            for(y in 0..256) {
                world.setBiome(x, y, z, Biome.SNOWY_TUNDRA)
            }
            for(y in 45..150) {
                world.setBiome(x, y, z, Biome.SNOWY_TUNDRA)
            }
            */


        }

        return chunk
    }





    override fun getDefaultPopulators(world: World): MutableList<BlockPopulator> {
        return mutableListOf(
            RockPopulator(),
            TreePopulator()
        )
    }

    override fun canSpawn(world: World, x: Int, z: Int): Boolean {
        return world.getHighestBlockAt(x, z).type == Material.SNOW_BLOCK
    }



}