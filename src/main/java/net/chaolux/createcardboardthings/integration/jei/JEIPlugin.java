package net.chaolux.createcardboardthings.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.registration.IRecipeRegistration;
import net.chaolux.createcardboardthings.Config;
import net.chaolux.createcardboardthings.CreateCardboardThings;
import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    private static final ResourceLocation ID=new ResourceLocation(CreateCardboardThings.MOD_ID,"jei_plugin");
    private static final ResourceLocation CARDBOARD_SWORD=new ResourceLocation("create","cardboard_sword");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration iRecipeRegistration) {
        if(!Config.cardboardIngot()) return;
        Item item= ForgeRegistries.ITEMS.getValue(CARDBOARD_SWORD);
        if(item == null || item == Items.AIR) return;
        ItemStack itemStack=new ItemStack(item);
        ItemStack stack=itemStack.copy();
        stack.enchant(Enchantments.KNOCKBACK,1);
        IJeiAnvilRecipe iJeiAnvilRecipe=iRecipeRegistration.getVanillaRecipeFactory().createAnvilRecipe(itemStack, List.of(new ItemStack(ModItems.CARDBOARD_INGOT.get())),List.of(stack),new ResourceLocation(CreateCardboardThings.MOD_ID,"cardboard_sword_knockback"));
        iRecipeRegistration.addRecipes(RecipeTypes.ANVIL,List.of(iJeiAnvilRecipe));
    }
}
