package fr.iglee42.createqualityoflife.items;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.equipment.armor.BacktankUtil;
import com.simibubi.create.content.equipment.armor.DivingHelmetItem;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
import fr.iglee42.createqualityoflife.registries.ModArmorMaterials;
import fr.iglee42.createqualityoflife.registries.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class ShadowRadianceHelmet extends DivingHelmetItem {
    public ShadowRadianceHelmet(ArmorMaterial material, Properties properties, ResourceLocation textureLoc) {
        super(material, properties, textureLoc);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }
    static {
        GogglesItem.addIsWearingPredicate(player -> ModItems.SHADOW_RADIANCE_HELMET.isIn(player.getItemBySlot(EquipmentSlot.HEAD)));
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (level.canSeeSky(player.blockPosition())){
            if (!player.isCreative() && !BacktankUtil.getAllWithAir(player).isEmpty())BacktankUtil.consumeAir(player, BacktankUtil.getAllWithAir(player).get(0), 0.01f);
            else if (!player.isCreative()) return;
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION,20*11,1,false,false));
        }
    }
}
