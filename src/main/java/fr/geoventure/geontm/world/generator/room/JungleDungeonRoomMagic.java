package fr.geoventure.geontm.world.generator.room;

import fr.geoventure.geontm.blocks.generic.TrappedBrick.Trap;
import fr.geoventure.geontm.lib.ForgeDirection;
import fr.geoventure.geontm.world.gen.LegacyDungeonBlocks.Blocks;
import fr.geoventure.geontm.world.gen.LegacyDungeonBlocks.ModBlocks;
import fr.geoventure.geontm.world.gen.LegacyDungeonWorld;
import fr.geoventure.geontm.world.gen.component.LegacyBlock;
import fr.geoventure.geontm.world.generator.CellularDungeon;
import fr.geoventure.geontm.world.generator.CellularDungeonRoom;
import fr.geoventure.geontm.world.generator.DungeonToolbox;
import fr.geoventure.geontm.world.generator.DungeonToolbox.MetaBlock;
import fr.geoventure.geontm.world.generator.JungleDungeon;
import fr.geoventure.geontm.world.generator.TimedGenerator.ITimedJob;

import java.util.ArrayList;
import java.util.List;



/** 1.7 {@code fr.geoventure.geontm.world.generator.room.JungleDungeonRoomMagic}, on {@link LegacyDungeonWorld}; jobs go to the site's queue. */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
public class JungleDungeonRoomMagic extends JungleDungeonRoom {

	public JungleDungeonRoomMagic(CellularDungeon parent) {
		super(parent);
	}

	public void generateMain(final LegacyDungeonWorld world, final int x, final int y, final int z) {
		super.generateMain(world, x, y, z);
		
		ITimedJob job = new ITimedJob() {

			@Override
			public void work() {

				int ix = world.rand.nextInt(3) + 1;
				int iz = world.rand.nextInt(3) + 1;
				
				LegacyBlock bl = world.getBlock(x + ix, y, z + iz);
				if(bl == ModBlocks.brick_jungle || bl == ModBlocks.brick_jungle_cracked || bl == ModBlocks.brick_jungle_lava) {
					world.setBlock(x + ix, y, z + iz, ModBlocks.brick_jungle_trap, Trap.MAGIC_CONVERSTION.ordinal(), 3);
				}
			}
		};
		
		parent.timed.addOp(job);
	}
}
