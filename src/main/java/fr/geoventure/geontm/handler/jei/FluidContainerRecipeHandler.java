package fr.geoventure.geontm.handler.jei;

import fr.geoventure.geontm.inventory.FluidContainerRegistry;
import fr.geoventure.geontm.inventory.fluid.FluidStack;
import fr.geoventure.geontm.items.ModFluidItems;
import fr.geoventure.geontm.items.ModItems;
import fr.geoventure.geontm.items.ModSingularityItems;
import fr.geoventure.geontm.items.machine.ItemFluidIcon;
import fr.geoventure.geocore.viewer.Role;
import fr.geoventure.geocore.viewer.SlotLayout;
import fr.geoventure.geocore.viewer.ViewerType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * 1.7 {@code FluidRecipeHandler} ("Fluid Containers"): the fluid and the empty container on the left of the
 * two-way arrow, the full container on the right. 1.7 looked every one of the three up both ways, so each is also an
 * invisible ingredient of the other role.
 */
public class FluidContainerRecipeHandler extends GeoNtmRecipeCategory<FluidContainerRegistry.Entry> {

    public FluidContainerRecipeHandler(ViewerType<FluidContainerRegistry.Entry> type, Component title) {
        super(type, title, machines().get(0), "gui_nei_fluid.png");
    }

    public static List<FluidContainerRegistry.Entry> buildRecipes() {
        return FluidContainerRegistry.INSTANCE.allContainers();
    }

    /** 1.7 {@code getMachinesForRecipe}. */
    public static List<ItemStack> machines() {
        return List.of(new ItemStack(ModFluidItems.fluid_barrel.get()), new ItemStack(ModFluidItems.fluid_tank.get()),
                new ItemStack(ModItems.fluid_tank_lead_empty.get()), new ItemStack(ModItems.canister_empty.get()),
                new ItemStack(ModItems.gas_empty.get()), new ItemStack(ModSingularityItems.cell.get()),
                new ItemStack(ModItems.disperser_canister_empty.get()), new ItemStack(ModItems.glyphid_gland_empty.get()));
    }

    @Override
    public void setRecipe(SlotLayout builder, FluidContainerRegistry.Entry recipe) {
        List<ItemStack> left = new ArrayList<>();
        left.add(ItemFluidIcon.make(new FluidStack(recipe.type(), recipe.amount())));
        builder.addSlot(Role.INPUT, 30, 24).addItemStack(left.get(0));
        if (!recipe.empty().isEmpty()) {
            left.add(recipe.empty());
            builder.addSlot(Role.INPUT, 48, 24).addItemStack(recipe.empty());
        }
        builder.addSlot(Role.OUTPUT, 120, 24).addItemStack(recipe.full());
        builder.addInvisible(Role.INPUT, List.of(recipe.full()));
        builder.addInvisible(Role.OUTPUT, left);
    }
}
