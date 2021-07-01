package com.github.dytroInc.wintersurvival.listeners

import com.github.dytroInc.wintersurvival.plugin.WinterSurvival
import com.github.dytroInc.wintersurvival.system.EnvelopedMail
import com.github.dytroInc.wintersurvival.system.Items.foodCookedResult
import com.github.dytroInc.wintersurvival.system.Items.fuel
import com.github.dytroInc.wintersurvival.system.Items.goodsCookedResult
import com.github.dytroInc.wintersurvival.system.GameSystem
import com.github.dytroInc.wintersurvival.system.Items
import com.github.dytroInc.wintersurvival.system.crafting.*
import com.github.monun.invfx.openWindow
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import kotlin.random.Random.Default.nextInt
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.block.LeavesDecayEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.text.DecimalFormat


class BlockListener : Listener {
    @EventHandler
    fun breakBlock(e: BlockBreakEvent) {

        val tool = e.player.inventory.itemInMainHand
        if((e.block.type == Material.SNOW_BLOCK || e.block.type == Material.POWDER_SNOW) && e.player.gameMode != GameMode.CREATIVE && !tool.type.name.contains("shovel", true)) {
            e.isCancelled = true
        }
        if(e.block.type == Material.DEEPSLATE) e.isCancelled = true
        GameSystem.goods.filter {
            it.sender == e.block.location
        }.forEach {
            it.sender.world.dropItemNaturally(it.sender.clone().add(0.0, 1.0, 0.0), it.itemStack)
        }
        if((e.block.type == Material.CAMPFIRE)) {
            GameSystem.campfire.remove(e.block.location)
        }
        if((e.block.type == Material.BLAST_FURNACE)) {
            GameSystem.furnace.remove(e.block.location)
        }
    }

    @EventHandler
    fun decay(e: LeavesDecayEvent) {
        e.isCancelled = true
    }

    @EventHandler
    fun loot(e: BlockBreakEvent) {
        if(e.player.gameMode == GameMode.CREATIVE) return
        val drops = ArrayList<ItemStack>()
        e.block.let {
            e.isDropItems = false
            when(it.type) {
                Material.SPRUCE_LEAVES -> {
                    if(nextInt(5) == 0) {
                        drops.add(
                            ItemStack(Material.SWEET_BERRIES, nextInt(1, 3)).apply {
                                itemMeta = itemMeta!!.apply {
                                    displayName(Component.text(
                                        "${ChatColor.WHITE}산딸기"
                                    ))
                                }
                            }
                        )
                    }
                }
                Material.CAMPFIRE -> {
                    drops.add(Items.STICK.clone().apply {
                        amount = 3
                    })
                }
                Material.BLAST_FURNACE -> {
                    drops.add(Items.STICK.clone().apply {
                        amount = 15
                    })
                    drops.add(ItemStack(Material.COBBLESTONE, 30))
                }
                else -> e.isDropItems = true
            }
            drops.forEach { drop ->
                it.world.dropItemNaturally(it.location, drop)
            }
        }

    }

    @EventHandler
    fun placeBlock(e: BlockPlaceEvent) {

        if(e.block.type == Material.CAMPFIRE) {
            GameSystem.campfire[e.block.location] = 5.0
        }
        if(e.block.type == Material.BLAST_FURNACE) {
            GameSystem.furnace[e.block.location] = 30.0
        }

    }

    @EventHandler
    fun rightClick(e: PlayerInteractEvent) {
        val p = e.player
        e.clickedBlock?.let {
            if(e.action == Action.RIGHT_CLICK_BLOCK) {
                if(WinterSurvival.cooldown.contains(p.uniqueId)) return
                WinterSurvival.cooldown.add(p.uniqueId)
                object : BukkitRunnable() {
                    override fun run() {
                        WinterSurvival.cooldown.remove(p.uniqueId)
                    }
                }.runTaskLater(WinterSurvival.instance, 2)
                handleInteraction(e, it, p)
            }
        }
    }

    private fun handleInteraction(e: PlayerInteractEvent, b: Block, p: Player) {
        when(b.type) {
            Material.CRAFTING_TABLE -> {
                e.isCancelled = true
                p.openWindow(WorkbenchCraftingInventory().getInv())
            }
            Material.BEEHIVE ->  {
                p.openWindow(CarpentryCraftingInventory().getInv())
            }
            Material.STONECUTTER -> {
                e.isCancelled = true
                p.openWindow(CutterCraftingInventory().getInv())
            }
            Material.SMITHING_TABLE -> {
                e.isCancelled = true
                p.openWindow(ToolWorkbenchCraftingInventory().getInv())
            }
            Material.LOOM -> {
                e.isCancelled = true
                p.openWindow(LoomCraftingInventory().getInv())
            }
            Material.CAMPFIRE -> {
                e.isCancelled = true
                if(p.isSneaking) {
                    p.sendMessage("${ChatColor.GOLD}예상 연료 소모 시간: " +
                            "${ChatColor.AQUA}${DecimalFormat("#.##").format(GameSystem.campfire[b.location]?.div(0.2))}" +
                            "${ChatColor.GOLD}초")
                } else {
                    e.item?.let { item ->
                        if(item.fuel() > 0) {

                            GameSystem.campfire[b.location] = (GameSystem.campfire[b.location]?: 5.0) + item.fuel()
                            item.subtract()
                            p.playSound(
                                p.location,
                                Sound.ITEM_FIRECHARGE_USE,
                                SoundCategory.MASTER,
                                1f,
                                1f
                            )
                        } else if(item.foodCookedResult() != null) {
                            GameSystem.goods.add(EnvelopedMail(item.foodCookedResult()!!, 20 * 5, p.uniqueId, b.location, item.clone().apply {
                                amount = 1
                            }))
                            item.subtract()
                            p.playSound(
                                p.location,
                                Sound.ITEM_FIRECHARGE_USE,
                                SoundCategory.MASTER,
                                1f,
                                1f
                            )
                        }
                    }
                }
            }
            Material.BLAST_FURNACE -> {
                e.isCancelled = true
                if(p.isSneaking) {
                    p.sendMessage("${ChatColor.GOLD}예상 연료 소모 시간: " +
                            "${ChatColor.AQUA}${DecimalFormat("#.##").format(GameSystem.furnace[b.location]?.div(0.2))}" +
                            "${ChatColor.GOLD}초")
                } else {
                    e.item?.let { item ->
                        if(item.fuel() > 0) {
                            GameSystem.furnace[b.location] = (GameSystem.furnace[b.location]?: 35.0) + item.fuel()
                            item.subtract()
                            p.playSound(
                                p.location,
                                Sound.ITEM_FIRECHARGE_USE,
                                SoundCategory.MASTER,
                                1f,
                                1f
                            )
                        } else if(item.foodCookedResult() != null) {
                            GameSystem.goods.add(EnvelopedMail(item.foodCookedResult()!!, 20 * 3, p.uniqueId, b.location, item.clone().apply {
                                amount = 1
                            }))
                            item.subtract()
                            p.playSound(
                                p.location,
                                Sound.ITEM_FIRECHARGE_USE,
                                SoundCategory.MASTER,
                                1f,
                                1f
                            )
                        } else if(item.goodsCookedResult() != null) {
                            GameSystem.goods.add(EnvelopedMail(item.goodsCookedResult()!!, 20 * 4, p.uniqueId, b.location, item.clone().apply {
                                amount = 1
                            }))
                            item.subtract()
                            p.playSound(
                                p.location,
                                Sound.ITEM_FIRECHARGE_USE,
                                SoundCategory.MASTER,
                                1f,
                                1f
                            )
                        }
                    }
                }
            }
            else -> {}

        }
    }
}