package fr.geoventure.geontm.tileentity.machine.rbmk;
import fr.geoventure.geontm.entity.projectile.EntityRBMKDebris;
import fr.geoventure.geontm.tileentity.ModRBMKBlockEntities;
import net.minecraft.core.BlockPos; import net.minecraft.world.level.block.state.BlockState;
/** OTHER is transparent to neutron propagation. */
public class BlockEntityRBMKBlank extends BlockEntityRBMKBase { public BlockEntityRBMKBlank(BlockPos p,BlockState s){super(ModRBMKBlockEntities.RBMK_BLANK.get(),p,s,0);}@Override public void onMelt(int reduce) { spawnDebris(EntityRBMKDebris.DebrisType.BLANK, 1 + level.getRandom().nextInt(2)); super.onMelt(reduce); } @Override public RBMKColumnType getConsoleType(){return RBMKColumnType.BLANK;} }
