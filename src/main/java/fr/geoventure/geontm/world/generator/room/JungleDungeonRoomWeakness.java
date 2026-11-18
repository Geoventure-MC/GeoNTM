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



/** 1.7 {@code fr.geoventure.geontm.world.generator.room.JungleDungeonRoomWeakness}, on {@link LegacyDungeonWorld}; jobs go to the site's queue. */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
public class JungleDungeonRoomWeakness extends JungleDungeonRoom {

	public JungleDungeonRoomWeakness(CellularDungeon parent) {
		super(parent);
	}

	public void generateMain(final LegacyDungeonWorld world, final int x, final int y, final int z) {
		super.generateMain(world, x, y, z);
		
		ITimedJob job = new ITimedJob() {

			@Override
			public void work() {

				
				for(int a = 1; a < 4; a++) {
					for(int b = 1; b < 4; b++) {
						
						if(world.rand.nextInt(2) == 0) {
							LegacyBlock bl = world.getBlock(x + a, y, z + b);
							if(bl == ModBlocks.brick_jungle || bl == ModBlocks.brick_jungle_cracked || bl == ModBlocks.brick_jungle_lava) {
								world.setBlock(x + a, y, z + b, ModBlocks.brick_jungle_trap, Trap.WEAKNESS.ordinal(), 3);
							}
						}
					}
				}
			}
		};
		
		parent.timed.addOp(job);
	}
}
