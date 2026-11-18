package fr.geoventure.geontm.module.machine;

import fr.geoventure.geontm.api.energymk2.IEnergyHandlerMK2;
import fr.geoventure.geontm.inventory.NTMItemHandler;
import fr.geoventure.geontm.inventory.fluid.tank.FluidTankNTM;
import fr.geoventure.geontm.inventory.recipes.loader.GenericRecipe;
import fr.geoventure.geontm.util.BobMathUtil;
import fr.geoventure.geontm.inventory.recipes.AssemblyMachineRecipes;
import fr.geoventure.geontm.inventory.recipes.loader.GenericRecipes;

/**
 * Assembler processing module (1.7.10 {@code ModuleMachineAssembler}): 12 item input slots, 1 output slot,
 * one input and one output tank; each tank is resized to {@code max(fill, 2 × recipe amount, 4,000)}.
 */
public class ModuleMachineAssembler extends ModuleMachineBase {

    public ModuleMachineAssembler(int index, IEnergyHandlerMK2 battery, NTMItemHandler inventory) {
        super(index, battery, inventory);
        this.inputSlots = new int[12];
        this.outputSlots = new int[1];
        this.inputTanks = new FluidTankNTM[1];
        this.outputTanks = new FluidTankNTM[1];
    }

    @Override
    public void setupTanks(GenericRecipe recipe) {
        super.setupTanks(recipe);
        if (recipe == null) return;
        for (int i = 0; i < inputTanks.length; i++) if (recipe.inputFluid != null && recipe.inputFluid.length > i)
            inputTanks[i].changeTankSize((int) BobMathUtil.max(inputTanks[i].getFill(), recipe.inputFluid[i].fill * 2L, 4_000));
        for (int i = 0; i < outputTanks.length; i++) if (recipe.outputFluid != null && recipe.outputFluid.length > i)
            outputTanks[i].changeTankSize((int) BobMathUtil.max(outputTanks[i].getFill(), recipe.outputFluid[i].fill * 2L, 4_000));
    }

    @Override
    public GenericRecipes getRecipeSet() {
        return AssemblyMachineRecipes.INSTANCE;
    }

    public ModuleMachineAssembler itemInput(int from) {
        for (int i = 0; i < inputSlots.length; i++) inputSlots[i] = from + i;
        return this;
    }

    public ModuleMachineAssembler itemOutput(int slot) {
        outputSlots[0] = slot;
        return this;
    }

    public ModuleMachineAssembler fluidInput(FluidTankNTM a) { inputTanks[0] = a; return this; }
    public ModuleMachineAssembler fluidOutput(FluidTankNTM a) { outputTanks[0] = a; return this; }
}
