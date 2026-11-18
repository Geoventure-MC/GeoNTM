package fr.geoventure.geontm.gametest;

import fr.geoventure.geontm.interfaces.ILookOverlay;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.lang.reflect.Method;
import java.util.List;

/** Dedicated-server contract checks for all block look-overlay implementations. */
public final class LookOverlayGameTests {

    private static final List<String> OVERLAY_BLOCKS = List.of(
            "fr.geoventure.geontm.blocks.network.FluidValve",
            "fr.geoventure.geontm.blocks.network.BlockFluidDuct",
            "fr.geoventure.geontm.blocks.generic.BlockToolConvertible",
            "fr.geoventure.geontm.blocks.machine.BlockMachineBattery",
            "fr.geoventure.geontm.blocks.machine.FoundryOutlet",
            "fr.geoventure.geontm.blocks.machine.FoundryCastingBase",
            "fr.geoventure.geontm.blocks.machine.HeaterFirebox",
            "fr.geoventure.geontm.blocks.machine.HeaterElectric",
            "fr.geoventure.geontm.blocks.machine.MachineCatalyticCracker",
            "fr.geoventure.geontm.blocks.machine.MachineCondenser",
            "fr.geoventure.geontm.blocks.machine.MachineDeuteriumExtractor",
            "fr.geoventure.geontm.blocks.machine.MachineDeuteriumTower",
            "fr.geoventure.geontm.blocks.machine.MachineDrain",
            "fr.geoventure.geontm.blocks.machine.MachineFractionTower",
            "fr.geoventure.geontm.blocks.machine.MachineHeatBoiler",
            "fr.geoventure.geontm.blocks.machine.MachineHeatBoilerIndustrial",
            "fr.geoventure.geontm.blocks.machine.MachineIndustrialTurbine",
            "fr.geoventure.geontm.blocks.machine.MachinePump",
            "fr.geoventure.geontm.blocks.machine.MachineSawmill",
            "fr.geoventure.geontm.blocks.machine.MachineSteamEngine",
            "fr.geoventure.geontm.blocks.machine.MachineStirling",
            "fr.geoventure.geontm.blocks.machine.MachineTurbineGas",
            "fr.geoventure.geontm.blocks.machine.fusion.MachineFusionBoiler",
            "fr.geoventure.geontm.blocks.machine.fusion.MachineFusionBreeder",
            "fr.geoventure.geontm.blocks.machine.fusion.MachineFusionMHDT",
            "fr.geoventure.geontm.blocks.machine.storage.MachineBatterySocket");

    private LookOverlayGameTests() {}

    /** Loads every server-registered producer and checks that its method descriptors stay client-free. */
    public static void blockClassesAreDedicatedServerSafe(GameTestHelper helper) {
        try {
            for (Method method : ILookOverlay.class.getDeclaredMethods()) {
                assertServerType(helper, method.getReturnType(), "ILookOverlay." + method.getName());
                for (Class<?> parameter : method.getParameterTypes()) {
                    assertServerType(helper, parameter, "ILookOverlay." + method.getName());
                }
            }

            for (String name : OVERLAY_BLOCKS) {
                Class<?> block = Class.forName(name, false, ILookOverlay.class.getClassLoader());
                helper.assertTrue(ILookOverlay.class.isAssignableFrom(block),
                        name + " no longer implements ILookOverlay");
                Method method = block.getDeclaredMethod("getLookOverlay", Level.class, BlockPos.class, Player.class);
                assertServerType(helper, method.getReturnType(), name + ".getLookOverlay");
                for (Class<?> parameter : method.getParameterTypes()) {
                    assertServerType(helper, parameter, name + ".getLookOverlay");
                }
            }
            helper.succeed();
        } catch (ReflectiveOperationException | LinkageError exception) {
            helper.fail("Look-overlay server classloading failed: " + exception);
        }
    }

    private static void assertServerType(GameTestHelper helper, Class<?> type, String owner) {
        String name = type.getName();
        helper.assertFalse(name.startsWith("net.minecraft.client.") || name.startsWith("fr.geoventure.geontm.client."),
                owner + " references client-only type " + name);
    }
}
