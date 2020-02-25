package com.endlesnights.sunflowermod;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(SunflowerMod.MODID)
@EventBusSubscriber(bus=Bus.MOD)
public class SunflowerMod
{
	public static final String MODID = "sunflowermod";
	public static final String NAME = "Sunflower Direction Mod";
	
	public static Block sunflower = null;
	
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
    	sunflower = registerBlock(new BlockSunFlower (Block.Properties.from(Blocks.SUNFLOWER)), "sunflower");
    	
        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            RenderType transparentRenderType = RenderType.func_228641_d_();
            RenderTypeLookup.setRenderLayer(sunflower, transparentRenderType);
        }
    }
    
	@SubscribeEvent
	public static void onInterModEnqueue(InterModEnqueueEvent event)
	{
		PlaceHandler.registerPlaceEntry(Items.SUNFLOWER.getRegistryName(), sunflower);
	}
	
    public static Block registerBlock(Block block, String name)
    {
        block.setRegistryName(name);
        ForgeRegistries.BLOCKS.register(block);
        return block;
    }
}
