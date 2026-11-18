package fr.geoventure.geontm.module.machine;

import fr.geoventure.geontm.api.energymk2.IEnergyHandlerMK2;
import fr.geoventure.geontm.inventory.NTMItemHandler;
import fr.geoventure.geontm.inventory.fluid.tank.FluidTankNTM;
import fr.geoventure.geontm.inventory.recipes.RockMillRecipes;
import fr.geoventure.geontm.inventory.recipes.loader.GenericRecipe;
import fr.geoventure.geontm.inventory.recipes.loader.GenericRecipes;
import fr.geoventure.geontm.util.BobMathUtil;

/** 1.7 {@code ModuleMachineRockMill}: 3 item inputs and outputs, one tank each way sized {@code max(fill, 2 × recipe, 4,000)}. */
public class ModuleMachineRockMill extends ModuleMachineBase {

    public ModuleMachineRockMill(int index, IEnergyHandlerMK2 battery, NTMItemHandler inventory) {
        super(index, battery, inventory);
        this.inputSlots = new int[3];
        this.outputSlots = new int[3];
        this.inputTanks = new FluidTankNTM[1];
        this.outputTanks = new FluidTankNTM[1];
    }

    @Override
    public GenericRecipes getRecipeSet() {
        return RockMillRecipes.INSTANCE;
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

    public ModuleMachineRockMill itemInput(int from) { for (int i = 0; i < inputSlots.length; i++) inputSlots[i] = from + i; return this; }
    public ModuleMachineRockMill itemOutput(int from) { for (int i = 0; i < outputSlots.length; i++) outputSlots[i] = from + i; return this; }
    public ModuleMachineRockMill fluidInput(FluidTankNTM a) { inputTanks[0] = a; return this; }
    public ModuleMachineRockMill fluidOutput(FluidTankNTM a) { outputTanks[0] = a; return this; }
}
