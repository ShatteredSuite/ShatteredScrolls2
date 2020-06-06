package com.github.shatteredsuite.scrolls.data.scroll.cost

import com.github.shatteredsuite.scrolls.items.ScrollInstance
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect

class PotionCostType : CostType("potion") {
    override fun deserialize(data: Any): CostData {
        return PotionCostData(data as PotionEffect)
    }
}

class PotionCostData(val potion: PotionEffect) : CostData("potion") {
    override fun serialize(): Any {
        return potion
    }

    override fun onInteract(instance: ScrollInstance, player: Player): ScrollInstance {
        player.addPotionEffect(potion)
        return instance
    }
}