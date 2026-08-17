package net.chaolux.createcardboardthings.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.registration.IRecipeRegistration;
import net.chaolux.createcardboardthings.Config;
import net.chaolux.createcardboardthings.CreateCardboardThings;
import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    private static final ResourceLocation ID=ResourceLocation.fromNamespaceAndPath(CreateCardboardThings.MOD_ID,"jei_plugin");
    private static final ResourceLocation CARDBOARD_SWORD=ResourceLocation.fromNamespaceAndPath("create","cardboard_sword");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration iRecipeRegistration) {
        if(!Config.cardboardIngot()) return;
        Item item= BuiltInRegistries.ITEM.get(CARDBOARD_SWORD);
        if(item == Items.AIR || Minecraft.getInstance().level == null) return;
        HolderLookup.RegistryLookup<Enchantment> holderLookup=Minecraft.getInstance().level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> holder=holderLookup.getOrThrow(Enchantments.KNOCKBACK);
        ItemStack itemStack=new ItemStack(item);
        ItemStack stack=itemStack.copy();
        stack.enchant(holder,1);
        IJeiAnvilRecipe iJeiAnvilRecipe=iRecipeRegistration.getVanillaRecipeFactory().createAnvilRecipe(itemStack, List.of(new ItemStack(ModItems.CARDBOARD_INGOT.get())),List.of(stack),ResourceLocation.fromNamespaceAndPath(CreateCardboardThings.MOD_ID,"cardboard_sword_knockback"));
        iRecipeRegistration.addRecipes(RecipeTypes.ANVIL,List.of(iJeiAnvilRecipe));
    }
}
