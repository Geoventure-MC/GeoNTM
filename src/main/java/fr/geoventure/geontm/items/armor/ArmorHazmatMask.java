package fr.geoventure.geontm.items.armor;

import fr.geoventure.geontm.api.data.ArmorDataMaps;
import fr.geoventure.geontm.api.item.IGasMask;
import fr.geoventure.geontm.handler.ArmorUtil;
import fr.geoventure.geontm.util.ArmorRegistry.HazardClass;
import fr.geoventure.geocore.world.ArmorSlot;
import fr.geoventure.geocore.world.GeoCoreArmorMaterial;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * 1.7 {@code ArmorHazmatMask}: a hazmat hood that takes a gas-mask filter. A full hood has no blacklist.
 * Only the red and grey hoods used the M65 model upstream; the PaA hood this port uses it for draws the
 * ordinary armor texture.
 */
public class ArmorHazmatMask extends ArmorHazmat implements IGasMask {

    public ArmorHazmatMask(GeoCoreArmorMaterial material, ArmorSlot slot, Properties properties) {
        super(material, slot, properties);
    }

    @Override
    public void addInformation(ItemStack stack, List<Component> tooltip) {
        ArmorUtil.addGasMaskTooltip(stack, tooltip);
        super.addInformation(stack, tooltip);
    }

    @Override
    public List<HazardClass> getBlacklist(ItemStack stack) {
        return ArmorDataMaps.gasMaskBlacklist(this);
    }

    @Override
    public ItemStack getFilter(ItemStack stack) {
        return GasMaskFilters.get(stack);
    }

    @Override
    public boolean isFilterApplicable(ItemStack stack, ItemStack filter) {
        return GasMaskFilters.applicable(filter);
    }

    @Override
    public void installFilter(ItemStack stack, ItemStack filter) {
        GasMaskFilters.install(stack, filter);
    }

    @Override
    public void damageFilter(ItemStack stack, int damage) {
        GasMaskFilters.damage(stack, damage);
    }

    @Override
    protected InteractionResult compatUse(Level level, Player player, InteractionHand hand) {
        InteractionResult removed = GasMaskFilters.sneakRemove(level, player, hand);
        return removed != null ? removed : super.compatUse(level, player, hand);
    }
}
