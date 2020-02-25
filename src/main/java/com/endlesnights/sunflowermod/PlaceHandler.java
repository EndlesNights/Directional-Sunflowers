package com.endlesnights.sunflowermod;

import java.util.Collection;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.TallGrassBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid=SunflowerMod.MODID)
public class PlaceHandler
{
	private static final HashMap<ResourceLocation,Block> PLACE_ENTRIES = new HashMap<>();
	
	@SubscribeEvent
	public static void onBlockEntityPlace(RightClickBlock event)
	{	
		ItemStack held = event.getItemStack();
		ResourceLocation rl = held.getItem().getRegistryName();

		if(PLACE_ENTRIES.containsKey(rl))
			placePlant(event, held, PLACE_ENTRIES.get(rl));
	}

	private static void placePlant(RightClickBlock event, ItemStack held, Block block)
	{		
		BlockPos pos = event.getPos();
		Direction face = event.getFace();
		BlockPos placeAt = pos.offset(face);
		World world = event.getWorld();		
		
		if(world.getBlockState(pos).getBlock() instanceof TallGrassBlock
			&& world.getBlockState(pos.down()).getBlock() instanceof GrassBlock)
		{
			placeAt = pos;
		}
		
		if(world.getBlockState(placeAt.down()).getBlock() instanceof GrassBlock
				&& (world.isAirBlock(placeAt) 
						|| world.getFluidState(placeAt).getFluid() == Fluids.WATER 
						|| world.getBlockState(placeAt).getBlock() instanceof TallGrassBlock)
				
				&& (world.isAirBlock(placeAt.up()) 
						|| world.getFluidState(placeAt.up()).getFluid() == Fluids.WATER 
						|| world.getBlockState(placeAt.up()).getBlock() instanceof TallGrassBlock))
		{	
			
			world.setBlockState(placeAt, block.getDefaultState().with(HorizontalBlock.HORIZONTAL_FACING, event.getPlayer().getHorizontalFacing().getOpposite()));
			world.setBlockState(placeAt.up(), block.getDefaultState().with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER).with(HorizontalBlock.HORIZONTAL_FACING, event.getPlayer().getHorizontalFacing().getOpposite()));
			
			world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), block.getSoundType(world.getBlockState(placeAt)).getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
			event.getPlayer().swingArm(event.getHand());
			event.setCanceled(true);
		}
	}

	public static void registerPlaceEntry(ResourceLocation itemName, Block torchSlab)
	{
		if(!PLACE_ENTRIES.containsKey(itemName))
			PLACE_ENTRIES.put(itemName, torchSlab);
	}

	public static Collection<Block> getPlaceEntryBlocks()
	{
		return PLACE_ENTRIES.values();
	}
}
